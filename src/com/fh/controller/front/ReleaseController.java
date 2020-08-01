package com.fh.controller.front;

import cn.hutool.core.date.DateUtil;
import com.fh.annotation.Limit;
import com.fh.controller.base.BaseController;
import com.fh.entity.MemUser;
import com.fh.entity.result.R;
import com.fh.service.front.AccountManager;
import com.fh.util.*;
import com.fh.util.express.ImageUtils;
import com.fh.util.verificationCode.PhoneVerificaCodeUtil;
import com.fh.util.verificationCode.RandomCodeUtil;
import com.fh.util.verificationCode.VerifyCodeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.apache.shiro.session.Session;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 功能描述：前台页面跳转【不需要登录】
 *
 * @author Ajie
 * @date 2019/11/26 0026
 */
@Controller
@RequestMapping(value = "/release")
@Slf4j
public class ReleaseController extends BaseController {

    // 加减乘 验证码
    @Resource(name = "verifyCode")
    private VerifyCodeService verifyCodeService;
    // 用户管理
    @Resource(name = "accountService")
    private AccountManager accountService;


    /**
     * 功能描述：测试图片压缩上传！！！！！！！！！
     *
     * @param
     * @return
     * @author Ajie
     * @date 2019/10/29 0029
     */
    @RequestMapping(value = "/toAddimg")
    private ModelAndView toAddimg() {
        ModelAndView mv = this.getModelAndView();
        mv.setViewName("front/A_pictureUpload2");
        return mv;
    }

    /**
     * 功能描述：ajax请求限流测试
     * @author Ajie
     * @date 2020/4/17 0017
     */
    @RequestMapping(value = "/test")
    @ResponseBody
    @Limit(name = "获取用户列表", prefix = "s0324", period = 60, count = 6)
    public R testLock() throws Exception {

        List<PageData> userAll =  accountService.listAll();

        return R.ok().data("itms", userAll).message("用户列表");
    }

