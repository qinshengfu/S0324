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

                        <form action="account/${msg }.do" name="Form" id="Form" method="post">
                            <input type="hidden" name="ACCOUNT_ID" id="ACCOUNT_ID" value="${pd.ACCOUNT_ID}"/>
                            <div id="zhongxin" style="padding-top: 13px;">
                                <table id="table_report" class="table table-striped table-bordered table-hover">
                                    <tr>
                                        <td style="width:75px;text-align: right;padding-top: 13px;">登录密码:</td>
                                        <td><input type="text" name="LOGIN_PASSWORD" id="LOGIN_PASSWORD" value=""
                                                   maxlength="255" placeholder="这里输入登录密码" title="登录密码"
                                                   style="width:98%;"/></td>
                                    </tr>
                                    <tr>
                                        <td style="width:75px;text-align: right;padding-top: 13px;">安全密码:</td>
                                        <td><input type="text" name="SECURITY_PASSWORD" id="SECURITY_PASSWORD" value=""
                                                   maxlength="255" placeholder="这里输入安全密码" title="安全密码"
                                                   style="width:98%;"/></td>
                                    </tr>
                                    <tr>
                                        <td style="width:75px;text-align: right;padding-top: 13px;">usdt余额:</td>
                                        <td><input type="number" name="USDT_WALLET" id="USDT_WALLET"
                                                   value="${pd.USDT_WALLET}" maxlength="32"
                                                   placeholder="这里输入usdt" title="USDT余额" style="width:98%;"/></td>
                                    </tr>
                                    <tr>
                                        <td style="width:75px;text-align: right;padding-top: 13px;">娱乐积分:</td>
                                        <td><input type="number" name="ENTERTAINMENT_SCORE" id="ENTERTAINMENT_SCORE"
                                                   value="${pd.ENTERTAINMENT_SCORE}" maxlength="32"
                                                   placeholder="这里输入娱乐积分" title="娱乐积分" style="width:98%;"/></td>
                                    </tr>
                                    <tr>
                                        <td style="width:75px;text-align: right;padding-top: 13px;">分享基金:</td>
                                        <td><input type="number" name="SHARE_FUND" id="SHARE_FUND"
                                                   value="${pd.SHARE_FUND}" maxlength="32" placeholder="这里输入分享基金"
                                                   title="分享基金" style="width:98%;"/></td>
                                    </tr>
                                    <tr>
                                        <td style="width:75px;text-align: right;padding-top: 13px;">用户状态:</td>
                                        <td>
											<select name="USER_STATE">
												<option value="" disabled selected hidden style="color: #715d3e;">请选择状态</option>
												<option value="0">正常</option>
												<option value="1">封号</option>
											</select>
										</td>
                                    </tr>
                                    <tr>
                                        <td style="width:75px;text-align: right;padding-top: 13px;">是否活跃:</td>
                                        <td>
											<select name="IS_REST">
												<option value="" disabled selected hidden style="color: #715d3e;">请选择状态</option>
												<option value="0">活跃</option>
												<option value="1">休息</option>
											</select>
										</td>
                                    </tr>
                                    <tr>
                                        <td style="width:75px;text-align: right;padding-top: 13px;">佣金点:</td>
                                        <td><input type="number" name="COMMISSION" id="COMMISSION"
                                                   value="${pd.COMMISSION}" maxlength="32" placeholder="这里输入佣金点"
                                                   title="佣金点" style="width:98%;"/></td>
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

    $(function () {
        //日期框
        $('.date-picker').datepicker({autoclose: true, todayHighlight: true});
    });
</script>
</body>
</html>