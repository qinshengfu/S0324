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
    <title>实名认证</title>
</head>

<body style="background-color: white;">
<header class="mui-bar mui-bar-nav">
    <a class="mui-action-back mui-icon mui-icon-left-nav mui-pull-left"></a>
    <h1 class="mui-title"><spring:message code='real.home' text='实名认证'/></h1>
</header>
<div class="mui-content">
    <form class="login_pass" id="Form">
        <div class="mui-input-row">
            <label><spring:message code='real.name' text='姓名'/></label>
            <input type="text" name="FULL_NAME" placeholder="<spring:message code='real.name' text='姓名'/>"
                   value="${pd.FULL_NAME}">
        </div>
        <div class="mui-input-row">
            <label><spring:message code='real.idNumer' text='身份证号码'/></label>
            <input type="text" name="IDNUMBER" placeholder="<spring:message code='real.idNumer' text='身份证号码'/>"
                   value="${pd.IDNUMBER}">
        </div>

        <div class="real_name">
            <h4><spring:message code='real.card' text='身份证正反面照片'/>:</h4>
            <div class="real_name_div">
                <%-- 图片存放地址--%>
                <input class="picPath" type="hidden" name="FRONTPIC"/>
                <div class="real_name_div_1">
                    <c:if test="${pd.FRONTPIC == null}">
                        <img src="static/front/images/upload.png" id="FRONTPIC">
                        <input type="file" onchange="upload(this,'FRONTPIC')">
                    </c:if>
                    <c:if test="${pd.FRONTPIC != null}">
                        <img src="${pd.FRONTPIC}" id="FRONTPIC">
<%--                        <input type="file" onchange="upload(this,'FRONTPIC')">--%>
                    </c:if>
                </div>
                <p>(<spring:message code='real.front' text='正面'/>)</p>
            </div>
            <div class="real_name_div">
                <input class="picPath" type="hidden" name="BACKPIC"/>
                <div class="real_name_div_1">
                    <c:if test="${pd.BACKPIC == null}">
                        <img src="static/front/images/upload.png" id="BACKPIC">
                        <input type="file" onchange="upload(this,'BACKPIC')">
                    </c:if>
                    <c:if test="${pd.BACKPIC != null}">
                        <img src="${pd.BACKPIC}" id="BACKPIC">
<%--                        <input type="file" onchange="upload(this,'BACKPIC')">--%>
                    </c:if>
                </div>
                <p>(<spring:message code='real.back' text='反面'/>)</p>
            </div>
        </div>
    </form>
    <!-- 按钮 -->
    <button type="button" class="mui-btn mui-btn-blue mui-btn-block mui_btn"><spring:message code='common.submission'
                                                                                             text='提交'/></button>
</div>

</body>

<script type="text/javascript">

    // 客户端验证
    mui('.mui-content').on('tap', '.mui_btn', function () {

        <c:if test="${pd.STATUS == 0}">
            mui.toast("<spring:message code='common.isReal' text='已认证'/>");
            mui(this).button('loading'); // 置灰
        return
        </c:if>

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
        // 身份证上传校验
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

    // 服务端校验
    function server_verification() {
        var url = "front/updataRealName.do";
        //异步提交表单(先确保jquery.form.js已经引入了)
        var options = {
            url: url,
            success: function (data) {
                console.log(data)
                mui(".mui_btn").button('reset'); // 释放
                /*后台验证后*/
                if (data == "success") {
                    mui.toast("<spring:message code='common.modifiedSuccess' text='修改成功'/>");
                    setTimeout(function () {
                        window.location.reload();
                    }, 1000);
                    return false;
                }
                if (data == "0") {
                    mui.toast("<spring:message code='common.illegalRequest' text='非法请求'/>")
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
