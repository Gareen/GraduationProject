<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<head>
    <%@include file="../common/lib.jsp" %>
</head>


<body>
<%@include file="../common/index.jsp" %>
<div id="view-content">
    <%@include file="../common/sidebar.jsp" %>
    <script src="${ctx}/js/system/resourceManagement.js"></script>

    <div id="view-content-main">

        <div class="container-fluid">
            <div class="row rowtop">
                <div class="col-md-12">
                    <div class="console-title console-title-border clearfix">
                        <div class="fl">
                            <h5>资源管理</h5>
                        </div>
                        <div class="fr">
                            <button class="btn btn-success" id="add">新增</button>
                            <button class="btn btn-primary" id="edit">修改</button>
                            <button class="btn btn-danger" id="delete">删除</button>
                        </div>
                    </div>
                    <div class="alert alert-success margin-top">
                        <a href="#" class="close" data-dismiss="alert"><span class="fa fa-close"></span></a>
                        <strong>提示！</strong>可以进行目前系统中所有的资源模块的查看和管理.<br/>
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
                            <%--上级资源的id--%>
                            <input type="hidden" id="pid">
                            <input type="hidden" id="jud">
                            <tr>
                                <td class='text-right'>父级资源:&nbsp;&nbsp;&nbsp;&nbsp;</td>
                                <td>
                                    <div id="parentNodeName" style="color: slategray"></div>
                                </td>
                            </tr>
                            <tr style="height: 10px;"></tr>
                            <tr>
                                <td class='text-right'>资源编号:&nbsp;&nbsp;&nbsp;&nbsp;</td>
                                <td>
                                    <input type="text" id="nodeNo">
                                    <span style="color: red;">&nbsp;&nbsp;(输入格式: 10X | 20X)</span>
                                </td>
                            </tr>
                            <tr style="height: 10px;"></tr>
                            <tr>
                                <td class='text-right'>资源名称:&nbsp;&nbsp;&nbsp;&nbsp;</td>
                                <td>
                                    <input type="text" id="nodeName">
                                </td>
                            </tr>
                            <tr style="height: 10px;"></tr>
                            <tr>
                                <td class='text-right'>资源路径:&nbsp;&nbsp;&nbsp;&nbsp;</td>
                                <td>
                                    <input type="text" id="nodePath">
                                </td>
                            </tr>
                            <tr style="height: 10px;"></tr>
                            <tr>
                                <td class='text-right'>资源图标:&nbsp;&nbsp;&nbsp;&nbsp;</td>
                                <td>
                                    <input type="text" id="icon">
                                </td>
                            </tr>
                            <tr style="height: 10px;"></tr>
                            <tr>
                                <td class='text-right'>资源等级:&nbsp;&nbsp;&nbsp;&nbsp;</td>
                                <td>
                                    <select id="level">
                                        <option value="1">1</option>
                                        <option value="2">2</option>
                                    </select>
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

    </div>
</div>


</body>
</html>
