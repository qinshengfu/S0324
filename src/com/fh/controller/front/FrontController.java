package com.fh.controller.front;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import com.fh.controller.base.BaseController;
import com.fh.entity.MemUser;
import com.fh.entity.result.R;
import com.fh.service.front.AccountManager;
import com.fh.service.front.Message_FeedbackManager;
import com.fh.service.front.RealManager;
import com.fh.service.front.Sys_configManager;
import com.fh.service.record.Sys_FAQManager;
import com.fh.service.record.Sys_newsManager;
import com.fh.util.*;
import com.fh.util.express.ImageUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.session.Session;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * 功能描述：前台页面登录后才能跳转的页面
 *
 * @author Ajie
 * @date 2019/11/26 0026
 */
@Controller
@RequestMapping(value = "/front")
@Slf4j
public class FrontController extends BaseController {


    // 新闻公告
    @Resource(name = "sys_newsService")
    private Sys_newsManager sys_newsService;
    // 用户管理
    @Resource(name = "accountService")
    private AccountManager accountService;
    // 系统参数
    @Resource(name = "sys_configService")
    private Sys_configManager sys_configService;
    // 实名认证
    @Resource(name = "realService")
    private RealManager realService;
    /**
     * 留言反馈
     */
    @Resource(name = "message_feedbackService")
    private Message_FeedbackManager message_feedbackService;
    /**
     * 常见问题
     */
    @Resource(name = "sys_faqService")
    private Sys_FAQManager sys_faqService;

    /**
     * 功能描述：访问前台【账户安全】页面
     *
     * @author Ajie
     * @date 2020年3月27日16:08:57
     */
    @RequestMapping(value = "to_accountSecurity")
    private ModelAndView toAccountSecurity() throws Exception {
        ModelAndView mv = getModelAndView();
        mv.setViewName("front/center/account");
        return mv;
    }

    /**
     * 功能描述：访问前台【个人中心】页面
     *
     * @author Ajie
     * @date 2020年3月27日16:08:57
     */
    @RequestMapping(value = "to_center")
    private ModelAndView toCenter() throws Exception {
        // 获取系统参数
        PageData par = sys_configService.findById(new PageData());
        ModelAndView mv = getModelAndView();
        MemUser user = (MemUser) Jurisdiction.getSession().getAttribute(Const.SESSION_MEMUSER);
        PageData pd = new PageData();
        pd.put("ACCOUNT_ID", user.getACCOUNT_ID());
        user = accountService.findByIdPojo(pd);
        // 用户升级条件，需要团队活跃多少人
        int userUpgradeCost = Convert.toInt(par.get("TEAM_ACTIVE_NUMBER"));
        // 查询团队活跃人数
        int activeNumber = Convert.toInt(accountService.findByActiveTeams(pd).get("TEAM_SUM"));
        // 最高佣金点，升一级增加多少佣金点
        double maxCommission = Convert.toDouble(par.get("COMMISSION_CEILING"));
        double commissionRise = Convert.toDouble(par.get("COMMISSION_RISE"));
        int maxRank = Convert.toInt(NumberUtil.div(maxCommission, commissionRise));
        // 计算 距离下一级进度剩下百分之多少
        int distance = upgradeProgress(userUpgradeCost, activeNumber, user.getUSER_RANK(), maxRank);
        // 查询用户认证信息
        pd.put("USER_ID", user.getACCOUNT_ID());
        pd = realService.findByUserId(pd);
        mv.setViewName("front/center/center");
        mv.addObject("user", user);
        mv.addObject("distance", distance);
        mv.addObject("pd", pd);
        mv.addObject("flag", "my");
        return mv;
    }

