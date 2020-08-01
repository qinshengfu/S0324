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
    <title>互转记录</title>
</head>

<style type="text/css">
    .coin_table table th, .coin_table table td {
        width: 20%;
    }
</style>
<body>
<header class="mui-bar mui-bar-nav">
    <a class="mui-action-back mui-icon mui-icon-left-nav mui-pull-left"></a>
    <h1 class="mui-title"><spring:message code='record.transferRecord' text='互转记录'/></h1>
</header>
<div class="mui-content">
    <div class="coin_table">
        <table border="0" cellspacing="" cellpadding="">
            <tr>
                <th><spring:message code='record.date' text='日期'/></th>
                <th><spring:message code='record.user' text='用户'/></th>
                <th><spring:message code='record.type' text='类型'/></th>
                <th><spring:message code='record.money' text='金额'/></th>
                <th><spring:message code='record.state' text='状态'/></th>
            </tr>
            <c:forEach items="${list}" var="var">
                <tr>
                    <td>${var.GMT_CREATE}</td>
                    <td>${var.HE_NAME}</td>
                    <c:if test="${var.SCORE_REC_ID != null}">
                        <td>娱乐积分</td>
                    </c:if>
                    <c:if test="${var.FUND_REC_ID != null}">
                        <td>分享基金</td>
                    </c:if>
                    <c:if test="${var.USDT_REC_ID != null}">
                        <td>usdt</td>
                    </c:if>
                    <td>${var.TAG}${var.MONEY}</td>
                    <td>${var.STATUS}</td>
                </tr>
            </c:forEach>
        </table>
    </div>
</div>

</body>

<script>
</script>

</html>
