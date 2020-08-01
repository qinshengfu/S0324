<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <base href="<%=basePath%>">
    <!-- 下拉框 -->
    <link rel="stylesheet" href="static/ace/css/chosen.css"/>
    <!-- jsp文件头和头部 -->
    <%@ include file="../../system/index/top.jsp" %>
    <!-- 日期框 -->
    <link rel="stylesheet" href="static/ace/css/datepicker.css"/>
    <style type="text/css">
        .yulantu {
            z-index: 9999999999999999;
            position: absolute;
            border: 3px solid #438EB9;
            display: none;
            width: 68%;
        }
    </style>
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

                        <form action="recharge_record/${msg }.do" name="Form" id="Form" method="post">
                            <input type="hidden" name="RECHARGE_RECORD_ID" value="${pd.RECHARGE_RECORD_ID}"/>
                            <input type="hidden" name="USER_ID" value="${pd.USER_ID}"/>
                            <div id="zhongxin" style="padding-top: 13px;">
                                <table id="table_report" class="table table-striped table-bordered table-hover">
                                    <tr>
                                        <td style="width:75px;text-align: right;padding-top: 13px;">用户名:</td>
                                        <td><input type="text" name="USER_NAME" readonly value="${pd.USER_NAME}"
                                                   style="width:98%;"/>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td style="width:75px;text-align: right;padding-top: 13px;">充值金额:</td>
                                        <td><input type="number" name="MONEY" readonly value="${pd.MONEY}"
                                                   style="width:98%;"/></td>
                                    </tr>
                                    <tr>
                                        <td style="width:75px;text-align: right;padding-top: 13px;">支付凭证:</td>
                                        <td class='center'>
                                            <img style="margin-top: -3px;" alt="图片"
                                                 src="static/images/extension/tupian.png">
                                            <c:if test="${pd.WALLET_ADDRESS == '系统赠送'}">
                                                后台充值
                                            </c:if>
                                            <a style="cursor:pointer;"
                                               onmouseover="showTU('${pd.WALLET_ADDRESS}','yulantu${vs.index+1}');"
                                               onmouseout="hideTU('yulantu${vs.index+1}');">[预览]</a>
                                            <div class="yulantu" id="yulantu${vs.index+1}"></div>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td style="width:75px;text-align: right;padding-top: 13px;">是否通过:</td>
                                        <td>
                                            <select name="is_adopt">
                                                <option value="" disabled selected hidden style="color: #715d3e;">
                                                    请选择状态
                                                </option>
                                                <option value="0">通过</option>
                                                <option value="1">驳回</option>
                                            </select>
                                        </td>
                                    </tr>


                                    <tr>
                                        <td style="text-align: center;" colspan="10">
                                            <a class="btn btn-mini btn-primary" onclick="save();">保存</a>
                                            <a class="btn btn-mini btn-danger" onclick="top.Dialog.close();">取消</a>
                                        </td>
                                    </tr>
                                </table>
                            </div>
                            <div id="zhongxin2" class="center" style="display:none"><br/><br/><br/><br/><br/><img
                                    src="static/images/jiazai.gif"/><br/><h4 class="lighter block green">提交中...</h4>
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
</div>
<!-- /.main-container -->


<!-- 页面底部js¨ -->
<%@ include file="../../system/index/foot.jsp" %>
<!-- 下拉框 -->
<script src="static/ace/js/chosen.jquery.js"></script>
<!-- 日期框 -->
<script src="static/ace/js/date-time/bootstrap-datepicker.js"></script>
<!--提示框-->
<script type="text/javascript" src="static/js/jquery.tips.js"></script>
<script type="text/javascript">
    $(top.hangge());

    //保存
    function save() {

        $("#Form").submit();
        $("#zhongxin").hide();
        $("#zhongxin2").show();
    }

    //显示图片
    function showTU(path, TPID) {
        $("#" + TPID).html('<img width="100%" src="' + path + '">');
        $("#" + TPID).show();
    }

    //隐藏图片
    function hideTU(TPID) {
        $("#" + TPID).hide();
    }

    $(function () {
        //日期框
        $('.date-picker').datepicker({autoclose: true, todayHighlight: true});
    });
</script>
</body>
</html>