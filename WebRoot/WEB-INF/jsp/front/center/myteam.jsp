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
	<title>我的团队</title>
</head>

<style type="text/css">
		.coin_table table th,.coin_table td{
			width: 33.33%;
		}
	</style>
	<body>
		<header class="mui-bar mui-bar-nav">
			<a class="mui-action-back mui-icon mui-icon-left-nav mui-pull-left"></a>
			<h1 class="mui-title"><spring:message code='team.home' text='我的团队'/> </h1>
		</header>
		<div class="mui-content">
			<!--  -->
			<div class="setup_inp">
				<div class="mui-input-row">
					<label><spring:message code='team.push' text='直推'/>：</label>
					<input type="text" value="${user.RECOMMENDED_NUMBER}" disabled="disabled">
				</div>
				<div class="mui-input-row">
					<label><spring:message code='team.active' text='活跃'/>：</label>
					<input type="text" value="${pd.activeNum}" disabled="disabled">
				</div>
				<div class="mui-input-row">
					<label>休息：</label>
					<input type="text" value="${pd.notActiveNum}" disabled="disabled">
				</div>
				<div class="mui-input-row">
					<label><spring:message code='team.teamSize' text='团队人数'/>：</label>
					<input type="text" value="${user.TEAM_NUMBER}" disabled="disabled">
				</div>
				<div class="mui-input-row">
					<label>团队业绩余额：</label>
					<input type="text" value="${pd.teamPerformance}" disabled="disabled">
				</div>

			</div>
			<!--  -->
			<div class="coin_table">
				<table border="0" cellspacing="" cellpadding="">
					<tr>
						<th><spring:message code='team.userName' text='用户名'/></th>
						<th><spring:message code='team.rank' text='级别'/></th>
						<th><spring:message code='team.status' text='会员状态'/></th>
					</tr>
					<c:forEach items="${teamList}" var="var">
						<tr>
							<td>${var.USER_NAME}</td>
							<td>V${var.USER_RANK}</td>
							<c:if test="${var.IS_REST == 0}" >
							<td><spring:message code='team.active' text='活跃'/></td>
							</c:if>
							<c:if test="${var.IS_REST == 1}" >
							<td><spring:message code='team.inaction' text='不活跃'/></td>
							</c:if>
						</tr>
					</c:forEach>
				</table>
			</div>
		</div>
	</body>

</html>
