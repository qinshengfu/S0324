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
    <title>新闻动态</title>
</head>

<body>
<header class="mui-bar mui-bar-nav">
    <a class="mui-action-back mui-icon mui-icon-left-nav mui-pull-left"></a>
    <h1 class="mui-title"><spring:message code='center.news' text='新闻动态'/></h1>
</header>
<div class="mui-content">
    <div class="news_list">
        <ul class="mui-table-view">
            <c:forEach items="${newsList}" var="var">
                <li class="mui-table-view-cell">
                    <a class="mui-navigate-right" href="front/to_newsDetail/${var.SYS_NEWS_ID}.do">
                        ${var.TITLE}
                    </a>
                </li>
            </c:forEach>
        </ul>
    </div>
</div>
</body>

</html>
