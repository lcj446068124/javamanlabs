<%--
  Created by IntelliJ IDEA.
  User: root
  Date: 2016/3/7
  Time: 21:13
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>monitor</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="<c:url value='/resources/site/css/site.css'/>" rel="stylesheet">
    <link href="<c:url value='/resources/bootstrap/css/bootstrap.min.css'/>" rel="stylesheet">
    <link href="<c:url value='/resources/bootstrap/css/bootstrap-theme.min.css'/>" rel="stylesheet">

    <script src="<c:url value='/resources/jquery/jquery-2.1.4.min.js'/>"></script>
    <script src="<c:url value='/resources/bootstrap/js/bootstrap.min.js'/> "></script>

</head>
<body>

<nav class="navbar navbar-inverse navbar-fixed-top">
    <div class="container-fluid">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar"
                    aria-expanded="false" aria-controls="navbar">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="#">Project name</a>
        </div>
        <div id="navbar" class="navbar-collapse collapse">
            <ul class="nav navbar-nav navbar-right">
                <li><a href="#">Dashboard</a></li>
                <li><a href="#">Settings</a></li>
                <li><a href="#">Profile</a></li>
                <li><a href="#">Help</a></li>
            </ul>
        </div>
    </div>
</nav>

<div class="container-fluid">
    <div class="row">
        <div class="col-sm-3 col-md-2 sidebar">
            <ul class="nav nav-sidebar">
                <li class="active"><a href="#">Overview <span class="sr-only">(current)</span></a></li>
                <li><a href="#">Reports</a></li>
                <li><a href="#">Analytics</a></li>
                <li><a href="#">Export</a></li>
            </ul>
            <ul class="nav nav-sidebar">
                <li><a href="">Nav item</a></li>
                <li><a href="">Nav item again</a></li>
                <li><a href="">One more nav</a></li>
                <li><a href="">Another nav item</a></li>
                <li><a href="">More navigation</a></li>
            </ul>
            <ul class="nav nav-sidebar">
                <li><a href="">Nav item again</a></li>
                <li><a href="">One more nav</a></li>
                <li><a href="">Another nav item</a></li>
            </ul>
        </div>

        <div class="col-sm-9 col-md-10  main">
            <h1 class="page-header">Dashboard</h1>
            <div class="row placeholders">
                <!-- dashboard -->
            </div>

            <h2 class="sub-header">Section title</h2>
            <div class="table-responsive">
                <table class="table table-striped">
                    <thead>
                    <tr>
                        <th>任务名称</th>
                        <th>任务状态</th>
                        <th>是否锁定</th>
                        <th>任务控制</th>
                        <th>开始时间</th>
                        <th>结束时间</th>
                        <th>作业耗时</th>
                        <th>操作</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${requestScope.list}" var="entity">
                        <tr>
                            <td>${entity.jobName}</td>
                            <td class="status_td">
                                <c:choose>
                                    <c:when test="${entity.jobStatus == 'WAIT'}">
                                        等待
                                    </c:when>
                                    <c:when test="${entity.jobStatus == 'COMPLETED'}">
                                        完成
                                    </c:when>
                                    <c:when test="${entity.jobStatus == 'RUNNING'}">
                                        运行
                                    </c:when>
                                    <c:otherwise>
                                        异常
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td class="lock_td">
                                <c:choose>
                                    <c:when test="${entity.jobLock == 1}">
                                        释放
                                    </c:when>
                                    <c:otherwise>
                                        锁定
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td class="hangup_td">
                                <c:choose>
                                    <c:when test="${entity.runFlag == 0}">
                                        正常
                                    </c:when>
                                    <c:otherwise>
                                        挂起
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${entity.lastStartTime}"/>
                            </td>
                            <td>
                                <fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${entity.lastEndTime}"/>
                            </td>
                            <td>
                                    ${entity.lastEndTime.getTime() - entity.lastStartTime.getTime()}
                            </td>
                            <td>
                                <div class="btn-group btn-group-xs" role="group" aria-label="...">
                                    <button type="button" class="btn btn-primary release" data-id="${entity.jobName}">
                                        解锁
                                    </button>
                                    <button type="button" class="btn btn-default hangup" data-flag="${entity.runFlag}"
                                            data-id="${entity.jobName}">
                                        <c:choose>
                                            <c:when test="${entity.runFlag == 0}">
                                                挂起
                                            </c:when>
                                            <c:otherwise>
                                                恢复
                                            </c:otherwise>
                                        </c:choose>
                                    </button>
                                </div>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>

<script>
    $(function () {
        function getBtOpCeil(bt, selector) {
            var td = $(bt).parents('td').get(0);
            return $(td).siblings('td[class="' + selector + '"]');
        }

        $('.btn.release').click(function () {
            var id = $(this).attr('data-id');
            var p = $(this).parents('td').get(0);
            var lockCeil = getBtOpCeil(this, 'lock_td');
            var statusCeil = getBtOpCeil(this, 'status_td');
            $.ajax({
                type: 'post',
                url: '<c:url value="/release"/>',
                data: {'id': id},
                success: function (data) {
                    lockCeil.html('等待');
                    statusCeil.html('释放')
                },
                error: function (e) {
                    console.log(e);
                }
            });
        });

        $('.btn.hangup').click(function () {
            var flag = $(this).attr('data-flag');
            flag = parseInt(flag);
            var id = $(this).attr('data-id');
            if (!flag) {
                $(this).attr('data-flag', 1).html('恢复');
            } else {
                $(this).attr('data-flag', 0).html('挂起');
            }
            var hangupCeil = getBtOpCeil(this, 'hangup_td');
            $.ajax({
                type: 'post',
                url: '<c:url value="/hangup"/>',
                data: {'id': id, 'flag': !flag ? 1 : 0},
                success: function (data) {
                    console.log(data);
                    hangupCeil.html(flag ? '正常' : '挂起')
                },
                error: function (e) {
                    console.log(e);
                }
            });
        });
    })
</script>
</body>
</html>
