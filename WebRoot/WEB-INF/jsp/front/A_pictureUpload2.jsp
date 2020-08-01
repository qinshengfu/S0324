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

<head>
    <title>jquery+lrz实现本地图片预览和压缩后上传的在线演示-aijQuery.cn</title>
    <script type="text/javascript" src="static/front/js/jquery-3.3.1.min.js"></script>
    <script type="text/javascript" src="static/js/lrz.bundle.js"></script>
    <link rel="stylesheet" href="//maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta.2/css/bootstrap.min.css">
    <style type="text/css">
        #drop {
            height: 200px;
            border: 2px dashed #2D67D1;
        }

        h2 {
            line-height: 130px
        }

        .drag_hover {
            border: 2px dashed redEnvelope !important;
            background-color: #2E6DA4 !important;
        }
    </style>
</head>
<body style="text-align:center">
<h3>jquery+lrz实现本地图片预览和压缩后上传的在线演示</h3>
<div id="drop" class="w-75 mx-auto">
    <h2>可把图片直接拖拽到这里</h2>

</div>              <!--accept="image/*" 表示只接收图片文件 -->
<input type="file" name="file" accept="image/*" class="form-control-file mt-1 mx-auto" id="selectfile">
<button id="Up" class="btn btn-primary mt-2">上传</button>

<br><br>
<br><br><br><br>


<div>
    <a onclick="test()" >乐观锁测试</a>
</div>


<p>
    验证码：<img id="code" alt="点击更换" title="点击更换" src="">
</p>

<p>
    邮箱验证码：
    <button id="email" type="button"> 点击发送邮箱验证码</button>
</p>
<p>
    本次邮箱验证码为：<input id="emailCode" readonly type="text"/>
</p>


<p>
    <input type="text" id="userPhone" placeholder="请输入手机号码">
    <button type="button" id="second">点击获取验证码</button>
</p>

<p>
    本次短信验证码为： <input type="text" name="securityCode" placeholder="请输入验证码">
</p>

<p>
    <a href="release\fileDown.do"> 文件流下载app </a>
</p>

<p>
    <a href="release\apk\mobileqq_android.apk"> 直连下载app </a>
</p>


