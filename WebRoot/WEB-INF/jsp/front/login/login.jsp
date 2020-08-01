<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>

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
    <title>登录</title>
</head>

<style type="text/css">
    .mui-content {
        background: transparent;
    }

    body {
        background-color: #fff;
    }

    .login_form {
        position: absolute;
        top: 55%;
        left: 50%;
        -webkit-transform: translate(-50%, -50%);
        -moz-transform: translate(-50%, -50%);
        -o-transform: translate(-50%, -50%);
        transform: translate(-50%, -50%);
        width: 80%;
        border-radius: 10px;
        background-color: #fff;
        padding: 20px;
    }
</style>
<body>
<!-- 背景 -->
<%--<div class="login_bg"></div>--%>
<div class="mui-content">

    <!-- 中英文切换开关 -->
    <%--<div id="mySwitch" class="mui-switch mui-switch-blue mui-active">
        <div class="mui-switch-handle"></div>
    </div>--%>

    <!-- logo -->
    <div class="logo">
        <img class="logo-img">
    </div>
    <div class="add_bg">
        <form class="mui-input-group login_form" id="Form">
            <div class="mui-input-row">
                <label><i class="iconfont icon-icon03"></i><span><spring:message code="login.user"
                                                                                 text="用户"/></span></label>
                <input type="text" name="USER_NAME" id="USER_NAME" class="mui-input-clear"
                       placeholder="<spring:message code="login.pleaseUser" text="请输入账号"/>">
            </div>
            <div class="mui-input-row">
                <label><i class="iconfont icon-mima1"></i><span><spring:message code="login.password"
                                                                                text="密码"/></span></label>
                <input type="password" name="LOGIN_PASSWORD" id="LOGIN_PASSWORD" class="mui-input-clear"
                       placeholder="<spring:message code="login.pleasePassword" text="请输入密码"/>">
            </div>
            <div style="display: flex;justify-content: space-between;align-items: center">
                <a href="static/front/app/s0324.apk" class="forget_pass">
                    <button type="button" style="background-color: transparent;border: none;color:#188af0;">APP</button>
                </a>
                <%--<a href="static/front/app/s0324.ipa" class="forget_pass">
                    <button type="button" style="background-color: transparent;border: none;color:#188af0;">IOS</button>
                </a>--%>
                <a href="release/forgetPassword.do" class="forget_pass">
                    <button type="button" style="background-color: transparent;border: none;color:#188af0;">
                        <spring:message
                                code="login.forgetPassword" text="忘记密码？"/></button>
                </a>
            </div>
            <!-- 登录按钮 -->
            <div class="login_btn">
                <button type="button" class="mui-btn mui-btn-blue mui-btn-block login"><spring:message
                        code="login.login"
                        text="登录"/></button>
                <a href="release/toRegister.do">
                    <button type="button" class="mui-btn mui-btn-block"><spring:message code="login.register"
                                                                                        text="注册"/></button>
                </a>
            </div>
        </form>
    </div>
</div>

</body>

<script type="text/javascript">

    <%--var lang;--%>
    <%--var nowLang = '${nowLang}';--%>
    $(function () {
        /*if (nowLang == 'en_US') {
            // 关闭开关
            mui("#mySwitch").switch().toggle();
        }*/
        // 从cookie 中取用户名密码
        if ($.cookie('loginname') != 'undefined' && $.cookie('password') != 'undefined') {
            var loginname = $.cookie('loginname');
            var password = $.cookie('password');
            $("#USER_NAME").val(loginname);
            $("#LOGIN_PASSWORD").val(password);
        }

    });

    /*// 监听 开关 是否打开
    document.getElementById("mySwitch").addEventListener("toggle", function (event) {
        if (event.detail.isActive) {
            lang = "zh_CN";
            if (nowLang != "zh_CN") {
                window.location.href = "release/langSwitching.do?lang=" + lang;
            }
        } else {
            lang = "en_US";
            if (nowLang != "en_US") {
                window.location.href = "release/langSwitching.do?lang=" + lang;
            }
        }
    });*/

    // 登录验证
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
        mui.toast("<spring:message code='login.loginSuccess' text='登录成功'/>"); //登录成功提示
        setTimeout(function () {
            window.location.href = "front/to_index.do";
        }, 1000)
    }

    // 服务端校验
    function server_verification() {
        var url = "release/login.do";
        //异步提交表单(先确保jquery.form.js已经引入了)
        var options = {
            url: url,
            success: function (data) {
                mui(".login").button('reset'); // 释放
                /*后台登录验证后*/
                if (data === "success") {
                    saveCookie()
                    toIndex();
                    return false;
                }
                if (data === "0") {
                    mui.toast("<spring:message code='common.illegalRequest' text='非法请求'/>");
                    return false;
                }
                if (data === "1") {
                    mui.toast("<spring:message code='login.WrongAccountOrPassword' text='账号或密码错误'/>");
                    return false;
                }
                if (data === "2") {
                    mui.toast("<spring:message code='login.AccountSealed' text='账号被封禁'/>");
                    return false;
                }
                return false;
            }
        };
        $("#Form").ajaxSubmit(options);
    }

    // 保存cookie
    function saveCookie() {
        $.cookie('loginname', $("#USER_NAME").val(), {
            expires: 30
        });
        $.cookie('password', $("#LOGIN_PASSWORD").val(), {
            expires: 30
        });
    }

</script>


</html>
