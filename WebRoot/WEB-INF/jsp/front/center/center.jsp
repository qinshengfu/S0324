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
    <title>个人中心</title>
</head>
<style>
    .mui-progressbar span {
        background: #fd6346;
    }

    .mui-badge {
        position: absolute;
        right: -16px;
        top: -6px;
        font-size: 12px;
        transform: scale(0.8);
    }

    body {
        padding-bottom: 60px;
    }


</style>
<body>
<header class="mui-bar mui-bar-nav">
    <h1 class="mui-title"><spring:message code='center.home' text='个人中心'/></h1>
</header>
<div class="mui-content">

    <!-- 新加 -->
    <div class="cen_bg flex2">
        <div class="flex">
            <img class="logo-img" alt="">
            <p>${user.USER_NAME}</p>
<%--            <div>123*****8987</div>--%>
        </div>
        <button type="button" class="mui-btn mui-btn-blue">V${user.COMMISSION}</button>
    </div>

    <!-- 新加 -->
    <ul class="cen_wf flex1">
        <li>
            <a href="front/to_problem.do">
                <img src="static/front/images/liuyan.png" alt="">
                <div>留言反馈</div>
            </a>
        </li>
        <li>
            <a href="front/toFAQ.do">
                <img src="static/front/images/wenti.png" alt="">
                <div>常见问题</div>
            </a>
        </li>
    </ul>

    <!-- 新加 -->
    <ul class="cenli">
        <li>
            <a href="front/toMyUsdtAddress.do">
                <div><i class="iconfont icon-dizhi3"></i>我的USDT收币地址</div>
                <i class="iconfont icon-you-"></i>
            </a>
        </li>
        <li>
            <a href="front/toLoginPassword.do">
                <div><i class="iconfont icon-mima"></i>登录密码</div>
                <i class="iconfont icon-you-"></i>
            </a>
        </li>
        <li>
            <a href="front/toTwoPassword.do">
                <div><i class="iconfont icon-yanzhengma3"></i>二级密码</div>
                <i class="iconfont icon-you-"></i>
            </a>
        </li>
        <li>
            <a href="javascript:outLogin()">
                <div><i class="iconfont icon-tuichu2"></i>退出</div>
                <i class="iconfont icon-you-"></i>
            </a>
        </li>
    </ul>
</div>

<%@ include file="../../front/footer/footer.jsp" %>

<script type="text/javascript">

/*    $(function () {
        var gap = ${distance};
        if (gap == 100) {
            $('#gap').html("max")
        } else {
            $('#gap').html(gap + '%')
        }

        // 进度条
        mui('#informationBar').progressbar().setProgress(gap);
    });*/


    //退出登录
    function outLogin() {
        var btn = ["<spring:message code='common.confirm' text='确认'/>", "<spring:message code='common.cancel' text='取消'/>"];
        mui.confirm("<spring:message code='center.logOut' text='确认退出登录吗?'/>", "", btn, function (e) {
            if (e.index == 0) {
                $("body").html("");
                mui.toast("<spring:message code='common.success' text='成功'/>");
                setTimeout(function() {
                    window.location.href = "front/outLogin.do";
                }, 800)

            }
        });
    }
</script>
</body>

</html>
