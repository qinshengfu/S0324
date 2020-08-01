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
    <title>注册</title>
</head>

<style type="text/css">
    .mui-content {
        background: transparent;
    }
</style>
<body>
<header class="mui-bar mui-bar-nav">
    <a class="mui-action-back mui-icon mui-icon-left-nav mui-pull-left"></a>
    <h1 class="mui-title"><spring:message code='login.register' text='注册'/></h1>
</header>

<div class="mui-content">
    <form class="mui-input-group login_form regist" id="Form">
        <div class="mui-input-row">
            <label><i class="iconfont icon-icon03"></i><span>用户名</span></label>
            <input type="text" class="mui-input-clear" name="USER_NAME"
                   placeholder="<spring:message code='register.userName' text='用户名'/>">
        </div>
        <div class="mui-input-row">
            <label><i class="iconfont icon-mima1"></i><span>登录密码</span></label>
            <input type="password" class="mui-input-clear" name="loginPassword"
                   placeholder="<spring:message code='register.loginPassword' text='登录密码'/>">
        </div>
        <div class="mui-input-row">
            <label><i class="iconfont icon-mima1"></i><span>确认登录密码</span></label>
            <input type="password" class="mui-input-clear" name="confirmLogin"
                   placeholder="<spring:message code='register.confirmLoginPassword' text='确认登录密码'/>">
        </div>
        <div class="mui-input-row">
            <label><i class="iconfont icon-mima1"></i><span>安全密码</span></label>
            <input type="password" class="mui-input-clear" name="securityPassword"
                   placeholder="<spring:message code='register.securityPassword' text='安全密码'/>">
        </div>
        <div class="mui-input-row">
            <label><i class="iconfont icon-mima1"></i><span>确认安全密码</span></label>
            <input type="password" class="mui-input-clear" name="confirmSecurity"
                   placeholder="<spring:message code='register.confirmSecurityPassword' text='确认安全密码'/>">
        </div>
        <div class="mui-input-row">
            <label><i class="iconfont icon-gerenzhongxin"></i><span>邀请码</span></label>
            <input type="text" id="code" name="code"
                   placeholder="<spring:message code='register.invitationCode' text='邀请码'/>">
        </div>

        <!-- 登录按钮 -->
        <div class="login_btn">
            <button type="button" class="mui-btn mui-btn-blue mui-btn-block login"><spring:message code='login.register'
                                                                                                   text='注册'/></button>
        </div>
    </form>
</div>

</body>

<script type="text/javascript">
    mui.init()
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
        });

        //校验通过，继续执行业务逻辑
        if (check) {
            if (!checkLoginPassword($("input[name=USER_NAME]").val())) {
                mui.toast("用户名格式为字母开头+数字最少6位");
                mui(".login").button('reset'); // 释放
                return false;
            }
            if (!checkLoginPassword($("input[name=loginPassword]").val())) {
                mui.toast("<spring:message code='register.loginPassFormat' text='登录密码格式为英文+数字最少6位'/>");
                mui(".login").button('reset'); // 释放
                return false;
            }
            if (!checkSecurityPassword($("input[name=securityPassword]").val())) {
                mui.toast("<spring:message code='register.securityPassFormat' text='安全密码格式纯数字'/>");
                mui(".login").button('reset'); // 释放
                return false;
            }
            server_verification()
        }
    });

    $(function () {
        var flag = "${pd.tag}";
        if (flag !== "") {
            $("#code").val(flag)
            $("#code").attr("readonly", "readonly")
        }
        // 聚焦
        $("input[name=USER_NAME]").focus();

    });

    // 服务端校验
    function server_verification() {
        var url = "release/register.do";
        //异步提交表单(先确保jquery.form.js已经引入了)
        var options = {
            url: url,
            success: function (data) {
                mui(".login").button('reset'); // 释放
                /*后台登录验证后*/
                if (data == "success") {
                    toPage();
                    return false;
                }
                if (data == "0") {
                    mui.toast("<spring:message code='common.illegalRequest' text='非法请求'/>");
                    return false;
                }
                if (data == "1") {
                    mui.toast("<spring:message code='register.isAlreadyRegistered' text='用户名已被注册'/>");
                    return false;
                }
                if (data == "2") {
                    mui.toast("<spring:message code='register.codeNotExist' text='邀请码不存在'/>");
                    return false;
                }
                if (data == "8") {
                    mui.toast("用户名格式为字母开头+数字最少6位");
                    return false;
                }
                if (data == "4") {
                    mui.toast("<spring:message code='register.inconsistentLogin' text='登录密码不一致'/>");
                    return false;
                }
                if (data == "5") {
                    mui.toast("<spring:message code='register.inconsistentSecurity' text='安全密码不一致'/>");
                    return false;
                }
                if (data == "6") {
                    mui.toast("<spring:message code='register.loginPassFormat' text='登录密码格式为英文+数字最少6位'/>");
                    return false;
                }
                if (data == "7") {
                    mui.toast("<spring:message code='register.securityPassFormat' text='安全密码格式纯数字'/>");
                    return false;
                }
                if (data == "9") {
                    mui.toast("邀请码生成失败请重试");
                    return false;
                }
                if (data == "3") {
                    mui.toast("<spring:message code='register.insufficientFundShared' text='推荐人分享基金不足'/>");
                    return false;
                } else {
                    mui.toast("<spring:message code='common.unknownError' text='未知错误！'/>");
                }
            }
        };
        $("#Form").ajaxSubmit(options);
    }

    // 跳转页面
    function toPage() {
        mui.toast("<spring:message code='register.registerSuccess' text='注册成功'/>"); //登录成功提示

        window.location.href = "release/toLogin";
    }

    // 登录密码是否符合要求，英文加数字最少6位
    function checkLoginPassword(password) {
        var pattern = /^[A-Za-z0-9]{6,40}$/;
        return pattern.test(password)
    }

    // 安全密码是否符合要求，纯数字
    function checkSecurityPassword(password) {
        var pattern = /^\d{1,40}$/;
        return pattern.test(password)
    }

</script>

</html>
