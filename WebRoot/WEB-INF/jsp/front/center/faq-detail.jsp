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
    <title>新闻详情</title>
</head>


<body>
<header class="mui-bar mui-bar-nav">
    <a class="mui-action-back mui-icon mui-icon-left-nav mui-pull-left"></a>
    <h1 class="mui-title"><spring:message code='center.news' text='新闻动态'/></h1>
</header>
<div class="mui-content">
    <div class="news_detail">
        <h4>${pd.TITLE}</h4>
        <div class="news_detail_div">
            ${pd.CONTENT}
            <div>
                <p>${pd.GMT_MODIFIED}</p>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript">
    mui.init()
</script>
</body>

</html>
