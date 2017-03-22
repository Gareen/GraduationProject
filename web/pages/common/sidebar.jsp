<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<script src="${ctx}/js/common/sidebar.js"></script>

<%--<script src="${ctx}/js/common/test.js"></script>--%>
<style>
    #view-content-left {
        width: 50px;
    }

    #view-content-left-sub {
        left: 50px;
    }

    #view-content-main {
        left: 230px;
    }

    .bg-white {
        background-color: white;
    }
</style>

<!-- 左侧第一排菜单-->
<div id="view-content-left">

    <div id="nav1-switch" class="fa fa-bars"></div>

    <ul class="sidebar-nav nav1" id="sideNav1">

        <li class="nav-item">
            <a href="/sams/score/groupManagement/toIndex.do" id="socreMgr" p_id="200">
                <div class="iconTooltip nav-icon " data-toggle="tooltip" data-placement="right"
                     data-title="<div class='nav-icon-tooltip'>成绩管理</div>">
                    <m class="fa fa-television"></m>
                </div>
                <div class="nav-title">成绩管理</div>
            </a>
        </li>

        <li class="nav-item">
            <a href="/sams/system/termManagement/toIndex.do" id="systemMgr" p_id="100">
                <div class="iconTooltip nav-icon " data-toggle="tooltip" data-placement="right"
                     data-title="<div class='nav-icon-tooltip'>后台资源管理</div>">
                    <m class="fa fa-cog"></m>
                </div>
                <div class="nav-title">后台资源管理</div>
            </a>
        </li>
    </ul>
</div>

<!-- 左侧第二排菜单-->
<div id="view-content-left-sub">
    <div id="nav2-switch-left" style="z-index: 100"><span class="fa fa-chevron-right"></span></div>
    <div id="nav2-switch-right"><span class="fa fa-chevron-left"></span></div>

    <%--第二层导航标题--%>
    <div id="left-sub-title"></div>

    <%--第二层导航--%>
    <ul class="sidebar-nav nav2" id="sideNav2">
        <li class='nav-item' v-for="p in pathObj">
            <a :href="p.path">
                <div class='nav-icon'></div>
                <div class='nav-title' v-text="p.title"></div>
            </a>
        </li>
    </ul>
</div>


<div class="modal fade" data-backdrop="static" id="modifywin">
    <div class="modal-dialog" style="width: auto;max-width: 500px;min-width: 350px;">
        <div class="modal-content">
            <div class="modal-header">
                <input type="hidden" value="${sessionScope.user.tea_no}" id="user_no">
                <span class="close fa fa-close" data-dismiss="modal" aria-hidden="true" style="float: right; "></span>
                <%--弹出框的标题--%>
                <h5 class="modal-title" id="modal_title">修改用户密码</h5>
            </div>
            <div class="modal-body clearfix">
                <table class="form" width="100%" id="info_form">
                    <tr>
                        <td class='text-right'>旧密码:&nbsp;&nbsp;&nbsp;&nbsp;</td>
                        <td><input type='password' id="old_pwd"/></td>
                    </tr>
                    <tr style="height: 10px;"></tr>
                    <tr>
                        <td class='text-right'>新密码:&nbsp;&nbsp;&nbsp;&nbsp;</td>
                        <td><input type='password' id="new_pwd"/></td>
                    </tr>
                </table>
            </div>

            <div class="modal-footer">
                <a class="btn btn-primary" id="modifysubmit">提交</a>
                <a class="btn btn-default" data-dismiss="modal">取消</a>
            </div>
        </div>
    </div>
</div>
<script>


</script>

