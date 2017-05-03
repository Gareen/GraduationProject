<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <%@include file="../common/lib.jsp"%>
</head>


<body>
<%@include file="../common/index.jsp"%>
<div id="view-content">
    <%@include file="../common/sidebar.jsp"%>
    <script src="${ctx}/js/system/classManagement.js"></script>

    <div id="view-content-main">

        <div class="container-fluid">
            <div class="row rowtop">
                <div class="col-md-12">
                    <div class="console-title console-title-border clearfix">
                        <div class="fl">
                            <h5>班级管理</h5>
                        </div>
                        <div class="fr">
                            <button class="btn btn-success" id="createTea">新增</button>
                            <button class="btn btn-primary" id="modifyTea">修改</button>
                            <button class="btn btn-danger" id="deleteTea">删除</button>
                        </div>
                    </div>


                    <div class="alert alert-success margin-top">
                        <a href="#" class="close" data-dismiss="alert"><span class="fa fa-close"></span></a>
                        <strong>提示！</strong>请选中课程进行修改/删除操作<br/>
                    </div>

                </div>
            </div>

            <%--  <div class="panel search" style="height:55px" id="search-panel">
                  <div class="main clearfix">
                      <div>
                          <table border="0" cellspacing="0" cellpadding="0">
                              <tr>
                                  <!--选择日期查询，若不选择状态为销案，则查询结果不包含状态为销案的事故-->
                                  <td class="lb"><label for="accidentTime">事故日期</label></td>
                                  <td>
                                      <div id="accidentTime" class="formItem"></div>
                                  </td>
                                  <td>
                                      <!--查询按钮-->
                                      <input type="button" id="query_button" class="find mr8"/>
                                      <input type="button" id="export" class="dc" title="导出" />
                                  </td>
                              </tr>
                          </table>
                      </div>
                  </div>
              </div>
              <!--空白行-->
              <div id="totalNumber" style="text-align: right;font-weight: bold;font-size: 13px;">&nbsp;</div>--%>

            <div class="row">
                <div class="col-md-12" id="dataTable-panel">
                    <%--表格区--%>
                    <div id="dataTable"></div>

                </div>

            </div>


        </div>

    </div>
</div>

</div>

<%--新建教师--%>
<div class="modal fade" data-backdrop="static" id="createWin" >
    <div class="modal-dialog" style="width: auto;max-width: 500px;min-width: 350px;">
        <div class="modal-content">
            <div class="modal-header">

                <span type="button" class="close fa fa-close" data-dismiss="modal" aria-hidden="true" style="float: right; "></span>

                <%--弹出框的标题--%>
                <h5 class="modal-title" id="tea_win_title"></h5>
            </div>
            <div class="modal-body clearfix" >
                <table class="form" width="100%" id="info_form">
                    <%--新增和更新判断--%>
                    <input type="hidden" id="jud">
                    <tr>
                        <td class='text-right'>教师工号:&nbsp;&nbsp;&nbsp;&nbsp;</td>
                        <td><input type='text'  id="tea_no"/></td>
                    </tr>
                    <tr style="height: 10px;"></tr>
                    <tr>
                        <td class='text-right'>教师姓名:&nbsp;&nbsp;&nbsp;&nbsp;</td>
                        <td><input type='text'  id="tea_name"/></td>
                    </tr>
                    <tr style="height: 10px;"></tr>
                    <tr>
                        <td class='text-right'>密码:&nbsp;&nbsp;&nbsp;&nbsp;</td>
                        <td> <input type='password'  id="tea_password"/></td>
                    </tr>
                    <tr style="height: 10px;"></tr>
                    <tr>
                        <td class='text-right'>权限级别:&nbsp;&nbsp;&nbsp;&nbsp;</td>
                        <td>
                            <select id="tea_permission">
                                <option value="1">1:&nbsp;所有操作均可</option>
                                <option value="2">2:&nbsp;不可新增教师</option>
                            </select>
                        </td>
                    </tr>
                </table>
            </div>

            <div class="modal-footer">
                <a class="btn btn-primary" id="createSubmit">提交</a>
                <a class="btn btn-default" data-dismiss="modal">取消</a>
            </div>
        </div>
    </div>
</div>

</body>
</html>

