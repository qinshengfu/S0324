package com.fh.util;

import org.springframework.context.ApplicationContext;
/**
 * 项目名称：
 * @author:fh qq313596790[青苔]
 * 修改时间：2018/8/2
*/
public class Const {

	public static final String ONLINE_NUMBER = "onlineNumber"; 		//在线人数
	public static final String TIMED_TASK_STATIC = "timedTaskStaticBonus"; 		// 每日发放静态奖金定时任务
	public static final String INVEST_BONUS_TASK = "investBonusTask"; 		// 投资奖金定时任务
	public static final String USER_STATUS_CHECK_TASK = "userStatusCheckTask"; 		// 用户状态检查任务

	public static final String APP_NEWS = "applicati_news";					//服务器缓存前台新闻公告信息
	public static final String APP_CHART = "applicati_chart";				//服务器缓存前台轮播图信息
	public static final String PAR = "applicatiSysConfig";					//前台系统参数设置
	public static final String SESSION_MEMUSER = "sessionMemuser";     	    //session 前台会员用户
	public static final String SESSION_MEMUSER_TEMPORARY = "sessionMemuserTemporary";     	    //session 临时前台会员用户

	public static final String SESSION_MAILBOX = "sessionMailbox";			//找回密码的邮箱 信息 pagedata 类型
	public static final String SESSION_PHONE = "sessionPhone";			//找回密码的手机号
	public static final String SESSION_PHONE_CHECK_CODE = "sessionPhoneCheckCode";	//短信验证码
	public static final String SESSION_EMAIL_CHECK_CODE = "sessionEmailCheckCode";	//邮箱验证码
	public static final String SESSION_SECURITY_CODE = "sessionSecCode";	//验证码
	public static final String SESSION_USER = "sessionUser";				//session用的用户
	public static final String SESSION_ROLE_RIGHTS = "sessionRoleRights";
	public static final String SESSION_menuList = "menuList";				//当前菜单
	public static final String SESSION_allmenuList = "allmenuList";			//全部菜单
	public static final String SESSION_QX = "QX";							//主职角色权限
	public static final String SESSION_QX2 = "QX2";							//副职角色权限
	public static final String SESSION_userpds = "userpds";			
	public static final String SESSION_USERROL = "USERROL";					//用户对象
	public static final String SESSION_USERNAME = "USERNAME";				//用户名
	public static final String SESSION_U_NAME = "U_NAME";					//用户姓名
	public static final String DEPARTMENT_IDS = "DEPARTMENT_IDS";			//当前用户拥有的最高部门权限集合
	public static final String DEPARTMENT_ID = "DEPARTMENT_ID";				//当前用户拥有的最高部门权限
	public static final String TRUE = "T";
	public static final String FALSE = "F";
	public static final String SKIN = "SKIN";									//用户皮肤
	public static final String LOGIN = "/login_toLogin.do";					//登录地址
	public static final String SYSNAME = "admin/config/SYSNAME.txt";		//系统名称路径
	public static final String PAGE	= "admin/config/PAGE.txt";				//分页条数配置路径
	public static final String EMAIL = "admin/config/EMAIL.txt";			//邮箱服务器配置路径
	public static final String SMS1 = "admin/config/SMS1.txt";				//短信账户配置路径1
	public static final String SMS2 = "admin/config/SMS2.txt";				//短信账户配置路径2
	public static final String FWATERM = "admin/config/FWATERM.txt";		//文字水印配置路径
	public static final String IWATERM = "admin/config/IWATERM.txt";		//图片水印配置路径
	public static final String WEIXIN	= "admin/config/WEIXIN.txt";		//微信配置路径
	public static final String WEBSOCKET = "admin/config/WEBSOCKET.txt";	//WEBSOCKET配置路径
	public static final String LOGINEDIT = "admin/config/LOGIN.txt";		//登录页面配置
	public static final String FILEPATHIMG = "uploadFiles/uploadImgs/";		//图片上传路径
	public static final String FILEPATHFILE = "uploadFiles/file/";			//文件上传路径
	public static final String FILEPATHFILEOA = "uploadFiles/uploadFile/";	//文件上传路径(oa管理)
	public static final String FILEPATHTWODIMENSIONCODE = "uploadFiles/twoDimensionCode/"; //二维码存放路径
	public static final String NO_INTERCEPTOR_PATH = ".*/((login)|(logout)|(code)|(app)|(weixin)|(static)|(main)|(websocket)|(uploadImgs)|(static/front)|(twoDimensionCode)|(login_toLogin)).*";	//不对匹配该值的访问路径拦截（正则）
	public static final String NO_DEFAULTERINTERCEPTOR_PATH = ".*/((release)).*";//默认开放/不拦截
	public static final String NO_MEMUSERINTERCEPTOR_PATH = ".*/((front)).*";//手机端前台访问拦截  必须登陆才能操作
	public static ApplicationContext WEB_APP_CONTEXT = null; //该值会在web容器启动时由WebAppContextListener初始化
	
	/**
	 * APP Constants
	 */
	//系统用户注册接口_请求协议参数)
	public static final String[] SYSUSER_REGISTERED_PARAM_ARRAY = new String[]{"USERNAME","PASSWORD","NAME","EMAIL","rcode"};
	public static final String[] SYSUSER_REGISTERED_VALUE_ARRAY = new String[]{"用户名","密码","姓名","邮箱","验证码"};
	
	//app根据用户名获取会员信息接口_请求协议中的参数
	public static final String[] APP_GETAPPUSER_PARAM_ARRAY = new String[]{"USERNAME"};
	public static final String[] APP_GETAPPUSER_VALUE_ARRAY = new String[]{"用户名"};
	
}
