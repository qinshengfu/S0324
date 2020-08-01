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
    <h1 class="mui-title">常见问题</h1>
</header>
<div class="mui-content">
    <!-- 新加 -->
    <ul class="cenli" id="dataList">
        <li>
            <a href="">
                <div>如何充值?</div>
                <i class="iconfont icon-you-"></i>
            </a>
        </li>
    </ul>
</div>

</body>

<script>

    $(function () {
        getFAQList()
    });

    // 获取常见问题列表
    function getFAQList() {
        $.get("front/getFAQList", function (result) {
            if (result.success) {
                setFAQList(result.data.item)
            }
        })
    }

    // 渲染常见问题列表
    function setFAQList(data) {
        var ulDom = $('#dataList');
        ulDom.html("");
        var str = '';
        for (let i = 0; i < data.length; i++) {
            var pd = data[i];
            str += '<li>\n' +
                '       <a href="front/toFAQDetails/' + pd.SYS_FAQ_ID + '">\n' +
                '           <div>' + pd.TITLE + '</div>\n' +
                '           <i class="iconfont icon-you-"></i>\n' +
                '       </a>\n' +
                '   </li>'
        }
        ulDom.append(str)
    }

</script>

</html>
