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
    <title></title>
</head>
<body>
<header class="mui-bar mui-bar-nav">
    <a class="mui-action-back mui-icon mui-icon-left-nav mui-pull-left"></a>
    <h1 class="mui-title">修改二级密码</h1>
</header>
<div class="mui-content">
    <!-- 新加 -->
    <from id="From">
        <div class="now_re_inp">
            <input type="hidden" name="flag" value="loginPassword">
            <div>
                <p>旧密码</p>
                <input type="password" name="oidPass" placeholder="请输入旧密码">
            </div>
            <div>
                <p>新密码</p>
                <input type="password" name="newPass" placeholder="请输入新密码">
            </div>
            <div>
                <p>确认新密码</p>
                <input type="password" name="confirmPass" placeholder="请确认新密码">
            </div>
            <button type="button" onclick="isEmpty('From')" class="mui-btn mui-btn-blue mui-btn-block mui_btn">提交
            </button>
        </div>
    </from>


</div>
</body>

<script>

    // 空值判断
    function isEmpty(id) {
        mui(".mui_btn").button('loading'); // 置灰
        check = true;
        mui(id + " input").each(function () {
            //若当前input为空，则alert提醒
            if (!this.value || this.value.trim() == "") {
                var label = this.previousElementSibling;
                mui.alert(label.innerText + "<spring:message code='common.notEmpty' text='不允许为空'/>");
                mui(".mui_btn").button('reset'); // 释放
                check = false;
                return false;
            }
        }); //校验通过，继续执行业务逻辑
        if (check) {
            server_verification(id);
        }
    }

    // 服务端校验
    function server_verification(id) {
        var url = "front/updataPass.do";
        //异步提交表单(先确保jquery.form.js已经引入了)
        var options = {
            url: url,
            success: function (data) {
                mui(".mui_btn").button('reset'); // 释放
                /*后台验证后*/
                if (data == "success") {
                    mui.toast("<spring:message code='common.modifiedSuccess' text='修改成功'/>");
                    setTimeout(function () {
                        window.location.href = "release/toLogin.do"
                    }, 1000);
                    return false;
                }
                if (data == "3") {
                    mui.toast("<spring:message code='security.newPasswordInconsistent' text='新密码不一致'/>");
                    return false;
                }
                if (data == "4" || data == "5") {
                    mui.toast("<spring:message code='security.oldPasswordInconsistent' text='旧密码不一致'/>");
                    return false;
                } else {
                    mui.toast("<spring:message code='common.illegalRequest' text='非法请求'/>")
                }
            }
        };
        $(id).ajaxSubmit(options);
    }
</script>

</html>
