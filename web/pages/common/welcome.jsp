<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <%@ include file="../common/lib2.jsp" %>

    <title>系统首页</title>

    <style type="text/css">

        .header {
            height: 59px;
            border-bottom: 1px solid #404553;
            background-color: #23262E;

        }

        .layui-header {
            position: relative;
            z-index: 2;
            height: 60px;
        }

        .layui-nav a {
            text-decoration: none;
        }

        .nav-clz {
            top: 60px;
            width: 200px;
        }

        .header-info {
            height: 60px;
            position: absolute;
            top: 0;
            right: 30px;
            z-index: 201;
        }

        .header-info span {
            font-size: 14px;
            line-height: 60px;
            color: #c2c2c2;
        }

        .main-banner {
            height: 1px;
            background: rgba(0, 0, 0, 0.4);
        }

        .sign-out {
            margin-left: 10px;
        }

        .sign-out:hover {
            color: #5FB878;
            cursor: pointer;
        }

        section {
            padding-left: 200px;
        }

        .search-bar {
            width: 100%;
            height: 43px;
            background-color: #f8f8f8;
            box-shadow: 0 0 1px;
        }

        .chart-area {
            width: 100%;
            background-color: #CCCCCC;
        }
    </style>
</head>

<body>

<header class="header layui-header">

    <div class="nav-clz">
        <ul class="layui-nav layui-nav-tree layui-nav-side" style=" border-radius: 0; margin-top: 60px;">
            <li class="layui-nav-item layui-nav-itemed" id="stuBar">
                <a href="javascript:;" pid="200" id="stu">学生成绩管理</a>
                <dl class="layui-nav-child">
                    <dd v-for="p in pathObj">
                        <a :href="p.path">
                            <i :class="p.icon"></i>
                            <cite v-text="p.title"></cite>
                        </a>
                    </dd>
                </dl>
            </li>

            <li class="layui-nav-item layui-nav-itemed" id="backBar">
                <a href="javascript:;" pid="100" id="blk">后台资源管理</a>
                <dl class="layui-nav-child">
                    <dd v-for="p in pathObj">
                        <a :href="p.path">
                            <i :class="p.icon"></i>
                            <cite v-text="p.title"></cite>
                        </a>
                    </dd>
                </dl>
            </li>
        </ul>
    </div>

    <div class="header-info">
        <span> 当前登陆教师:</span>
        <span> ${sessionScope.user.tea_name} </span>
        <span class="fa fa-sign-out sign-out" aria-hidden="true" style="font-size: 18px;" id="signOut"></span>
    </div>

</header>

<div class="main-banner"></div>

<section class="main-content">
    <div class="search-bar">

    </div>
    <div class="chart-area">

    </div>
</section>

</body>

<script src="${ctx}/js/common/welcome.js"></script>
</html>


