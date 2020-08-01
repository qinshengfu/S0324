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
    <title>充币</title>
</head>

<%-- 此页面为线下充值 --%>

<body>
<header class="mui-bar mui-bar-nav">
    <a class="mui-action-back mui-icon mui-icon-left-nav mui-pull-left"></a>
    <h1 class="mui-title"><spring:message code='charging.home' text='充币'/></h1>
    <a href="front/to_coinRecord.do" class="header_right"><spring:message code='charging.record' text='充币记录'/></a>
</header>
<div class="mui-content">
    <!--  -->
    <form id="Form">
        <div class="setup_inp">
            <div class="mui-input-row">
                <label><spring:message code='real.usdt' text='USDT收币地址'/>：</label>
                <input class="copy" type="text" readonly value="${par.USDT_ADDRESS}" data-clipboard-text="${par.USDT_ADDRESS}">
            </div>
            <div class="mui-input-row">
                <label><spring:message code='record.money' text='金额'/>:</label>
                <input type="number" name="money" value="${par.MIN_RECHARGE}" placeholder="<spring:message code='record.money' text='金额'/>">
            </div>
            <div class="mui-input-row">
                <label><spring:message code='register.securityPassword' text='安全密码'/>:</label>
                <input type="password" name="securityPassword"
                       placeholder="<spring:message code='register.securityPassword' text='安全密码'/>">
            </div>
            <div class="real_name">
                <div class="real_name_div">
                    <%-- 图片存放地址--%>
                    <input class="picPath" type="hidden" name="voucher"/>
                    <div class="real_name_div_1">
                        <img src="static/front/images/upload.png" id="voucher" alt="上传图片">
                        <input type="file" onchange="upload(this,'voucher')">
                    </div>
                        <p>(<spring:message code='common.pay' text='支付凭证'/>)</p>
                </div>
            </div>

            <div class="coin_text">
                <div class="all_color"><spring:message code='charging.info1' text='提示信息'/></div>
                <p class="p_color"><spring:message code='charging.info2' text='提示信息'/></p>
                <p class="p_color"><spring:message code='charging.info3' text='提示信息'/></p>
                <p class="p_color"><spring:message code='charging.info4' text='提示信息'/></p>
                <p class="p_color" style="color: red">
                    <spring:message code='charging.info5' text='充值范围：'/>
                    ${par.MIN_RECHARGE} ~ ${par.MAX_RECHARGE}
                    <spring:message code='charging.info6' text=' 否则充值不成功'/>
                </p>
            </div>
        </div>
    </form>

    <!-- 按钮 -->
    <button type="button" class="mui-btn mui-btn-blue mui-btn-block mui_btn">确定</button>
</div>
</body>

<script type="text/javascript">

    var min = '${par.MIN_RECHARGE}';
    var max = '${par.MAX_RECHARGE}';

    // 客户端验证
    mui('.mui-content').on('tap', '.mui_btn', function () {
        calculaResults();
        mui(this).button('loading'); // 置灰
        check = true;
        mui(".mui-input-row input").each(function () {
            //若当前input为空，则alert提醒
            if (!this.value || this.value.trim() == "") {
                var label = this.previousElementSibling;
                mui.alert(label.innerText + "<spring:message code='common.notEmpty' text='不允许为空'/>");
                mui(".mui_btn").button('reset'); // 释放
                check = false;
                return false;
            }
        });
        // 支付凭证上传校验
        if (check) {
            mui(".real_name_div input").each(function () {
                //若当前input为空，则alert提醒
                if (!this.value || this.value.trim() == "") {
                    var label = this.nextElementSibling.nextElementSibling;
                    mui.alert(label.innerText + "<spring:message code='common.notEmpty' text='不允许为空'/>");
                    mui(".mui_btn").button('reset'); // 释放
                    check = false;
                    return false;
                }
            });
        }
        //校验通过，继续执行业务逻辑
        if (check) {
            server_verification();
        }
    });

    // 计算结果
    function calculaResults() {
        var num = $("input[name = 'money']").val();
        if (parseFloat(num).toString() == "NaN") {
            $("input[name = 'money']").val(min);
            return false;
        }
        if (num < 0) {
            $("input[name = 'money']").val(min);
            return false;
        }
        if (num < Number(min)) {
            $("input[name = 'money']").val(min);
            return false;
        }
        if (num >= Number(max)) {
            $("input[name = 'money']").val(max);
            return false;
        }
    }

    // 服务端校验
    function server_verification() {
        var url = "front/applyRecharge.do";
        //异步提交表单(先确保jquery.form.js已经引入了)
        var options = {
            url: url,
            success: function (result) {
                console.log(result)
                mui(".mui_btn").button('reset'); // 释放
                /*后台验证后*/
                if (result.success) {
                    mui.toast(result.message);
                    setTimeout(function () {
                        window.location.reload();
                    }, 1000);
                    return false;
                } else {
                    mui.toast(result.message);
                }
            }
        };
        $("#Form").ajaxSubmit(options);
    }
