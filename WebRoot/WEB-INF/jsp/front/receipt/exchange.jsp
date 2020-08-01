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
	<title>额度兑换</title>
</head>
<%-- 此页面暂时用不上 --%>
	<body>
		<header class="mui-bar mui-bar-nav">
			<a class="mui-action-back mui-icon mui-icon-left-nav mui-pull-left"></a>
			<h1 class="mui-title">额度兑换</h1>
		</header>
		<div class="mui-content">
			<!-- -->
			<div class="exch_top">
				<div class="exch_top_div">
					<div>USDT余额</div>
					<div>
						<p>兑换</p>
						<i class="iconfont icon-youjiantou1"></i>
					</div>
					<div>USD额度</div>
				</div>
				<div class="mui-input-row">
					<input type="number" placeholder="请输入兑换数量">
					<button type="button" class="mui-btn mui-btn-blue mui-btn-block">全部</button>
				</div>
				<p class="p_color">USDT余额<span>0.00</span>USDT</p>
				<button type="button" class="mui-btn mui-btn-blue mui-btn-block mui_btn">确定</button>
			</div>
			<div class="exch_p">
				<p class="p_color">提示：最低兑换数量为<span>300.00</span>USDT,最高数量为<span>10000.00</span>USDT</p>
			</div>
			
		</div>
		<script type="text/javascript">
			mui.init()
		</script>
	</body>

</html>
