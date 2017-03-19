<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<head>
  <%@include file="../common/lib.jsp"%>
  <c:set var="ctx" value="${pageContext.request.contextPath}" />

  <meta charset="utf-8">
  <link rel="stylesheet" href="${ctx}/framework/bootstrap/dist/css/bootstrap.min.css">
  <link rel="stylesheet" href="${ctx}/framework/fontAwesome/css/font-awesome.css">
  <link rel="stylesheet" href="${ctx}/css/login.css" type="text/css" />
  <link rel="stylesheet" href="${ctx}/css/commons.css" type="text/css" />

  <script src="${ctx}/framework/jqwidgets/scripts/jquery-1.11.1.min.js"></script>
  <script src="${ctx}/framework/bootstrap/dist/js/bootstrap.min.js"></script>
  <script src="${ctx}/framework/bootstrap/js/modal.js"></script>
  <script src="${ctx}/js/common/base.js"></script>

  <title>用户登录</title>
</head>


<body class="bg">
  <div class="logo"></div>
  <div style="min-width: 350px;max-width: 500px;width: 25%;top: 20%;right: 15%;position: absolute;">
    <div class="form-top">
      <div class="form-top-left">
        <h2>欢迎使用 SAMS</h2>
        <p>请输入您的工号和密码登录系统:</p>
      </div>
      <div class="form-top-right">
        <span class="fa fa-lock"></span>
      </div>
    </div>
    <div class="form-bottom">
      <div class="login-form">
        <div class="form-group">
          <input type="text" placeholder="工号" class="form-username form-control" id="loginName" maxlength="20">
        </div>
        <div class="form-group">
          <input type="password" placeholder="密码" class="form-password form-control" id="password" maxlength="20">
        </div>
        <button  class="btn" id="login_btn">登录</button>
      </div>
    </div>
  </div>
</body>
<script src="${ctx}/js/system/login.js"></script>
</html>
