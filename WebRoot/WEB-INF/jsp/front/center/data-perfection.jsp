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
    <title>资料完善</title>
</head>

<style type="text/css">
    .coin_table table th, .coin_table td {
        width: 33.33%;
    }
</style>
<body>
<header class="mui-bar mui-bar-nav">
    <a class="mui-action-back mui-icon mui-icon-left-nav mui-pull-left"></a>
    <h1 class="mui-title"><spring:message code='real.perfection' text='资料完善'/></h1>
</header>
<div class="mui-content">
    <!--  -->
    <form class="setup_inp" id="Form">
        <input type="hidden" name="ACCOUNT_ID" value="${user.ACCOUNT_ID}">
        <div class="mui-input-row">
            <label><spring:message code='real.usdt' text='USDT收币地址'/>：</label>
            <input type="text" name="USDT_IN_ADDRESS" value="${user.USDT_IN_ADDRESS}">
        </div>
        <div class="mui-input-row">
            <label><spring:message code='real.question' text='密保问题'/>：</label>
            <input type="text" name="SECURITY_QUESTION" value="${user.SECURITY_QUESTION}">
        </div>
        <div class="mui-input-row">
            <label><spring:message code='real.answer' text='密保答案'/>：</label>
            <input type="text" name="ANSWER" value="${user.ANSWER}">
        </div>
    </form>
    <!--  -->
    <button type="button" class="mui-btn mui-btn-blue mui-btn-block mui_btn"><spring:message code='common.submission'
                                                                                             text='提交'/></button>

</div>

</body>

<script>
    // 客户端验证
    mui('.mui-content').on('tap', '.mui_btn', function () {
        mui(this).button('loading'); // 置灰
        check = true;
        mui(".mui-input-row input").each(function () {
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
            server_verification();
        }
    });

    // 服务端校验
    function server_verification() {
        var url = "front/updataInfo.do";
        //异步提交表单(先确保jquery.form.js已经引入了)
        var options = {
            url: url,
            success: function (data) {
                console.log(data)
                mui(".mui_btn").button('reset'); // 释放
                /*后台验证后*/
                if (data == "success") {
                    mui.toast("<spring:message code='common.modifiedSuccess' text='修改成功'/>");
                    setTimeout(function () {
                        window.location.reload();
                    }, 1000);
                    return false;
                }
                if (data == "2") {
                    mui.toast("<spring:message code='real.phoneError' text='手机号格式错误！'/>")
                } else {
                    mui.toast("<spring:message code='common.illegalRequest' text='非法请求'/>")
                }
            }
        };
        $("#Form").ajaxSubmit(options);
    }

    // 检查手机号格式
    function checkPhone(phone) {
        var pattern = /^1[3|4|5|6|7|8|9][0-9]{9}$/;
        return pattern.test(phone);
    }

</script>

</html>
