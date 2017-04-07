<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<head>
    <%@include file="../common/lib.jsp" %>
    <style>
        pt12 {
            padding-top: 12px;
        }

        .changeColor {
            color: red;
        }
    </style>
</head>

<body>
<%@include file="../common/index.jsp" %>
<div id="view-content">
    <%@include file="../common/sidebar.jsp" %>

    <div id="view-content-main">

        <div class="container-fluid">
            <div class="row rowtop">
                <div class="col-md-12">
                    <div class="console-title console-title-border clearfix">
                        <div class="fl">
                            <h5>实验分组管理</h5>
                        </div>
                        <div class="fr" id="term_info">
                            <input type="hidden" id="term_id">
                            <h6 class="fr ml10">当前学期:
                                <span v-if="showTerm" v-text="term_name" :class="{changeColor: showChangeClass}"></span>
                                <span v-else>暂无</span>
                                <a id="changeTerm">更改学期</a>
                            </h6>
                        </div>
                    </div>
                </div>
            </div>

            <div class="row">
                <div class="col-md-12">
                    <div class="panel search" style="height:55px; background-color: #f8f8f8; box-shadow: 0px 0px 1px;  "
                         id="search-panel">
                        <div class="main" style="padding-top: 2px;border-top-width: 0px;">
                            <div>
                                <table border="0" cellspacing="0" cellpadding="0">
                                    <tr>
                                        <td class="lb " style="padding-top: 13px;">
                                            <label for="choose_course">选择课程: </label>
                                        </td>
                                        <td class="lb " style="padding-top: 12px;">
                                            <div id="choose_course"></div>
                                        </td>
                                        <td class="lb"></td>
                                        <td class="lb " style="padding-top: 13px;">
                                            <label for="choose_class">选择班级: </label>
                                        </td>
                                        <td class="lb " style="padding-top: 12px;">
                                            <div id="choose_class"></div>
                                        </td>
                                        <td class="lb"></td>
                                        <td>
                                            <button class="btn btn-primary" id="query_button"
                                                    style="width: 60px; height: 30px;">搜索
                                            </button>
                                        </td>
                                    </tr>
                                </table>
                            </div>
                        </div>
                    </div>

                    <div class="row" id="gridTable">
                        <div class="col-md-12" id="dataTable-panel">
                            <%--表格区--%>
                            <div id="dataTable"></div>
                        </div>
                    </div>

                </div>
            </div>


            <%--模态框区--%>

            <%--改变学期模态框--%>
            <div class="modal fade" data-backdrop="static" id="termWin">
                <div class="modal-dialog" style="width: auto;max-width: 500px;min-width: 350px;">
                    <div class="modal-content">
                        <div class="modal-header">

                            <span type="button" class="close fa fa-close" data-dismiss="modal" aria-hidden="true"
                                  style="float: right; "></span>

                            <%--弹出框的标题--%>
                            <h5 class="modal-title">更改学期</h5>
                        </div>
                        <div class="modal-body clearfix">
                            <table class="form" width="100%" id="info_form">
                                <tr>
                                    <td class='text-right'>选择学期:&nbsp;&nbsp;&nbsp;&nbsp;</td>
                                    <td>
                                        <div id="chooseTerms"></div>
                                    </td>
                                </tr>
                            </table>
                        </div>

                        <div class="modal-footer">
                            <a class="btn btn-primary" data-dismiss="modal" id="termSub">确定</a>
                            <a class="btn btn-default" data-dismiss="modal">取消</a>
                        </div>
                    </div>
                </div>
            </div>

            <%--分组信息模态框--%>
            <div class="modal fade" data-backdrop="static" id="groupWin">
                <div class="modal-dialog" style="width: auto;max-width: 500px;min-width: 350px;">
                    <div class="modal-content">
                        <div class="modal-header">
                            <span type="button" class="close fa fa-close" data-dismiss="modal" aria-hidden="true"
                                  style="float: right; "></span>
                            <%--弹出框的标题--%>
                            <h5 class="modal-title" id="group_win_title"></h5>
                        </div>
                        <div class="modal-body clearfix">
                            <table class="form" width="100%">
                                <tr>
                                    <td class="text-right">
                                        <label for="group_num">分组编号:&nbsp;&nbsp;&nbsp;&nbsp;</label>
                                    </td>
                                    <td>
                                        <div id="group_num"></div>
                                    </td>
                                </tr>
                                <tr style="height: 10px;"></tr>
                                <tr>
                                    <td class="text-right">
                                        <label for="group_leader">小组组长:&nbsp;&nbsp;&nbsp;&nbsp;</label>
                                    </td>
                                    <td>
                                        <div id="group_leader"></div>
                                    </td>
                                </tr>
                                <tr style="height: 10px;"></tr>
                                <tr>
                                    <td class="text-right">
                                        <label for="group_members">小组成员:&nbsp;&nbsp;&nbsp;&nbsp;</label>
                                    </td>
                                    <td style="position: relative">
                                        <div id="group_members"></div>
                                        <a id="clearChecked" style="position: absolute; left: 240px; top: 3px;">清除所选</a>
                                    </td>
                                </tr>
                            </table>
                        </div>

                        <div class="modal-footer">
                            <a class="btn btn-primary" data-dismiss="modal" id="submit">确定</a>
                            <a class="btn btn-default" data-dismiss="modal">取消</a>
                        </div>
                    </div>
                </div>
            </div>
        </div>

    </div>
</div>

</div>

<script src="${ctx}/js/score/groupInitManagement.js"></script>

</body>
</html>