    /**
     * 功能描述：升级进度计算，返回距离当前进度是多少
     *
     * @param cost         升级条件
     * @param acitveNumber 团队人数
     * @param userRank     用户当前级别
     * @param maxRank      最高级
     * @return 当前进度百分比
     * @author Ajie
     * @date 2020/4/6 0006
     */
    public int upgradeProgress(int cost, int acitveNumber, int userRank, int maxRank) {
        // 最高级
        if (userRank >= maxRank) {
            return 100;
        }
        // 下一级
        int nextLevel = Convert.toInt(NumberUtil.add(userRank, 1));
        // 下一级需要多少团队人数
        int teamNumber = Convert.toInt(NumberUtil.mul(nextLevel, cost));
        // 减去当前团队人数
        int subTeam = Convert.toInt(NumberUtil.sub(teamNumber, acitveNumber));
        // 用结果 除 升级条件
        double gap = NumberUtil.div(subTeam, cost);
        // 用 （1 - 差多少升级）* 100
        return Convert.toInt(NumberUtil.mul(NumberUtil.sub(1, gap), 100));
    }

    /**
     * 功能描述：访问前台【我的收币地址】页面
     *
     * @author Ajie
     * @date 2020年3月27日16:08:57
     */
    @RequestMapping(value = "to_coinCollection")
    private ModelAndView toCoinCollection() throws Exception {
        ModelAndView mv = getModelAndView();
        mv.setViewName("front/center/coin-collection");
        return mv;
    }

    /**
     * 功能描述：访问前台【资料完善】页面
     *
     * @author Ajie
     * @date 2020年3月27日16:08:57
     */
    @RequestMapping(value = "to_dataPerfection")
    private ModelAndView toDataPerfection() throws Exception {
        ModelAndView mv = getModelAndView();
        PageData pd = new PageData();
        MemUser user = (MemUser) Jurisdiction.getSession().getAttribute(Const.SESSION_MEMUSER);
        pd.put("ACCOUNT_ID", user.getACCOUNT_ID());
        user = accountService.findByIdPojo(pd);
        mv.setViewName("front/center/data-perfection");
        mv.addObject("user", user);
        return mv;
    }

    /**
     * 功能描述：访问前台【我的团队】页面
     *
     * @author Ajie
     * @date 2020年3月27日16:08:57
     */
    @RequestMapping(value = "to_myTeam")
    private ModelAndView toMyTeam() throws Exception {
        ModelAndView mv = getModelAndView();
        PageData pd = new PageData();
        MemUser user = (MemUser) Jurisdiction.getSession().getAttribute(Const.SESSION_MEMUSER);
        pd.put("ACCOUNT_ID", user.getACCOUNT_ID());
        user = accountService.findByIdPojo(pd);
        // 查询我的团队 只显示直推的
        List<MemUser> myTeamList = accountService.findByByIdOnPath(pd);
        // 查询团队活跃人数
        pd = accountService.findByActiveTeams(pd);
        int activeNumber = Convert.toInt(pd.get("TEAM_SUM"));
        // 计算不活跃人数
        int teanNumber = user.getTEAM_NUMBER();
        int notActiveNumber = teanNumber - activeNumber;
        pd.put("activeNum", activeNumber);
        pd.put("notActiveNum", notActiveNumber);
        // 查询团队业绩余额（团队的总入金（充值）减去团队提现的金额）
        pd.put("ACCOUNT_ID", user.getACCOUNT_ID());
        pd.put("teamPerformance", accountService.findByIdTeamPerformance(pd).get("TEAM_PERFORMANCE"));
        mv.setViewName("front/center/myteam");
        mv.addObject("user", user);
        mv.addObject("teamList", myTeamList);
        mv.addObject("pd", pd);
        return mv;
    }

    /**
     * 功能描述：访问前台【新闻动态】页面
     *
     * @author Ajie
     * @date 2020年3月27日16:08:57
     */
    @RequestMapping(value = "to_news")
    private ModelAndView toNews() throws Exception {
        ModelAndView mv = getModelAndView();
        List<PageData> newsList = sys_newsService.listAll(new PageData());
        mv.setViewName("front/center/news");
        mv.addObject("newsList", newsList);
        return mv;
    }

