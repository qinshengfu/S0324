package com.fh.controller.front;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Console;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.fh.annotation.CacheLock;
import com.fh.entity.MemUser;
import com.fh.entity.result.R;
import com.fh.service.front.RealManager;
import com.fh.service.record.*;
import com.fh.util.*;
import org.apache.shiro.session.Session;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import sun.print.PageableDoc;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 说明：前台首页控制器
 * 创建人：Ajie
 * 创建时间：2020年3月30日16:51:25
 */
@Controller
@RequestMapping("/front")
public class IndexController extends BaseFrontController {

    // 轮播图
    @Resource(name = "sys_chartService")
    private Sys_chartManager sys_chartService;
    // usdt钱包线上充值记录
    @Resource(name = "recharge_recordService")
    private Recharge_recordManager recharge_recordService;
    // 跑单场次
    @Resource(name = "run_listService")
    private Run_listManager run_listService;
    // 实名认证
    @Resource(name = "realService")
    private RealManager realService;


    /**
     * 获取头部信息
     *
     * @return
     */
    @RequestMapping(value = "/getList")
    @ResponseBody
    public Object getList() {
        PageData pd = new PageData();
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            pd = this.getPageData();
            Session session = Jurisdiction.getSession();
            // 前台当前登录用户信息
            MemUser user = (MemUser) session.getAttribute(Const.SESSION_MEMUSER);
            map.put("user", user);
            String strWEBSOCKET = Tools.readTxtFile(Const.WEBSOCKET);//读取WEBSOCKET配置
            if (null != strWEBSOCKET && !"".equals(strWEBSOCKET)) {
                String strIW[] = strWEBSOCKET.split(",fh,");
                if (strIW.length == 7) {
                    map.put("oladress", strIW[2] + ":" + strIW[3]);        //在线管理和站内信服务器IP和端口
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return AppUtil.returnObject(pd, map);
    }

    /**
     * 功能描述：访问前台【充币1】页面
     *
     * @author Ajie
     * @date 2020年3月27日16:14:23
     */
    @RequestMapping(value = "to_coinCharging")
    public ModelAndView toCoinCharging() throws Exception {
        ModelAndView mv = getModelAndView();
        PageData pd = new PageData();
        PageData par = sys_configService.findById(pd);
        mv.setViewName("front/index/coin-charging");
        mv.addObject("par", par);
        return mv;
    }

    @RequestMapping(value = "walletQrCode")
    public void walletQrCode(HttpServletRequest request, HttpServletResponse response) throws Exception {
        MemUser user = (MemUser) request.getSession().getAttribute(Const.SESSION_MEMUSER);
        // 通过输出流把邀请码输出到页面
        ServletOutputStream out = response.getOutputStream();
        // 调用工具类
        TwoDimensionCode.encoderQRCode(user.getUSDT_ADDRESS(), out);

    }

    /**
     * 功能描述：访问前台【充币记录】页面
     *
     * @author Ajie
     * @date 2020年3月27日16:14:23
     */
    @RequestMapping(value = "to_coinRecord")
    public ModelAndView tpCoinRecord() throws Exception {
        ModelAndView mv = getModelAndView();
        MemUser user = (MemUser) Jurisdiction.getSession().getAttribute(Const.SESSION_MEMUSER);
        PageData pd = new PageData();
        pd.put("USER_ID", user.getACCOUNT_ID());
        List<PageData> list = recharge_recordService.listByUserId(pd);
        // 分页操作 调用 Pager

        mv.setViewName("front/index/coin-record");
        mv.addObject("list", list);
        return mv;
    }

    /**
     * 功能描述：访问前台【充币2】页面
     *
     * @author Ajie
     * @date 2020年3月27日16:14:23
     */
    @RequestMapping(value = "to_coinSetup")
    public ModelAndView toCoinSetup() throws Exception {
        PageData par = sys_configService.findById(new PageData());
        ModelAndView mv = getModelAndView();
        mv.setViewName("front/index/coin-setup");
        mv.addObject("par", par);
        return mv;
    }

    /**
     * 功能描述：访问前台【划转】页面
     *
     * @author Ajie
     * @date 2020年6月19日17:37:05
     */
    @RequestMapping(value = "toTransfer")
    public ModelAndView toTransfer() {
        ModelAndView mv = getModelAndView();
        mv.setViewName("front/index/transfer");
        return mv;
    }

    /**
     * 功能描述：访问前台【usdt转账】页面
     *
     * @author Ajie
     * @date 2020年3月31日15:02:45
     */
    @RequestMapping(value = "to_usdtTransfer")
    public ModelAndView toUsdtTransfer() throws Exception {
        ModelAndView mv = getModelAndView();
        PageData par = sys_configService.findById(new PageData());
        mv.setViewName("front/index/usdtTransfer");
        mv.addObject("par", par);
        return mv;
    }

    /**
     * 功能描述：访问前台【分享基金转账】页面
     *
     * @author Ajie
     * @date 2020年3月31日15:02:45
     */
    @RequestMapping(value = "to_fundTransfer")
    public ModelAndView toFundTransfer() throws Exception {
        ModelAndView mv = getModelAndView();
        PageData par = sys_configService.findById(new PageData());
        mv.setViewName("front/index/fundTransfer");
        mv.addObject("par", par);
        return mv;
    }

    /**
     * 功能描述：访问前台【娱乐积分转账】页面
     *
     * @author Ajie
     * @date 2020年4月8日17:55:43
     */
    @RequestMapping(value = "to_integralTransfer")
    public ModelAndView toIntegralTransfer() throws Exception {
        ModelAndView mv = getModelAndView();
        PageData par = sys_configService.findById(new PageData());
        mv.setViewName("front/index/integralTransfer");
        mv.addObject("par", par);
        return mv;
    }

    /**
     * 功能描述：访问前台【首页】页面
     *
     * @author Ajie
     * @date 2020年3月27日16:14:23
     */
    @RequestMapping(value = "to_index")
    public ModelAndView toIndex() throws Exception {
        ModelAndView mv = getModelAndView();
        PageData pd = new PageData();
        MemUser user = (MemUser) Jurisdiction.getSession().getAttribute(Const.SESSION_MEMUSER);
        List<PageData> chartList = sys_chartService.listAll(pd);
        pd.put("ACCOUNT_ID", user.getACCOUNT_ID());
        user = accountService.findByIdPojo(pd);
        // 查询今日静态收益总额
        pd.put("USER_ID", user.getACCOUNT_ID());
        pd.put("STATUS", "已完成");
        pd.put("nowTime", DateUtil.getDay());
        String todayStaticIncome = profit_recordService.getDayEarningsSumByUserId(pd).get("EARNINGS_SUM").toString();
        // 查询今天动态收益总额
        String todayDynamicIncome = usdt_recService.getDayEarningsSumByUserId(pd).get("EARNINGS_SUM").toString();
        // 查询最新一条跑单记录
        pd = receipt_recService.getLatestRecord(pd);
        // 查询最新一条新闻公告
        PageData news = sys_newsService.getLatestNews();
        mv.setViewName("front/index/index");
        mv.addObject("chartList", chartList);
        mv.addObject("user", user);
        mv.addObject("pd", pd);
        mv.addObject("todayStaticIncome", todayStaticIncome);
        mv.addObject("todayDynamicIncome", todayDynamicIncome);
        mv.addObject("news", news);
        mv.addObject("flag", "index");
        return mv;
    }

    /**
     * 功能描述：访问前台【互转记录】页面
     *
     * @author Ajie
     * @date 2020年3月27日16:14:23
     */
    @RequestMapping(value = "to_interturnRecord")
    public ModelAndView toInterturnRecord() throws Exception {
        ModelAndView mv = getModelAndView();
        MemUser user = (MemUser) Jurisdiction.getSession().getAttribute(Const.SESSION_MEMUSER);
        PageData pd = new PageData();
        pd.put("USER_ID", user.getACCOUNT_ID());
        pd.put("AMOUNT_TYPE", "转账");
        // usdt 转账
        List<PageData> usdtList = usdt_recService.listByUserIdAndType(pd);
        // 分享基金转账
        List<PageData> fundList = fund_recService.listByUserIdAndType(pd);
        // 娱乐积分
        List<PageData> scoreList = score_recService.listByUserIdAndType(pd);
        List<PageData> list = new ArrayList<>();
        list.addAll(usdtList);
        list.addAll(fundList);
        list.addAll(scoreList);
        // 时间降序排序
        list = SortUtil.sortTime(list);
        mv.setViewName("front/index/interturn-record");
        mv.addObject("list", list);
        return mv;
    }

    /**
     * 功能描述：访问前台【分享基金互转记录】页面
     *
     * @author Ajie
     * @date 2020年3月27日16:14:23
     */
    @RequestMapping(value = "to_fundRecord")
    public ModelAndView tofundRecord() throws Exception {
        ModelAndView mv = getModelAndView();
        MemUser user = (MemUser) Jurisdiction.getSession().getAttribute(Const.SESSION_MEMUSER);
        PageData pd = new PageData();
        pd.put("USER_ID", user.getACCOUNT_ID());
        pd.put("AMOUNT_TYPE", "转账");
        List<PageData> list = fund_recService.listByUserIdAndType(pd);
        mv.setViewName("front/index/interturn-record");
        mv.addObject("list", list);
        return mv;
    }

    /**
     * 功能描述：访问前台【娱乐积分互转记录】页面
     *
     * @author Ajie
     * @date 2020年3月27日16:14:23
     */
    @RequestMapping(value = "to_integralRecord")
    public ModelAndView toIntegralRecord() throws Exception {
        ModelAndView mv = getModelAndView();
        MemUser user = (MemUser) Jurisdiction.getSession().getAttribute(Const.SESSION_MEMUSER);
        PageData pd = new PageData();
        pd.put("USER_ID", user.getACCOUNT_ID());
        pd.put("AMOUNT_TYPE", "转账");
        List<PageData> list = score_recService.listByUserIdAndType(pd);
        mv.setViewName("front/index/interturn-record");
        mv.addObject("list", list);
        return mv;
    }

    /**
     * 功能描述：访问前台【收益记录】页面
     *
     * @author Ajie
     * @date 2020年3月27日16:14:23
     */
    @RequestMapping(value = "to_profitRecord")
    public ModelAndView toProfitRecord() throws Exception {
        ModelAndView mv = getModelAndView();
        MemUser user = (MemUser) Jurisdiction.getSession().getAttribute(Const.SESSION_MEMUSER);
        PageData pd = new PageData();
        pd.put("USER_ID", user.getACCOUNT_ID());
        pd.put("TAG", '+');
        List<PageData> list = usdt_recService.listByUserIdAndTag(pd);
        mv.setViewName("front/index/profit-record");
        mv.addObject("list", list);
        return mv;
    }

    /**
     * 功能描述：访问前台【娱乐积金钱包记录】页面
     *
     * @author Ajie
     * @date 2020年4月3日16:00:17
     */
    @RequestMapping(value = "to_scoreRecord")
    public ModelAndView toScoreRecord() throws Exception {
        ModelAndView mv = getModelAndView();
        MemUser user = (MemUser) Jurisdiction.getSession().getAttribute(Const.SESSION_MEMUSER);
        PageData pd = new PageData();
        pd.put("USER_ID", user.getACCOUNT_ID());
        List<PageData> list = score_recService.listByUserId(pd);
        mv.setViewName("front/index/scoreRecord");
        mv.addObject("list", list);
        return mv;
    }

    /**
     * 功能描述：访问前台【分享基金钱包记录】页面
     *
     * @author Ajie
     * @date 2020年4月3日16:00:13
     */
    @RequestMapping(value = "to_fundWalletRecord")
    public ModelAndView toFundRecord() throws Exception {
        ModelAndView mv = getModelAndView();
        MemUser user = (MemUser) Jurisdiction.getSession().getAttribute(Const.SESSION_MEMUSER);
        PageData pd = new PageData();
        pd.put("USER_ID", user.getACCOUNT_ID());
        List<PageData> list = fund_recService.listByUserId(pd);
        mv.setViewName("front/index/fundRecord");
        mv.addObject("list", list);
        return mv;
    }

    /**
     * 功能描述：访问前台【usdt钱包记录】页面
     *
     * @author Ajie
     * @date 2020年4月9日16:02:14
     */
    @RequestMapping(value = "to_usdtWalletRecord")
    public ModelAndView toUsdtRecord() throws Exception {
        ModelAndView mv = getModelAndView();
        mv.setViewName("front/index/usdtRecord");
        return mv;
    }

    /**
     * 功能描述：usdt钱包记录分页
     *
     * @author Ajie
     * @date 2020/4/9 0007
     */
    @RequestMapping(value = "to_usdtPage")
    @ResponseBody
    public Object toUsdtPage() throws Exception {
        Pager<PageData> pager = new Pager<>();
        MemUser user = (MemUser) Jurisdiction.getSession().getAttribute(Const.SESSION_MEMUSER);
        PageData pd = this.getPageData();
        // 页码
        int num = Integer.parseInt(pd.get("num").toString());
        // 每页数据条数
        int pageSize = Integer.parseInt(pd.get("size").toString());
        // 查询usdt记录
        pd.put("USER_ID", user.getACCOUNT_ID());
        List<PageData> billList = usdt_recService.listByUserId(pd);
        // 第 N 页
        pager.setCurentPageIndex(num);
        // 每页 N 条
        pager.setCountPerpage(pageSize);
        pager.setBigList(billList);
        // 得到小的集合(分页的当前页的记录)
        List<PageData> curPageData = pager.getSmallList();
        // 得到总页数
        int totalPage = pager.getPageCount();
        pd.put("curPageData", curPageData);
        pd.put("totalPage", totalPage);
        return pd;
    }

    /**
     * 功能描述：访问前台【接单记录】页面
     *
     * @author Ajie
     * @date 2020年3月27日16:14:23
     */
    @RequestMapping(value = "to_receiptRecord")
    public ModelAndView toReceiptRecord() throws Exception {
        ModelAndView mv = getModelAndView();
        MemUser user = (MemUser) Jurisdiction.getSession().getAttribute(Const.SESSION_MEMUSER);
        PageData pd = new PageData();
        pd.put("USER_ID", user.getACCOUNT_ID());
        List<PageData> list = receipt_recService.listByUserId(pd);
        // 分页调用 Pager

        mv.setViewName("front/index/receipt-record");
        mv.addObject("list", list);
        return mv;
    }

    /**
     * 功能描述：访问前台【提币】页面
     *
     * @author Ajie
     * @date 2020年3月27日16:14:23
     */
    @RequestMapping(value = "to_withdrawMoney")
    public ModelAndView toWithdrawMoney() throws Exception {
        ModelAndView mv = getModelAndView();
        MemUser user = (MemUser) Jurisdiction.getSession().getAttribute(Const.SESSION_MEMUSER);
        PageData pd = new PageData();
        // 查询系统参数
        PageData par = sys_configService.findById(pd);
        // 查询用户信息
        pd.put("ACCOUNT_ID", user.getACCOUNT_ID());
        user = accountService.findByIdPojo(pd);
        mv.setViewName("front/index/withdraw-money");
        mv.addObject("user", user);
        mv.addObject("par", par);
        return mv;
    }

    /**
     * 功能描述：访问前台【提币记录】页面
     *
     * @author Ajie
     * @date 2020年3月27日16:14:23
     */
    @RequestMapping(value = "to_witmoeyRecord")
    public ModelAndView toWitmoeyRecord() throws Exception {
        ModelAndView mv = getModelAndView();
        MemUser user = (MemUser) Jurisdiction.getSession().getAttribute(Const.SESSION_MEMUSER);
        PageData pd = new PageData();
        pd.put("USER_ID", user.getACCOUNT_ID());
        pd.put("AMOUNT_TYPE", "提币");
        List<PageData> list = usdt_recService.listByUserIdAndType(pd);
        // 分页调用 Pager

        mv.setViewName("front/index/witmoey-record");
        mv.addObject("list", list);
        return mv;
    }

    /**
     * 功能描述：访问前台【接单】页面
     *
     * @author Ajie
     * @date 2020年3月27日16:14:23
     */
    @RequestMapping(value = "to_receipt")
    public ModelAndView toReceipt() throws Exception {
        ModelAndView mv = getModelAndView();
        MemUser memUser = (MemUser) Jurisdiction.getSession().getAttribute(Const.SESSION_MEMUSER);
        PageData user = new PageData();
        user.put("ACCOUNT_ID", memUser.getACCOUNT_ID());
        user = accountService.findById(user);
        mv.setViewName("front/receipt/receipt");
        mv.addObject("flag", "receipt");
        mv.addObject("user", user);
        return mv;
    }

    /**
     * 功能描述：访问前台【接单支付】页面
     *
     * @author Ajie
     * @date 2020年3月27日16:14:23
     */
    @RequestMapping(value = "toNowRece")
    public ModelAndView toNowRece() throws Exception {
        ModelAndView mv = getModelAndView();
        MemUser memUser = (MemUser) Jurisdiction.getSession().getAttribute(Const.SESSION_MEMUSER);
        PageData user = new PageData();
        // 获取最新用户信息
        user.put("ACCOUNT_ID", memUser.getACCOUNT_ID());
        user = accountService.findById(user);
        // 查询系统参数
        PageData param = sys_configService.findById(new PageData());
        mv.setViewName("front/receipt/now-rece");
        mv.addObject("user", user);
        mv.addObject("par", param);
        return mv;
    }

    /**
     * 功能描述：接单页面，账单流水分页
     *
     * @author Ajie
     * @date 2020/4/7 0007
     */
    @RequestMapping(value = "to_receiptPage")
    @ResponseBody
    public Object toReceiptPage() throws Exception {
        // 获取系统参数
        PageData par = sys_configService.findById(new PageData());
        Pager<PageData> pager = new Pager<>();
        MemUser user = (MemUser) Jurisdiction.getSession().getAttribute(Const.SESSION_MEMUSER);
        PageData pd = this.getPageData();
        // 查询最新用户信息
        pd.put("ACCOUNT_ID", user.getACCOUNT_ID());
        user = accountService.findByIdPojo(pd);
        // 查询最新一条跑单记录
        pd.put("USER_ID", user.getACCOUNT_ID());
        PageData order = receipt_recService.getLatestRecord(pd);
        // 已发放奖金金额、本金金额
        double bonusSum = 0;
        double moneySum = 0;
        if (order != null) {
            // 根据订单ID查询已发放的奖金的总额
            pd.put("ORDER_PID", order.get("RECEIPT_REC_ID"));
            pd.put("STATUS", "已完成");
            bonusSum = Convert.toDouble(profit_recordService.getBonusSumByOrderId(pd).get("BONUS_SUM"));
            moneySum = Convert.toDouble(profit_recordService.getMoneySumByOrderId(pd).get("MONEY_SUM"));
        }
        pd.put("bonusSum", bonusSum);
        pd.put("moneySum", moneySum);
        pd.put("order", order);
        pd.put("par", par);
        pd.put("user", user);
        // 页码
        int num = Integer.parseInt(pd.get("num").toString());
        // 每页数据条数
        int pageSize = Integer.parseInt(pd.get("size").toString());
        // 查询正在跑单的记录
        pd.put("USER_ID", user.getACCOUNT_ID());
        pd.put("STATUS", "已完成");
        List<PageData> billList = null;
        // 0：正在执行、1：历史订单
        if ("1".equals(pd.get("pdType").toString())) {
            billList = receipt_recService.listByUserId(pd);
        } else {
            billList = profit_recordService.listByUserIdAndStatus(pd);
        }
        // 第 N 页
        pager.setCurentPageIndex(num);
        // 每页 N 条
        pager.setCountPerpage(pageSize);
        pager.setBigList(billList);
        // 得到小的集合(分页的当前页的记录)
        List<PageData> curPageData = pager.getSmallList();
        // 得到总页数
        int totalPage = pager.getPageCount();
        pd.put("curPageData", curPageData);
        pd.put("totalPage", totalPage);
        return pd;
    }

    /**
     * 功能描述：请求提币
     *
     * @author Ajie
     * @date 2020/3/31 0031
     */
    @RequestMapping(value = "cashMoney")
    @ResponseBody
    public String cashMoney() throws Exception {
        PageData pd = this.getPageData();
        PageData par = sys_configService.findById(pd);
        // 获取session存的用户
        MemUser memUser = (MemUser) Jurisdiction.getSession().getAttribute(Const.SESSION_MEMUSER);
        // 根据用户id 查询数据
        MemUser user = accountService.findByIdPojo(pd);
        int dayMaxWithdrawalsCount = Convert.toInt(par.get("DAY_MAX_WITHDRAWALS_COUNT"));
        if (user.getDAY_WITHDRAWALS_COUNT() >= dayMaxWithdrawalsCount) {
            return "8";
        }
        if (!user.getACCOUNT_ID().equals(memUser.getACCOUNT_ID())) {
            return "0";
        }
        // 提现手续费、上级赠送的USDT、最低提现
        double charge = Convert.toDouble(par.get("CHARGE_FOR_WITHDRAWAL"));
        double minCash = Convert.toDouble(par.get("MIN_CASH"));
        // 提币数量、二级密码
        double usdtNum = Convert.toDouble(pd.getString("moneyAmount"));
        String securityPassword = pd.getString("securityPassword");
        // 开始校验
        if (Tools.isEmpty(securityPassword)) {
            return "1";
        }
        securityPassword = StringUtil.applySha256(securityPassword);
        // usdt不够, 直接提示非法参数
        if (user.getUSDT_WALLET() < charge) {
            return "2";
        }
        // 是否最低提现
        if (usdtNum < minCash) {
            return "7";
        }
        // 密码校验 提示具体值
        if (!user.getSECURITY_PASSWORD().equals(securityPassword)) {
            return "4";
        }
        // 受理时间段校验
        String[] timeSlot = StringUtil.strList2(par.getString("CASH_TIME"));
        //调用判断方法
        boolean flag = DateUtil.isBelongTime(timeSlot[0], timeSlot[1]);
        if (!flag) {
            return "5";
        }
        // 累积充值多少U 才能提现
        double withdrawalConditions = Convert.toDouble(par.get("WITHDRAWAL_CONDITIONS"));
        // 查询用户累积充值总和
        pd.put("AMOUNT_TYPE", "充值");
        pd.put("USER_ID", user.getACCOUNT_ID());
        double sum = Convert.toDouble(usdt_recService.findByUserIdAndType(pd).get("MONEY_SUM"));
        if (sum < withdrawalConditions) {
            return "6";
        }
        // 计算到账数量，异步存数据库
        double result = NumberUtil.sub(usdtNum, charge);
        pd.put("money", usdtNum);
        pd.put("actualArrival", result);
        pd.put("charge", charge);
        pd.put("USER_NAME", user.getUSER_NAME());
        pd.put("USER_ID", user.getACCOUNT_ID());
        pd.put("WALLET_ADDRESS", user.getUSDT_IN_ADDRESS());
        accountService.updateWithdrawMoney(pd);
        // 每天提现累积+1
        accountService.updateDayWithdrawalsCount(user, true);
        return "success";
    }

    /**
     * 功能描述：请求usdt转账
     *
     * @author Ajie
     * @date 2020/3/31 0031
     */
    @RequestMapping(value = "usdtTransfer")
    @ResponseBody
    public String usdtTransfer() throws Exception {
        PageData pd = this.getPageData();
        PageData par = sys_configService.findById(pd);
        // 获取session存的用户
        MemUser user = (MemUser) Jurisdiction.getSession().getAttribute(Const.SESSION_MEMUSER);
        // 查询最新一条跑单记录
        pd.put("USER_ID", user.getACCOUNT_ID());
        PageData order = receipt_recService.getLatestRecord(pd);
        if (order != null) {
            // 订单未完成无法转账
            if ("收益中".equals(order.getString("STATUS"))) {
                return "5";
            }
        }
        pd.put("ACCOUNT_ID", user.getACCOUNT_ID());
        user = accountService.findByIdPojo(pd);
        // 不能给自己转账
        if (user.getUSER_NAME().equals(pd.get("payee"))) {
            return "4";
        }
        // 转账数量、二级密码
        double usdtNum = Convert.toDouble(pd.getString("money"));
        // 非法请求
        if (usdtNum <= 0) {
            return "0";
        }
        int multiple = Convert.toInt(par.get("TRANSFER_MULTIPLE"));
        if (usdtNum % multiple != 0) {
            return "6";
        }
        String securityPassword = pd.getString("password");
        // 注册时赠送的usdt
//        double giveUsdt = Convert.toDouble(par.getString("EACH_FULL_FUND"));
        // 开始校验
        if (Tools.isEmpty(securityPassword)) {
            return "0";
        }
        securityPassword = StringUtil.applySha256(securityPassword);
        // usdt不够
        if (user.getUSDT_WALLET() < usdtNum) {
            return "1";
        }
        // 密码校验 提示具体值
        if (!user.getSECURITY_PASSWORD().equals(securityPassword)) {
            return "2";
        }
        // 根据用户名查询收款人
        pd.put("USER_NAME", pd.get("payee"));
        MemUser payee = accountService.findByName(pd);
        if (payee == null) {
            return "3";
        }
        // 验证收款人是否为我下级
        boolean isYes = StrUtil.contains(payee.getRE_PATH(), user.getACCOUNT_ID());
        if (!isYes) {
            return "7";
        }
        // 存数据库
        pd.put("money", usdtNum);
        pd.put("USER_NAME", user.getUSER_NAME());
        pd.put("USER_ID", user.getACCOUNT_ID());
        pd.put("HE_NAME", payee.getUSER_NAME());
        pd.put("HE_ID", payee.getACCOUNT_ID());
        accountService.updateUsdtTransfer(pd);
        // 调用用户状态检查任务
        QuartzManager.runAJobNow(Const.USER_STATUS_CHECK_TASK);
        return "success";
    }

    /**
     * 功能描述：请求分享基金转账
     *
     * @author Ajie
     * @date 2020/3/31 0031
     */
    @RequestMapping(value = "fundTransfer")
    @ResponseBody
    public String fundTransfer() throws Exception {
        PageData pd = this.getPageData();
        PageData par = sys_configService.findById(new PageData());
        // 获取session存的用户
        MemUser user = (MemUser) Jurisdiction.getSession().getAttribute(Const.SESSION_MEMUSER);
        pd.put("ACCOUNT_ID", user.getACCOUNT_ID());
        user = accountService.findByIdPojo(pd);
        // 不能给自己转账
        if (user.getUSER_NAME().equals(pd.get("payee"))) {
            return "4";
        }
        // 转账数量、二级密码
        double usdtNum = Convert.toDouble(pd.getString("money"));
        // 非法请求
        if (usdtNum <= 0) {
            return "0";
        }
        int multiple = Convert.toInt(par.get("TRANSFER_MULTIPLE"));
        if (usdtNum % multiple != 0) {
            return "6";
        }
        String securityPassword = pd.getString("password");
        // 开始校验
        if (Tools.isEmpty(securityPassword)) {
            return "0";
        }
        securityPassword = StringUtil.applySha256(securityPassword);
        // 分享基金不够
        if (user.getSHARE_FUND() < usdtNum) {
            return "1";
        }
        // 密码校验 提示具体值
        if (!user.getSECURITY_PASSWORD().equals(securityPassword)) {
            return "2";
        }
        // 根据用户名查询收款人
        pd.put("USER_NAME", pd.get("payee"));
        MemUser payee = accountService.findByName(pd);
        if (payee == null) {
            return "3";
        }
        // 存数据库
        pd.put("money", usdtNum);
        pd.put("USER_NAME", user.getUSER_NAME());
        pd.put("USER_ID", user.getACCOUNT_ID());
        pd.put("HE_NAME", payee.getUSER_NAME());
        pd.put("HE_ID", payee.getACCOUNT_ID());
        accountService.updateFundransfer(pd);
        return "success";
    }

    /**
     * 功能描述：请求娱乐积分转账
     *
     * @author Ajie
     * @date 2020年4月8日17:57:57
     */
    @RequestMapping(value = "integralTransfer")
    @ResponseBody
    public String integralTransfer() throws Exception {
        PageData pd = this.getPageData();
        PageData par = sys_configService.findById(new PageData());
        // 获取session存的用户
        MemUser user = (MemUser) Jurisdiction.getSession().getAttribute(Const.SESSION_MEMUSER);
        pd.put("ACCOUNT_ID", user.getACCOUNT_ID());
        user = accountService.findByIdPojo(pd);
        // 不能给自己转账
        if (user.getUSER_NAME().equals(pd.get("payee"))) {
            return "4";
        }
        // 转账数量、二级密码
        double usdtNum = Convert.toDouble(pd.getString("money"));
        // 非法请求
        if (usdtNum <= 0) {
            return "0";
        }
        int multiple = Convert.toInt(par.get("TRANSFER_MULTIPLE"));
        if (usdtNum % multiple != 0) {
            return "6";
        }
        String securityPassword = pd.getString("password");
        // 开始校验
        if (Tools.isEmpty(securityPassword)) {
            return "0";
        }
        securityPassword = StringUtil.applySha256(securityPassword);
        // 娱乐积分不够
        if (user.getENTERTAINMENT_SCORE() < usdtNum) {
            return "1";
        }
        // 密码校验 提示具体值
        if (!user.getSECURITY_PASSWORD().equals(securityPassword)) {
            return "2";
        }
        // 根据用户名查询收款人
        pd.put("USER_NAME", pd.get("payee"));
        MemUser payee = accountService.findByName(pd);
        if (payee == null) {
            return "3";
        }
        // 存数据库
        pd.put("money", usdtNum);
        pd.put("USER_NAME", user.getUSER_NAME());
        pd.put("USER_ID", user.getACCOUNT_ID());
        pd.put("HE_NAME", payee.getUSER_NAME());
        pd.put("HE_ID", payee.getACCOUNT_ID());
        accountService.updateIntegral(pd);
        return "success";
    }

    /**
     * 功能描述：请求接单
     *
     * @author Ajie
     * @date 2020/3/31 0031
     */
    @RequestMapping(value = "invest")
    @ResponseBody
    @CacheLock(prefix = "S0324")
    public R invest() throws Exception {
        PageData par = sys_configService.findById(new PageData());
        PageData pd = this.getPageData();
        MemUser user = (MemUser) Jurisdiction.getSession().getAttribute(Const.SESSION_MEMUSER);
        pd.put("ACCOUNT_ID", user.getACCOUNT_ID());
        user = accountService.findByIdPojo(pd);
        /*boolean isReal = checkReal(user);
        // 未认证
        if (!isReal) {
            return R.error().message("未实名认证");
        }*/
        // 是否是休息号
        if ("1".equals(user.getIS_REST())) {
            return R.error().message("请先充值" + par.get("RELEASE_FROM_REST") + "USDT激活后在接单");
        }
        // 获取前台传过来的 金额、密码
        double money = Convert.toDouble(pd.get("money"));
        int orderMultiple = Convert.toInt(par.get("ORDER_MULTIPLE"));
        if (money % orderMultiple != 0) {
            return R.error().message("必须是" + orderMultiple + "的倍数");
        }
        // 最小投资
        double minInvestment = Convert.toDouble(par.get("MIN_INVESTMENT"));
        if (money < minInvestment) {
            return R.error().message("最小投资" + minInvestment);
        }
        // 投资上限
        double investmentCeiling = Convert.toDouble(par.get("INVESTMENT_CEILING"));
        if (money > investmentCeiling) {
            return R.error().message("最大投资" + minInvestment);
        }
        String password = pd.getString("password");
        // 提示非法请求
        if (Tools.isEmpty(password) || money < 1) {
            return R.error().message("非法请求");
        }
        // 密码错误
        password = StringUtil.applySha256(password);
        if (!user.getSECURITY_PASSWORD().equals(password)) {
            return R.error().message("密码错误");
        }
        // 金额不足
        if (user.getUSDT_WALLET() < money) {
            return R.error().message("金额不足");
        }
        // 查询最新订单 是否已经结算
        pd.put("USER_ID", user.getACCOUNT_ID());
        PageData orderRec = receipt_recService.getLatestRecord(pd);
        if (orderRec != null) {
            String state = orderRec.getString("STATUS");
            if ("收益中".equals(state)) {
                return R.error().message("上一轮未结算");
            }
        }
        // 每轮跑单时间 (小时)
        String eachHour = par.getString("RUN_TIME_EACH");
        String dueTime = DateUtil.getAddHourDate(DateUtil.getTime(), eachHour);
        // 扣除用户usdrt并创建跑单记录，
        pd.put("USER_NAME", user.getUSER_NAME());
        pd.put("money", money);
        pd.put("dueTime", dueTime);
        accountService.updateInvestMoneyAndNewRec(pd);
        // 取订单ID 计算收益、创建对应记录
        PageData order = receipt_recService.getLatestRecord(pd);
        String orderId = order.getString("RECEIPT_REC_ID");
        calculationBonus(orderId, money);
        return R.ok();
    }

    /**
     * 功能描述：随机一个跑单场次
     *
     * @return 场次名称
     * @author Ajie
     * @date 2020/4/1 0001
     */
    public String randomName() throws Exception {
        // 查看所有场次
        List<PageData> list = run_listService.listAll(new PageData());
        // 随机一个
        int index = RandomUtil.randomInt(0, list.size());
        return list.get(index).getString("NAME");
    }

    /**
     * 功能描述：计算收益
     *
     * @param orderId 订单ID
     * @param money   投资金额
     * @author Ajie
     * @date 2020/4/2 0002
     */
    public void calculationBonus(String orderId, double money) throws Exception {
        // 取系统参数配置
        PageData pd = new PageData();
        PageData par = sys_configService.findById(pd);
        // 取用户信息
        MemUser user = (MemUser) Jurisdiction.getSession().getAttribute(Const.SESSION_MEMUSER);
        pd.put("ACCOUNT_ID", user.getACCOUNT_ID());
        user = accountService.findByIdPojo(pd);
        // 佣金点、娱乐积金、分享基金的利润比例
        double commission = NumberUtil.div(user.getCOMMISSION(), 100);
        double entertainmentScore = Convert.toDouble(par.get("ENTERTAINMENT_SCORE"));
        entertainmentScore = NumberUtil.div(entertainmentScore, 100);
        double shareFund = Convert.toDouble(par.get("SHARE_FUND"));
        shareFund = NumberUtil.div(shareFund, 100);
        // 计算用户收益
        double profit, entertainmentNum, fund;
        profit = NumberUtil.mul(money, commission);
        entertainmentNum = NumberUtil.mul(profit, entertainmentScore);
        fund = NumberUtil.mul(profit, shareFund);
        // 异步执行写库操作
        MemUser finalUser = user;
        ThreadUtil.execute(() -> {
            try {
                distributionBonus(money, finalUser, orderId);
                // 增加娱乐积分和分享基金待发放记录
                addWalletRecord(finalUser, entertainmentNum, fund, "", "", "接单收益");
                // 给所有符合条件的上级发放极差
                gapAward(finalUser, money);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * 功能描述：随机分配奖金,并创建记录
     *
     * @param money    本金
     * @param user     用户信息
     * @param orderPid 所属跑单ID
     * @author Ajie
     * @date 2020/4/3 0003
     */
    public void distributionBonus(double money, MemUser user, String orderPid) throws Exception {
        // 现在时间
        String nowTime = DateUtil.getTime();
        // 查询系统参数
        PageData par = sys_configService.findById(new PageData());
        // 转为整数
        int moneyInt = Convert.toInt(NumberUtil.mul(money, 100));

        // 每轮跑单时间 (小时)
        int eachHour = Convert.toInt(par.get("RUN_TIME_EACH"));
        // 随机多少分钟发放一次收益
        int minMinute = Convert.toInt(par.get("MIN_MINUTE"));
        int maxMinute = Convert.toInt(par.get("MAX_MINUTE"));
        // 范围随机分钟
        int minute = RandomUtil.randomInt(minMinute, maxMinute);
        // 计算 N 小时内 会产生多少条记录 并且 转整数
        int count = Convert.toInt(NumberUtil.div(NumberUtil.mul(eachHour, 60), minute));
//        count -= 1;
        if (count <= 0) {
            count = 1;
        }
        // 计算本金单笔最大金额
        int maxMoney = Convert.toInt(NumberUtil.div(moneyInt, count));
        logger.info("本金：" + moneyInt + " 个数：" + count + " 最大金额：" + maxMoney * 1.2 + "最少金额: " + 1);
        // 本金列表
        List<Double> moneyList = RedEnvelope.getRedPackage(moneyInt, count, (int) (maxMoney * 1.2), 1);
        // 循环写入数据库
        for (Double amount : moneyList) {
            // 奖金发放时间累加
            nowTime = DateUtil.getAddMinuteDate(nowTime, String.valueOf(minute));
            String releaseTime = nowTime;
            double profitBonus = incomeCalculation(amount, user);
            ThreadUtil.execute(() -> {
                try {
                    addProfitRecord(user.getUSER_NAME(), user.getACCOUNT_ID(), amount, profitBonus, orderPid, releaseTime);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }

    /**
     * 功能描述：根据本金计算收益
     *
     * @author Ajie
     * @date 2020/4/15 0015
     */
    public double incomeCalculation(double money, MemUser user) throws Exception {
        // 系统参数
        PageData par = sys_configService.findById(new PageData());
        // 佣金点、娱乐积金、分享基金的利润比例
        double commission = NumberUtil.div(user.getCOMMISSION(), 100);
        double entertainmentScore = Convert.toDouble(par.get("ENTERTAINMENT_SCORE"));
        entertainmentScore = NumberUtil.div(entertainmentScore, 100);
        double shareFund = Convert.toDouble(par.get("SHARE_FUND"));
        shareFund = NumberUtil.div(shareFund, 100);

        double result = NumberUtil.mul(money, commission);
        // 需要扣除的手续费
        double charge = NumberUtil.mul(result, NumberUtil.add(entertainmentScore, shareFund));
        // 返回扣除手续费的金额
        return NumberUtil.sub(result, charge);
    }

    /**
     * 功能描述：新增跑单收益记录
     *
     * @param userName    用户名
     * @param userId      用户id
     * @param assets      本金
     * @param earnings    收益
     * @param orderPid    所属跑单ID
     * @param releaseTime 奖金发放时间
     * @author Ajie
     * @date 2020/4/3 0003
     */
    public void addProfitRecord(String userName, String userId, Double assets, Double earnings, String orderPid, String releaseTime) throws Exception {
        PageData pd = new PageData();
        pd.put("GMT_MODIFIED", releaseTime);    //发放时间
        pd.put("USER_NAME", userName);    //用户名
        pd.put("USER_ID", userId);    //用户ID
        pd.put("AMOUNT_TYPE", randomName());    //金额类型
        pd.put("ASSETS", assets);    //本金
        pd.put("EARNINGS", earnings);    //收益
        pd.put("STATUS", "待发放");    //状态
        pd.put("ACTUAL_ARRIVAL", assets + earnings);    //实际到账
        pd.put("ORDER_PID", orderPid);    //所属跑单ID
        profit_recordService.save(pd);
    }

    /**
     * 功能描述：增加娱乐积金和分享基余额
     *
     * @param score 娱乐积分
     * @param fund  分享基金
     * @author Ajie
     * @date 2020/4/3 0003
     */
    public void addMoney(MemUser user, double score, double fund) throws Exception {
        // 用户表
        PageData pd = new PageData();
        pd.put("tag", "+");
        pd.put("ACCOUNT_ID", user.getACCOUNT_ID());
        pd.put("GMT_MODIFIED", DateUtil.getTime());
        // 增加娱乐积金
        pd.put("money", score);
        accountService.updateScoreNumber(pd);
        // 增加分享基金
        pd.put("money", fund);
        accountService.updateSharedFundsNumber(pd);
    }

    /**
     * 功能描述：新增【待发放】的娱乐积分和分享基金钱包记录
     *
     * @param user   用户信息
     * @param score  娱乐积分
     * @param fund   分享基金
     * @param heName 对方用户名
     * @param heId   对方ID
     * @param pdType 数据类型
     * @author Ajie
     * @date 2020-6-26 11:17:51
     */
    public void addWalletRecord(MemUser user, double score, double fund, String heName, Object heId, String pdType) throws Exception {
        PageData pd = new PageData();
        pd.put("USER_NAME", user.getUSER_NAME());    //用户名
        pd.put("USER_ID", user.getACCOUNT_ID());    //用户ID
        pd.put("AMOUNT_TYPE", pdType);    //金额类型
        pd.put("TAG", "+");    //标签 + 或者 -
        // 创建娱乐积分记录
        pd.put("MONEY", score);    //金额
        pd.put("HE_NAME", heName);    //对方用户名
        pd.put("HE_ID", heId);    //对方用户ID
        pd.put("STATUS", "待发放");    //状态
        score_recService.save(pd);
        // 创建分享基金记录
        pd.put("MONEY", fund);    //金额
        fund_recService.save(pd);
    }

    /**
     * 功能描述：检查用户是否已经实名认证通过
     *
     * @param user 用户信息
     * @return 已认证:true, 未认证：false
     * @author Ajie
     * @date 2020/4/9 0009
     */
    public boolean checkReal(MemUser user) throws Exception {
        PageData pd = new PageData();
        pd.put("USER_ID", user.getACCOUNT_ID());
        pd = realService.findByUserId(pd);
        if (pd == null) {
            return false;
        }
        String status = pd.get("STATUS").toString();
        // 0：已认证 1：未认证
        return "0".equals(status);
    }

    /**
     * 功能描述：给上级发放极差奖
     *
     * @param user  投资用户信息
     * @param money 投资金额
     * @author Ajie
     * @date 2020/4/10 0010
     */
    public void gapAward(MemUser user, double money) throws Exception {
        if ("10000".equals(user.getACCOUNT_ID())) {
            return;
        }
        logger.info("开始发放极差奖\n");
        PageData pd = new PageData();
        // 查询所有上级
        pd.put("RE_PATH", user.getRE_PATH());
        List<PageData> allSuperior = accountService.listAllSuperiorByPath(pd);
        // 定义已发放佣金点总额
        double totalCommission = user.getCOMMISSION();
        for (PageData map : allSuperior) {
            // 上级佣金点
            double supreorCommission = Convert.toDouble(map.get("COMMISSION"));
            if (supreorCommission <= totalCommission) {
                continue;
            }
            logger.info("上级：" + map.get("USER_NAME") + " 佣金点：" + map.get("COMMISSION") + "  已发放佣金点：" + totalCommission);
            // 给上级发放极差奖的比例
            double gap = NumberUtil.sub(supreorCommission, totalCommission);
            // 计算上级得到的奖金
            double bonus = NumberUtil.mul(money, NumberUtil.div(gap, 100));
            // 执行拆分奖金，10%进入娱乐积分，10%进入分享基金，剩下80%进入USDT钱包
            ThreadUtil.execute(() -> {
                try {
                    splitBonus(map, bonus, user, money);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            // 累积到已发放佣金点总额中
            totalCommission = NumberUtil.add(totalCommission, gap);
        }
    }

    /**
     * 功能描述：线下充值
     *
     * @return 处理结果
     * @author Ajie
     * @date 2020/4/27 0027
     */
    @RequestMapping(value = "/applyRecharge")
    @ResponseBody
    public R applyRecharge() throws Exception {
        // 传过来的参数
        PageData pd = this.getPageData();
        // 系统参数
        PageData par = sys_configService.findById(new PageData());
        MemUser user = (MemUser) Jurisdiction.getSession().getAttribute(Const.SESSION_MEMUSER);
        double min = Convert.toDouble(par.get("MIN_RECHARGE"));
        double max = Convert.toDouble(par.get("MAX_RECHARGE"));
        double mul = Convert.toDouble(par.get("RECHARGE_MULTIPLE"));
        // 充值金额
        double money = Convert.toDouble(pd.get("money"));
        // 安全密码
        String securityPassword = pd.getString("securityPassword");
        boolean isScope = money < 0 || money < min || money > max || money % mul != 0;
        if (isScope) {
            return R.parError().message("充值范围错误");
        }
        if (Tools.isEmpty(securityPassword)) {
            return R.parError();
        }
        if (Tools.isEmpty(pd.getString("voucher"))) {
            return R.parError();
        }
        // 安全密码加密后校验
        securityPassword = StringUtil.applySha256(securityPassword);
        if (!securityPassword.equals(user.getSECURITY_PASSWORD())) {
            return R.parError().message("安全密码错误");
        }
        // 创建一条待审核的充值记录
        pd.put("USER_NAME", user.getUSER_NAME());
        pd.put("ACCOUNT_ID", user.getACCOUNT_ID());
        accountService.updateOffLineRechargeMoneyAndRec(pd);
        return R.ok().message("等待审核");
    }

    /**
     * 功能描述：把极差奖拆分成 10%进入娱乐积分，10%进入分享基金，剩下80%进入USDT钱包
     *
     * @param user       用户信息
     * @param bonus      奖金
     * @param sourceUser 来源（下级投资人的用户信息）
     * @param money      本金
     * @author Ajie
     * @date 2020/6/26 0026
     */
    public void splitBonus(PageData user, double bonus, MemUser sourceUser, double money) throws Exception {
        // 查询系统参数
        PageData par = sys_configService.findById(new PageData());
        // 娱乐积金、分享基金的利润比例
        double entertainmentScore = Convert.toDouble(par.get("ENTERTAINMENT_SCORE"));
        entertainmentScore = NumberUtil.div(entertainmentScore, 100);
        double shareFund = Convert.toDouble(par.get("SHARE_FUND"));
        shareFund = NumberUtil.div(shareFund, 100);
        // 计算用户收益
        double usdt, entertainmentNum, fund;
        entertainmentNum = NumberUtil.mul(bonus, entertainmentScore);
        fund = NumberUtil.mul(bonus, shareFund);
        usdt = bonus - (entertainmentNum + fund);
        // 创建一条待发放的usdt极差奖记录
        this.addUsdtRecord(user, usdt, "极差奖", "待发放", "+", money, sourceUser.getUSER_NAME(), sourceUser.getACCOUNT_ID(), "");
        // 增加娱乐积分和分享基金待发放记录
        MemUser user1 = accountService.findByIdPojo(user);
        addWalletRecord(user1, entertainmentNum, fund, sourceUser.getUSER_NAME(), sourceUser.getACCOUNT_ID(), "极差奖");
    }


}

