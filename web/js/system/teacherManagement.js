$(function () {

    // 查询状态
    var query_flag = true;

    let teacher = $getTea();

    // 页面调整大小后自动适配
    $(window).on("resize", function (){
        $('#dataTable').jqxGrid({
            height : jqxUtil.getGridHeight(30,30,30)
        });
    });

    sidebar.callback = function (){
        $('#dataTable').jqxGrid("render");
    };

    // 此处的search方法是预留
    var search = function() {

        // 获取选中行的boundIndex
        var rowIndex = -1;

        // 获取选中行记录的id
        var rowId = 0;

        // 此处是为了多次刷新准备的
        if(!query_flag){
            return;
        }
        query_flag = false;

        // 设置数据源
        var source = {
            url: "./queryTeachers.do",
            datatype: "json",
            type: "post",
           /* data: {
                "id": $("#tea_no").val()
            },*/
            id: 'tea_no',
            datafields: [
                // 教师工号
                {name: 'tea_no', type: 'String'},
                // 教师姓名
                {name: 'tea_name', type: 'String'},
                // 教师权限
                {name: 'tea_permission', type: 'String'}
            ],
        };

        /*data-blind*/
        var dataAdapter = new $.jqx.dataAdapter(source);

        $("#dataTable").jqxGrid({
            width: "100%",
            height :  jqxUtil.getGridHeight(30,30,30),
            source : dataAdapter,
            theme:jqx_default_theme,
            altrows: true,
            filterable: true,
            showfilterrow: true,
            rendered: function () {

                $(":text").jqxInput({theme: jqx_default_theme, width:'30%', height:"25px"});
                $(":password").jqxPasswordInput({theme: jqx_default_theme, width:'45%', height:"25px"});
                $("#tea_permission").jqxDropDownList({theme: jqx_default_theme, width:'45%', height:"25px", autoDropDownHeight: true});
                // 新建窗口初始化
                $("#createTea").click(function () {
                    if (teacher && teacher['tea_permission'] !== '1') {
                        $bs.error('您无权新增教师，请联系管理员！');
                        return;
                    }

                    $("#jud").val("").val("add");
                    $("#tea_win_title").html("新增教师");
                    $("#tea_no").jqxInput({disabled:false});
                    $("#tea_permission").jqxDropDownList({ disabled: false });
                    $("#tea_no").val("");
                    $("#tea_name").val("");
                    $("#tea_password").val("");
                    $("#tea_permission").val("1");
                    $("#createWin").modal("show");
                });

                // 获取当前表单中填写的教师信息
                var createTea = function () {
                    var tea = {};
                    tea["jud"] = $("#jud").val();
                    tea["id"] = $("#tea_no").val();
                    tea["name"] = $("#tea_name").val();
                    tea["pwd"] = $("#tea_password").val();
                    tea["pre"] = $("#tea_permission").val();
                    return tea;
                }

                // 提交按钮点击事件
                var createSubmit = $("#createSubmit");
                createSubmit.unbind("click").click(function () {

                    var tea = createTea();
                    $.post(
                        "./saveTeacher.do",
                        tea,
                        function (rtn) {
                            // {"status":"success","msg":"保存成功 !","data":null}
                            var status = rtn["status"];
                            var msg = rtn["msg"];
                            // 关闭window
                            createSubmit.next("a").click();
                            if (status === 'success') {
                                $bs.success(msg);
                                destroyGrid("dataTable");
                                search();
                            } else {
                                $bs.error(msg);
                            }
                        }
                    )
                });


                $("#dataTable").unbind("rowselect").on('rowselect', function (event) {
                    var args = event.args;
                    rowIndex = args.rowindex;
                    var rowData = args.row;
                    // rowData {tea_no: "10000", tea_name: "管理员", tea_permission: "1", uid: 0, boundindex: 0…}
                    // 获取选中行的id
                    rowId = rowData["tea_no"];
                });

                /*删除教师*/
                $("#deleteTea").unbind("click").click(function() {
                    if (rowIndex === -1) {
                        $bs.alert("请选中一位教师 !");
                        return;
                    }
                    // $bs.confirm("确定要删除这条记录吗 ?");
                    /* var d = confirm();
                     if (! d) {
                     return;
                     }*/
                    $bs.confirm("确定要删除这条记录吗 ?", function () {
                        $.post(
                            "./deleteTeacherById.do",
                            {"id":rowId},
                            function (rtn) {
                                // {"status":"success","msg":"保存成功 !","data":null}
                                var status = rtn["status"];
                                var msg = rtn["msg"];
                                // 关闭window
                                $("#msg_n").click();
                                if (status == 'success') {
                                    $bs.success(msg);
                                    destroyGrid("dataTable");
                                    search();
                                } else {
                                    $bs.error(msg);
                                }
                            }
                        )
                    })

                });

                /*修改教师*/
                $("#modifyTea").unbind("click").click(function () {
                    $("#jud").val("").val("mod");
                    $("#tea_win_title").html("修改教师");
                    $("#tea_no").jqxInput({disabled:true});
                    if (rowIndex == -1) {
                        $bs.alert("请选中一位教师 !");
                        return;
                    }
                    $.post(
                        "./queryTeacherById.do",
                        {"id":rowId},
                        function (rtn) {
                            // {"tea_no":"12121112","tea_password":"sams","tea_name":"徐老师","tea_permission":"2"}
                            for (var id in rtn) {
                                $("#" + id).val(rtn[id]);
                            }
                            // 如果是管理员的话, 那么就不可以修改权限
                            var tea_no = $("#tea_no").val();
                            // 此处初始化一下下拉列表的可用, 对管理员进行特殊的处理
                            $("#tea_permission").jqxDropDownList({ disabled: false });
                            if (tea_no == '10000') {
                                $("#tea_permission").jqxDropDownList({ disabled: true });
                            } else {
                                $("#tea_permission").jqxDropDownList({ disabled: false });
                            }
                            $("#createWin").modal("show");
                            $("#createSubmit").unbind("click").click(function() {
                                var tea = createTea();
                                $.post(
                                    "./saveTeacher.do",
                                    tea,
                                    function (rtn) {
                                        var status = rtn["status"];
                                        var msg = rtn["msg"];
                                        if (status == 'success') {
                                            $bs.success(msg);
                                            destroyGrid("dataTable");
                                            search();
                                        } else {
                                            $bs.error(msg);
                                        }
                                        // 关闭window
                                        createSubmit.next("a").click();
                                    }
                                )
                            });
                        }
                    )
                })
            },
            columns : [
                {text: '教师工号', 	    dataField: 'tea_no', 			align: "center", 		cellsAlign: 'center', 		width: "35%"},
                {text: '教师姓名', 		dataField: 'tea_name', 		align: "center", 		cellsAlign: 'center', 		width: "35%"},
                {text: '管理权限', 		dataField: 'tea_permission', 		align: "center", 		cellsAlign: 'center', 		width: "30%"},
            ],
        }).on("bindingcomplete", function () {
            query_flag = true;
        });

    };


    search();
})