package com.fh.util;

import com.fh.util.mail.SimpleMailSender;
import com.fh.util.verificationCode.RandomCodeUtil;

/**
 * 功能描述：邮箱验证码
 * @author Ajie
 * @date 2019/10/31 0031
 */
public class Verification {
	
	/**
	 * 设置邮箱验证码
	 * @return
	 * @throws Exception
	 */
	public static String getEmail(){
		return RandomCodeUtil.getInvitaCode(6, 0);
	}

	
	/**发送邮件
	 * @param toEMAIL 目标邮箱
	 * @param TITLE   标题
	 * @param CONTENT 内容
	 * @param TYPE    发送格式   1：文本格式;2：HTML格式
	 * @throws Exception
	 */
	public static String sendE_mail(String toEMAIL,String TITLE,String CONTENT,String TYPE){
		String strEMAIL = Tools.readTxtFile(Const.EMAIL);		//读取邮件配置
		String info= "ok";
		if(null != strEMAIL && !"".equals(strEMAIL)){
			String strEM[] = strEMAIL.split(",fh,");
			try {
			
				SimpleMailSender.sendEmail(strEM[0], strEM[1], strEM[2], strEM[3],toEMAIL, TITLE, CONTENT, TYPE);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				info = "e_err";//发送失败
			}//调用发送邮件函数

		}else//配置文件出错
			info = "e_null";
		return info;
	}

}
