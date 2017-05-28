<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html>
<head>
  <title>学生成绩管理系统</title>
  <link rel="shortcut icon" href="../../images/title-icon.ico"/>
</head>

<body>

<div id="view-top">

  <div class="aiis fl">
      <%--<a href="#"></a>--%>

      <a href="../../common/welcome/index.do" title="跳转到首页">
          <span class="glyphicon glyphicon-home"></span>
      </a>

  </div>

  <div class="fl topbar-nav">
    <div>

    </div>
  </div>

  <div class="fr topbar-nav">
    <div>
      <a><span class="nav-title" id="role_name">${sessionScope.user.tea_name}</span></a>
      <ul>
        <li><a href="#"><span class="nav-title" id="modify_pwd">修改密码</span></a></li>
        <li><a href="#"><span class="nav-title" id="login_out">退出</span></a></li>
      </ul>
    </div>
  </div>

  <div class="fr " style="font-size: 25px; color: white; margin-right: 10px;"> <%--topbar-nav--%>
    <div>
      <%-- <a>
         <span class="nav-title"><span class="fa fa-file nav-icon"></span>帮助与文档</span>
       </a>--%>
      <dl class="nav-title" style="font-size: 15px;">
        <i>
          <span id="YYYYMMDD"></span>
          <span id="EEE" ></span>
        </i>
        &nbsp;
        <strong ><span id="clock"></span></strong>
      </dl>
    </div>
  </div>


</div>


</body>
</html>