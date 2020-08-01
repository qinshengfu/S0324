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
    <title>提币</title>
</head>


<body>
<header class="mui-bar mui-bar-nav">
    <a class="mui-action-back mui-icon mui-icon-left-nav mui-pull-left"></a>
    <h1 class="mui-title"><spring:message code='withdraw.home' text='提币'/></h1>
    <a href="front/to_witmoeyRecord.do" class="header_right"><spring:message code='withdraw.record' text='提币记录'/></a>
</header>
<div class="mui-content">
    <!--  -->
    <form class="wit_moey" id="Form">
        <input type="hidden" name="ACCOUNT_ID" readonly value="${user.ACCOUNT_ID}">
        <div class="mui-input-row">
            <label><spring:message code='withdraw.accountBalance' text='账户余额'/></label>
            <input type="text" id="USDT_WALLET" value="" disabled="disabled">
            <p>USDT</p>
        </div>
        <div class="mui-input-row">
            <label><spring:message code='withdraw.inAddress' text='提币地址'/></label>
            <input type="text" id="address" value="" disabled="disabled">
        </div>
        <div class="mui-input-row">
            <label><spring:message code='withdraw.amount' text='提币数量'/></label>
            <input style="width: 30%;" type="number" id="moneyAmount" name="moneyAmount"
                   oninput="calculaResults(this.value)" placeholder="0">
            <button type="button" id="whole" class="mui-btn mui-btn-blue"><spring:message code='withdraw.extractAll'
                                                                                          text='提取全部'/></button>
        </div>
        <div class="mui-input-row">
            <label><spring:message code='withdraw.charge' text='手续费'/></label>
            <input type="number" value="${par.CHARGE_FOR_WITHDRAWAL}" disabled="disabled">
            <p>USDT</p>
        </div>
        <div class="mui-input-row">
            <label><spring:message code='withdraw.arrival' text='到账数量'/></label>
            <input type="number" id="arrivalNumber" readonly placeholder="0">
            <p>USDT</p>
        </div>
        <div class="mui-input-row">
            <label><spring:message code='register.securityPassword' text='安全密码'/></label>
            <input type="password" name="securityPassword"
                   placeholder="<spring:message code='register.securityPassword' text='安全密码'/>">
        </div>

    </form>
    <!--  -->
    <div class="coin_text">
        <p class="p_color"><spring:message code='withdraw.info1' text='提现须知'/></p>
        <%--        <p class="p_color">最小提币数量为 <span>100</span> USDT</p>--%>
        <p class="p_color"><spring:message code='withdraw.info2' text='信息'/></p>
        <p class="p_color"><spring:message code='withdraw.info3' text='信息'/> <span>${par.CASH_TIME}</p>
    </div>
    <!-- 按钮 -->
    <button type="button" onclick="checkEmpty()" class="mui-btn mui-btn-blue mui-btn-block mui_btn"><spring:message
            code='common.confirm' text='提交'/></button>
</div>

</body>

<script type="text/javascript">

    // 最大可提现usdt
    var maxUsdt = 0;
    // 提现手续费
    var charge = ${par.CHARGE_FOR_WITHDRAWAL};
    // 最少提现
    var minCash = ${par.MIN_CASH};

    $(function () {
        $.get('front/getUserInfo', function (result) {
            if (result.success) {
                if (!result.data.USDT_IN_ADDRESS) {
                    alert("请先填写收币地址")
                    $("body").html("");
                    setTimeout(function () {
                        window.location.href = "front/toMyUsdtAddress.do"
                    }, 200)
                }
                maxUsdt = result.data.USDT_WALLET;
                $("#address").val(result.data.USDT_IN_ADDRESS);
                $("#USDT_WALLET").val(result.data.USDT_WALLET);
            }
        });
        $("#whole").click(function () {
            $("#moneyAmount").val(maxUsdt);
            calculaResults(maxUsdt)
        });
    });


    // 检查是否有空值
    function checkEmpty() {
        mui(".mui_btn").button('loading'); // 置灰
        isPass = true;
        mui("#Form input").each(function () {
            //若当前input为空，则alert提醒
            if (!this.value || this.value.trim() == "") {
                var label = this.previousElementSibling;
                mui.alert(label.innerText + "<spring:message code='common.notEmpty' text='不允许为空！'/>");
                mui(".mui_btn").button('reset'); // 释放
                isPass = false;
                return false;
            }
            if ($("#moneyAmount").val().trim() == "" || $("#moneyAmount").val().trim() < minCash) {
                mui.toast("最少提现 " + minCash + " USDT");
                mui(".mui_btn").button('reset'); // 释放
                isPass = false;
                return false;
            }
        });
        //校验通过，继续执行业务逻辑
        if (isPass) {
            server_verification();
        }
    }

    // 计算到账结果
    function calculaResults(num) {
        if (num < 0 || num == "") {
            $("#moneyAmount").val("");
            $("#arrivalNumber").val("");
            return false;
        }
        if (num > maxUsdt) {
            $("#moneyAmount").val(maxUsdt);
            calculaResults(maxUsdt);
            return false;
        }
        if (num <= charge) {
            $("#arrivalNumber").val("");
            return false;
        }
        // 扣除手续费写入到账数量
        var result = num - charge;
        $("#arrivalNumber").val(result);
    }

    // 服务端校验
    function server_verification() {
        var url = "front/cashMoney.do";
        //异步提交表单(先确保jquery.form.js已经引入了)
        var options = {
            url: url,
            success: function (data) {
                mui(".mui_btn").button('reset'); // 释放
                /*后台登录验证后*/
                if (data === "success") {
                    mui.toast("<spring:message code='withdraw.audit' text='等待审核'/>");
                    setTimeout(function () {
                        window.location.reload();
                    }, 1000);
                    return false;
                }
                if (data < 4) {
                    mui.toast("<spring:message code='common.illegalRequest' text='非法请求'/>");
                    return false;
                }
                if (data == 4) {
                    mui.toast("<spring:message code='common.passwrodError' text='密码错误'/>");
                    return false;
                }
                if (data == 5) {
                    mui.toast("<spring:message code='withdraw.notTime' text='不在受理时间'/>");
                    return false;
                }
                if (data == 6) {
                    mui.toast("<spring:message code='withdraw.cumulative' text='累积充值'/> " +
                        ${par.WITHDRAWAL_CONDITIONS} +
                            " <spring:message code='withdraw.cash' text='才能提现'/>");
                    return false;
                }
                if (data == 7) {
                    mui.toast("最少提现 " + minCash + " USDT");
                    return false;
                }
                if (data == 8) {
                    mui.toast("今日提现次数已上限");
                    return false;
                }
            }
        };
        $("#Form").ajaxSubmit(options);
    }

</script>

</html>
