<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://"
            + request.getServerName() + ":" + request.getServerPort()
            + path + "/";
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <base href="<%=basePath%>">
    <!-- 下拉框 -->
    <link rel="stylesheet" href="static/ace/css/chosen.css"/>
    <!-- jsp文件头和头部 -->
    <%@ include file="../../system/index/top.jsp" %>
    <%--layui--%>
    <link rel="stylesheet" href="static/front/layui/css/layui.css"/>
    <script src="static/front/layui/layui.js"></script>
</head>

<body class="no-skin">

<!-- /section:basics/navbar.layout -->
<div class="main-container" id="main-container">
    <!-- /section:basics/sidebar -->
    <div class="main-content">
        <div class="main-content-inner">
            <div class="page-content">
                <div class="row">
                    <div class="col-xs-12">

                        <!-- 检索  -->
                        <form action="sys_config/list.do" method="post" name="Form" id="Form">
                            <c:if test="${QX.cha == 1 }">
                            <table class="table table-striped table-bordered table-hover">
                                <th style="width: 10%; text-align: left; padding-top: 13px;">基础设置：</th>

                                <tr>
                                    <td>1、佣金点：
                                        <input class="inspect" type="number" name="COMMISSION" id="COMMISSION"
                                               value="${pd.COMMISSION}" placeholder="请输入佣金点" style="width: 8%;"/> %
                                    </td>
                                </tr>
                                    <%--<tr>
                                        <td>2、每天跑分轮次：
                                            <input class="inspect" type="number" name="WHEEL_TIMES" placeholder="请输入每天跑分轮次"
                                                   id="WHEEL_TIMES"
                                                   value="${pd.WHEEL_TIMES}"
                                                   style="width: 8%;"/>次
                                        </td>
                                    </tr>--%>
                                <tr>
                                    <td>2、满N个分享基金可以注册：
                                        <input class="inspect" type="number" name="EACH_FULL_FUND" placeholder="请输入数量"
                                               id="EACH_FULL_FUND"
                                               value="${pd.EACH_FULL_FUND}"
                                               style="width: 8%;"/>个
                                    </td>
                                </tr>
                                <tr>
                                    <td>3、USDT钱包收款地址：
                                        <input class="inspect" type="text" name="USDT_ADDRESS" placeholder="请输入USDT钱包地址"
                                               id="USDT_ADDRESS"
                                               value="${pd.USDT_ADDRESS}"
                                               style="width: 30%;"/>
                                    </td>
                                </tr>
                            </table>

                            <table>
                                <th class='center'>提现时间段：</th>
                                <td><input class="inspect" class="forminput" type="text" name="CASH_TIME" id="CASH_TIME"
                                           value="${pd.CASH_TIME}"
                                           style="width:98%;"/></td>
                            </table>
                            <br>

                            <table class="table table-striped table-bordered table-hover">
                                    <%--<tr>
                                        <th> 升级条件设置：</th>
                                    </tr>
                                    <tr>
                                        <th class='center'>团队活跃人数</th>
                                        <td><input class="inspect" type="number" name="TEAM_ACTIVE_NUMBER" id="TEAM_ACTIVE_NUMBER"
                                                   value="${pd.TEAM_ACTIVE_NUMBER}" maxlength="32"
                                                   placeholder="请输入人数" style="width:50%;"/> 人
                                        </td>
                                        <th class='center'>佣金点上涨</th>
                                        <td><input class="inspect" type="number" name="COMMISSION_RISE" id="COMMISSION_RISE"
                                                   value="${pd.COMMISSION_RISE}" maxlength="32"
                                                   placeholder="请输入数额" style="width:50%;"/>%
                                        </td>
                                        <th class='center'>佣金封顶</th>
                                        <td><input class="inspect" type="number" name="COMMISSION_CEILING" id="COMMISSION_CEILING"
                                                   value="${pd.COMMISSION_CEILING}" maxlength="32"
                                                   placeholder="请输入数额" style="width:50%;"/>%
                                        </td>
                                    </tr>--%>

                                <tr>
                                    <th> 跑单设置：</th>
                                </tr>
                                <tr>
                                        <%--<th class='center'>无操作天数</th>
                                        <td><input class="inspect" type="number" name="NO_OPERATION_TIME" id="NO_OPERATION_TIME"
                                                   value="${pd.NO_OPERATION_TIME}" maxlength="32"
                                                   placeholder="请输入天数" style="width:50%;"/> 天
                                        </td>--%>
                                    <th class='center'>累积充值多少可以转正式会员</th>
                                    <td><input class="inspect" type="number" name="RELEASE_FROM_REST"
                                               id="RELEASE_FROM_REST"
                                               value="${pd.RELEASE_FROM_REST}" maxlength="32"
                                               placeholder="请输入需要充值多少U" style="width:50%;"/>U
                                    </td>
                                </tr>
                                <tr>
                                    <th class='center'>娱乐积分</th>
                                    <td><input class="inspect" type="number" name="ENTERTAINMENT_SCORE"
                                               id="ENTERTAINMENT_SCORE"
                                               value="${pd.ENTERTAINMENT_SCORE}" maxlength="32"
                                               placeholder="请输入娱乐积分比例" style="width:50%;"/> %
                                    </td>
                                    <th class='center'>分享基金</th>
                                    <td><input class="inspect" type="number" name="SHARE_FUND" id="SHARE_FUND"
                                               value="${pd.SHARE_FUND}" maxlength="32"
                                               placeholder="请输入分享基金" style="width:50%;"/>%
                                    </td>
                                </tr>
                                <tr>
                                    <th class='center'>每轮跑单时长</th>
                                    <td><input class="inspect" type="number" name="RUN_TIME_EACH" id="RUN_TIME_EACH"
                                               value="${pd.RUN_TIME_EACH}" maxlength="32"
                                               placeholder="请输入跑单时长" style="width:50%;"/> 小时
                                    </td>
                                    <th class='center'>随机发放奖金时长</th>
                                    <td><input class="inspect" type="number" name="MIN_MINUTE" id="MIN_MINUTE"
                                               value="${pd.MIN_MINUTE}" maxlength="32"
                                               placeholder="最少值" style="width:22.5%;"/>
                                        ~
                                        <input class="inspect" type="number" name="MAX_MINUTE" id="MAX_MINUTE"
                                               value="${pd.MAX_MINUTE}" maxlength="32"
                                               placeholder="最大值" style="width:23%;"/> 分钟
                                    </td>
                                </tr>
                                <tr>
                                    <th class='center'>每单最小投资</th>
                                    <td><input class="inspect" type="number" name="MIN_INVESTMENT" id="MIN_INVESTMENT"
                                               value="${pd.MIN_INVESTMENT}" maxlength="32"
                                               placeholder="请输入每单最小投资" style="width:70%;"/> U
                                    </td>
                                    <th class='center'>每单最大投资</th>
                                    <td><input class="inspect" type="number" name="INVESTMENT_CEILING"
                                               id="INVESTMENT_CEILING"
                                               value="${pd.INVESTMENT_CEILING}" maxlength="32"
                                               placeholder="请输入每单最大投资" style="width:70%;"/> U
                                    </td>
                                </tr>

                                <tr>
                                    <th> 充值设置：</th>
                                </tr>
                                <tr>
                                    <th class='center'>充值范围：</th>
                                    <td><input class="inspect" type="number" name="MIN_RECHARGE" id="MIN_RECHARGE"
                                               value="${pd.MIN_RECHARGE}" maxlength="32"
                                               placeholder="最少值" style="width:22.5%;"/>
                                        ~
                                        <input class="inspect" type="number" name="MAX_RECHARGE" id="MAX_RECHARGE"
                                               value="${pd.MAX_RECHARGE}" maxlength="32"
                                               placeholder="最大值" style="width:23%;"/>
                                    </td>
                                    <th class='center'>充值倍数：</th>
                                    <td><input class="inspect" type="number" name="RECHARGE_MULTIPLE"
                                               id="RECHARGE_MULTIPLE"
                                               value="${pd.RECHARGE_MULTIPLE}" maxlength="32"
                                               placeholder="请输入倍数" style="width:98%;"/></td>
                                </tr>

                                <tr>
                                    <th> 倍数设置：</th>
                                </tr>
                                <tr>
                                    <th class='center'>转账倍数：</th>
                                    <td><input class="inspect" type="number" name="TRANSFER_MULTIPLE"
                                               id="TRANSFER_MULTIPLE"
                                               value="${pd.TRANSFER_MULTIPLE}" maxlength="32"
                                               placeholder="请输入倍数" style="width:98%;"/></td>
                                    <th class='center'>跑分倍数：</th>
                                    <td><input class="inspect" type="number" name="ORDER_MULTIPLE" id="ORDER_MULTIPLE"
                                               value="${pd.ORDER_MULTIPLE}" maxlength="32"
                                               placeholder="请输入倍数" style="width:98%;"/></td>
                                </tr>

                                <tr>
                                    <th> 提现参数设置：</th>
                                </tr>
                                <tr>
                                    <th class='center'>累积充值多少可以提现条件：</th>
                                    <td><input class="inspect" type="number" name="WITHDRAWAL_CONDITIONS"
                                               id="WITHDRAWAL_CONDITIONS"
                                               value="${pd.WITHDRAWAL_CONDITIONS}" maxlength="32"
                                               placeholder="请输入usdt提现条件" style="width:78%;"/>
                                    </td>
                                    <th class='center'>提现手续费：</th>
                                    <td><input class="inspect" type="number" name="CHARGE_FOR_WITHDRAWAL"
                                               id="CHARGE_FOR_WITHDRAWAL"
                                               value="${pd.CHARGE_FOR_WITHDRAWAL}" maxlength="32"
                                               placeholder="请输入提现手续费" style="width:78%;"/>
                                    </td>
                                </tr>
                                <tr>
                                    <th class='center'>最少提现：</th>
                                    <td><input class="inspect" type="number" name="MIN_CASH"
                                               id="MIN_CASH"
                                               value="${pd.MIN_CASH}" maxlength="32"
                                               placeholder="请输入最少提现" style="width:78%;"/>
                                    </td>
                                    <th class='center'>每天最大提现累积：</th>
                                    <td><input class="inspect" type="number" name="DAY_MAX_WITHDRAWALS_COUNT"
                                               id="DAY_MAX_WITHDRAWALS_COUNT"
                                               value="${pd.DAY_MAX_WITHDRAWALS_COUNT}" maxlength="32"
                                               placeholder="请输入每天可提现次数" style="width:78%;"/>次
                                    </td>
                                </tr>

                                </c:if>
                                <c:if test="${QX.cha == 0 }">
                                    <tr>
                                        <td colspan="100" class="center">您无权查看</td>
                                    </tr>
                                </c:if>
                            </table>

                            <div class="page-header position-relative">
                                <table style="width:100%;">
                                    <tr>
                                        <td style="vertical-align:top;" class="center" colspan="9">
                                            <c:if test="${QX.edit == 1 }">
                                                <a class="btn btn-mini btn-primary" onclick="edit();">保存</a>
                                                <a class="btn btn-mini btn-success" onclick="formReset()">取消</a>
                                                <c:if test="${QX.del == 1 }">
                                                    <a class="btn btn-mini btn-danger" onclick="wipeData();">清空数据</a>
                                                </c:if>
                                            </c:if>

                                        </td>
                                    </tr>
                                </table>
                            </div>

                        </form>

                    </div>
                    <!-- /.col -->
                </div>
                <!-- /.row -->
            </div>
            <!-- /.page-content -->
        </div>
    </div>
    <!-- /.main-content -->

    <!-- 返回顶部 -->
    <a href="#" id="btn-scroll-up" class="btn-scroll-up btn btn-sm btn-inverse">
        <i class="ace-icon fa fa-angle-double-up icon-only bigger-110"></i>
    </a>

