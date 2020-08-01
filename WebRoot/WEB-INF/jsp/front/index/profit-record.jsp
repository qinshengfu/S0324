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
    <title>收益记录</title>
</head>


<body>
<header class="mui-bar mui-bar-nav">
    <a class="mui-action-back mui-icon mui-icon-left-nav mui-pull-left"></a>
    <h1 class="mui-title"><spring:message code='record.incomeRecord' text='收益记录'/></h1>
</header>
<div class="mui-content">
    <!-- nav -->
    <%--<div class="profit_nav">
        <div class="mui-segmented-control">
            <a class="mui-control-item mui-active" href="#item1">个人收益</a>
            <a class="mui-control-item" href="#item2">团队收益</a>
        </div>
    </div>--%>
    <!--  -->
    <div class="mui-content-padded profit_nav_table">
        <div id="item1" class="mui-control-content mui-active">
            <ul class="mui-table-view">
                <li class="mui-table-view-cell">
                    <!--  -->
                    <div class="coin_table">
                        <table border="0" cellspacing="" cellpadding="">
                            <tr>
                                <th><spring:message code='record.date' text='日期'/></th>
                                <th><spring:message code='record.type' text='类型'/></th>
                                <th><spring:message code='record.money' text='金额'/></th>
                                <th><spring:message code='record.state' text='状态'/></th>
                            </tr>
                            <c:forEach items="${list}" var="var">
                                <tr>
                                    <td>${var.GMT_CREATE}</td>
                                    <td>${var.AMOUNT_TYPE}</td>
                                    <td>${var.TAG}${var.MONEY}</td>
                                    <td>${var.STATUS}</td>
                                </tr>
                            </c:forEach>

                        </table>
                    </div>
                </li>
            </ul>
        </div>

        <%--<div id="item2" class="mui-control-content">
            <ul class="mui-table-view">
                <li class="mui-table-view-cell">
                    <!--  -->
                    <div class="coin_table">
                        <table border="0" cellspacing="" cellpadding="">
                            <tr>
                                <th>日期</th>
                                <th>类型</th>
                                <th>金额</th>
                                <th>状态</th>
                            </tr>
                            <tr>
                                <td>2020-03-27 00:00:00</td>
                                <td>团队收益</td>
                                <td>100</td>
                                <td>已完成</td>
                            </tr>
                            <tr>
                                <td>2020-03-27 00:00:00</td>
                                <td>12000000</td>
                                <td>100</td>
                                <td>已完成</td>
                            </tr>
                        </table>
                    </div>
                </li>
            </ul>
        </div>--%>
    </div>

</div>

</body>

</html>
