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
    <title>首页</title>
</head>

<body>
<header class="mui-bar mui-bar-nav">
    <h1 class="mui-title">首页</h1>
</header>
<div class="mui-content">
    <!-- 新加 钱包余额-->
    <div class="ind_packt">
        <div class="ind_packt_1" onclick="window.location.href='front/to_usdtWalletRecord.do'">
            <p>钱包余额(USDT)</p>
            <p>${user.USDT_WALLET}</p>
        </div>
        <div class="flex1">
            <a href="front/to_coinCharging.do">充币</a>
            <a href="front/to_withdrawMoney.do">提币</a>
            <a href="front/toTransfer.do">划转</a>
        </div>
    </div>
    <!-- 今日收益 -->
    <div class="jrsy_div">
        <p>今日收益</p>
        动态：<span>${todayDynamicIncome}</span>&emsp;&emsp;静态：<span>${todayStaticIncome}</span>
    </div>
    <!-- 积分 基金 改动-->
    <div class="index_paly">
        <ul>
            <li onclick="window.location.href='front/to_scoreRecord.do'">
                <p>娱乐积分</p>
                <p>${user.ENTERTAINMENT_SCORE}</p>
            </li>
            <li onclick="window.location.href='front/to_fundWalletRecord.do'">
                <p>分享基金</p>
                <p>${user.SHARE_FUND}</p>
            </li>
        </ul>
    </div>
    <!-- 新加 -->
    <div class="index_gird">
        <ul class="mui-table-view mui-grid-view mui-grid-9">
            <li class="mui-table-view-cell mui-media mui-col-xs-4 mui-col-sm-3">
                <a href="front/to_myTeam.do">
                    <i class="iconfont icon-tuandui_keshi"></i>
                    <div class="mui-media-body">团队</div>
                </a>
            </li>
            <li class="mui-table-view-cell mui-media mui-col-xs-4 mui-col-sm-3">
                <a href="front/to_share.do">
                    <i class="iconfont icon-yaoqing1"></i>
                    <div class="mui-media-body">邀请</div>
                </a>
            </li>
            <li class="mui-table-view-cell mui-media mui-col-xs-4 mui-col-sm-3">
                <a href="front/to_news.do">
                    <i class="iconfont icon-xinwen1"></i>
                    <div class="mui-media-body">新闻</div>
                </a>
            </li>
        </ul>
    </div>
    <!-- 新加 公告 -->
    <div class="ind_gg flex1">
        <i class="iconfont icon-gonggao1"></i>
        <marquee behavior="" direction="">${news.TITLE}</marquee>
    </div>

    <%@ include file="../../front/footer/footer.jsp" %>

</body>

</html>