    /**
     * 功能描述：访问前台【新闻详情】页面
     *
     * @author Ajie
     * @date 2020年3月27日16:14:23
     */
    @RequestMapping(value = "to_newsDetail/{id}")
    private ModelAndView toNewsDetail(@PathVariable String id) throws Exception {
        ModelAndView mv = getModelAndView();
        PageData pd = new PageData();
        pd.put("SYS_NEWS_ID", id);
        pd = sys_newsService.findById(pd);
        mv.setViewName("front/center/news-detail");
        mv.addObject("pd", pd);
        return mv;
    }

    /**
     * 功能描述：访问前台【问题反馈】页面
     *
     * @author Ajie
     * @date 2020年3月27日16:14:23
     */
    @RequestMapping(value = "to_problem")
    private ModelAndView toProblem() {
        ModelAndView mv = getModelAndView();
        mv.setViewName("front/center/problem");
        return mv;
    }

    /**
     * 功能描述：访问前台【常见问题详情】页面
     *
     * @author Ajie
     * @date 2020年3月27日16:14:23
     */
    @RequestMapping(value = "toFAQDetails/{id}")
    private ModelAndView toFAQDetails(@PathVariable String id) throws Exception {
        ModelAndView mv = getModelAndView();
        PageData pd = new PageData();
        pd.put("SYS_FAQ_ID", id);
        pd = sys_faqService.findById(pd);
        mv.setViewName("front/center/faq-detail");
        mv.addObject("pd", pd);
        return mv;
    }

    /**
     * 功能描述：访问前台【问题反馈详情】页面
     *
     * @author Ajie
     * @date 2020年6月22日11:55:00
     */
    @RequestMapping(value = "toFeedbackDetails/{id}")
    private ModelAndView toFeedbackDetails(@PathVariable(value = "id") String id) throws Exception {
        ModelAndView mv = getModelAndView();
        PageData pd = new PageData();
        pd.put("MESSAGE_FEEDBACK_ID", id);
        pd = message_feedbackService.findById(pd);
        mv.setViewName("front/center/huifu-pro");
        mv.addObject("pd", pd);
        return mv;
    }

    /**
     * 功能描述：访问前台【反馈历史】页面
     *
     * @author Ajie
     * @date 2020年3月27日16:14:23
     */
    @RequestMapping(value = "to_problemHistory")
    private ModelAndView toProblemHistory() {
        ModelAndView mv = getModelAndView();
        mv.setViewName("front/center/problem-history");
        return mv;
    }

    /**
     * 功能描述：访问前台【邀请码】页面
     *
     * @author Ajie
     * @date 2020年3月27日16:14:23
     */
    @RequestMapping(value = "to_share")
    private ModelAndView toShare(HttpServletRequest request) {
        MemUser user = (MemUser) request.getSession().getAttribute(Const.SESSION_MEMUSER);
        ModelAndView mv = getModelAndView();
        mv.setViewName("front/center/share");
        mv.addObject("user", user);
        return mv;
    }

    /**
     * 功能描述：访问前台【实名认证】页面
     *
     * @author Ajie
     * @date 2020年4月9日09:18:29
     */
    @RequestMapping(value = "to_realName")
    private ModelAndView toRealName(HttpServletRequest request) throws Exception {
        MemUser user = (MemUser) request.getSession().getAttribute(Const.SESSION_MEMUSER);
        ModelAndView mv = getModelAndView();
        PageData pd = new PageData();
        pd.put("USER_ID", user.getACCOUNT_ID());
        pd = realService.findByUserId(pd);
        mv.setViewName("front/center/real_name");
        mv.addObject("pd", pd);
        return mv;
    }

    /**
     * 功能描述：访问前台【我的USDT地址】页面
     *
     * @author Ajie
     * @date 2020年6月19日17:20:26
     */
    @RequestMapping(value = "toMyUsdtAddress")
    private ModelAndView toMyUsdtAddress() throws Exception {
        ModelAndView mv = getModelAndView();
        PageData pd = this.getPageData();
        MemUser user = (MemUser) Jurisdiction.getSession().getAttribute(Const.SESSION_MEMUSER);
        pd.put("ACCOUNT_ID", user.getACCOUNT_ID());
        user = accountService.findByIdPojo(pd);
        mv.setViewName("front/center/chang-coin");
        mv.addObject("user", user);
        return mv;
    }

