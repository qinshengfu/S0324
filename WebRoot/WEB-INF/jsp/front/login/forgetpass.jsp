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
    <title>找回密码</title>
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
            <label><i class="iconfont icon-icon03"></i><span><spring:message code='login.user' text='账号'/></span></label>
            <input type="text" class="mui-input-clear" name="USER_NAME" placeholder="<spring:message code='login.pleaseUser' text='请输入账号'/>">
        </div>
        <!-- 登录按钮 -->
        <div class="login_btn">
            <button type="button" class="mui-btn mui-btn-blue mui-btn-block login"><spring:message code='common.submission' text='提交'/></button>
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
        mui.openWindow({
            url: 'release/toSecretProtection.do', //成功跳转的页面
            id: 'release/toSecretProtection.do',
            waiting: {
                autoShow: false, //去掉原有跳转加载框 防止两次加载框
            },
            extras: {} //传参
        })
    }

    // 服务端校验
    function server_verification() {
        var url = "release/toRetrieve.do";
        //异步提交表单(先确保jquery.form.js已经引入了)
        var options = {
            url: url,
            success: function (data) {
                mui(".login").button('reset'); // 释放
                /*后台验证后*/
                if (data == "success") {
                    toIndex();
                    return false;
                }
                if (data == "0") {
                    mui.toast("<spring:message code='common.userNotExist' text='用户不存在'/>");
                    return false;
                }
            }
        };
        $("#Form").ajaxSubmit(options);
    }

</script>

</html>