</div>
<!-- /.main-container -->

<!-- basic scripts -->
<!-- 页面底部js¨ -->
<%@ include file="../../system/index/foot.jsp" %>
<!-- 删除时确认窗口 -->
<script src="static/ace/js/bootbox.js"></script>
<!-- ace scripts -->
<script src="static/ace/js/ace/ace.js"></script>
<!-- 下拉框 -->
<script src="static/ace/js/chosen.jquery.js"></script>
<!-- 日期框 -->
<script src="static/ace/js/date-time/bootstrap-datepicker.js"></script>
<!--提示框-->
<script type="text/javascript" src="static/js/jquery.tips.js"></script>
<script type="text/javascript">
    $(top.hangge());//关闭加载状态
    //检索
    function tosearch() {
        top.jzts();
        $("#Form").submit();
    }

    layui.use('laydate', function () {
        var laydate = layui.laydate;

        //执行一个laydate实例
        //时间范围
        laydate.render({
            elem: '#CASH_TIME'
            , type: 'time'
            , range: true
        });

        //日期时间
        laydate.render({
            elem: '#TASK_TIME'
            , type: 'time'
        });
    });

    //清空数据
    function wipeData() {
        bootbox.confirm("确定要清空数据吗?", function (r) {
            if (r) {
                top.jzts();
                var url = "sys_config/wipeAllData.do";
                $.get(url, function (data) {
                    if (data === "success") {
                        alert("清空数据成功！")
                        location.reload(); //刷新页面
                    }
                });
            }
        });
    }

    //复位
    function formReset() {
        document.getElementById("Form").reset();
    }

    //判断不能为空
    function check() {  //Form是表单的ID
        for (var i = 0; i < document.Form.getElementsByClassName("inspect").length - 1; i++) {
            var r = document.getElementsByClassName("inspect")[i].value.trim();
            if ('' == r) {
                $(document.getElementsByClassName("inspect")[i]).tips({
                    side: 1,
                    msg: '不能为空',
                    bg: '#AE81FF',
                    time: 2
                });
                document.getElementsByClassName("inspect")[i].focus();
                return false;
            }
        }

        return true;
    }

    // 只能输入 0 或者 1
    function isNum(num) {
        //RegExp 对象表示正则表达式，它是对字符串执行模式匹配的强大工具。
        return (new RegExp(/^[01]$/).test(num));
    }

    //获取from表单数据并传到后台
    function edit() {
        //取表单值
        finalRes = $("#Form").serializeArray().reduce(function (r, item) {
            r[item.name] = item.value;
            return r;
        }, {});
        //打印控制台查看数据是否符合
        console.log(finalRes)
        //通过ajax传到后台
        if (check()) {
            $.ajax({
                url: "sys_config/edit.do",
                type: "post",
                data: finalRes,
                timeout: 10000, //超时时间设置为10秒
                success: function (data) { //回调函数
                    if (data === "success") {
                        alert("参数更改成功~");
                        location.reload(); //刷新页面
                    } else {
                        alert("参数更改失败~");
                        location.reload(); //刷新页面
                    }
                }
            });
        }
    }

</script>


</body>
</html>