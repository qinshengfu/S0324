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
    body {
        /*background: url(static/front/images/init.png) no-repeat;
        background-size: 100% 100%;
        background-attachment: fixed;
        height: 100vh;
        width: 100vw;*/
        background: linear-gradient(to right, #ffce29, #fc7b05);
        /*background: #fffebb;*/
    }

    .share_posi {
        width: 90%;
        /*background: url(static/front/images/init.png) no-repeat;
        background-size: 100% 100%;
        background-attachment: fixed;*/
        background: linear-gradient(to right, #ffce29, #fc7b05);
        /*background-color: rgba(119, 255, 90, 0.6);*/
        border-radius: 6px;
        margin: 10% auto;
        text-align: center;
    }

    .share_posi .logo {
        width: 120px;
    }

    .share_yqm p {
        color: #000;
    }

    .share_yqm div {
        color: #ff4529;
        font-size: 25px;
        margin-top: 15px;
        font-weight: bold;
        text-shadow: 0 0 10px #ffcc28;
    }

    .line {
        border: 1px dashed #fff;
        position: relative;
        margin: 20px auto;
    }

    /*.line:after {
        content: '';
        display: block;
        position: absolute;
        left: -15px;
        top: -15px;
        width: 30px;
        height: 30px;
        border-radius: 50%;
        background-color: rgb(255, 73, 41);
    }

    .line:before {
        content: '';
        display: block;
        position: absolute;
        right: -15px;
        top: -15px;
        width: 30px;
        height: 30px;
        border-radius: 50%;
        background-color: rgb(255, 102, 38);
    }*/

    .share_ewm {
        padding: 10px;
    }

    .share_ewm p {
        color: #000;
    }

    .share_ewm img {
        width: 100%;
        height: 100%;
    }

    .ewm_bor {
        margin: 20px auto;
        width: 150px;
        height: 150px;
        border: 5px solid #ff4529;
        padding: 5px;
        background-color: white;
        border-radius: 6px;
    }
</style>
<body>
<header class="mui-bar mui-bar-nav">
    <a class="mui-action-back mui-icon mui-icon-left-nav mui-pull-left"></a>
    <h1 class="mui-title">邀请分享</h1>
</header>
<%--<div class="mui-content">
    <div class="ewm_posi">
        <div class="ewm_posi_1">我的邀请码是：${user.INVITATION_CODE}</div>
        <div class="flex2">
            <!-- 头像文字 -->
            <div class="flex">
                <img src="static/front/images/eddh_bg.png" alt="">
                <div>
                    <h4>HI,我是TINE</h4>
                    <p>人生的道路虽然漫长，但紧要处常常只有几步……</p>
                </div>
            </div>
            <!-- 二维码 -->
            <img id="QRcode" src="front/qr_code.do" alt="">
        </div>
    </div>
</div>--%>

<div id="save" class="mui-content">
    <div class="share_posi" id="picShare">
        <!-- logo -->
        <img class="logo logo-img" src="" alt="">
        <!--  -->
        <div class="share_yqm">
            <p>邀请码</p>
            <div>
                ${user.INVITATION_CODE}
                <button type="button" class="mui-btn mui-btn-blue copy" style="padding: 0px 2px;" data-clipboard-text="${user.INVITATION_CODE}">
                    <spring:message code='common.replication' text='复制'/>
                </button>
            </div>
        </div>
        <!-- line -->
        <div class="line"></div>
        <!-- 二维码 -->
        <div class="share_ewm">
            <input id="previewImage" type="hidden">
            <p>邀请二维码</p>
            <div class="ewm_bor"><img id="QRcode" src="front/qr_code.do" alt=""></div>
        </div>
    </div>
    <!-- 按钮 -->
    <%--<a id="mui_btn">
        <button type="button" onclick="saveSharePic()" class="mui-btn mui-btn-blue mui-btn-block mui_btn">保存图片</button>
    </a>--%>
</div>

</body>

<script>

    $(document).ready(function () {

        /*html2canvas($("#picShare")).then(function (canvas) {
            var imgUri = canvas.toDataURL("image/png", 1);
            $("#mui_btn").attr("href", imgUri).attr("download", "推广图.png");
        })*/
        longPress("save", saveSharePic)
    });



    var context = "<%=basePath%>" + "release/toRegister.do?tag=${user.INVITATION_CODE}";

    console.log(context)

</script>


</html>
