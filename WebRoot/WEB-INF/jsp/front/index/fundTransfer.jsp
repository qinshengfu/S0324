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
    <title>分享基金转账</title>
</head>

<body>
<header class="mui-bar mui-bar-nav">
    <a class="mui-action-back mui-icon mui-icon-left-nav mui-pull-left"></a>
    <h1 class="mui-title"><spring:message code='transfer.home' text='转账'/></h1>
    <a href="front/to_fundRecord.do" class="header_right"><spring:message code='transfer.record' text='转账记录'/></a>
</header>
<div class="mui-content">
    <!--  -->
    <form class="setup_inp" id="Form">
        <div class="mui-input-row">
            <label><spring:message code='withdraw.accountBalance' text='账户余额'/></label>
            <input type="text" id="SHARE_FUND" value="" disabled>
        </div>
        <div class="mui-input-row">
            <label><spring:message code='record.money' text='金额'/></label>
            <input type="number" name="money" id="money" oninput="calculaResults(this.value)"
                   placeholder="<spring:message code='record.money' text='金额'/>">
        </div>
        <div class="mui-input-row">
            <label><spring:message code='transfer.payee' text='收款人'/></label>
            <input type="text" name="payee" placeholder="<spring:message code='transfer.payee' text='收款人'/>">
        </div>
        <div class="mui-input-row">
            <label><spring:message code='register.securityPassword' text='安全密码'/></label>
            <input type="password" name="password"
                   placeholder="<spring:message code='register.securityPassword' text='安全密码'/>">
        </div>
    </form>

    <!-- 按钮 -->
    <button type="button" onclick="checkEmpty()" class="mui-btn mui-btn-blue mui-btn-block mui_btn"><spring:message
            code='common.confirm' text='确认'/></button>
</div>

</body>

<script type="text/javascript">
    $(function () {
        $.get('front/getUserInfo', function (result) {
            if (result.success) {
                $("#SHARE_FUND").val(result.data.SHARE_FUND);
            }
        });
    });

    // 检查是否有空值
    function checkEmpty() {
        mui(".mui_btn").button('loading'); // 置灰
        isPass = true;
        mui("#Form input").each(function () {
            //若当前input为空，则alert提醒
            if (!this.value || this.value.trim() == "") {
                var label = this.previousElementSibling;
                mui.alert(label.innerText + "<spring:message code='common.notEmpty' text='不允许为空'/>");
                mui(".mui_btn").button('reset'); // 释放
                isPass = false;
                return false;
            }
        });
        //校验通过，继续执行业务逻辑
        if (isPass) {
            server_verification();
        }
    }

    // 计算到账结果
    function calculaResults(num) {
        if (num <= 0 || num == "") {
            $("#money").val("");
            return false;
        }
    }

    // 服务端校验
    function server_verification() {
        var url = "front/fundTransfer.do";
        //异步提交表单(先确保jquery.form.js已经引入了)
        var options = {
            url: url,
            success: function (data) {
                console.log(data)
                mui(".mui_btn").button('reset'); // 释放
                /*后台登录验证后*/
                if (data === "success") {
                    mui.toast("<spring:message code='common.success' text='成功'/>");
                    setTimeout(function () {
                        window.location.reload();
                    }, 1000)
                    return false;
                }
                if (data < 1) {
                    mui.toast("<spring:message code='common.illegalRequest' text='非法请求！'/>");
                    return false;
                }
                if (data == 1) {
                    mui.toast("<spring:message code='common.insufficient' text='可用余额不足'/>");
                    return false;
                }
                if (data == 2) {
                    mui.toast("<spring:message code='common.passwrodError' text='密码错误'/>");
                    return false;
                }
                if (data == 3) {
                    mui.toast("<spring:message code='common.userNotExist' text='用户不存在'/>");
                    return false;
                }
                if (data == 4) {
                    mui.toast("<spring:message code='common.yourself' text='不能给自己转账'/>");
                    return false;
                }
                if (data == 6) {
                    mui.toast("<spring:message code='common.mustBe' text='必须是'/>"
                        + ${par.TRANSFER_MULTIPLE} + "<spring:message code='common.multiple' text='倍数'/> ");
                    return false;
                }
            }
        };
        $("#Form").ajaxSubmit(options);
    }

</script>

</html>
