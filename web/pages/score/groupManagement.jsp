<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<head>
    <%@include file="../common/lib.jsp" %>
    <style>
        pt12 {
            padding-top: 12px;
        }
    </style>
</head>


<body>
<%@include file="../common/index.jsp" %>
<div id="view-content">
    <%@include file="../common/sidebar.jsp" %>
    <script src="${ctx}/js/score/groupManagement.js"></script>

    <div id="view-content-main">

        <div class="container-fluid">
            <div class="row rowtop">
                <div class="col-md-12">
                    <div class="console-title console-title-border clearfix">
                        <div class="fl">
                            <h5>分组成绩管理</h5>
                        </div>
                        <%--<div class="fr" id="group_class_info">
                            <h6 class="fr ml10" >上课教师:
                                <span v-if="teacherName?showTea:false" >{{teacherName}}</span>
                                <span v-else>暂无</span>
                            </h6>
                        </div>--%>
                    </div>

                </div>
            </div>

            <div class="row">
                <div class="col-md-12" >
                    <div class="panel search" style="height:55px; background-color: #f8f8f8; box-shadow: 0px 0px 1px;  " id="search-panel">
                        <div class="main" style="padding-top: 2px;border-top-width: 0px;">
                            <div>
                                <table border="0" cellspacing="0" cellpadding="0">
                                    <tr>
                                        <td class="lb " style="padding-top: 12px;" >
                                            <label for="group_choose_class">选择课程: </label>
                                        </td>
                                        <td>
                                           <div id="group_choose_class"></div>
                                        </td>

                                        <td class="lb" style="padding-top: 12px;">
                                            <label for="group_choose_class_place">上课地点: </label>
                                        </td>
                                        <td>
                                            <div id="group_choose_class_place"></div>
                                        </td>

                                        <td class="lb" style="padding-top: 12px;">
                                            <label for="group_choose_class_week">选择周数:</label>
                                        </td>
                                        <td>
                                            <div id="group_choose_class_week"></div>
                                        </td>

                                        <td class="lb" style="padding-top: 12px;">
                                            <label for="group_choose_classTime">上课时间:</label>
                                        </td>
                                        <td>
                                            <div id="group_choose_classTime"></div>
                                        </td>
                                        <td class="lb" ></td>
                                        <td>
                                            <button class="btn btn-primary" id="query_button" style="width: 60px; height: 30px;">确认</button>
                                        </td>
                                    </tr>
                                </table>
                            </div>
                        </div>
                    </div>
                    <div class="row"  id="gridTable">
                        <div class="col-md-12" id="dataTable-panel" >
                            <%--表格区--%>
                            <div id="dataTable" ></div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="modal fade" data-backdrop="static" id="createWin" >
                <div class="modal-dialog" style="width: auto;max-width: 500px;min-width: 350px;">
                    <div class="modal-content">
                        <div class="modal-header">

                            <span type="button" class="close fa fa-close" data-dismiss="modal" aria-hidden="true" style="float: right; "></span>

                            <%--弹出框的标题--%>
                            <h5 class="modal-title" id="group_win_title"></h5>
                        </div>
                        <div class="modal-body clearfix" >
                            <table class="form" width="100%" id="info_form">
                                <%--新增和更新判断--%>
                                <input type="hidden" id="jud">
                                <%-- 小组编码 --%>
                                <input type="hidden" id="group_id">
                                <tr>
                                    <td class='text-right'>小组编号:&nbsp;&nbsp;&nbsp;&nbsp;</td>
                                    <td>
                                        <select id="group_num"></select>
                                    </td>
                                </tr>
                                <tr style="height: 10px;"></tr>
                                <tr>
                                    <td class='text-right'>小组组长:&nbsp;&nbsp;&nbsp;&nbsp;</td>
                                    <td><div id="group_leader"></div></td>
                                </tr>
                                <tr style="height: 10px;"></tr>
                                <tr>
                                    <td class='text-right'>小组成员:&nbsp;&nbsp;&nbsp;&nbsp;</td>
                                    <td> <div id="group_member"></div></td>
                                </tr>
                                <tr style="height: 10px;"></tr>
                                <tr>
                                    <td class='text-right'>小组成绩:&nbsp;&nbsp;&nbsp;&nbsp;</td>
                                    <td><input type='text' id="group_score"/></td>
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

        </div>

    </div>
</div>

</div>


</body>
</html>
