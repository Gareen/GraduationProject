<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<head>
  <title>学生成绩管理系统</title>
  <link rel="shortcut icon" href="../../images/title-icon.ico"/>
</head>


<body>



<div id="view-top">


  <div class="aiis fl">
    <%--<a href="#"><span class="glyphicon glyphicon-home"></span></a>--%>
    <span class="glyphicon glyphicon-home"></span>
  </div>

  <%--<div class="fl topbar-nav">
    <div>
      <a><span class="nav-title" >管理</span></a>
    </div>
  </div>--%>

  <div class="fl topbar-nav">
    <div>

      <%-- <ul>
         <li><a href="#"><span class="nav-title">1</span></a></li>
         <li><a href="#"><span class="nav-title">2</span></a></li>
         <li><a href="#"><span class="nav-title">3</span></a></li>
         <li><a href="#"><span class="nav-title">4</span></a></li>
       </ul>--%>
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


 <%-- <div id="view-content">


    &lt;%&ndash;<%@include file="sidebar.jsp"%>&ndash;%&gt;

    &lt;%&ndash;<div id="view-content-main">

      <div class="container-fluid">
            <div class="row">
              <div class="col-md-1"></div>
              <div class="col-md-1">.col-md-1</div>
              <div class="col-md-1">.col-md-1</div>
              <div class="col-md-1">.col-md-1</div>
              <div class="col-md-1">.col-md-1</div>
              <div class="col-md-1">.col-md-1</div>
              <div class="col-md-1">.col-md-1</div>
              <div class="col-md-1">.col-md-1</div>
              <div class="col-md-1">.col-md-1</div>
              <div class="col-md-1">.col-md-1</div>
              <div class="col-md-1">.col-md-1</div>
              <div class="col-md-1">.col-md-1</div>
            </div>
            <div class="row">
              <div class="col-md-8">.col-md-8</div>
              <div class="col-md-4">.col-md-4</div>
            </div>
            <div class="row">
              <div class="col-md-4">.col-md-4</div>
              <div class="col-md-4">.col-md-4</div>
              <div class="col-md-4">.col-md-4</div>
            </div>
            <div class="row">
              <div class="col-md-6">.col-md-6</div>
              <div class="col-md-6">.col-md-6</div>
            </div>

         </div>
       </div>&ndash;%&gt;

  </div>--%>




</body>
</html>