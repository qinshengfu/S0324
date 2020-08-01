package com.fh.listener;

import com.fh.util.Const;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * 说明：
 * HTTP协议是短链接的，所以无法在服务端根据建立了多少连接来统计当前有多少人在线。 不过可以通过统计session有多少来估计在线人数。
 * 一旦一个用户访问服务器，就会创建一个session. 如果该用户持续访问，那么该session会持续有效。
 * 如果经历了30分钟，该用户也没有做任何操作，就表示该用户“下线” 了，其对应的session也会被销毁。
 * 所以可以通过统计有多少session被保留来估计当前在线人数。
 *
 * 创建人：Ajie
 * 创建时间：2020年3月6日09:46:02
 */
public class OnlineNumberListener implements HttpSessionListener {

    @Override
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {
        ServletContext application = httpSessionEvent.getSession().getServletContext();
        Integer onlineNumber = (Integer) application.getAttribute(Const.ONLINE_NUMBER);
        // 注 ：第一次从application取数据时是空的，要初始化为 0
        if (null == onlineNumber) {
            onlineNumber = 0;
        }
        onlineNumber++;
        application.setAttribute(Const.ONLINE_NUMBER, onlineNumber);
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
        ServletContext application = httpSessionEvent.getSession().getServletContext();
        Integer onlineNumber = (Integer) application.getAttribute(Const.ONLINE_NUMBER);
        if (null == onlineNumber) {
            onlineNumber = 0;
        } else {
            onlineNumber--;
        }
        application.setAttribute(Const.ONLINE_NUMBER, onlineNumber);
    }
}