    /**
     * 功能描述：访问前台【登录密码】页面
     *
     * @author Ajie
     * @date 2020年6月19日17:20:26
     */
    @RequestMapping(value = "toLoginPassword")
    private ModelAndView toLoginPassword() {
        ModelAndView mv = getModelAndView();
        mv.setViewName("front/center/chang-loginpass");
        return mv;
    }

    /**
     * 功能描述：访问前台【二级密码】页面
     *
     * @author Ajie
     * @date 2020年6月19日17:20:26
     */
    @RequestMapping(value = "toTwoPassword")
    private ModelAndView toTwoPassword() {
        ModelAndView mv = getModelAndView();
        mv.setViewName("front/center/chang-two");
        return mv;
    }

    /**
     * 功能描述：访问前台【常见问题】页面
     *
     * @author Ajie
     * @date 2020年6月19日17:20:26
     */
    @RequestMapping(value = "toFAQ")
    private ModelAndView toFAQ() {
        ModelAndView mv = getModelAndView();
        mv.setViewName("front/center/comm-pro");
        return mv;
    }

    /**
     * 功能描述：生成二维码
     *
     * @author Ajie
     * @date 2020/4/24 0024
     */
    @RequestMapping(value = "/qr_code")
    public void qrCode(HttpServletResponse response, HttpServletRequest request) throws Exception {
        MemUser user = (MemUser) request.getSession().getAttribute(Const.SESSION_MEMUSER);
        // 获取请求路径
        String path = request.getContextPath();
        String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
                + path + "/";
        String context = basePath + "release/toRegister.do?tag=" + user.getINVITATION_CODE();
        // 通过输出流把邀请码输出到页面
        ServletOutputStream out = response.getOutputStream();
        // 调用工具类
        TwoDimensionCode.encoderQRCode(context, out);
    }

    /**
     * 暂时不需要这个功能
     * 功能描述：访问前台【额度兑换】页面
     *
     * @author Ajie
     * @date 2020年3月27日16:14:23
     */
    @RequestMapping(value = "to_exchange")
    private ModelAndView toExchange() throws Exception {
        ModelAndView mv = getModelAndView();
        mv.setViewName("front/receipt/exchange");
        return mv;
    }

    /**
     * 功能描述：修改个人资料
     *
     * @author Ajie
     * @date 2020/3/30
     */
    @RequestMapping(value = "updataInfo")
    @ResponseBody
    private String updataInfo() throws Exception {
        PageData pd = this.getPageData();
        if (Tools.isEmpty(pd.getString("USDT_IN_ADDRESS"))) {
            return "0";
        }
        if (Tools.isEmpty(pd.getString("SECURITY_QUESTION"))) {
            return "0";
        }
        if (Tools.isEmpty(pd.getString("ANSWER"))) {
            return "0";
        }
        pd.put("GMT_MODIFIED", DateUtil.now());
        // 更新数据库
        accountService.edit(pd);
        return "success";
    }

