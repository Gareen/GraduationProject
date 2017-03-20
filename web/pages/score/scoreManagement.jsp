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
    <script src="${ctx}/js/score/scoreManagement.js"></script>

    <div id="view-content-main">

        <div class="container-fluid">
            <div class="row rowtop">
                <div class="col-md-12">
                    <div class="console-title console-title-border clearfix">
                        <div class="fl">
                            <h5>学生成绩管理</h5>
                        </div>
                        <div class="fr" id="group_class_info">
                            <h6 class="fr ml10">上课教师:
                                <span v-if="teacherName?showTea:false">{{teacherName}}</span>
                                <span v-else>暂无</span>
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
                                        <td class="lb " style="padding-top: 12px;">
                                            <label for="score_choose_term">选择学期: </label>
                                        </td>
                                        <td>
                                            <div id="score_choose_term"></div>
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
                                        <td class="lb"></td>
                                        <td>
                                            <button class="btn btn-primary" id="query_button"
                                                    style="width: 60px; height: 30px;">确认
                                            </button>
                                        </td>
                                        <td class="lb"></td>
                                        <td>
                                            <button class="btn btn-success" id="excelExport"
                                                    style="width: 90px; height: 30px;">打印成绩单
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


        </div>

    </div>
</div>

</div>


</body>
</html>