</body>
<script language="javascript">

    function test () {

        $.get("release/test.do", function(data) {
            console.log(data);
        }, "json")

    }



    // 点击获取短信验证码
    $("#second").click(function () {
        sendyzm($("#second"));
    });

    //用ajax提交到后台的发送短信接口
    function sendyzm(obj) {
        var phone = $("#userPhone").val();
        var r = isPhoneNum();
        if (r) {
            $.post("release/phoneCode.do", {phone: phone}, function (data) {
                if (data === "success") {
                    alert("验证码发送成功")
                } else {
                    alert("验证码发送失败！原因：" + data +" 请联系管理员")
                }

            });
            setTime(obj);//开始倒计时
        }
    }

    //60s倒计时实现逻辑
    var countdown = 60;

    function setTime(obj) {
        if (countdown == 0) {
            obj.prop('disabled', false);
            obj.text("点击获取验证码");
            countdown = 60;//60秒过后button上的文字初始化,计时器初始化;
            return;
        } else {
            obj.prop('disabled', true);
            obj.text("(" + countdown + "s)后重新发送");
            countdown--;
        }
        setTimeout(function () {
            setTime(obj)
        }, 1000) //每1000毫秒执行一次
    }

    //校验手机号是否合法
    function isPhoneNum() {
        var phonenum = $("#userPhone").val();
        var reg = /^1[3|4|5|7|8][0-9]{9}$/;
        if (!reg.test(phonenum)) {
            alert('请输入有效的手机号码！');
            return false;
        } else {
            return true;
        }
    }

    //点击后发送邮箱验证码验证码
    $("#email").click(function () {
        $.get("release/emailCode.do");
    });

    //点击后获取邮箱验证码验证码
    $("#emailCode").click(function () {

        $.get("release/getEmailCode.do", function (data) {
            console.log(data)
            if (data === "") {
                $("#emailCode").val("验证码已失效");
            } else {
                $("#emailCode").val(data)
            }
        });

    });

    $(function () {
        changeCode();
        //点击后跟换验证码
        $("#code").click(function () {
            changeCode()
        })

    });

    //获取时间戳
    function genTimestamp() {
        var time = new Date();
        return time.getTime();
    }

    //后台生成的验证码
    function changeCode() {
        $("#code").attr("src", "release/verifyCode.do" + genTimestamp());
    }

    // 定义图片原始大小、压缩后的大小
    var oldfilesize, newfilesize;
    // 当上传按钮内容发送改变后 获取文件并调用压缩图片的方法
    var $file = $("#selectfile");
    $file.on("change", function () {
        GetFile($file.get(0).files);
    });
    //声明一个formdata 用来上传
    var UForm = new FormData();
    $("#Up").on("click", function () {
        DoUp();
    });

    // jQuery拖拽功能
    var $this = $("#drop");
    with ($this) {
        on("dragenter", function () {
            $this.addClass("drag_hover");
        });
        on("dragleave", function () {
            $this.removeClass("drag_hover");
        });
        on("dragover", function (e) {
            e.originalEvent.dataTransfer.dragEffect = 'copy';
            e.preventDefault();
        });
        on("drop", function (e) {
            e.preventDefault();
            var files = e.originalEvent.dataTransfer.files;
            $this.removeClass("drag_hover");
            if (files.length != 0) {
                GetFile(files);
            }
            ;
        });
    }

    // GetFile 处理获取到的file对象，并对它进行压缩处理：
    function GetFile(files) {
        // 用三目运算符频道文件是否存在
        var file = files ? files[0] : false;
        if (!file) {
            return;
        }
        if (file) {
            oldfilesize = Math.floor((file.size / 1024) * 100) / 100 ;
            // 如果图片少于3M 则不进行压缩
            if (oldfilesize < 3000){
                UForm.append("files",file);
                ShowFile(file);
                return;
            }
            lrz(file, {
                width: 1024, //设置压缩后的最大宽
                height: 768,
                quality: 1 //图片压缩质量，取值 0 - 1，默认为0.7
            }).then(function (rst) {
                newfilesize = Math.floor((rst.file.size / 1024) * 100) / 100;
                console.log("图片压缩成功，原为：" + oldfilesize + "KB,压缩后为：" + newfilesize+"KB");
                // 把压缩后的图片文件存入 formData中，这样用ajax传到后台才能接收
                UForm.append("files", rst.file);
                ShowFile(rst.file);
            }).catch(function (err) {
                console.log(err);
                alert("压缩图片时出错,请上传图片文件！");
                return false;
            });
        }
    }

    // ShowFile 把处理后的图片显示出来，实现图片的预览功能：
    function ShowFile(file) {
        // 使用fileReader对文件对象进行操作
        var reader = new FileReader();
        reader.onload = function (e) {
            var img = new Image();
            img.src = e.target.r;
            console.log(img) // <img style="width: 600px; height: 185px;"  src="" alt="点击上传图片">
            $("#drop").html(img);
        location.href
        };
        reader.onerror = function (e, b, c) {
            //error
        };
        // 读取为数据url
        reader.readAsDataURL(file);
    }


    // 使用AJAX上传数据到后台
    function DoUp() {
        $.ajax({
            url: "release/addPic",
            type: "POST",
            data: UForm,
            contentType: false,//禁止修改编码
            processData: false,//不要把data转化为字符
            success: function (data) {
                // 上传成功 返回图片路径
                alert(data)
            },
            error: function (e) {
                alert("上传出错!请检查是否选择了图片");
            }
        });
    }

    // 图片上传
    function setImg(obj) {
        var f = $(obj).val();
        if (f == null || f == undefined || f == '') {
            return false;
        }
        if (!/\.(?:png|jpg|bmp|gif|PNG|JPG|BMP|GIF)$/.test(f)) {
            mui.toast('请上传图片文件！！！')
            $(obj).val('');
            return false;
        }
        var url = "fish/addPic";
        //异步提交表单(先确保jquery.form.js已经引入了)
        var options = {
            url: url,
            success: function (data) {
                picture_path = (data + "").trim();
                var sta = picture_path;
                $("img[class=photourlShow]").attr({src: sta});
                $("input[class=picPath]").attr({value: sta});
                $(obj).val('');
                mui.toast('上传成功~')
            }
        };
        $("#Form").ajaxSubmit(options);
    }


</script>

</html>