    /**
     * 功能描述：修改用户密码
     *
     * @author Ajie
     * @date 2020/3/30
     */
    @RequestMapping(value = "updataPass")
    @ResponseBody
    private String updataPass() throws Exception {
        PageData pd = this.getPageData();
        MemUser user = (MemUser) Jurisdiction.getSession().getAttribute(Const.SESSION_MEMUSER);
        // 旧密码、新密码、确认密码
        String oidPass = pd.getString("oidPass").trim();
        String newPass = pd.getString("newPass").trim();
        String confirmPass = pd.getString("confirmPass").trim();
        // 密码类型
        String passType = pd.getString("flag").trim();
        // 提示非法请求
        if (Tools.isEmpty(oidPass)) {
            return "0";
        }
        if (Tools.isEmpty(newPass)) {
            return "1";
        }
        if (Tools.isEmpty(confirmPass)) {
            return "2";
        }
        // 下面开始提示具体值
        if (!newPass.equals(confirmPass)) {
            return "3";
        }
        // 密码加密
        oidPass = StringUtil.applySha256(oidPass);
        newPass = StringUtil.applySha256(newPass);
        pd = new PageData();
        if ("loginPassword".equals(passType)) {
            if (!user.getLOGIN_PASSWORD().equals(oidPass)) {
                return "4";
            }
            pd.put("LOGIN_PASSWORD", newPass);
        } else {
            if (!user.getSECURITY_PASSWORD().equals(oidPass)) {
                return "5";
            }
            pd.put("SECURITY_PASSWORD", newPass);
        }
        pd.put("ACCOUNT_ID", user.getACCOUNT_ID());
        pd.put("GMT_MODIFIED", DateUtil.now());
        // 更新数据库
        accountService.edit(pd);
        return "success";
    }

