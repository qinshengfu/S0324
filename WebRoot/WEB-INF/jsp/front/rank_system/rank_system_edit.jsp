<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html lang="en">
	<head>
	<base href="<%=basePath%>">
	<!-- 下拉框 -->
	<link rel="stylesheet" href="static/ace/css/chosen.css" />
	<!-- jsp文件头和头部 -->
	<%@ include file="../../system/index/top.jsp"%>
	<!-- 日期框 -->
	<link rel="stylesheet" href="static/ace/css/datepicker.css" />
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
					
					<form action="rank_system/${msg }.do" name="Form" id="Form" method="post">
						<input type="hidden" name="RANK_SYSTEM_ID" id="RANK_SYSTEM_ID" value="${pd.RANK_SYSTEM_ID}"/>
						<div id="zhongxin" style="padding-top: 13px;">
						<table id="table_report" class="table table-striped table-bordered table-hover">
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">条件:</td>
								<td>
									<select name="ASK" id="ASK">
										<c:if test="${pd.ASK == 1}">
											<option hidden value="1">直推</option>
										</c:if>
										<c:if test="${pd.ASK == 0}">
											<option hidden value="0">团队</option>
										</c:if>
										<option value="1">直推</option>
										<option value="0">团队</option>
									</select>
								</td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">人数:</td>
								<td><input type="number" name="NUMBER_PEOPLE" id="NUMBER_PEOPLE" value="${pd.NUMBER_PEOPLE}" maxlength="32" placeholder="这里输入人数" title="人数" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">佣金点:</td>
								<td><input type="number" name="COMMISSION" id="COMMISSION" value="${pd.COMMISSION}" maxlength="32" placeholder="这里输入佣金点" title="佣金点" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">等级:</td>
								<td><input type="text" name="RANK" id="RANK" value="${pd.RANK}" maxlength="40" placeholder="这里输入等级" title="等级" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="text-align: center;" colspan="10">
									<a class="btn btn-mini btn-primary" onclick="save();">保存</a>
									<a class="btn btn-mini btn-danger" onclick="top.Dialog.close();">取消</a>
								</td>
							</tr>
						</table>
						</div>
						<div id="zhongxin2" class="center" style="display:none"><br/><br/><br/><br/><br/><img src="static/images/jiazai.gif" /><br/><h4 class="lighter block green">提交中...</h4></div>
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
	<%@ include file="../../system/index/foot.jsp"%>
	<!-- 下拉框 -->
	<script src="static/ace/js/chosen.jquery.js"></script>
	<!-- 日期框 -->
	<script src="static/ace/js/date-time/bootstrap-datepicker.js"></script>
	<!--提示框-->
	<script type="text/javascript" src="static/js/jquery.tips.js"></script>
		<script type="text/javascript">
		$(top.hangge());
		//保存
		function save(){
			if($("#ASK").val()==""){
				$("#ASK").tips({
					side:3,
		            msg:'请选择条件',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#ASK").focus();
			return false;
			}
			if($("#NUMBER_PEOPLE").val()==""){
				$("#NUMBER_PEOPLE").tips({
					side:3,
		            msg:'请输入人数',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#NUMBER_PEOPLE").focus();
			return false;
			}
			if($("#COMMISSION").val()==""){
				$("#COMMISSION").tips({
					side:3,
		            msg:'请输入佣金点',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#COMMISSION").focus();
			return false;
			}
			if($("#RANK").val()==""){
				$("#RANK").tips({
					side:3,
		            msg:'请输入等级',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#RANK").focus();
			return false;
			}
			$("#Form").submit();
			$("#zhongxin").hide();
			$("#zhongxin2").show();
		}
		
		$(function() {
			//日期框
			$('.date-picker').datepicker({autoclose: true,todayHighlight: true});
		});
		</script>
</body>
</html>