package com.fh.listener;

import com.fh.util.Const;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
/**
 * 
* 类名称：WebAppContextListener.java
* 类描述： 对Context的监听分生命周期的监听，和Context上Attribute变化的监听两种。
* 作者：FH 
* 联系方式：
* @version 1.0
 */
public class WebAppContextListener implements ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent event) {
		System.out.println("web 应用销毁");
	}

	@Override
	public void contextInitialized(ServletContextEvent event) {
		System.out.println("web 应用初始化");

		Const.WEB_APP_CONTEXT = WebApplicationContextUtils.getWebApplicationContext(event.getServletContext());
		//System.out.println("========获取Spring WebApplicationContext");
	}

}
