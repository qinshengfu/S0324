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
    <!-- 日期框 -->
    <link rel="stylesheet" href="static/ace/css/datepicker.css"/>
    <style>

        .content p {
            margin: 0;
        }

        .orgchart .node .content {
            height: 100% !important;
        }

        .btnRecenter {
            margin: 10px;
            padding: 8px;
        }

        .search {
            margin: 20px;
            height: 80px;
        }
    </style>
</head>
<body>

<%--树形图--%>
<div>
    <div class="search">
        <button class="btnRecenter">重置图表</button>
        <label>
            会员账号:<input type="text" id="userName" name="userName" placeholder="请输入用户名"/>
        </label>
        <button onclick="findByUserName()">查询</button>
        <button onclick="topUser()">顶层</button>
    </div>
    <div id="chart-container"></div>
</div>

<!-- basic scripts -->
<!-- 页面底部js¨ -->
<%@ include file="../../system/index/foot.jsp" %>
<%--组织架构图--%>
<link rel="stylesheet" href="static/front/css/jquery.orgchart.css"/>
<script src="static/front/js/jquery.orgchart.js" type="text/javascript" charset="utf-8"></script>

<!-- ace scripts -->
<script src="static/ace/js/ace/ace.js"></script>
<script type="text/javascript">
    $(top.hangge());//关闭加载状态

    var nodes = [];

    // 根据用户名顶置用户
    function findByUserName() {

        var userName = $('#userName').val();
        if (!userName) {
            return;
        }
        var url = 'account/getUserList.do?USER_NAME=' + userName;
        $.get(url, function (result) {
            if (result.success) {
                childNodes(result.data.item);
            }
        });
    }

    // 顶点用户作为根节点
    function topUser() {
        $.get('account/getUserList.do', function (result) {
            if (result.success) {
                childNodes(result.data.item);
            }
        });
    }

    $(function () {
        topUser();

        $('.btnRecenter').click(function () {
            var chart = $('.orgchart');
            var posX = chart.width() > chart.parent('div').width() ? -(chart.width() / 2.5) : '0';
            console.log(posX)
            chart.css('transform', 'matrix(1, 0, 0, 1, ' + posX + ', 0)');
        });
    });

    // 渲染子节点
    function childNodes(data) {
        $('#chart-container').html('');
        nodes = data;

        // 循环计算子节点数
        for (var i = 0; i < data.length; i++) {
            var children = childCount(data[i].ACCOUNT_ID);
            if (children.length > 0) {
                nodes[i].children = children;
            }
        }
        var datascource = nodes[0];

        // 递归计算下级节点总数
        function childCount(id) {
            var array = [];
            for (var i = 0; i < data.length; i++) {
                if (data[i].RECOMMENDER == id) {
                    array.push(data[i]);
                }
            }
            return array;
        }

        $('#chart-container').orgchart({
            'data': datascource,
            'visibleLevel': 3,
            'pan': true,
            'zoom': true,
            'toggleSiblingsResp': true,
            'nodeTitle': 'USER_NAME',
            'createNode': function ($node, data) {
                var secondMenu = '';
                secondMenu += '<div class="content"> ' +
                    '<p> 等级：' + data.USER_RANK + '</p> ' +
                    '<p> 佣金点：' + data.COMMISSION + '</p> ' +
                    '<p> 团队人数：' + data.TEAM_NUMBER + '</p> ' +
                    ' </div>'
                $node.append(secondMenu);
            }
        });

    }

</script>

</body>
</html>