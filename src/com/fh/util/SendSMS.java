package com.fh.util;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;

public class SendSMS {


	// 短信平台相关参数
	private final static String JGID="3089";//机构ID名
	private final static String YHMC="T9603";//帐户名称
	private final static String YHMM="123456";//密码
	private final static String IpAddr="124.172.234.157:8180";//接口服务器IP地址

	/**
	 * 功能描述：短信验证码
	 * @author Ajie
	 * @date 2019/10/31 0031
	 * @param phone 手机号， note 内容
	 * @return String 结果
	 */
	public static String sendSMS(String phone,String note) throws Exception {
		URL url = null;
		note = note+"【EB】";
		String send_content=URLEncoder.encode(note , "utf-8");//发送内容
		phone = phone.trim();
		url = new URL("http://"+IpAddr+"/service.asmx/SendMessageStr?Id="+JGID+"&Name="+YHMC+"&Psw="+YHMM+"&Message="+send_content+"&Phone="+phone+"&Timestamp=0");
		BufferedReader in = null;
		String inputLine = "";		
		try {
//			System.out.println("开始发送短信手机号码为 ："+phone);
			in = new BufferedReader(new InputStreamReader(url.openStream()));
			inputLine = in.readLine();
//			System.out.println(inputLine+"*************");
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("网络异常,发送短信失败！");
			inputLine=String.valueOf(-1000);
		}
		System.out.println("结束发送短信返回值：  "+inputLine);
		return inputLine;
	}
	public static void main(String[] args) {
		try {
			sendSMS("13977128507", "短信验证码为：123141");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
