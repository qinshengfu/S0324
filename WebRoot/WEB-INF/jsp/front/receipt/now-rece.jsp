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
    <title>接单</title>
</head>

<body>
<header class="mui-bar mui-bar-nav">
    <a class="mui-action-back mui-icon mui-icon-left-nav mui-pull-left"></a>
    <h1 class="mui-title">马上接单</h1>
</header>
<div class="mui-content">
    <!-- 新加 -->

    <div class="now_re">
        <div>钱包余额（USDT）</div>
        <span>${user.USDT_WALLET}</span>
    </div>
    <div class="now_re_inp">
        <div>
            <p>请输入接单USDT数量</p>
            <input type="number" id="money" placeholder="数量(最低${par.MIN_INVESTMENT},最高${par.INVESTMENT_CEILING},且整除${par.ORDER_MULTIPLE})">
        </div>
        <div>
            <p>二级密码</p>
            <input type="password" id="password" placeholder="二级密码">
        </div>
        <button type="button" class="mui-btn mui-btn-blue mui-btn-block mui_btn">马上接单</button>
    </div>
</div>

</body>

<script>

    // 点击马上接单按钮后
    $('.mui_btn').click(function () {
        // 取投资金额和密码
        var money = $('#money').val();
        var password = $('#password').val();
        if (money < Number(${par.MIN_INVESTMENT})) {
            mui.toast("<spring:message code='order.notLess' text='金额不可小于'/> ${par.MIN_INVESTMENT}");
            return false;
        }
        if (money > Number(${par.INVESTMENT_CEILING})) {
            mui.toast("<spring:message code='common.maximum' text='最多投资'/>：" + ${par.INVESTMENT_CEILING});
            return false;
        }
        if (password == '') {
            mui.toast("<spring:message code='common.notEmpty' text='不允许为空'/>");
            return false;
        }

        server_verification(money, password);

        $('.tc_zf_bg').fadeOut()
    });


    // 服务端校验
    function server_verification(money, password) {

        var url = "front/invest.do";
        $.post(url, {money: money, password: password}, function (result) {
            console.log(result)
            mui.toast(result.message);
            if (result.success) {
                setTimeout(function () {
                    window.history.go(-1)
                }, 1000);
            }
        })
    }

</script>

</html>
