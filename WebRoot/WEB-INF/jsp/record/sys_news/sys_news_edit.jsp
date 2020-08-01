<%@ page language="java" contentType="text/html; charset=UTF-8"
		 pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html>
<html lang="en">
<head>
	<base href="<%=basePath%>">

	<!-- jsp文件头和头部 -->
	<%@ include file="../../system/index/top.jsp" %>
	<style type="text/css">
		#dialog-add, #dialog-message, #dialog-comment {
			width: 100%;
			height: 100%;
			position: fixed;
			top: 0px;
			z-index: 99999999;
			display: none;
		}

		.commitopacity {
			position: absolute;
			width: 100%;
			height: 700px;
			background: #7f7f7f;
			filter: alpha(opacity=50);
			-moz-opacity: 0.5;
			-khtml-opacity: 0.5;
			opacity: 0.5;
			top: 0px;
			z-index: 99999;
		}

		.commitbox {
			width: 100%;
			margin: 0px auto;
			position: absolute;
			top: 0px;
			z-index: 99999;
		}

		.commitbox_inner {
			width: 96%;
			height: 255px;
			margin: 6px auto;
			background: #efefef;
			border-radius: 5px;
		}

		.commitbox_top {
			width: 100%;
			height: 253px;
			margin-bottom: 10px;
			padding-top: 10px;
			background: #FFF;
			border-radius: 5px;
			box-shadow: 1px 1px 3px #e8e8e8;
		}

		.commitbox_top textarea {
			width: 95%;
			height: 195px;
			display: block;
			margin: 0px auto;
			border: 0px;
		}

		.commitbox_cen {
			width: 95%;
			height: 40px;
			padding-top: 10px;
		}

		.commitbox_cen div.left {
			float: left;
			background-size: 15px;
			background-position: 0px 3px;
			padding-left: 18px;
			color: #f77500;
			font-size: 16px;
			line-height: 27px;
		}

		.commitbox_cen div.left img {
			width: 30px;
		}

		.commitbox_cen div.right {
			float: right;
			margin-top: 7px;
		}

		.commitbox_cen div.right span {
			cursor: pointer;
		}

		.commitbox_cen div.right span.save {
			border: solid 1px #c7c7c7;
			background: #6FB3E0;
			border-radius: 3px;
			color: #FFF;
			padding: 5px 10px;
		}

		.commitbox_cen div.right span.quxiao {
			border: solid 1px #f77400;
			background: #f77400;
			border-radius: 3px;
			color: #FFF;
			padding: 4px 9px;
		}
	</style>
</head>
<body class="no-skin">
<!-- /section:basics/navbar.layout -->
<div class="main-container" id="main-container">
	<!-- /section:basics/sidebar -->
	<div class="main-content">

		<div id="zhongxin">
			<textarea name="CONTENT" id="CONTENT" style="display: none">${pd.CONTENT}</textarea>

			<table style="width: 98%; margin-top: 10px; margin-left: 9px;">
				<tr>
					<td style="margin-top: 0px;">
						<div style="float: left;" style="width:81%">
							<label>标题：</label>
							<input value="${pd.TITLE}" placeholder="请输入标题" name="TITLE" id="TITLE"
								   style="width: 500px; height: 32px;"/>
						</div>

					</td>
				</tr>

				<tr>
					<td style="margin-top: 0px;">
						<div style="float: left; heght: 130px; border: 5px solid white" rows="1" cols="50"></div>
					</td>
				</tr>
				<tr>
					<td style="margin-top: 0px;">
						<div style="float: left; heght: 130px; border: 5px solid white" rows="1" cols="50"></div>
					</td>
				</tr>

				<tr>
					<td style="padding-top: 3px;">
						<script id="editor" type="text/plain" style="width: 643px; height: 259px;"></script>
					</td>
				</tr>
				<tr>
					<td style="text-align: center; padding-top: 15px;" id="nr"><a
							class="btn btn-mini btn-primary" onclick="sendFhsm();">保存</a> <a
							class="btn btn-mini btn-danger" onclick="top.Dialog.close();">取消</a>
					</td>
				</tr>
			</table>
		</div>

	</div>
</div>

<%@ include file="../../system/index/foot.jsp" %>
<!-- ace scripts -->
<script src="static/ace/js/ace/ace.js"></script>
<!-- 编辑框-->
<script type="text/javascript" charset="utf-8">window.UEDITOR_HOME_URL = "<%=path%>/plugins/ueditor/";
</script>
<script type="text/javascript" charset="utf-8" src="plugins/ueditor/ueditor.config.js"></script>
<script type="text/javascript" charset="utf-8" src="plugins/ueditor/ueditor.all.js"></script>
<!-- 编辑框-->
<!--引入属于此页面的js -->
<script type="text/javascript" src="static/js/myjs/fhsms.js"></script>
<!--提示框-->
<script type="text/javascript" src="static/js/jquery.tips.js"></script>
<script type="text/javascript">
	var ID = "";
	window.onload = function () {

		ID = "${pd.SYS_NEWS_ID}";

		if (ID) {
			var CONTENT = $("#CONTENT").text();
			var editor = UE.getEditor("editor");
			editor.ready(function () {
				editor.setContent(CONTENT);
			})
		}

	}
</script>
<script>
	function sendFhsm() {
		var aa = document.getElementById("ueditor_0").contentWindow.document.getElementsByTagName("p");
		var CONTENT = aa[0].parentNode.innerHTML;//.replace("<br>", "");<p>
		//var CONTENT = document.getElementById("editor").parentNode.innerHTML;

		if ($("#TITLE").val() == "") {
			$("#TITLE").tips({
				side: 3,
				msg: '请输入标题',
				bg: '#AE81FF',
				time: 2
			});
			$("#TITLE").focus();
			return false;
		}
		if (CONTENT == "") {
			$(aa[0]).tips({
				side: 1,
				msg: '请输入内容',
				bg: '#AE81FF',
				time: 3
			});
			return false;
		}
		$("#zhongxin").hide();
		var TITLE = $("#TITLE").val();

		console.log(CONTENT)
		if (!ID) {
			$.ajax({
				type: "post",
				url: 'sys_news/save.do',
				data: {
					SYS_NEWS_ID: ID,
					CONTENT: CONTENT,
					TITLE: TITLE
				},
				dataType: 'json',
				async: true,
				success: function (data) {

				}
			});
		} else {
			$.ajax({
				type: "post",
				url: 'sys_news/edit.do?ID=' + ID,
				data: {
					SYS_NEWS_ID: ID,
					CONTENT: CONTENT,
					TITLE: TITLE
				},
				dataType: 'json',
				async: true,
				success: function (data) {

				}
			});
		}
		setTimeout("close()", 100);

	}
</script>
</body>
</html>
