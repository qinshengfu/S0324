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
<!-- 公共文件 -->
<%@ include file="../../front/public/unit.jsp" %>
<head>
	<title>反馈历史</title>
</head>

<%-- 此页面暂时用不上！！！！ --%>

<body>
		<header class="mui-bar mui-bar-nav">
			<a class="mui-action-back mui-icon mui-icon-left-nav mui-pull-left"></a>
			<h1 class="mui-title">反馈历史</h1>
		</header>
		<div class="mui-content">
			<div class="coin_table">
				<table border="0" cellspacing="" cellpadding="">
					<tr>
						<th>日期</th>
						<th>反馈类型</th>
						<th>图片</th>
						<th>状态</th>
					</tr>
					<tr>
						<td>2020-03-27 00:00:00</td>
						<td>系统问题</td>
						<td>已上传</td>
						<td>提交成功</td>
					</tr>
					<tr>
						<td>2020-03-27 00:00:00</td>
						<td>系统问题</td>
						<td>已上传</td>
						<td>提交成功</td>
					</tr>
				</table>
			</div>
		</div>
		<script type="text/javascript">
			mui.init()
		</script>
	</body>

</html>
