package com.fh.controller.front;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.thread.ThreadUtil;
import com.fh.dao.RedisDao;
import com.fh.entity.MemUser;
import com.fh.entity.Page;
import com.fh.entity.result.R;
import com.fh.service.record.Withdrawals_recordManager;
import com.fh.service.system.FHlogManager;
import com.fh.util.*;
import org.apache.taglibs.standard.lang.jstl.test.beans.PublicInterface2;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 说明：前台用户表
 * 创建人：
 * 创建时间：2020-03-25
 */
@Controller
@RequestMapping(value = "/account")
public class AccountController extends BaseFrontController {

    String menuUrl = "account/list.do"; //菜单地址(权限用)

    // 日志管理
    @Resource(name = "fhlogService")
    private FHlogManager FHLOG;
    // usdt线上钱包转出记录
    @Resource(name = "withdrawals_recordService")
    private Withdrawals_recordManager withdrawals_recordService;
    // Redis
    @Resource(name = "redisDaoImpl")
    private RedisDao redisDaoImpl;


    /**
     * 修改
     *
     * @param
     * @throws Exception
     */
    @RequestMapping(value = "/edit")
    public ModelAndView edit() throws Exception {
        logBefore(logger, Jurisdiction.getUsername() + "修改Account");
        if (!Jurisdiction.buttonJurisdiction(menuUrl, "edit")) {
            return null;
        } //校验权限
        ModelAndView mv = this.getModelAndView();
        PageData pd = this.getPageData();
        if (Tools.notEmpty(pd.getString("LOGIN_PASSWORD"))) {
            pd.put("LOGIN_PASSWORD", StringUtil.applySha256(pd.getString("LOGIN_PASSWORD")));
        } else {
            pd.remove("LOGIN_PASSWORD");
        }
        if (Tools.notEmpty(pd.getString("SECURITY_PASSWORD"))) {
            pd.put("SECURITY_PASSWORD", StringUtil.applySha256(pd.getString("SECURITY_PASSWORD")));
        } else {
            pd.remove("SECURITY_PASSWORD");
        }
        pd.put("GMT_MODIFIED", DateUtil.now());
        accountService.editFor(pd);
        // 异步执行 检查用户等级操作
        ThreadUtil.execute(() -> {
            try {
                for (int i = 0; i < 2; i++) {
                    this.checkRank();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        FHLOG.save(Jurisdiction.getUsername(), "修改用户信息");
        mv.addObject("msg", "success");
        mv.setViewName("save_result");
        return mv;
    }

    /**
     * 列表
     *
     * @param page
     * @throws Exception
     */
    @RequestMapping(value = "/list")
    public ModelAndView list(Page page) throws Exception {
        logBefore(logger, Jurisdiction.getUsername() + "列表Account");
        //if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;} //校验权限(无权查看时页面会有提示,如果不注释掉这句代码就无法进入列表页面,所以根据情况是否加入本句代码)
        ModelAndView mv = this.getModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        String keywords = pd.getString("keywords");                //关键词检索条件
        if (null != keywords && !"".equals(keywords)) {
            pd.put("keywords", keywords.trim());
        }
        page.setPd(pd);
        List<PageData> varList = accountService.list(page);    //列出Account列表
        mv.setViewName("front/account/account_list");
        mv.addObject("varList", varList);
        mv.addObject("pd", pd);
        mv.addObject("QX", Jurisdiction.getHC());    //按钮权限
        return mv;
    }

    /**
     * usdt 提现列表
     *
     * @param page
     * @throws Exception
     */
    @RequestMapping(value = "/withdrawalsList")
    public ModelAndView withdrawalsList(Page page) throws Exception {
        if (!Jurisdiction.buttonJurisdiction(menuUrl, "cha")) {
            return null;
        } //校验权限(无权查看时页面会有提示,如果不注释掉这句代码就无法进入列表页面,所以根据情况是否加入本句代码)
        ModelAndView mv = this.getModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        String keywords = pd.getString("keywords");                //关键词检索条件
        if (null != keywords && !"".equals(keywords)) {
            pd.put("keywords", keywords.trim());
        }
        page.setPd(pd);
        List<PageData> varList = usdt_recService.withdrawalsList(page);    //列出Usdt_rec列表
        mv.setViewName("front/account/usdtCashList");
        mv.addObject("varList", varList);
        mv.addObject("pd", pd);
        mv.addObject("QX", Jurisdiction.getHC());    //按钮权限
        return mv;
    }

    /**
     * 功能描述：提现确认交易完成
     *
     * @author Ajie
     * @date 2020/3/31 0031
     */
    @RequestMapping(value = "/usdtComplete")
    @ResponseBody
    private String usdtComplete() throws Exception {
        PageData pd = this.getPageData();
        pd = usdt_recService.findById(pd);
        // 修改状态已完成
        pd.put("STATUS", "已完成");
        pd.put("GMT_MODIFIED", DateUtil.now());
        usdt_recService.edit(pd);
        // 清空Redis所有缓存
        redisDaoImpl.deleteAll();
        return "success";
    }

    /**
     * 功能描述：提现驳回
     *
     * @author Ajie
     * @date 2020/3/31 0031
     */
    @RequestMapping(value = "/usdtReject")
    @ResponseBody
    private String usdtReject() throws Exception {
        PageData pd = this.getPageData();
        pd = usdt_recService.findById(pd);
        // 修改状态已完成
        pd.put("STATUS", "驳回");
        pd.put("GMT_MODIFIED", DateUtil.now());
        usdt_recService.edit(pd);
        // 退回资金给用户
        pd.put("ACCOUNT_ID", pd.get("USER_ID"));
        pd.put("money", pd.get("MONEY"));
        pd.put("tag", "+");
        pd.put("GMT_MODIFIED", DateUtil.now());
        accountService.updateUsdtNumber(pd);
        return "success";
    }

    /**
     * 钱包管理列表
     *
     * @param page
     * @throws Exception
     */
    @RequestMapping(value = "/usdtList")
    public ModelAndView usdtlist(Page page) throws Exception {
        logBefore(logger, Jurisdiction.getUsername() + "usdt钱包管理");
        ModelAndView mv = this.getModelAndView();
        PageData pd = this.getPageData();
        String keywords = pd.getString("keywords");                //关键词检索条件
        if (null != keywords && !"".equals(keywords)) {
            pd.put("keywords", keywords.trim());
        }
        pd.put("name", null);
        page.setPd(pd);
        List<PageData> varList = accountService.list(page);    //列出Account列表
        // 循环查询钱包余额
        for (int i = 0; i < varList.size(); i++) {
            PageData map = varList.get(i);
            PageData resule = BlockUtil.selectBalance(map.getString("USDT_ADDRESS"));
            if (resule != null) {
                double usdt = Double.parseDouble(resule.get("token_numbers").toString());
                map.put("ETH", resule.get("eth_numbers"));
                map.put("USDT", usdt * Math.pow(10, 12));
            } else {
                map.put("ETH", 0);
                map.put("USDT", 0);
            }
            varList.set(i, map);
        }
        FHLOG.save(Jurisdiction.getUsername(), "usdt钱包管理");
        mv.setViewName("front/account/usdtWalletList");
        mv.addObject("varList", varList);
        mv.addObject("pd", pd);
        mv.addObject("QX", Jurisdiction.getHC());    //按钮权限
        return mv;
    }

    /**
     * 用户组织架构图
     *
     * @throws Exception
     */
    @RequestMapping(value = "/userChart")
    public ModelAndView userChart() throws Exception {
        ModelAndView mv = this.getModelAndView();
        mv.setViewName("front/account/userChart");
        return mv;
    }

    @RequestMapping(value = "/getUserList")
    @ResponseBody
    public R getUserList() throws Exception {
        List<PageData> varList = new ArrayList<>(16);
        // 接收用户名
        PageData pd = this.getPageData();
        if (pd.isEmpty()) {
            pd.put("ACCOUNT_ID", "10000");
            pd = accountService.findById(pd);
            varList.add(pd);
        } else {
            MemUser user = accountService.findByName(pd);
            if (user == null) {
                return R.parError();
            }
            pd.put("ACCOUNT_ID", user.getACCOUNT_ID());
            pd = accountService.findById(pd);
            varList.add(pd);
        }
        // 查所有下级
        varList.addAll(accountService.listAllSubByUserId(pd));
        return R.ok().data("item", varList);
    }


    /**
     * 去编辑页面
     *
     * @param
     * @throws Exception
     */
    @RequestMapping(value = "/goEdit")
    public ModelAndView goUserEdit() throws Exception {
        ModelAndView mv = this.getModelAndView();
        PageData pd = this.getPageData();
        // 根据ID读取
        pd = accountService.findById(pd);
        mv.setViewName("front/account/account_edit");
        mv.addObject("msg", "edit");
        mv.addObject("pd", pd);
        return mv;
    }

    /**
     * 去USDT转出输入框页面
     *
     * @param
     * @throws Exception
     */
    @RequestMapping(value = "/goUserEdit")
    public ModelAndView goUsdtEdit() throws Exception {
        ModelAndView mv = this.getModelAndView();
        PageData pd = this.getPageData();
        Object num = pd.get("num");
        // 根据ID读取
        pd = accountService.findById(pd);
        pd.put("num", num);
        // 获取系统参数
        PageData par = sys_configService.findById(pd);
        mv.setViewName("front/account/usdt_edit");
        mv.addObject("msg", "outTurn");
        mv.addObject("pd", pd);
        mv.addObject("par", par);
        return mv;
    }

    /**
     * 去ETH转出输入框页面
     *
     * @param
     * @throws Exception
     */
    @RequestMapping(value = "/goEthEdit")
    public ModelAndView goEthEdit() throws Exception {
        ModelAndView mv = this.getModelAndView();
        PageData pd = this.getPageData();
        Object num = pd.get("num");
        // 根据ID读取
        pd = accountService.findById(pd);
        pd.put("num", num);
        // 获取系统参数
        PageData par = sys_configService.findById(pd);
        mv.setViewName("front/account/usdt_edit");
        mv.addObject("msg", "ethOutTurn");
        mv.addObject("pd", pd);
        mv.addObject("par", par);
        return mv;
    }

    /**
     * USDT确认转出
     *
     * @param
     * @throws Exception
     */
    @RequestMapping(value = "/outTurn")
    @ResponseBody
    public PageData outTurn() throws Exception {
        logBefore(logger, Jurisdiction.getUsername() + "usdt转出");
        if (!Jurisdiction.buttonJurisdiction(menuUrl, "edit")) {
            return null;
        } //校验权限
        PageData pd = this.getPageData();
        // 获取转出地址、转入地址、数量
        String outAddress = pd.getString("USDT_SITE");
        String inAddress = pd.getString("transfer");
        double num = Double.parseDouble(pd.get("num").toString());
        pd = new PageData();
        // 调用代转币
        HashMap result = BlockUtil.usdtTransfer(outAddress, inAddress, num);
        // 如果成功
        if ("1".equals(result.get("statuses").toString())) {
            String orderox = result.get("orderox").toString();
            addWalletRec(Jurisdiction.getUsername(), orderox, String.valueOf(num), outAddress, inAddress);
            // 调用查询交易哈希 statuses: 0交易中；1成功；2失败  msg: 返回信息
            PageData info = BlockUtil.getTradingStatus(orderox);
            if ("1".equals(info.get("statuses"))) {
                pd.put("msg", "转出成功");
            } else {
                pd.put("msg", "转出中");
            }
            if ("2".equals(info.get("statuses"))) {
                pd.put("msg", "转出失败");
            }
        } else {
            pd.put("msg", "转出失败");
        }
        return pd;
    }

    /**
     * Eth确认转出
     *
     * @param
     * @throws Exception
     */
    @RequestMapping(value = "/ethOutTurn")
    @ResponseBody
    public PageData ethOutTurn() throws Exception {
        logBefore(logger, Jurisdiction.getUsername() + "ETH转出");
        if (!Jurisdiction.buttonJurisdiction(menuUrl, "edit")) {
            return null;
        } //校验权限
        PageData pd = this.getPageData();
        // 获取转出地址、转入地址、数量
        String outAddress = pd.getString("USDT_SITE");
        String inAddress = pd.getString("transfer");
        double num = Double.parseDouble(pd.get("num").toString());
        // 调用代转币
        HashMap result = BlockUtil.usdtTransferEth(outAddress, inAddress, num);
        pd = new PageData();
        // 如果成功
        if ("1".equals(result.get("statuses").toString())) {
            String orderox = result.get("orderox").toString();
            addWalletRec(Jurisdiction.getUsername(), orderox, String.valueOf(num), outAddress, inAddress);
            // 调用查询交易哈希 statuses: 0交易中；1成功；2失败  msg: 返回信息
            PageData info = BlockUtil.getTradingStatus(orderox);
            System.out.println("============》EHT回调 " + info);
            if ("1".equals(info.get("statuses"))) {
                pd.put("msg", "转出成功");
            } else {
                pd.put("msg", "转出中");
            }
            if ("2".equals(info.get("statuses"))) {
                pd.put("msg", "转出失败");
            }
            System.out.println("===========》pd信息 " + pd);
        } else {
            pd.put("msg", "转出失败");
        }
        return pd;
    }

    /**
     * 功能描述：增加线上钱包操作记录
     *
     * @param operator 操作人
     * @param orderox  交易哈希
     * @param money    数量
     * @param from     转出地址
     * @param to       转入地址
     * @author Ajie
     * @date 2020/2/11 0011
     */
    public void addWalletRec(String operator, String orderox, String money, String from, String to) throws Exception {
        PageData pd = new PageData();
        pd.put("WITHDRAWALS_RECORD_ID", "");    //主键 自增
        pd.put("GMT_CREATE", Tools.date2Str(new Date()));    //创建时间
        pd.put("GMT_MODIFIED", Tools.date2Str(new Date()));    //更新时间
        pd.put("TRADE_HASH", orderox);    //交易哈希
        pd.put("MONEY", money);    //金额
        pd.put("USER_NAME", operator);    //操作人
        pd.put("FROM_ADDRESS", from);    //转出地址
        pd.put("IN_ADDRESS", to);    //转入地址
        withdrawals_recordService.save(pd);
    }

    /**
     * 导出到excel
     *
     * @param
     * @throws Exception
     */
    @RequestMapping(value = "/excel")
    public ModelAndView exportExcel() throws Exception {
        logBefore(logger, Jurisdiction.getUsername() + "导出Account到excel");
        if (!Jurisdiction.buttonJurisdiction(menuUrl, "cha")) {
            return null;
        }
        ModelAndView mv;
        Map<String, Object> dataMap = new HashMap<String, Object>();
        List<String> titles = new ArrayList<String>();
        titles.add("创建时间");    //1
        titles.add("更新时间");    //2
        titles.add("用户名");    //3
        titles.add("登录密码");    //4
        titles.add("安全密码");    //5
        titles.add("USDT钱包");    //6
        titles.add("娱乐积分");    //7
        titles.add("分享基金");    //8
        titles.add("推荐人数");    //9
        titles.add("推荐人");    //10
        titles.add("推荐路径");    //11
        titles.add("代数");    //12
        titles.add("用户状态");    //13
        titles.add("团队人数");    //14
        titles.add("姓名");    //15
        titles.add("佣金点");    //16
        titles.add("手机号");    //17
        titles.add("usdt收币地址");    //18
        titles.add("钱包收款二维码");    //19
        titles.add("是否为休息号");    //21
        titles.add("USDT钱包地址");    //22
        titles.add("用户等级");    //23
        titles.add("usdt线上钱包余额");    //24
        titles.add("密保问题");    //25
        titles.add("密保答案");    //26
        dataMap.put("titles", titles);
        List<PageData> varOList = accountService.listAll();
        List<PageData> varList = new ArrayList<PageData>();
        for (int i = 0; i < varOList.size(); i++) {
            PageData vpd = new PageData();
            vpd.put("var1", varOList.get(i).getString("GMT_CREATE"));        //1
            vpd.put("var2", varOList.get(i).getString("GMT_MODIFIED"));        //2
            vpd.put("var3", varOList.get(i).getString("USER_NAME"));        //3
            vpd.put("var4", varOList.get(i).getString("LOGIN_PASSWORD"));        //4
            vpd.put("var5", varOList.get(i).getString("SECURITY_PASSWORD"));        //5
            vpd.put("var6", varOList.get(i).get("USDT_WALLET"));    //6
            vpd.put("var7", varOList.get(i).get("ENTERTAINMENT_SCORE"));    //7
            vpd.put("var8", varOList.get(i).get("SHARE_FUND"));    //8
            vpd.put("var9", varOList.get(i).get("RECOMMENDED_NUMBER"));    //9
            vpd.put("var10", varOList.get(i).getString("RECOMMENDER"));        //10
            vpd.put("var11", varOList.get(i).getString("RE_PATH"));        //11
            vpd.put("var12", varOList.get(i).get("ALGEBRA"));    //12
            vpd.put("var13", varOList.get(i).getString("USER_STATE"));        //13
            vpd.put("var14", varOList.get(i).get("TEAM_NUMBER"));    //14
            vpd.put("var15", varOList.get(i).getString("NAME"));        //15
            vpd.put("var16", varOList.get(i).get("COMMISSION"));    //16
            vpd.put("var17", varOList.get(i).get("PHONE"));    //17
            vpd.put("var18", varOList.get(i).getString("USDT_IN_ADDRESS"));        //18
            vpd.put("var19", varOList.get(i).getString("WALLET_QR_CODE"));        //19
            vpd.put("var21", varOList.get(i).getString("IS_REST"));        //21
            vpd.put("var22", varOList.get(i).getString("USDT_ADDRESS"));        //22
            vpd.put("var23", varOList.get(i).getString("USER_RANK"));        //23
            vpd.put("var24", varOList.get(i).get("USDT_WALLET_ACTUAL"));        //24
            vpd.put("var25", varOList.get(i).getString("SECURITY_QUESTION"));        //25
            vpd.put("var26", varOList.get(i).getString("ANSWER"));        //26
            varList.add(vpd);
        }
        dataMap.put("varList", varList);
        ObjectExcelView erv = new ObjectExcelView();
        mv = new ModelAndView(erv, dataMap);
        return mv;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        binder.registerCustomEditor(Date.class, new CustomDateEditor(format, true));
    }
}
