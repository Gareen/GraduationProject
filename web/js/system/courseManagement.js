$(function () {

    // 查询状态
    var query_flag = true;

    var teacher = $getTea();

    var promis = teacher["tea_permission"];

    var $table = $('#dataTable');

    // 页面调整大小后自动适配
    $(window).on("resize", function () {
        $table.jqxGrid({
            height: jqxUtil.getGridHeight(30, 30, 30)
        });
    });

    sidebar.callback = function () {
        $table.jqxGrid("render");
    };


    var search = function () {

        // 此处是为了多次刷新准备的
        if (!query_flag) {
            return;
        }

        // 行选中数据
        let rowSelectData;

        query_flag = false;

        // 设置数据源
        var source = {
            url: "./queryCourses.do",
            datatype: "json",
            type: "post",
            datafields: [
                {name: 'couNum', type: 'String'},
                {name: 'couId', type: 'String'},
                {name: 'couName', type: 'String'},
                {name: 'teaName', type: 'String'},
                {name: 'couClass', type: 'String'},
                {name: 'couCount', type: 'String'},
                {name: 'couTerm', type: 'String'},
                {name: 'couTimePlace', type: 'String'}
            ],
        };

        var dataAdapter = new $.jqx.dataAdapter(source);

        $table.jqxGrid({
            width: "100%",
            height: jqxUtil.getGridHeight(30, 30, 30),
            source: dataAdapter,
            theme: jqx_default_theme,
            altrows: true,
            filterable: true,
            showfilterrow: true,
            showgroupsheader: true,
            groupable: true,
            columns: [
                {text: '课程编号', dataField: 'couId', align: "center", cellsAlign: 'center', width: "10%", pinned: true},
                {text: '课程名', dataField: 'couName', align: "center", cellsAlign: 'center', width: "20%", pinned: true},
                {text: '上课教师', dataField: 'teaName', align: "center", cellsAlign: 'center', width: "10%"},
                {text: '上课班级', dataField: 'couClass', align: "center", cellsAlign: 'center', width: "20%"},
                {text: '选课人数', dataField: 'couCount', align: "center", cellsAlign: 'center', width: "10%"},
                {text: '上课学期', dataField: 'couTerm', align: "center", cellsAlign: 'center', width: "20%"},
                {text: '上课时间和地点', dataField: 'couTimePlace', align: "center", cellsAlign: 'left', width: "60%"}
            ],
            groups: ['teaName', 'couName'],
            ready: function () {
                // 记录唯一标识
                let couNum;

                let optmode;

                let $btn_add = $("#add");
                let $btn_edit = $("#edit");
                let $btn_del = $("#delete");
                let $title = $("#win_title");

                var $course = $("#course");
                var $couCredit = $("#couCredit");
                var $couPeriod = $("#couPeriod");
                var $couTea = $("#couTea");
                var $couTerm = $("#couTerm");
                var $couClz = $("#couClz");
                var $couCounts = $("#couCounts");
                var $timePlace = $("#timePlace");

                // 行选中事件
                $table.on('rowselect', function (event) {
                    let args = event.args;
                    rowSelectData = args.row;

                    console.log(rowSelectData);
                    if (rowSelectData) {
                        couNum = rowSelectData['couNum'];

                        $btn_edit.prop('disabled', false);
                        $btn_del.prop('disabled', false);
                    }
                });


                $btn_add.click(function () {
                    openWin('add');
                });

                $btn_edit.click(function () {
                    openWin('mod');
                });


                function openWin(mode) {

                    if (promis !== '1') {
                        $bs.error("权限不足，请联系管理员 ！");
                        return;
                    }

                    optmode = mode;

                    $.post(
                        'queryCoursesSelectModel.do',
                        function (rtn) {
                            $course.jqxDropDownList({
                                theme: jqx_default_theme,
                                source: rtn,
                                selectedIndex: 0,
                                height: '25',
                                autoDropDownHeight: true,
                                displayMember: 'key',
                                valueMember: 'value'
                            });
                        }
                    );

                    $timePlace.jqxTextArea({
                        theme: jqx_default_theme,
                        placeHolder: '输入上课时间和地点...',
                        height: 90,
                        width: 300,
                        minLength: 1
                    });
                    $couCredit.jqxNumberInput({
                        theme: jqx_default_theme,
                        digits: 1,
                        decimalDigits: 0,
                        width: 35,
                        min: 0,
                        max: 10
                    });
                    $couPeriod.jqxNumberInput({
                        theme: jqx_default_theme,
                        digits: 2,
                        decimalDigits: 0,
                        width: 35,
                        min: 0,
                        max: 99
                    });
                    $couCounts.jqxNumberInput({
                        theme: jqx_default_theme,
                        digits: 3,
                        decimalDigits: 0,
                        width: 35,
                        min: 0,
                        max: 300
                    });
                    // 生成下拉框
                    $.post(
                        './queryTeachers.do',
                        function (rtn) {
                            $couTea.jqxDropDownList({
                                theme: jqx_default_theme,
                                source: rtn,
                                selectedIndex: 0,
                                height: '25',
                                // autoDropDownHeight: true,
                                displayMember: 'key',
                                valueMember: 'value'
                            });
                        }
                    );

                    $.post(
                        './queryTerms.do',
                        function (rtn) {
                            $couTerm.jqxDropDownList({
                                theme: jqx_default_theme,
                                source: rtn,
                                selectedIndex: 0,
                                height: '25',
                                autoDropDownHeight: true,
                                displayMember: 'key',
                                valueMember: 'value'
                            });
                        }
                    );

                    $.post(
                        './queryClz.do',
                        function (rtn) {
                            $couClz.jqxDropDownList({
                                theme: jqx_default_theme,
                                source: rtn,
                                selectedIndex: 0,
                                height: '25',
                                autoDropDownHeight: true,
                                displayMember: 'key',
                                valueMember: 'value'
                            });
                        }
                    );

                    if ('add' === mode) {
                        $title.text('新增上课信息');

                        $course.jqxDropDownList({selectedIndex: 0});
                        $couCredit.jqxNumberInput('setDecimal', 0);
                        $couPeriod.jqxNumberInput('setDecimal', 0);
                        $couCounts.jqxNumberInput('setDecimal', 0);
                        $couTea.jqxDropDownList({selectedIndex: 0});
                        $couTerm.jqxDropDownList({selectedIndex: 0});
                        $couClz.jqxDropDownList({selectedIndex: 0});
                        $timePlace.val('');
                        $("#createWin").modal('show');

                    } else {
                        $title.text('修改上课信息');

                        $.post(
                            './queryCourseByCouNum.do',
                            {
                                couNum: couNum
                            },
                            function (rtn) {
                                var data = rtn.data;
                                //{"cou_number":"2","course_id":"705058","cou_credit":3,"cou_period":56,"cou_tea_no":"12312316","cou_counts":"42",
                                // "class_time_place":"gdf","cou_term_id":"1","class_id":"10001"}
                                if (rtn && rtn.status === 'success') {
                                    $course.val(data['course_id']);
                                    $couCredit.jqxNumberInput('setDecimal', new Number(data['cou_credit']));
                                    $couPeriod.jqxNumberInput('setDecimal', new Number(data['cou_period']));
                                    $couCounts.jqxNumberInput('setDecimal', new Number(data['cou_counts']));
                                    $couTea.val(data['cou_tea_no']);
                                    $couClz.val(data['class_id']);
                                    $timePlace.val(data['class_time_place'] ? data['class_time_place'] : "");

                                    $("#createWin").modal('show');
                                } else {
                                    $bs.error(rtn.msg);
                                }
                            }
                        )

                    }
                }

                $('#submit').unbind('click').click(function () {

                    // 封装上课信息参数
                    var postData = {};
                    postData.optMode = optmode;
                    postData.couNum = couNum;
                    postData.courseId = $course.val();
                    postData.couCredit = $couCredit.jqxNumberInput('getDecimal');
                    postData.couPeriod = $couPeriod.jqxNumberInput('getDecimal');
                    postData.couCounts = $couCounts.jqxNumberInput('getDecimal');
                    postData.couTea = $couTea.val();
                    postData.couClz = $couClz.val();
                    postData.couTerm = $couTerm.val();
                    postData.timePlace = $timePlace.val();
                    postData.promisssion = teacher['tea_permission'];

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
                });

                $btn_del.click(function () {
                    if (promis !== '1') {
                        $bs.error("权限不足，请联系管理员 ！");
                        return;
                    }

                    $bs.confirm("确认删除该课程？", function () {

                        $.post(
                            'delete.do',
                            {
                                couNum: couNum,
                                promis: promis
                            },
                            function (rtn) {
                                if (rtn && rtn.status === 'success') {
                                    $bs.success(rtn.msg);
                                } else {
                                    $bs.error(rtn.msg);
                                }
                            }
                        )

                    })
                });
            }
        }).on("bindingcomplete", function () {
            $table.jqxGrid('expandallgroups');
            query_flag = true;
        });

    };

    search();


    let $btn_cou = $("#couadd_mod");
    let $window = $("#window");
    let $addWindow = $("#addWindow");
    let $grid = $("#grid");
    let $optTitle = $("#optTitle");
    let cno = $("#course_no");
    let cname = $("#course_name");
    let cunit = $("#course_unit");
    let $cancel = $("#cancel");
    let $addtips = $("#add_tips");
    let $optCou = $("#optCou");

    cno.jqxInput({theme: jqx_default_theme, width: 100, height: 25});
    cname.jqxInput({theme: jqx_default_theme, width: 150, height: 25});
    cunit.jqxInput({theme: jqx_default_theme, width: 150, height: 25});

    let rowSelectData;

    let offset = $table.offset();


    let createCourseInfoGrid = function () {

        // 设置数据源
        let source1 = {
            url: "./queryCourseInfo.do",
            datatype: "json",
            type: "post",
            datafields: [
                {name: 'course_id', type: 'String'},
                {name: 'course_name', type: 'String'},
                // 开课单位
                {name: 'course_unit', type: 'String'},
            ],
            id: 'course_id'
        };

        // data-bind
        let dataAdapter1 = new $.jqx.dataAdapter(source1);

        $grid.jqxGrid({
            width: "100%",
            height: "99%",
            source: dataAdapter1,
            theme: jqx_default_theme,
            editable: false,
            selectionmode: 'singlerow',
            altrows: true,
            showtoolbar: true,
            columns: [
                {
                    text: '课程编号',
                    dataField: 'course_id',
                    align: "center",
                    cellsAlign: 'center',
                    width: "20%",
                    editable: false
                },
                {text: '课程名', dataField: 'course_name', align: "center", cellsAlign: 'center', width: "40%"},
                {text: '开课单位', dataField: 'course_unit', align: "center", cellsAlign: 'center', width: "40%"}
            ],
            renderToolbar: function (toolBar) {

                var container = $("<div style='overflow: hidden; position: relative; height: 100%; width: 100%;'></div>");
                var buttonTemplate = "<div style='float: left; padding: 3px; margin: 2px;'><div style='margin: 4px; width: 16px; height: 16px;'></div></div>";
                var addButton = $(buttonTemplate);
                var editButton = $(buttonTemplate);
                var deleteButton = $(buttonTemplate);
                container.append(addButton);
                container.append(editButton);
                container.append(deleteButton);
                // container.append("<span style='padding: 3px; margin: 2px;line-height: 33px;'></span></span>");
                toolBar.append(container);

                addButton.jqxButton({
                    theme: jqx_default_theme,
                    cursor: "pointer",
                    disabled: false,
                    enableDefault: false,
                    height: 25,
                    width: 25
                });
                addButton.find('div:first').addClass('jqx-icon-plus');
                addButton.jqxTooltip({theme: jqx_default_theme, position: 'bottom', content: "新增一条记录"});

                editButton.jqxButton({
                    theme: jqx_default_theme,
                    cursor: "pointer",
                    disabled: true,
                    enableDefault: false,
                    height: 25,
                    width: 25
                });
                editButton.find('div:first').addClass('jqx-icon-edit');
                editButton.jqxTooltip({theme: jqx_default_theme, position: 'bottom', content: "修改选中记录"});


                deleteButton.jqxButton({
                    theme: jqx_default_theme,
                    cursor: "pointer",
                    disabled: true,
                    enableDefault: false,
                    height: 25,
                    width: 25
                });
                deleteButton.find('div:first').addClass('jqx-icon-delete');
                deleteButton.jqxTooltip({theme: jqx_default_theme, position: 'bottom', content: "删除"});


                let rowIndex;
                // 行选中事件
                $grid.on('rowselect', function (event) {
                    let args = event.args;
                    rowIndex = args.rowindex;
                    rowSelectData = args.row;

                    if (rowSelectData) {
                        editButton.jqxButton({disabled: false});
                        deleteButton.jqxButton({disabled: false});
                    }

                });

                // 操作模式
                let optmod;

                // 点击新增按钮
                addButton.click(function () {
                    openWindow('add');
                });

                editButton.click(function () {
                    openWindow('mod');
                });

                deleteButton.click(function () {

                    if (promis !== '1') {
                        $bs.error("权限不足，请联系管理员 ！");
                        return;
                    }

                    var isDel = confirm('是否删除该门课程？');
                    var isConfirm;

                    if (isDel) {
                        isConfirm = confirm('删除该门课程将删除对应的上课信息，是否确认？');
                    }

                    if (isConfirm) {
                        $.when(
                            $.post(
                                './deleteCourseInfoByCouId.do',
                                {
                                    courseId: rowSelectData['course_id'],
                                    promis: promis
                                }
                            )
                        ).done(function (rtn) {
                            if (rtn && rtn.status === 'success') {
                                $grid.jqxGrid('deleterow', rowSelectData['course_id']);
                                $bs.success(rtn.msg + ', 刷新页面后生效!');
                            } else {
                                $bs.error(rtn.msg);
                            }
                        })
                    }
                });

                var openWindow = function (mode) {
                    optmod = mode;

                    initAddWindow();

                    if (mode === 'add') {
                        $optTitle.text('新增课程信息');
                        $addtips.hide();
                        cno.prop('disabled', false);
                        $addWindow.jqxWindow('open');

                    } else {
                        $.when(
                            $.post(
                                './queryCourseInfoById.do',
                                {
                                    courseId: rowSelectData['course_id']
                                }
                            )
                        ).done(function (rtn) {
                            if (rtn) {
                                $optTitle.text('修改课程信息');
                                cno.prop('disabled', true).val(rtn['course_id']);
                                cname.val(rtn['course_name']);
                                cunit.val(rtn['course_unit']);

                                $addtips.show();
                                $addWindow.jqxWindow('open');
                            } else {
                                $bs.error('发生错误！');
                            }

                        }).fail(function (msg) {
                            $bs.error('发生连接错误！');
                        });

                    }


                };

                // 初始化窗口
                var initAddWindow = function () {

                    cno.val('');
                    cname.val('');
                    cunit.val('信息工程学院');
                };

                $optCou.unbind('click').click(function () {

                    var postData = {};
                    postData.mode = optmod;
                    postData.promis = promis;
                    postData.cno = cno.val();
                    postData.cname = cname.val();
                    postData.cunit = cunit.val();

                    $.when(
                        $.post(
                            './saveOrUpdateCouInfo.do',
                            {
                                data: JSON.stringify(postData)
                            }
                        ).done(function (rtn) {
                            if (rtn && rtn.status === 'success') {
                                // $bs.success(rtn.msg);
                                if (optmod === 'add') {
                                    $grid.jqxGrid('addrow', cno, rtn.data, 'last');
                                }
                                if (optmod === 'mod') {
                                    // $grid.jqxGrid('updaterow', cno, rtn.data);
                                    $bs.success(rtn.msg);
                                }
                                initAddWindow();
                            } else {
                                $bs.error(rtn.msg);
                            }
                        })
                    )
                });

                $cancel.click(function () {
                    $addWindow.jqxWindow('close');
                });

                $window.on('close', function (event) {
                    $addWindow.jqxWindow('close');
                });
            }
        });
    };

    $window.jqxWindow({
        theme: jqx_default_theme,
        position: {
            x: offset.left + 150,
            y: offset.top + 50
        },
        autoOpen: false,
        zIndex: 1000,
        showCollapseButton: true,
        height: 400,
        width: 550,
        resizable: false,
        initContent: function () {
            createCourseInfoGrid();
        }
    });

    $addWindow.jqxWindow({
        theme: jqx_default_theme,
        position: {
            x: offset.left + 705,
            y: offset.top + 50
        },
        autoOpen: false,
        zIndex: 1000,
        showCollapseButton: true,
        height: 400,
        width: 300,
        resizable: false,
        initContent: function () {

        }
    });

    // 打开课程信息管理界面
    $btn_cou.click(function () {
        if ('1' !== teacher["tea_permission"]) {
            $bs.error('权限不足，请联系管理员！');
            return;
        }
        $window.jqxWindow('open');
    })

});