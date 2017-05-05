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
    <div id="view-content-main">

        <div class="container-fluid">
            <div class="row rowtop">
                <div class="col-md-12">
                    <div class="console-title console-title-border clearfix">
                        <div class="fl">
                            <h5>班级管理</h5>
                        </div>
                        <div class="fr">
                            <button class="btn btn-success" id="add">新增</button>
                            <button class="btn btn-primary" id="edit" disabled>修改</button>
                            <button class="btn btn-danger" id="delete" disabled>删除</button>
                        </div>
                    </div>


                    <div class="alert alert-success margin-top">
                        <a href="#" class="close" data-dismiss="alert"><span class="fa fa-close"></span></a>
                        <strong>提示！</strong>请选中班级进行修改/删除操作，需要管理员权限。<br/>
                    </div>

                </div>
            </div>

            <div class="row">
                <div class="col-md-12" id="dataTable-panel">
                    <%--表格区--%>
                    <div id="dataTable"></div>

                </div>

            </div>


        </div>

    </div>
</div>

<%--添加/修改学期信息--%>
<div class="modal fade" data-backdrop="static" id="createWin">
    <div class="modal-dialog" style="width: auto;max-width: 500px;min-width: 350px;">
        <div class="modal-content">
            <div class="modal-header">

                <span type="button" class="close fa fa-close" data-dismiss="modal" aria-hidden="true"
                      style="float: right; "></span>

                <%--弹出框的标题--%>
                <h5 class="modal-title" id="win_title"></h5>
            </div>
            <div class="modal-body clearfix">
                <table class="form" width="100%" id="info_form">
                    <tr>
                        <td class='text-right'>班级编号:&nbsp;&nbsp;&nbsp;&nbsp;</td>
                        <td>
                            <input id="clzId">
                        </td>
                    </tr>
                    <tr>
                        <td></td>
                        <td>
                            <span style="color:red; display: none" id="modtip">如要修改班级号，请删除班级后重新添加！</span>
                        </td>
                    </tr>
                    <tr style="height: 10px;"></tr>
                    <tr>
                        <td class='text-right'>班级名称:&nbsp;&nbsp;&nbsp;&nbsp;</td>
                        <td>
                            <input id="clzName">
                        </td>
                    </tr>
                </table>
            </div>

            <div class="modal-footer">
                <a class="btn btn-primary" id="submit">提交</a>
                <a class="btn btn-default" data-dismiss="modal">取消</a>
            </div>
        </div>
    </div>
</div>


<script src="${ctx}/js/system/classManagement.js"></script>

</body>
</html>

