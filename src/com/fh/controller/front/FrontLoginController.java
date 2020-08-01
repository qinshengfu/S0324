package com.fh.controller.front;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.NumberUtil;
import com.fh.entity.MemUser;
import com.fh.service.system.FHlogManager;
import com.fh.util.*;
import com.fh.util.verificationCode.RandomCodeUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.session.Session;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 说明：前台用户登录
 * 创建人：Ajie
 * 创建时间：2020年3月27日10:59:20
 */
@Controller
@Slf4j
@RequestMapping(value = "release")
public class FrontLoginController extends BaseFrontController {


    @Resource(name = "fhlogService")
    private FHlogManager FHLOG;

    /**
     * 功能描述：请求登录
     *
     * @return 验证结果
     * @author Ajie
     * @date 2020/3/27 0027
     */
    @RequestMapping(value = "login")
    @ResponseBody
    private String login() throws Exception {
        // 获取请求参数
        PageData pd = this.getPageData();
        if (pd.size() != 2) {
            return "0";
        }
        // 密码加密
        String passwrod = pd.getString("LOGIN_PASSWORD");
        pd.put("LOGIN_PASSWORD", StringUtil.applySha256(passwrod));
        // 根据用户名和密码开始校验
        MemUser user = accountService.findByNameAndPass(pd);
        // 账号或密码错误
        if (user == null) {
            return "1";
        }
        // 账号被封禁
        if (user.getUSER_STATE() == 1) {
            return "2";
        }
        // 存入session 和 登录序列中
        addUserLogin(user);
        return "success";
    }

    /**
     * 功能描述：存入session和登录序列
     *
     * @param user 用户信息
     * @author Ajie
     * @date 2020/3/27 0027
     */
    public void addUserLogin(MemUser user) {
        Session session = Jurisdiction.getSession();
        session.setAttribute(Const.SESSION_MEMUSER, user);
        // 从上下文缓存取登录列表
        Map<String, String> loginMap = (Map<String, String>) applicati.getAttribute("loginMap");
        if (loginMap == null) {
            loginMap = new HashMap<>();
        }
        // 添加当前登录用户，并更新缓存
        loginMap.put(user.getACCOUNT_ID(), session.getId().toString());
        applicati.setAttribute("loginMap", loginMap);
    }

    /**
     * 功能描述：请求注册
     *
     * @author Ajie
     * @date 2020/3/27 0027
     */
    @RequestMapping(value = "register")
    @ResponseBody
    private String register() throws Exception {
        // 获取请求参数
        PageData pd = this.getPageData();
        if (pd.size() != 6) {
            return "0";
        }
        // 验证用户名是否被注册
        MemUser user = accountService.findByName(pd);
        if (user != null) {
            return "1";
        }
        // 验证推荐人是否存在
        String invitationCode = pd.getString("code");
        pd.put("INVITATION_CODE", invitationCode);
        user = accountService.findByInvitationCode(pd);
        if (user == null) {
            return "2";
        }
        // 检查用户名是否符合 字母+数字格式
        if (!Tools.checkEnglishAngNumber(pd.getString("USER_NAME"))) {
            return "8";
        }
        // 登录密码是否一致
        if (!pd.getString("loginPassword").equals(pd.getString("confirmLogin"))) {
            return "4";
        }
        // 安全密码是否一致
        if (!pd.getString("securityPassword").equals(pd.getString("confirmSecurity"))) {
            return "5";
        }
        // 检查登录密码是否符合 英文+数字格式
        if (!Tools.checkEnglishAngNumber(pd.getString("loginPassword"))) {
            return "6";
        }
        // 检查安全密码是否符合 纯数字格式
        if (!Tools.checkPureNumber(pd.get("securityPassword").toString())) {
            return "7";
        }
        // 查询系统参数
        PageData pay = sys_configService.findById(pd);
        // 每次注册需要扣除推荐人N个分享基金
        String eachFullFund = pay.getString("EACH_FULL_FUND");
        int result = NumberUtil.compare(Double.parseDouble(eachFullFund), user.getSHARE_FUND());
        // 大于0 说明上级分享基金不够
        if (result > 0) {
            return "3";
        }
        pd.put("cost", eachFullFund);
        String usdtAddress = "usdtAddress";
        pd.put("usdtAddress", usdtAddress);
        // 保存到数据库中
        return saveUser(pd, user);
    }

