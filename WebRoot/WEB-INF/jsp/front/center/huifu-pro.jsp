<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

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
    <h1 class="mui-title">回复详情</h1>
</header>
<div class="mui-content">
    <div class="huifu">
        <p class="huifu_1">回复：</p>
        <div class="huifu_2">
            ${pd.REPLIED_CONTENT}
        </div>
        <div class="huifu_3">
            <p>回复人：${pd.REPLIED_NAME}</p>
            <p>${pd.GMT_MODIFIED}</p>
        </div>
    </div>

</div>

<script type="text/javascript">


</script>
</body>

</html>
