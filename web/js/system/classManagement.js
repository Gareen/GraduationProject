$(function () {

    // 查询状态
    var query_flag = true;

    var $table = $("#dataTable");

    // 页面调整大小后自动适配
    $(window).on("resize", function () {
        $table.jqxGrid({
            height: jqxUtil.getGridHeight(30, 30, 30)
        });
    });

    sidebar.callback = function () {
        $table.jqxGrid("render");
    };

    var teacher = $getTea();

    // 此处的search方法是预留
    var search = function () {


        // 此处是为了多次刷新准备的
        if (!query_flag) {
            return;
        }
        query_flag = false;

        // 设置数据源
        var source = {
            url: "./queryClasses.do",
            datatype: "json",
            type: "post",
            id: 'class_id',
            datafields: [
                {name: 'class_id', type: 'String'},
                {name: 'class_name', type: 'String'},
            ],
        };

        /*data-blind*/
        var dataAdapter = new $.jqx.dataAdapter(source);

        $table.jqxGrid({
            width: "100%",
            height: jqxUtil.getGridHeight(30, 30, 30),
            source: dataAdapter,
            theme: jqx_default_theme,
            altrows: true,
            filterable: true,
            showfilterrow: true,
            columns: [
                {text: '班级编号', dataField: 'class_id', align: "center", cellsAlign: 'center', width: "50%"},
                {text: '班级名称', dataField: 'class_name', align: "center", cellsAlign: 'center', width: "50%"},
            ],
            ready: function () {

                var rowSelectData;

                var $btn_add = $("#add");
                var $btn_edit = $("#edit");
                var $btn_del = $("#delete");

                var $title = $("#win_title");
                var $clzId = $("#clzId");
                var $clzName = $("#clzName");

                var $tip = $("#modtip");
                // 新增还是修改
                var optmode;

                // 行选中事件
                $table.on('rowselect', function (event) {
                    let args = event.args;
                    rowSelectData = args.row;

                    if (rowSelectData) {
                        $btn_edit.prop('disabled', false);
                        $btn_del.prop('disabled', false);
                    }
                });

                $btn_add.click(function () {
                    $tip.hide();
                    openWin('add');
                });

                $btn_edit.click(function () {
                    $tip.show();
                    openWin('mod');
                });
                // class_id: "10002", class_name: "14软件工程2"
                $btn_del.click(function () {

                    if ('1' !== teacher["tea_permission"]) {
                        $bs.error('权限不足，请联系管理员！');
                        return;
                    }

                    $bs.confirm("确认删除该学期么？", function () {
                        $.post(
                            './delete.do',
                            {
                                clzId: rowSelectData['class_id'],
                                promis: teacher['tea_permission']
                            },
                            function (rtn) {
                                if (rtn && rtn.status === 'success') {
                                    $bs.success(rtn.msg);
                                    $table.jqxGrid('deleterow', rtn.data);
                                } else {
                                    $bs.error(rtn.msg);
                                }
                            }
                        )
                    })
                });

                function openWin(opt) {
                    optmode = opt;

                    $clzId.jqxInput({theme: jqx_default_theme, height: "25px", width: 100});
                    $clzName.jqxInput({theme: jqx_default_theme, height: "25px"});


                    if ('1' !== teacher["tea_permission"]) {
                        $bs.error('权限不足，请联系管理员！');
                        return;
                    }

                    if ('add' === opt) {
                        $title.text('新增班级');
                        $clzId.prop('disabled', false);

                        $clzId.val('');
                        $clzName.val('');
                        $("#createWin").modal('show');

                    } else {
                        $title.text('修改班级');
                        $clzId.prop('disabled', true);

                        $.post(
                            './queryClassByClzId.do',
                            {
                                clzId: rowSelectData['class_id']
                            },
                            function (rtn) {

                                var data = rtn.data;
                                if (rtn && rtn["status"] === 'success') {

                                    $clzId.val(data['class_id']);
                                    $clzName.val(data['class_name']);
                                    $("#createWin").modal('show');
                                } else {
                                    $bs.error(rtn.msg);
                                }
                            }
                        )
                    }


                }

                $('#submit').unbind('click').click(function () {
                    var postData = {};
                    postData.optMode = optmode;
                    postData.clzId = $clzId.val();
                    postData.clzName = $clzName.val();
                    postData.tea = teacher['tea_permission'];

                    $.post(
                        './saveOrUpdate.do',
                        {
                            data: JSON.stringify(postData)
                        },
                        function (rtn) {

                            if (rtn.status === 'success') {
                                $bs.success(rtn.msg);
                            } else {
                                $bs.error(rtn.msg);
                            }
                        }
                    );
                    // 点击关闭按钮
                    $("#submit").next().unbind('click').click();
                })
            }
        }).on("bindingcomplete", function () {
            query_flag = true;
        });
    };

    search();
})