    /**
     * 功能描述：图片上传
     *
     * @param request     请求
     * @param pictureFile 文件数据
     * @return UUID后的图片路径
     * @author Ajie
     * @date 2020年4月9日11:14:37
     */
    @RequestMapping(value = "/addPic")
    @ResponseBody
    public String addUser(HttpServletRequest request, @RequestParam(name = "files") MultipartFile pictureFile) throws Exception {

        PageData pd = this.getPageData();
        // 得到上传图片的地址
        String imgPath = "";
        try {
            // ImageUtils为之前添加的工具类
            // 判断是二维码还是普通图片
            String tag = pd.getString("tag");
            if (Tools.isEmpty(tag)) {
                imgPath = ImageUtils.upload(request, pictureFile, Const.FILEPATHIMG);
            } else {
                imgPath = ImageUtils.upload(request, pictureFile, Const.FILEPATHTWODIMENSIONCODE);
            }
            if (imgPath != null) {
                logger.info("-----------------图片上传成功！");
            } else {
                logger.info("-----------------图片上传失败！");
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("----------------图片上传失败！");
        }
        logger.info("图片上传路径：" + imgPath);
        return imgPath;
    }

    /**
     * 功能描述：修改实名认证
     *
     * @author Ajie
     * @date 2020/3/30
     */
    @RequestMapping(value = "updataRealName")
    @ResponseBody
    private String updataRealName() throws Exception {
        PageData pd = this.getPageData();
        MemUser user = (MemUser) Jurisdiction.getSession().getAttribute(Const.SESSION_MEMUSER);
        // 姓名、身份证号码、正面图片地址、背面图片地址
        String fullName = pd.getString("FULL_NAME").trim();
        String idNumber = pd.getString("IDNUMBER").trim();
        String frontPic = pd.getString("FRONTPIC").trim();
        String backPic = pd.getString("BACKPIC").trim();
        boolean isEmpty = Tools.isEmpty(fullName) || Tools.isEmpty(idNumber) || Tools.isEmpty(frontPic) || Tools.isEmpty(backPic);
        // 非法请求
        if (isEmpty) {
            return "0";
        }

        // 查询用户是否已经认证了
        pd.put("USER_ID", user.getACCOUNT_ID());
        pd = realService.findByUserId(pd);
        if (pd == null) {
            // 新增一条未认证的记录
            addRealNameRecord(fullName, idNumber, frontPic, backPic, user);
        } else {
            // 更改认证记录为 未认证
            pd.put("STATUS", "1");
            pd.put("GMT_MODIFIED", Tools.date2Str(new Date()));
            realService.edit(pd);
        }
        return "success";
    }

    /**
     * 功能描述：添加实名认证记录
     *
     * @param fullName 姓名
     * @param idNumber 身份证号码
     * @param frontPic 正面图片地址
     * @param backPic  背面图片地址
     * @param user     用户信息
     * @author Ajie
     * @date 2020/4/9 0009
     */
    private void addRealNameRecord(String fullName, String idNumber, String frontPic, String backPic, MemUser user) throws Exception {
        PageData pd = new PageData();
        pd.put("USER_NAME", user.getUSER_NAME());    //用户名
        pd.put("USER_ID", user.getACCOUNT_ID());    //用户ID
        pd.put("FRONTPIC", frontPic);    //正面图片
        pd.put("BACKPIC", backPic);    //背面图片
        pd.put("FULL_NAME", fullName);    //姓名
        pd.put("IDNUMBER", idNumber);    //身份证号码
        pd.put("STATUS", "1");
        realService.save(pd);

    }

    /**
     * 功能描述：退出登录
     *
     * @return 重定向到登录页
     * @author Ajie
     * @date 2020/3/28 0028
     */
    @RequestMapping(value = "outLogin")
    private ModelAndView outLogin() {
        ModelAndView mv = this.getModelAndView();
        // 清缓存
        this.removeCache();
        mv.setViewName("front/login/login");
        return mv;
    }

    /**
     * 功能描述：清空缓存
     *
     * @author Ajie
     * @date 2020/3/27 0027
     */
    public void removeCache() {
        Session session = Jurisdiction.getSession();
        // 清空session
        session.removeAttribute(Const.SESSION_MEMUSER);

    }

    /**
     * 功能描述：保存留言反馈
     *
     * @author Ajie
     * @date 2020/6/22 0022
     */
    @RequestMapping(value = "infoFeedback")
    @ResponseBody
    public R messageFeedback() {
        PageData pd = this.getPageData();
        MemUser user = (MemUser) Jurisdiction.getSession().getAttribute(Const.SESSION_MEMUSER);
        // 反馈类型、反馈内容、反馈图片地址
        String voucher = pd.getString("voucher");
        pd.put("GMT_CREATE", DateUtil.now());
        pd.put("GMT_MODIFIED", "");
        pd.put("TITLE", "");
        pd.put("STATUS", "未查看");
        pd.put("USER_NAME", user.getUSER_NAME());
        pd.put("REPLIED_NAME", "");
        pd.put("REPLIED_CONTENT", "");
        pd.put("INFO_PIC", voucher);
        pd.put("MESSAGE_FEEDBACK_ID", this.get32UUID());
        try {
            message_feedbackService.save(pd);
        } catch (Exception e) {
            e.printStackTrace();
            return R.error().message(e.getMessage());
        }
        return R.ok().message("反馈成功");
    }

    /**
     * 功能描述：获取反馈列表
     *
     * @author Ajie
     * @date 2020/6/22 0022
     */
    @RequestMapping(value = "feedbackList")
    @ResponseBody
    public R getFeedbackList() throws Exception {
        PageData pd = this.getPageData();
        MemUser user = (MemUser) Jurisdiction.getSession().getAttribute(Const.SESSION_MEMUSER);
        pd.put("USER_NAME", user.getUSER_NAME());
        List<PageData> list = message_feedbackService.listAllByUserName(pd);
        return R.ok().data("item", list);
    }

    /**
     * 功能描述：获取常见问题列表
     *
     * @author Ajie
     * @date 2020/6/22 0022
     */
    @RequestMapping(value = "getFAQList")
    @ResponseBody
    public R getFAQList() throws Exception {
        PageData pd = this.getPageData();
        MemUser user = (MemUser) Jurisdiction.getSession().getAttribute(Const.SESSION_MEMUSER);
        pd.put("USER_NAME", user.getUSER_NAME());
        List<PageData> list = sys_faqService.listAll(pd);
        return R.ok().data("item", list);
    }

    /**
     * 功能描述：获取当前登录用户信息
     *
     * @author Ajie
     * @date 2020/6/22 0022
     */
    @RequestMapping(value = "getUserInfo")
    @ResponseBody
    public R getUserInfo() throws Exception {
        PageData pd = this.getPageData();
        MemUser memUser = (MemUser) Jurisdiction.getSession().getAttribute(Const.SESSION_MEMUSER);
        pd.put("ACCOUNT_ID", memUser.getACCOUNT_ID());
        PageData user = accountService.findById(pd);
        return R.ok().data(user);
    }


}