    /**
     * 功能描述：生成加减成验证码
     *
     * @author Ajie
     * @date 2019/10/30 0030
     */
    @RequestMapping(value = "/verifyCode")
    public void getMiaoshaVerifyCod(HttpServletResponse response) {

        try {
            BufferedImage image = verifyCodeService.getVerify();
            OutputStream out = response.getOutputStream();
            ImageIO.write(image, "JPEG", out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @描述：图片上传
     * @参数：请求和文件数据
     * @返回值：UUID后的图片路径
     * @创建人：Ajie
     * @创建时间：2019/10/15 0015
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
                System.out.println("-----------------图片上传成功！");
            } else {
                System.out.println("-----------------图片上传失败！");
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("----------------图片上传失败！");
        }
        System.out.println("图片上传路径：" + imgPath);
        return imgPath;
    }

    /**
     * 功能描述：发送短信验证码
     *
     * @param phone - 目标手机号
     * @author Ajie
     * @date 2019/10/31 0031
     */
    @RequestMapping(value = "/phoneCode", method = RequestMethod.POST, produces = "text/html; charset=UTF-8")
    @ResponseBody
    public String phoneCode(@RequestParam(name = "phone") String phone) throws Exception {
        // 获取6位 随机纯数字验证码
        String code = RandomCodeUtil.getInvitaCode(6, 1);
        System.out.println("短信验证码：" + code);
        // 有效期/分钟
        int validity = 3;
        String resule = PhoneVerificaCodeUtil.SendSMS(phone, code, validity);
        if ("success".equals(resule)) {
            // 放入session中
            Jurisdiction.getSession().setAttribute(Const.SESSION_PHONE_CHECK_CODE, code);
            log.info("本次短信验证码：{}", code);
        } else {
            log.info("短信验证码发送失败，原因是：{}", resule);
        }

        //TimerTask实现N分钟后从session中删除验证码
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Jurisdiction.getSession().removeAttribute(Const.SESSION_PHONE_CHECK_CODE);
                System.out.println("短信验证码--> 删除成功");
                timer.cancel();
            }
        }, validity * 60 * 1000);
        String spare = PhoneVerificaCodeUtil.getSpare();
        log.info("短信余额：{}", spare);

        return resule;
    }

    /**
     * 功能描述：访问注册页
     *
     * @author Ajie
     * @date 2019/11/26 0026
     */
    @RequestMapping(value = "/toRegister")
    public ModelAndView toRegister() {
        PageData pd = this.getPageData();
        ModelAndView mv = this.getModelAndView();
        mv.setViewName("front/login/register");
        mv.addObject("pd", pd);
        return mv;
    }

    /**
     * 功能描述：访问登录页
     *
     * @author Ajie
     * @date 2019/11/26 0026
     */
    @RequestMapping(value = "/toLogin")
    public ModelAndView toLogin() {
        ModelAndView mv = this.getModelAndView();
        String lang = (String) Jurisdiction.getSession().getAttribute("lang");
        mv.setViewName("front/login/login");
        mv.addObject("nowLang", lang);
        return mv;
    }

    /**
     * 功能描述：访问忘记密码页
     *
     * @author Ajie
     * @date 2019/11/26 0026
     */
    @RequestMapping(value = "/forgetPassword")
    public ModelAndView toForgetPassword() {
        ModelAndView mv = this.getModelAndView();
        mv.setViewName("front/login/forgetpass");
        return mv;
    }

    /**
     * 功能描述：语言转换（中/英）
     *
     * @param lang 语言标识
     * @author Ajie
     * @date 2020/4/6 0006
     */
    @RequestMapping(value = "/langSwitching")
    private String langSwitching(@Param("lang") String lang) {
        Session session = Jurisdiction.getSession();
        session.setAttribute("lang", lang);
        return "redirect:toLogin";
    }

    /**
     * 功能描述：请求寻回密码
     *
     * @author Ajie
     * @date 2020/4/6 0006
     */
    @RequestMapping(value = "/toRetrieve")
    @ResponseBody
    public String toRetrieve() throws Exception {
        PageData pd = this.getPageData();
        // 查询用户信息
        MemUser user = accountService.findByName(pd);
        if (user == null) {
            return "0";
        }
        Jurisdiction.getSession().setAttribute(Const.SESSION_MEMUSER_TEMPORARY, user);
        return "success";
    }

    /**
     * 功能描述：访问输入密保页面
     *
     * @author Ajie
     * @date 2020年4月6日15:29:01
     */
    @RequestMapping(value = "/toSecretProtection")
    public ModelAndView toSecretProtection() throws Exception {
        ModelAndView mv = this.getModelAndView();
        MemUser user = (MemUser) Jurisdiction.getSession().getAttribute(Const.SESSION_MEMUSER_TEMPORARY);
        mv.setViewName("front/login/securityQuestion");
        mv.addObject("user", user);
        return mv;
    }

    /**
     * 功能描述：寻回密码,验证密保问题
     *
     * @author Ajie
     * @date 2020/4/6 0006
     */
    @RequestMapping(value = "/retrievePassword")
    @ResponseBody
    public String retrievePassword() throws Exception {
        PageData pd = this.getPageData();
        // 查询用户信息
        MemUser user = accountService.findByName(pd);
        // 密保问题、密保答案、新登录密码、新安全密码
        String securityQuestion = pd.getString("SECURITY_QUESTION");
        String answer = pd.getString("ANSWER");
        String loginPassword = pd.getString("loginPassword");
        String securityPassword = pd.getString("securityPassword");
        boolean isEmpty = Tools.isEmpty(answer) || Tools.isEmpty(loginPassword) || Tools.isEmpty(securityPassword);
        // 非法请求
        if (isEmpty) {
            return "0";
        }
        if (!securityQuestion.equals(user.getSECURITY_QUESTION())) {
            return "0";
        }
        // 密保答案错误
        if (!answer.equals(user.getANSWER())) {
            return "1";
        }
        // 密码加密存数据库
        pd = new PageData();
        pd.put("LOGIN_PASSWORD", StringUtil.applySha256(loginPassword));
        pd.put("SECURITY_PASSWORD", StringUtil.applySha256(securityPassword));
        pd.put("ACCOUNT_ID", user.getACCOUNT_ID());
        pd.put("GMT_MODIFIED", DateUtil.now());
        accountService.edit(pd);
        return "success";
    }

}


