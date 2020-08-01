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
    <title>邀请码</title>
</head>

<style type="text/css">
    .mui-content {
        background: url(static/front/images/share_bg.png) no-repeat;
        background-size: 100% 100%;
        background-attachment: fixed;
        height: 100vh;

    .p_color {
        color: #fff;
    }

</style>
<body>
<header class="mui-bar mui-bar-nav">
    <a class="mui-action-back mui-icon mui-icon-left-nav mui-pull-left"></a>
    <h1 class="mui-title"><spring:message code='center.share' text='邀请分享'/></h1>
</header>
<div class="mui-content">
    <div class="ewm">
        <img src="front/qr_code.do">
        <p class="p_color"></p>
        <button type="button" class="mui-btn mui-btn-blue mui-btn-block mui_btn copy" data-clipboard-text=" ">
            <spring:message code='common.replication' text='复制'/>
        </button>
    </div>
</div>

</body>

<script>
    $(function () {
        var context = "<%=basePath%>" + "release/toRegister.do?tag=${user.INVITATION_CODE}";
        $(".p_color").html(context);
        $(".mui-btn-block").attr("data-clipboard-text", context);
    });

</script>

</html>
