<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%--选择重置次数--%>
<div class="modal fade" data-backdrop="static" id="resetWin">
    <div class="modal-dialog" style="width: auto;max-width: 500px;min-width: 350px;">
        <div class="modal-content">
            <div class="modal-header">
                 <span type="button" class="close fa fa-close" data-dismiss="modal" aria-hidden="true"
                       style="float: right; "></span>

                <%--弹出框的标题--%>
                <h5 class="modal-title">重置分数</h5>
            </div>
            <div class="modal-body clearfix">
                <table class="form" width="100%" id="info_form">
                    <tr>
                        <td class='text-right' style="padding-right: 8px;">选择次数:</td>
                        <td>
                            <div id="score_counts"></div>
                        </td>
                    </tr>
                    <tr style="height: 10px;"></tr>
                    <tr>
                        <td class='text-right' style=" padding-right: 8px"><strong>Tips:</strong></td>
                        <td>
                            <div>
                                <span style="color: red;">
                                    重置分数将会清空已登记的分数, 请慎重!
                                </span>
                            </div>
                        </td>
                    </tr>
                </table>
            </div>

            <div class="modal-footer">
                <a class="btn btn-danger" data-dismiss="modal" id="reset_sub">确定重置</a>
                <a class="btn btn-default" data-dismiss="modal" id="reset_cancel">取消</a>
            </div>
        </div>
    </div>
</div>

<%--删除次数--%>
<div class="modal fade" data-backdrop="static" id="deleteWin">
    <div class="modal-dialog" style="width: auto;max-width: 500px;min-width: 350px;">
        <div class="modal-content">
            <div class="modal-header">
                 <span type="button" class="close fa fa-close" data-dismiss="modal" aria-hidden="true"
                       style="float: right; "></span>

                <%--弹出框的标题--%>
                <h5 class="modal-title">删除成绩次数</h5>
            </div>
            <div class="modal-body clearfix">
                <table class="form" width="100%">
                    <tr>
                        <td class='text-right' style="padding-right: 8px;">选择次数:</td>
                        <td>
                            <div id="delete_score_counts"></div>
                        </td>
                    </tr>
                    <tr style="height: 10px;"></tr>
                    <tr>
                        <td class='text-right' style=" padding-right: 8px"><strong>Tips:</strong></td>
                        <td>
                            <div>
                                <span style="color: red;">
                                    删除成绩次数无法撤销, 请慎重!
                                </span>
                            </div>
                        </td>
                    </tr>
                </table>
            </div>

            <div class="modal-footer">
                <a class="btn btn-danger" data-dismiss="modal" id="delete_sub">确定删除</a>
                <a class="btn btn-default" data-dismiss="modal" id="delete_cancel">取消</a>
            </div>
        </div>
    </div>
</div>