$(function () {

    initWebSocket();

    // logo图
    $('.logo-img').attr({src: "static/front/images/logo.png"});

    var clipboard = new ClipboardJS('.copy');
    //优雅降级:safari 版本号>=10,提示复制成功;否则提示需在文字选中后，手动选择“拷贝”进行复制
    clipboard.on('success', function (e) {
        mui.toast("已成功复制");
    });
    clipboard.on('error', function (e) {
        mui.toast('请选择“拷贝”进行复制！');
    });

});

// websocket对象
var websocket;
// 在线管理服务器IP和端口
var oladress = "";
// 当前登录用户名
var userName = "";

/*
时间倒计时插件
id：绑定页面元素
endDateStr：结束时间
n: 几小时后
*/
function TimeDown(id, endDateStr, n) {
    //结束时间
    var endDate = new Date(endDateStr);
    endDate = endDate.setHours(endDate.getHours() + n);
    //当前时间
    var nowDate = new Date();
    //相差的总秒数
    var totalSeconds = parseInt((endDate - nowDate) / 1000);
    //天数
    var days = Math.floor(totalSeconds / (60 * 60 * 24));
    //取模（余数）
    var modulo = totalSeconds % (60 * 60 * 24);
    //小时数
    var hours = Math.floor(modulo / (60 * 60));
    modulo = modulo % (60 * 60);
    //分钟
    var minutes = Math.floor(modulo / 60);
    //秒
    var seconds = modulo % 60;
    //输出到页面
    if (endDate <= nowDate) {
        document.getElementById(id).innerHTML = content(0, 0, 0);
        return;
    } else {
        document.getElementById(id).innerHTML = content(hours, minutes, seconds);
    }
    /*
        if (endDate <= nowDate) {
            document.getElementById(id).innerHTML = "时间到";
            return;
        } else {
            document.getElementById(id).innerHTML = days + "天" + hours + "小时" + minutes + "分钟" + seconds + "秒";
        }
    */
    //延迟一秒执行自己
    setTimeout(function () {
        TimeDown(id, endDateStr, n);
    }, 1000)
}

// 倒计时的内容
function content(hours, minutes, seconds) {
    return "" +
        "                    <span class=\"mui-badge mui-badge-warning\">" + hours + "</span>\n" +
        "                    :\n" +
        "                    <span class=\"mui-badge mui-badge-warning\">" + minutes + "</span>\n" +
        "                    :\n" +
        "                    <span class=\"mui-badge mui-badge-warning\">" + seconds + "</span>\n";
}

/*
时间进度条
返回当前进度百分比
 */
function timeProgressBar(startTime, endTime) {
    // 转时间戳 Math.round(new Date() / 1000)

    // 开始时间
    var startDate = Math.round(new Date(startTime) / 1000);
    //结束时间
    var endDate = Math.round(new Date(endTime) / 1000);
    //当前时间
    var nowDate = Math.round(new Date() / 1000);
    /*
        console.log("开始时间：" + startDate);
        console.log("结束时间：" + endDate);
        console.log("当前时间：" + nowDate);
        var seconds1 = nowDate - startDate;
        var seconds2 = endDate - startDate;
        console.log("当前时间-开始时间：" + seconds1);
        console.log("结束时间-开始时间：" + seconds2);
        var division = seconds1 / seconds2;
        console.log("相除：" + division);
        console.log("乘100：" + division * 100);
    */

    // 开始计算进度 (当前时间-开始时间)/ (结束时间-开始时间) *100%
    return ((nowDate - startDate) / (endDate - startDate) * 100).toFixed(2)
}


// 初始化信息
function initWebSocket() {
    $.ajax({
        type: "POST",
        url: 'front/getList.do?tm=' + new Date().getTime(),
        data: encodeURI(""),
        dataType: 'json',
        //beforeSend: validateData,
        cache: false,
        success: function (data) {
            // 设置当登录用户姓名
            userName = "FT-" + data.user.USER_NAME;
            // 在线管理和站内信服务器IP和端口
            oladress = data.oladress;
            // 连接在线
            online();
        }
    });
}

