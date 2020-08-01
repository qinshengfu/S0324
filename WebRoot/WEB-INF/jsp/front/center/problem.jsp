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
    <title>问题反馈</title>
</head>

<body>
<header class="mui-bar mui-bar-nav">
    <a class="mui-action-back mui-icon mui-icon-left-nav mui-pull-left"></a>
    <h1 class="mui-title">问题反馈</h1>
</header>
<div class="mui-content">
    <from id="From">
        <div class="add_pro">
            <!--  -->
            <div class="proble">
                <i class="iconfont icon-sanjiaoleft"></i>
                <select name="INFO_TYPE">
                    <option value="意见反馈">意见反馈</option>
                    <option value="信息咨询">信息咨询</option>
                    <option value="平台问题">平台问题</option>
                    <option value="投诉举报">投诉举报</option>
                </select>
            </div>
            <!--  -->
            <div class="proble">
                <textarea name="INFO_CONTENT" rows="10" cols="" maxlength="100"
                          placeholder="请输入留言内容,字数控制在100以内"></textarea>
            </div>
            <!--  -->
            <div class="proble">
                <div class="coin_inp_1">
                    <%-- 图片存放地址--%>
                    <input class="picPath" type="hidden" name="voucher"/>
                    <%-- 图片上传控件 --%>
                    <div>
                        <input type="file" onchange="upload(this,'voucher')">
                        <img src="static/front/images/uplo.png" id="voucher" alt="">
                    </div>
                </div>
            </div>
        </div>
        <!--  -->
        <button type="button" class="mui-btn mui-btn-blue mui-btn-block mui_btn">提交</button>
    </from>
    <!-- 回复记录 -->
    <ul id="dataList" class="pro_addlist">
        <li>
            <div>标题：xxx</div>
            <div>状态：<a href="toFeedbackDetails/id">未查看</a></div>
            <div>时间：2020-06-17</div>
        </li>
        <li>
            <div>标题：xxx</div>
            <div>状态：<a href="huifu-pro.html">未查看</a></div>
            <div>时间：2020-06-17</div>
        </li>
    </ul>

</div>


</body>

<script type="text/javascript">
    $(function () {
        getfeedbackList()
    })

    $('.mui_btn').click(function () {
        console.log($('textarea[name = "INFO_CONTENT"]').val())
        if (!$('textarea[name = "INFO_CONTENT"]').val()) {
            mui.toast("请输入反馈内容");
            return false;
        }

        if (!$('input[name = "voucher"]').val()) {
            mui.toast("请上传反馈图片");
            return false;
        }

        server_verification();
    });

    // 服务端校验
    function server_verification() {
        var url = "front/infoFeedback.do";
        //异步提交表单(先确保jquery.form.js已经引入了)
        var options = {
            url: url,
            success: function (result) {
                mui(".mui_btn").button('reset'); // 释放
                mui.toast(result.message)
                if (result.success) {
                    setTimeout(function () {
                        window.location.reload();
                    }, 1000)
                }
            }
        };
        $('From').ajaxSubmit(options);
    }

    // 获取反馈列表
    function getfeedbackList() {
        $.get("front/feedbackList", function (result) {
            if (result.success) {
                setFeedbackList(result.data.item)
            }
        })
    }

    // 渲染反馈列表
    function setFeedbackList(data) {
        var ulDom = $("#dataList");
        ulDom.html("");

        var str = ''
        for (let i = 0; i < data.length; i++) {
            var pd = data[i];
            str += '<li>\n' +
                '       <div>标题：' + pd.INFO_TYPE + '</div>\n' +
                '       <div>状态：<a href="front/toFeedbackDetails/' + pd.MESSAGE_FEEDBACK_ID + '">' + pd.STATUS + '</a></div>\n' +
                '       <div>时间：' + pd.GMT_CREATE + '</div>\n' +
                '   </li>'
        }
        ulDom.append(str)
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