</script>


<%--图片压缩上传--%>
<script>

    //声明一个formdata 用来上传
    var UForm;
    // 定义图片原始大小、压缩后的大小
    var oldfilesize, newfilesize;

    // 当上传按钮内容发送改变后 获取文件并调用压缩图片的方法
    function upload(itself, id) {
        UForm = new FormData();
        GetFile(itself.files, id);
    }

    // GetFile 处理获取到的file对象，并对它进行压缩处理, id 是显示图片的容器
    function GetFile(files, id) {
        // 用三目运算符频道文件是否存在
        var file = files ? files[0] : false;
        if (!file) {
            return;
        }
        if (file) {
            oldfilesize = Math.floor((file.size / 1024) * 100) / 100;
            // 如果图片少于5M 则不进行压缩
            if (oldfilesize < 5000) {
                UForm.append("files", file);
                ShowFile(file, id);
                return;
            }
            lrz(file, {
                width: 2048, //设置压缩后的最大宽
                height: 1080,
                quality: 0.8 //图片压缩质量，取值 0 - 1，默认为0.7
            }).then(function (rst) {
                newfilesize = Math.floor((rst.file.size / 1024) * 100) / 100;
                console.log("图片压缩成功，原为：" + oldfilesize + "KB,压缩后为：" + newfilesize + "KB");
                // 把压缩后的图片文件存入 formData中，这样用ajax传到后台才能接收
                UForm.append("files", rst.file);
                ShowFile(rst.file, id);
            }).catch(function (err) {
                alert("压缩图片时出错，请上传图片文件！");
                return false;
            });
        }
    }

    // ShowFile 把处理后的图片显示出来，实现图片的预览功能：
    function ShowFile(file, id) {
        // 使用fileReader对文件对象进行操作
        var reader = new FileReader();
        reader.onload = function (e) {
            var img = new Image();
            img.src = e.target.result;
            // console.log(img)
            // 图片本地回显
            $('#' + id).attr({src: img.src});
            location.href
        };
        reader.onerror = function (e, b, c) {
            //error
        };
        // 读取为数据url
        reader.readAsDataURL(file);
        // 上传到服务器
        DoUp(id);
    }

    // 使用AJAX上传数据到后台
    function DoUp(id) {
        mui.toast("上传服务器中");
        $.ajax({
            url: "front/addPic.do",
            type: "POST",
            data: UForm,
            contentType: false,//禁止修改编码
            processData: false,//不要把data转化为字符
            success: function (data) {
                mui.toast("<spring:message code='common.uploadSuccess' text='上传成功！'/>");
                // 上传成功 返回图片路径
                picture_path = (data + "").trim();
                var sta = picture_path;
                $("input[name=" + id + "]").attr({value: sta});
            },
            error: function (e) {
                mui.toast("<spring:message code='common.unknownError' text='未知错误！'/>");
                console.log("上传出错！请检查是否选择了图片");
            }
        });
    }
</script>

</html>