//加入在线列表
function online() {
    if (window.WebSocket) {
        websocket = new WebSocket(encodeURI('ws://' + oladress));
        websocket.onopen = function () {
            //连接成功
            websocket.send('[join]' + userName);
        };
        websocket.onerror = function () {
            //连接失败
        };
        websocket.onclose = function () {
            //连接断开
        };
        //消息接收
        websocket.onmessage = function (messageEvent) {
            var message = JSON.parse(messageEvent.data);
            if (message.type == 'goOut') {
                $("body").html("");
                setTimeout(function () {
                    goOutLogin("1");
                }, 200)
            }
            if (message.type == 'thegoout') {
                $("body").html("");
                setTimeout(function () {
                    goOutLogin("2");
                }, 200)
            }
        };
    }
}

//下线
function goOutLogin() {
    alert("您被系统管理员强制下线或您的帐号在别处登录");
    setTimeout(function () {
        window.location.href = "release/toLogin.do";
    }, 200)
}


//因js没有长按事件，需要自行封装，函数名longPress，参数为：需长按元素的id、长按之后的逻辑函数func
function longPress(id, func) {
    var timeOutEvent;

    document.querySelector("#" + id).addEventListener("touchstart", function (e) {
        //开启定时器前先清除定时器，防止重复触发
        clearTimeout(timeOutEvent);
        //开启延时定时器
        timeOutEvent = setTimeout(function () {
            //调用长按之后的逻辑函数func
            mui.toast("调用保存图片方法")
            func();
        }, 200); //长按时间为200ms，可以自己设置
    });

    document.querySelector("#" + id).addEventListener("touchmove", function (e) {
        //长按过程中，手指是不能移动的，若移动则清除定时器，中断长按逻辑
        clearTimeout(timeOutEvent);
        /* e.preventDefault() --> 若阻止默认事件，则在长按元素上滑动时，页面是不滚动的，按需求设置吧 */
    });

    document.querySelector("#" + id).addEventListener("touchend", function (e) {
        //若手指离开屏幕，时间小于我们设置的长按时间，则为点击事件，清除定时器，结束长按逻辑
        clearTimeout(timeOutEvent);
    })
}

//保存图片
function saveSharePic() {
    // 想要保存的图片节点
    const dom = document.getElementById("picShare");
    // 创建一个新的canvas
    const canvas = document.createElement("canvas");
    const width = document.body.offsetWidth; // 可见屏幕的宽
    const height = document.body.offsetHeight; // 可见屏幕的高
    console.log("可见屏幕宽高：" + document.body.offsetWidth + "、" + document.body.offsetHeight);
    const scale = window.devicePixelRatio; // 设备的devicePixelRatio
    // 将Canvas画布放大scale倍，然后放在小的屏幕里，解决模糊问题
    canvas.width = width * scale;
    canvas.height = height * scale;
    console.log("canvas宽高：" + canvas.width + "、" + canvas.height);
    canvas.getContext('2d').scale(scale, scale);
    // dom节点绘制成canvas
    html2canvas(dom).then(function (canvas) {
        const img = canvas2Image(canvas, canvas.width, canvas.height);
        img.style.cssText = "width:100%;position:absolute;top:0;left:0;opacity:0;z-index:999;";
        console.log("图片宽高：" + img.width + "、" + img.height);
        document.body.appendChild(img);

    });
}

//利用canvas获取图片的base64编码创建图片对象
function canvas2Image(canvas, width, height) {
    const retCanvas = document.createElement("canvas");
    const retCtx = retCanvas.getContext("2d");
    retCanvas.width = width;
    retCanvas.height = height;
    retCtx.drawImage(canvas, 0, 0, width, height, 0, 0, width, height);
    const img = document.createElement("img");
    img.src = retCanvas.toDataURL("image/png", 1); // 可以根据需要更改格式
    return img;
}

// 下载图片 canvas：传入canvas的dom对象 name：保存的图片的名字
function downLoadCanvasImage(canvas,name) {
    var a = document.createElement("a");
    a.href = canvas.toDataURL();
    a.download = name;
    a.click();
}

// 将图片保存的方法 img：图片的dom对象 name：保存为图片时的名字
function downLoadImage(img,name) {
    var a = document.createElement("a");
    a.href = img.src;
    a.download = name;
    a.click();
}
