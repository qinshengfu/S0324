<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
            + path + "/";
%>

<html>
<base href="<%=basePath%>">
<!-- 公共文件 -->
<%@ include file="../../front/public/unit.jsp" %>
<head>
    <title>密保问题</title>
</head>

<style type="text/css">
    .mui-content {
        background: transparent;
    }
</style>
<body>
<header class="mui-bar mui-bar-nav">
    <a class="mui-action-back mui-icon mui-icon-left-nav mui-pull-left"></a>
    <h1 class="mui-title"><spring:message code='forgetpass.retrievePassword' text='找回密码'/></h1>
</header>

<div class="mui-content">
    <form class="mui-input-group login_form regist" id="Form">
        <div class="mui-input-row">
            <label><i class="iconfont icon-icon03"></i><span><spring:message code='login.user'
                                                                             text='账号'/></span></label>
            <input type="text" name="USER_NAME" readonly value="${user.USER_NAME}">
        </div>
        <div class="mui-input-row">
            <label><i class="iconfont icon-mima1"></i><span><spring:message code='forgetpass.securityQuestion'
                                                                            text='密保问题'/></span></label>
            <input type="text" name="SECURITY_QUESTION" readonly value="${user.SECURITY_QUESTION}">
        </div>
        <div class="mui-input-row">
            <label><i class="iconfont icon-mima1"></i><span><spring:message code='forgetpass.answer'
                                                                            text='密保答案'/></span></label>
            <input type="text" class="mui-input-clear" name="ANSWER" placeholder="密保答案">
        </div>
        <div class="mui-input-row">
            <label><i class="iconfont icon-mima1"></i><span><spring:message code='forgetpass.newLoginPassword'
                                                                            text='新登录密码'/></span></label>
            <input type="password" class="mui-input-clear" name="loginPassword" placeholder="新登录密码">
        </div>
        <div class="mui-input-row">
            <label><i class="iconfont icon-mima1"></i><span><spring:message code='forgetpass.newSecurityPassword'
                                                                            text='新安全密码'/></span></label>
            <input type="password" class="mui-input-clear" name="securityPassword" placeholder="新安全密码">
        </div>
        <!-- 登录按钮 -->
        <div class="login_btn">
            <button type="button" class="mui-btn mui-btn-blue mui-btn-block login"><spring:message
                    code='common.submission' text='提交'/></button>
        </div>
    </form>
</div>

</body>

<script type="text/javascript">

    // 注册验证
    mui('.login_form').on('tap', '.login', function () {
        mui(this).button('loading'); // 置灰
        check = true;
        mui(".mui-input-row input").each(function () {
            //若当前input为空，则alert提醒
            if (!this.value || this.value.trim() == "") {
                var label = this.previousElementSibling;
                mui.alert(label.innerText + "<spring:message code='common.notEmpty' text='不允许为空'/>");
                mui(".login").button('reset'); // 释放
                check = false;
                return false;
            }
        }); //校验通过，继续执行业务逻辑
        if (check) {
            server_verification();
        }
    });

    // 跳转页面
    function toIndex() {
        document.activeElement.blur(); //收起键盘
        // 关闭等待框 真机运行生效}
        if (mui.os.plus) {
            plus.nativeUI.closeWaiting()
        }
        if (mui.os.plus) {
            plus.nativeUI.closeWaiting()
        }
        mui.toast("<spring:message code='common.modifiedSuccess' text='修改成功'/>"); //登录成功提示
        setTimeout(function () {
            mui.openWindow({
                url: 'release/toLogin.do', //成功跳转的页面
                id: 'release/toLogin.do',
                waiting: {
                    autoShow: false, //去掉原有跳转加载框 防止两次加载框
                },
                extras: {} //传参
            })
        }, 1000)
    }

    // 服务端校验
    function server_verification() {
        var url = "release/retrievePassword.do";
        //异步提交表单(先确保jquery.form.js已经引入了)
        var options = {
            url: url,
            success: function (data) {
                mui(".login").button('reset'); // 释放
                /*后台验证后*/
                if (data == "success") {
                    toIndex()
                    return false;
                }
                if (data == "0") {
                    mui.toast("<spring:message code='common.illegalRequest' text='非法请求'/>");
                    return false;
                }
                if (data == "1") {
                    mui.toast("<spring:message code='common.answerIsWrong' text='密保答案错误'/>");
                    return false;
                }
            }
        };
        $("#Form").ajaxSubmit(options);
    }

</script>

</html>
