$(function () {

    // 查询状态
    var query_flag = true;

    var teacher = $getTea();

    var $table = $("#dataTable");

    $(window).on("resize", function () {
        $table.jqxGrid({
            height: jqxUtil.getGridHeight(30, 30, 30)
        });
    });

    sidebar.callback = function () {
        $table.jqxGrid('render');
    };


    var search = function () {

        // 行选中数据
        let rowSelectData;

        if (!query_flag) {
            return;
        }
        query_flag = false;

        var source = {
            dataType: "json",
            dataFields: [
                {name: 'stu_no', type: 'string'},
                {name: 'stu_name', type: 'string'},
                {name: 'stu_gender', type: 'string'},
                {name: 'stu_class_id', type: 'string'},
            ],
            id: 'stu_no',
            url: "./queryStudents.do"
        };

        var dataAdapter = new $.jqx.dataAdapter(source);

        $table.jqxGrid({
            width: "100%",
            height: jqxUtil.getGridHeight(30, 30, 30),
            source: dataAdapter,
            theme: jqx_default_theme,
            altrows: true,
            groupable: true,
            showgroupsheader: false,
            filterable: true,
            showfilterrow: true,
            columns: [
                {text: '学号', dataField: 'stu_no', align: "center", cellsAlign: 'center', width: "25%"},
                {text: '姓名', dataField: 'stu_name', align: "center", cellsAlign: 'center', width: "30%"},
                {text: '性别', dataField: 'stu_gender', align: "center", cellsAlign: 'center', width: "10%"},
                {text: '班级', dataField: 'stu_class_id', align: "center", cellsAlign: 'center', width: "35%"}
            ],
            groups: ['stu_class_id'],
            ready: function () {
                let $btn_add = $("#add");
                let $btn_edit = $("#edit");
                let $btn_del = $("#delete");
                let mod;

                // 行选中事件
                $table.on('rowselect', function (event) {
                    let args = event.args;
                    rowSelectData = args.row;
                    if (rowSelectData) {
                        $btn_edit.prop('disabled', false);
                        $btn_del.prop('disabled', false);
                    }
                });

                // 删除学生信息
                $btn_del.unbind('click').click(function () {

                    console.log(rowSelectData);
                    $bs.confirm("确认删除该学生？", function () {
                        $.post(
                            './delete.do',
                            {
                                stu: JSON.stringify({
                                    'stu_no': rowSelectData['stu_no'],
                                    'stu_class_id': rowSelectData['stu_class_id']
                                }),
                                teacher: JSON.stringify(teacher)
                            },
                            function (rtn) {

                                if (rtn && rtn.status === 'success') {

                                    $bs.success(rtn['msg']);
                                    $table.jqxGrid('deleterow', rtn.data);
                                    $table.jqxGrid('expandallgroups');
                                } else {
                                    $bs.error(rtn['msg']);
                                }
                            }
                        )
                    })
                });

                let optMode;

                // 新增或者修改学生信息
                $btn_add.unbind("click").click(function () {
                    initWin('add');

                });

                $btn_edit.unbind("click").click(function () {
                    initWin('mod');
                });

                function initWin(mod) {

                    optMode = mod;

                    let $title = $("#win_title");
                    $("#createWin :input").jqxInput({theme: jqx_default_theme, height: "25px"});
                    $("#gender, #stuClass").jqxDropDownList({
                        theme: jqx_default_theme,
                        selectedIndex: 0,
                        height: '25',
                        autoDropDownHeight: true
                    });
                    $("#gender").jqxDropDownList({width: 50});

                    $.post(
                        './queryClassesByTeaId.do',
                        {
                            teacher: JSON.stringify(teacher)
                        },
                        function (rtn) {
                            $("#stuClass").jqxDropDownList({
                                source: rtn,
                                displayMember: 'key',
                                valueMember: 'value'
                            });
                        }
                    );

                    if (mod === 'add') {
                        $title.text('新增学生');
                        $("#stuNo").prop('disabled', false);
                        $("#modtip").hide();
                        $("#createWin :input").val('');
                        $("#gender, #stuClass").jqxDropDownList({selectedIndex: 0});

                        $("#createWin").modal('show');
                    } else {
                        $title.text('修改学生');
                        $("#stuNo").prop('disabled', true);
                        $("#modtip").show();
                        $.post(
                            './queryStudent.do',
                            {
                                stuNo: rowSelectData['stu_no'],
                                teacher: JSON.stringify(teacher)
                            },
                            function (rtn) {
                                if (rtn && rtn.status === 'success') {
                                    let stu = rtn.data;
                                    //{"stu_no":"10002","stu_name":"test2","stu_gender":"1","stu_class_id":"10006"}
                                    $("#stuNo").val(stu['stu_no']);
                                    $("#stuName").val(stu['stu_name']);
                                    $("#gender").val(stu['stu_gender']);
                                    $("#stuClass").val(stu['stu_class_id']);
                                } else {
                                    $bs.error(rtn.msg);
                                    return;
                                }

                                $("#createWin").modal('show');
                            }
                        )
                    }


                }

                $("#submit").unbind('click').click(function () {

                    let postData = {};
                    postData['mode'] = optMode;
                    postData.stuNo = $("#stuNo").val();
                    postData.stuName = $("#stuName").val();
                    postData.gender = $("#gender").val();
                    postData.classes = $("#stuClass").val();

                    $.post(
                        './saveOrUpdate.do',
                        {
                            postData: JSON.stringify(postData)
                        },
                        function (rtn) {

                            if (rtn && rtn['status'] === 'success') {
                                $bs.success(rtn.msg);
                            } else {
                                $bs.error(rtn['msg']);
                            }
                        }
                    );

                    // 点击关闭按钮
                    $("#submit").next().unbind('click').click();
                });
            }

        }).on("bindingcomplete", function () {
            $table.jqxGrid('expandallgroups');
            query_flag = true;
        });

    };

    search();

    // 导出学生信息模版
    $("#export-stuTemp").click(function () {
        excelExport("export.do");
    });

    // 学生名册上传
    let $stuUpload = $("#import-students");
    let $uploadTool = $("#stu-upload");
    // 文件上传请求路径
    let uploadUrl = "./importStudents.do";

    $("#import_stu").click(function () {
        // 完成后需要修改成管理员权限进行添加
        if (teacher["tea_permission"] !== '1') {
            $bs.error("您无权导入学生信息，请联系管理员 ！");
            return;
        }
        initUpload();
        $stuUpload.modal("show");
    });

    // 上传初始化
    function initUpload() {
        // 先对队列中全部的待上传进行清空
        $uploadTool.jqxFileUpload('cancelAll');

        $uploadTool.jqxFileUpload({
            theme: jqx_default_theme,
            height: 150,
            uploadUrl: uploadUrl,
            fileInputName: 'studentsInfo',
            localization: {
                browseButton: '浏览文件',
                uploadButton: '全部上传',
                cancelButton: '全部取消',
                uploadFileTooltip: '点击上传',
                cancelFileTooltip: '点击取消'
            },
            multipleFilesUpload: true,
            uploadTemplate: 'warning',
            // 默认只找寻excel文件
            accept: ['.xlsx', '.xls']
        });

    }

    /*$uploadTool.on('uploadEnd', function (event) {
        var args = event.args;
        var serverResponce = args.response;
     let rtn = JSON.parse($(serverResponce).text());

     /!*if (rtn && rtn['status'] === 'success') {
     $('#dataTable').jqxGrid('render');
     }*!/
     console.log(111)
     });*/

});
