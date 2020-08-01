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
    <title></title>
</head>

<body>
<header class="mui-bar mui-bar-nav">
    <a class="mui-action-back mui-icon mui-icon-left-nav mui-pull-left"></a>
    <h1 class="mui-title">资料完善</h1>
</header>
<div class="mui-content">
    <!-- 新加 -->
    <div class="now_re" style="font-size: 14px;">
        USDT钱包只能向USDT地址发送资产，如果向非USDT地址发送资产将不可找回。仅支持ERC20的转出。请认真核实ERC20地址，确保无误后进行修改填写。
    </div>

    <form id="Form">
        <input type="hidden" name="ACCOUNT_ID" value="${user.ACCOUNT_ID}">
        <div class="now_re_inp">
            <div>
                <p>USDT收币地址</p>
                <input type="text" name="USDT_IN_ADDRESS" value="${user.USDT_IN_ADDRESS}" placeholder="请输入提币地址">
            </div>
            <div>
                <p>密保问题</p>
                <input type="text" name="SECURITY_QUESTION" value="${user.SECURITY_QUESTION}" placeholder="请输入密保问题">
            </div>
            <div>
                <p>密保答案</p>
                <input type="text" name="ANSWER" value="${user.ANSWER}" placeholder="请输入密保答案">
            </div>
            <%-- <div>
                 <p>二级密码</p>
                 <input type="password" name="password" placeholder="二级密码">
             </div>--%>
            <button type="button" class="mui-btn mui-btn-blue mui-btn-block mui_btn">提交</button>
        </div>
    </form>
</div>
</body>
<script>
    // 客户端验证
    mui('.mui-content').on('tap', '.mui_btn', function () {
        mui(this).button('loading'); // 置灰
        check = true;
        mui(".now_re_inp input").each(function () {
            //若当前input为空，则alert提醒
            if (!this.value || this.value.trim() == "") {
                var label = this.previousElementSibling;
                mui.alert(label.innerText + "<spring:message code='common.notEmpty' text='不允许为空'/>");
                mui(".mui_btn").button('reset'); // 释放
                check = false;
                return false;
            }
        }); //校验通过，继续执行业务逻辑
        if (check) {
            server_verification();
        }
    });

    // 服务端校验
    function server_verification() {
        var url = "front/updataInfo.do";
        //异步提交表单(先确保jquery.form.js已经引入了)
        var options = {
            url: url,
            success: function (data) {
                mui(".mui_btn").button('reset'); // 释放
                /*后台验证后*/
                if (data == "success") {
                    mui.toast("<spring:message code='common.modifiedSuccess' text='修改成功'/>");
                    setTimeout(function () {
                        window.location.reload();
                    }, 1000);
                    return false;
                }
                if (data == "2") {
                    mui.toast("<spring:message code='real.phoneError' text='手机号格式错误！'/>")
                } else {
                    mui.toast("<spring:message code='common.illegalRequest' text='非法请求'/>")
                }
            }
        };
        $("#Form").ajaxSubmit(options);
    }
</script>

</html>
