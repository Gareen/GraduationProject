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

                var $couId = $("#couId");
                var $couName = $("#couName");
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

                    $couName.jqxInput({theme: jqx_default_theme, height: "25px"});
                    $couId.jqxInput({theme: jqx_default_theme, height: "25px"});
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
                        $title.text('新增课程');

                        $couName.val('');
                        $couId.val('');
                        $couCredit.jqxNumberInput('setDecimal', 0);
                        $couPeriod.jqxNumberInput('setDecimal', 0);
                        $couCounts.jqxNumberInput('setDecimal', 0);
                        $couTea.jqxDropDownList({selectedIndex: 0});
                        $couTerm.jqxDropDownList({selectedIndex: 0});
                        $couClz.jqxDropDownList({selectedIndex: 0});
                        $timePlace.val('');
                        $("#createWin").modal('show');

                    } else {
                        $title.text('修改课程');

                        $.post(
                            './queryCourseById.do',
                            {
                                couNum: couNum
                            },
                            function (rtn) {
                                var data = rtn.data;
                                //{"cou_number":"2","course_id":"705058","cou_credit":3,"cou_period":56,"cou_tea_no":"12312316","cou_counts":"42",
                                // "class_time_place":"gdf","cou_term_id":"1","class_id":"10001"}
                                if (rtn && rtn.status === 'success') {
                                    $couId.val(data['course_id']);
                                    $couName.val(data['']);
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
                    var postData = {};
                    postData.optMode = optmode;
                    postData.termId = $termId.jqxNumberInput('getDecimal');
                    postData.termName = $termName.val();
                    postData.termYear = $termYear.jqxNumberInput('getDecimal');
                    postData.termMon = $termMon.val();
                    postData.tea = teacher['tea_permission'];

                    $.post(
                        // './saveOrUpdate.do',
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
                            // 'delete.do',
                            {
                                couNum: couNum,
                                promis: promis
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
            }
        }).on("bindingcomplete", function () {
            $table.jqxGrid('expandallgroups');
            query_flag = true;
        });

    };

    search();

    let $btn_cou = $("#couadd_mod");
    let $window = $("#window");

    let offset = $table.offset();

    // 设置数据源
    var source1 = {
        url: "./queryCourseInfo.do",
        datatype: "json",
        type: "post",
        datafields: [
            {name: 'course_id', type: 'String'},
            {name: 'course_name', type: 'String'},
            // 开课单位
            {name: 'course_unit', type: 'String'},
        ],
    };

    var dataAdapter1 = new $.jqx.dataAdapter(source1);

    $window.jqxWindow({
        theme: jqx_default_theme,
        position: {
            x: offset.left + 250,
            y: offset.top + 50
        },
        autoOpen: false,
        showCollapseButton: true,
        height: 400,
        width: 700,
        resizable: false,
        initContent: function () {
            var $grid = $("#grid");

            $grid.jqxGrid({
                width: "100%",
                height: "99%",
                source: dataAdapter1,
                theme: jqx_default_theme,
                altrows: true,
                columns: [
                    {text: '课程编号', dataField: 'course_id', align: "center", cellsAlign: 'center', width: "20%"},
                    {text: '课程名', dataField: 'course_name', align: "center", cellsAlign: 'center', width: "40%"},
                    {text: '开课单位', dataField: 'course_unit', align: "center", cellsAlign: 'center', width: "40%"}
                ]
            });
        }
    });

    $btn_cou.click(function () {
        $window.jqxWindow('open');
    })

});