    /**
     * 功能描述：新增用户写入数据库
     *
     * @param pd   用户信息
     * @param user 推荐人信息
     * @author Ajie
     * @date 2020/3/28 0028
     */
    public String saveUser(PageData pd, MemUser user) throws Exception {
        // 上级赠送的usdt
        Object cost = pd.get("cost");
        PageData map = new PageData();
        // 定义新注册用户的推荐关系路径
        String path = Tools.isEmpty(user.getRE_PATH()) ? user.getACCOUNT_ID() : user.getRE_PATH() + "," + user.getACCOUNT_ID();
        map.put("GMT_CREATE", DateUtil.now());
        map.put("GMT_MODIFIED", DateUtil.now());
        map.put("USER_NAME", pd.getString("USER_NAME"));
        map.put("LOGIN_PASSWORD", StringUtil.applySha256(pd.getString("loginPassword")));
        map.put("SECURITY_PASSWORD", StringUtil.applySha256(pd.getString("securityPassword")));
        map.put("USDT_WALLET", 0);
        map.put("ENTERTAINMENT_SCORE", 0);
        map.put("SHARE_FUND", 0);
        map.put("RECOMMENDED_NUMBER", 0);
        map.put("RECOMMENDER", user.getACCOUNT_ID());
        map.put("RE_PATH", path);
        map.put("ALGEBRA", user.getALGEBRA() + 1);
        map.put("USER_STATE", 0);
        map.put("TEAM_NUMBER", 0);
        map.put("NAME", "");
        map.put("COMMISSION", 0.5);
        map.put("PHONE", "");
        map.put("USDT_IN_ADDRESS", "");
        map.put("WALLET_QR_CODE", "");
        map.put("IS_REST", 1);
        map.put("USDT_ADDRESS", pd.getString("usdtAddress"));
        map.put("USER_RANK", 1);
        map.put("USDT_WALLET_ACTUAL", 0);
        map.put("SECURITY_QUESTION", "");
        map.put("ANSWER", "");
        // 随机生成邀请码
        int size = 6;
        int codeType = 0;
        String invitationCode;
        MemUser checkCode;
        // 最大重试99次
        int i = 0;
        int maxCount = 99;
        do {
            invitationCode = RandomCodeUtil.getInvitaCode(size, codeType);
            pd.put("INVITATION_CODE", invitationCode);
            checkCode = accountService.findByInvitationCode(pd);
            i++;
        } while (checkCode != null && i < maxCount);
        if (i >= maxCount) {
            return "9";
        }
        map.put("INVITATION_CODE", invitationCode);
        map.put("DAY_WITHDRAWALS_COUNT", 0);
        // 存入数据库
        accountService.save(map);
        // 更新推荐人 推荐数量、团队数量
        user.setGMT_MODIFIED(DateUtil.now());
        accountService.updateReAndTeamNumber(user);
        // 扣除推荐人分享基金
        pd.put("ACCOUNT_ID", user.getACCOUNT_ID());
        pd.put("money", cost);
        pd.put("GMT_MODIFIED", DateUtil.now());
        pd.put("tag", "-");
        accountService.updateSharedFundsNumber(pd);
        // 创建分享基金钱包记录
        this.addfundRecord(user, pd.get("cost"), "注册", "已完成", "-");
        // 异步执行用户级别所有上级等级判定
        ThreadUtil.execute(() -> {
            try {
                for (int j = 0; j < 2; j++) {
                    this.checkRank();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return "success";
    }

    /**
     * 功能描述：后台直接登录前台用户
     *
     * @author Ajie
     * @date 2020/3/30 0030
     */
    @RequestMapping(value = "adminLogin", produces = "text/html;charset=UTF-8")
    @ResponseBody
    private String adminLogin() throws Exception {
        if (!Jurisdiction.buttonJurisdiction("account/list.do", "edit")) {
            return "您没有权限";
        } //校验权限
        PageData pd = this.getPageData();
        MemUser user = accountService.findByName(pd);
        if (user == null) {
            return "非法操作！";
        }
        addUserLogin(user);
        FHLOG.save(Jurisdiction.getUsername(), "登录：" + user.getUSER_NAME());
        return "success";
    }


}
