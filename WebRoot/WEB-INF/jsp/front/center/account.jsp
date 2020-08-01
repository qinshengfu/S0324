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
    <title>账户安全</title>
</head>

<body>
<header class="mui-bar mui-bar-nav">
    <a class="mui-action-back mui-icon mui-icon-left-nav mui-pull-left"></a>
    <h1 class="mui-title"><spring:message code='security.home' text='账户安全'/></h1>
</header>
<div class="mui-content">
    <!-- nav -->
    <div class="profit_nav">
        <div class="mui-segmented-control">
            <a class="mui-control-item mui-active" href="#item1"><spring:message code='security.loginPasswrod'
                                                                                 text='登录密码修改'/></a>
            <a class="mui-control-item" href="#item2"><spring:message code='security.securityPasswrod'
                                                                      text='安全密码修改'/></a>
        </div>
    </div>
    <!--  -->
    <div class="mui-content-padded profit_nav_table">
        <form id="item1" class="mui-control-content mui-active">
            <input type="hidden" name="flag" value="loginPassword">
            <ul class="mui-table-view">
                <li class="mui-table-view-cell" style="color: #0062CC;">
                    <div class="setup_inp">
                        <div class="mui-input-row">
                            <label><spring:message code='security.originalPassword' text='原密码'/>：</label>
                            <input type="password" name="oidPass"
                                   placeholder="<spring:message code='security.originalPassword' text='原密码'/>">
                        </div>
                        <div class="mui-input-row">
                            <label><spring:message code='security.newPassword' text='新密码'/>：</label>
                            <input type="password" name="newPass"
                                   placeholder="<spring:message code='security.newPassword' text='新密码'/>">
                        </div>
                        <div class="mui-input-row">
                            <label><spring:message code='security.confirmPassword' text='确认新密码'/>：</label>
                            <input type="password" name="confirmPass"
                                   placeholder="<spring:message code='security.confirmPassword' text='确认新密码'/>">
                        </div>
                    </div>
                </li>
            </ul>
        </form>

        <form id="item2" class="mui-control-content">
            <input type="hidden" name="flag" value="securityPassword">
            <ul class="mui-table-view">
                <li class="mui-table-view-cell">
                    <div class="setup_inp">
                        <div class="mui-input-row">
                            <label><spring:message code='security.originalPassword' text='原密码'/>：</label>
                            <input type="password" name="oidPass"
                                   placeholder="<spring:message code='security.originalPassword' text='原密码'/>">
                        </div>
                        <div class="mui-input-row">
                            <label><spring:message code='security.newPassword' text='新密码'/>：</label>
                            <input type="password" name="newPass"
                                   placeholder="<spring:message code='security.newPassword' text='新密码'/>">
                        </div>
                        <div class="mui-input-row">
                            <label><spring:message code='security.confirmPassword' text='确认新密码'/>：</label>
                            <input type="password" name="confirmPass"
                                   placeholder="<spring:message code='security.confirmPassword' text='确认新密码'/>">
                        </div>
                    </div>
                </li>
            </ul>
        </form>

        <button type="button" class="mui-btn mui-btn-blue mui-btn-block mui_btn" onclick="checkPass()"><spring:message
                code='common.submission' text='提交'/></button>
    </div>
</div>

<script type="text/javascript">

    // 检查密码类型
    function checkPass() {
        if ($('#item1').hasClass('mui-active')) {
            isEmpty("#item1");
        } else {
            isEmpty("#item2");
        }
    }

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
</body>

</html>
