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

					<form action="message_feedback/${msg }.do" name="Form" id="Form" method="post">
						<input type="hidden" name="MESSAGE_FEEDBACK_ID" id="MESSAGE_FEEDBACK_ID" value="${pd.MESSAGE_FEEDBACK_ID}"/>
						<div id="zhongxin" style="padding-top: 13px;">
						<table id="table_report" class="table table-striped table-bordered table-hover">
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">回复人:</td>
								<td><input type="text" name="REPLIED_NAME" id="REPLIED_NAME" value="${pd.REPLIED_NAME}" maxlength="100" placeholder="这里输入回复人" title="回复人" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">回复内容:</td>
								<td>
									<textarea hidden name="REPLIED_CONTENT" id="REPLIED_CONTENT">${pd.REPLIED_CONTENT}</textarea>
									<!-- 加载编辑器的容器 -->
									<script id="editor" type="text/plain" style="width:98%;height:260px;"></script>
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

<!-- 编辑器配置文件 -->
<script type="text/javascript" charset="utf-8" src="plugins/ueditor/ueditor.config.js"></script>
<!-- 编辑器源码文件 -->
<script type="text/javascript" charset="utf-8" src="plugins/ueditor/ueditor.all.js"></script>

<!-- 实例化编辑器 -->
<script type="text/javascript">
	var ue = UE.getEditor('editor');

	var content = $("#REPLIED_CONTENT").text();
	// 初始化内容
	ue.ready(function () {
		ue.setContent(content);
	})
</script>

		<script type="text/javascript">
		$(top.hangge());
		//保存
		function save(){
			// 获取富文本的内容设置到 多行长文本里面
			var contentHtml = ue.getContent();
			$("#REPLIED_CONTENT").text(contentHtml);


			if($("#REPLIED_NAME").val()==""){
				$("#REPLIED_NAME").tips({
					side:3,
		            msg:'请输入回复人',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#REPLIED_NAME").focus();
			return false;
			}
			if($("#REPLIED_CONTENT").text()==""){
				$("#editor").tips({
					side:3,
		            msg:'请输入回复内容',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#editor").focus();
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