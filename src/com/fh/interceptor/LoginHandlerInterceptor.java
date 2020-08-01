package com.fh.interceptor;

import com.fh.entity.MemUser;
import com.fh.entity.system.User;
import com.fh.util.Const;
import com.fh.util.Jurisdiction;
import org.apache.shiro.session.Session;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * 类名称：登录过滤，权限验证
 * 类描述：
 *
 * @author FH qq313596790[青苔]
 * 作者单位：
 * 联系方式：
 * 创建时间：2017年11月2日
 * @version 1.6
 */
public class LoginHandlerInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String path = request.getServletPath();
        // 开放路径
        if (path.matches(Const.NO_INTERCEPTOR_PATH)) {
            return true;
        }
		//默认  不用登陆
        if (path.matches(Const.NO_DEFAULTERINTERCEPTOR_PATH)) {
            return true;
        }
		// 前台登陆才能操作
        if (path.matches(Const.NO_MEMUSERINTERCEPTOR_PATH)) {
            Session session = Jurisdiction.getSession();
            MemUser m_user = (MemUser) session.getAttribute(Const.SESSION_MEMUSER);
            if (m_user != null) {
                ServletContext application = request.getServletContext();
                @SuppressWarnings("unchecked")
                Map<String, String> loginMap = (Map<String, String>) application.getAttribute("loginMap");
                if (loginMap != null) {
                    String uid = m_user.getACCOUNT_ID();
                    String sessionid = session.getId().toString();
                    if ((sessionid.equals(loginMap.get(uid)))) {
                        return true;
                    } else {
                        session.removeAttribute(Const.SESSION_MEMUSER);
                        // 重定向到登录页面
                        response.sendRedirect(request.getContextPath() + "/release/toLogin.do");
                        return false;
                    }
                }
                // 表示服务器刚启动第一个登录的用户
                return true;
            } else {
                //登陆过滤
                response.sendRedirect(request.getContextPath() + "/release/toLogin.do");
                return false;
            }
        } else {
            return checkUp(request, response);
        }
    }

    /**
     * 功能描述：后台用户权限校验
     * @author Ajie
     * @date 2020/3/27 0027
     */
    public boolean checkUp(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String path = request.getServletPath();

        User user = (User) Jurisdiction.getSession().getAttribute(Const.SESSION_USER);
        if (user != null) {
            path = path.substring(1, path.length());
            boolean b = Jurisdiction.hasJurisdiction(path); //访问权限校验
            if (!b) {
                response.sendRedirect(request.getContextPath() + Const.LOGIN);
            }
            return b;
        } else {
            //登陆过滤
            response.sendRedirect(request.getContextPath() + "/release/toLogin.do");
            return false;
        }
    }

}
