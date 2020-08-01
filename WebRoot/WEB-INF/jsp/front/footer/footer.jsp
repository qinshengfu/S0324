<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
<head>
    <title></title>
</head>

<style type="text/css">
    .mui-tab-item i{
        display: block;
        font-size: 20px;
        margin-top: 3px;
        line-height: normal;
    }
    .mui-tab-label{
        font-size: 12px;
    }
    .mui-bar{
        -webkit-box-shadow: none;
        box-shadow: none;
        background-color: #fff;
    }
    .mui-bar-tab .mui-tab-item.mui-active{
        color: #ff7f07;
    }
    .mui-bar-tab .mui-tab-item{
        color: #b5b5b5;
    }
</style>

<body>
<nav class="mui-bar mui-bar-tab mui-bar1">
    <a class="mui-tab-item mui-active" id="index" href="front/to_index.do">
        <i class="iconfont icon-shouye5"></i>
        <span class="mui-tab-label"><spring:message code='footer.home' text='首页'/></span>
    </a>
    <a class="mui-tab-item" id="receipt" href="front/to_receipt.do">
        <i class="iconfont icon-tianchongxing-"></i>
        <span class="mui-tab-label"><spring:message code='order.home' text='接单'/></span>
    </a>
    <a class="mui-tab-item" id="my" href="front/to_center.do">
        <i class="iconfont icon-wode4"></i>
        <span class="mui-tab-label"><spring:message code='footer.my' text='我的'/></span>
    </a>
</nav>

<script type="text/javascript">

    // 移除高亮样式
    $(".mui-tab-item").removeClass("mui-active");
    var flag = '${flag}';
    // 添加高亮样式
    $("#"+flag).addClass("mui-active")

    // 解决a标签 和 导航栏 无法跳转
    mui('body').on('tap', 'a', function () {
        document.location.href = this.href;
    });

    //物理返回按键监听
    var first = null;
    mui.back = function () {
        //首次按键，提示‘再按一次退出应用’
        if (!first) {
            first = new Date().getTime();
            mui.toast('再按一次退出应用');
            setTimeout(function () {
                first = null;
            }, 1000);
        } else {
            if (new Date().getTime() - first < 1000) {
                plus.runtime.quit();
            }
        }
    };

    //解决mui 键盘弹出 将底部选项卡顶上来
    mui.plusReady(function () {
        var height = document.documentElement.clientHeight || document.body.clientHeight;
        plus.webview.currentWebview().setStyle({
            height: height
        });
        window.onresize = function () {
            plus.webview.currentWebview().setStyle({
                height: height
            })
        }
    });


</script>
</body>

</html>
