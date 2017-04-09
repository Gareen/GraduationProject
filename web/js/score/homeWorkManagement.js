$(function () {

    var term_info = {
        term_name: "",
        showChangeClass: false,
        showTerm: false
    };

    new Vue({
        el: "#term_info",
        data: term_info
    });

    // 页面调整大小后自动适配
    $(window).on("resize", function () {
        $('#dataTable').jqxGrid({
            height: jqxUtil.getSearchGridHeight()
        });


    });

    // 侧边栏点击后自动适配
    sidebar.callback = function () {
        $('#dataTable').jqxGrid("render");
    };

    var teacher = $getTea();

    var term_Id = 1;

    var query_flag = true;

    $.post(
        './queryCurrentTime.do',
        function (rtn) {
            if (rtn) {
                store.setItem("currentTime", rtn['term_name']);
                term_info.term_name = rtn["term_name"];
                term_info.showTerm = true;
                $("#term_id").val(rtn['term_id']);
            }
        }
    );

    $("#changeTerm").click(function () {
        $.post(
            './queryAllTerms.do',
            function (rtn) {
                if (rtn) {
                    $("#chooseTerms").jqxDropDownList({
                        source: rtn,
                        selectedIndex: 0,
                        width: '170',
                        height: '25',
                        theme: jqx_default_theme,
                        autoDropDownHeight: true,
                        displayMember: 'key',
                        valueMember: 'value'
                    });
                    $("#termWin").modal('show');
                } else {
                    $bs.error('发生错误 !');
                }
            }
        );
    });

    $("#termSub").click(function () {
        var ct = store.getItem('currentTime');
        var value = $("#chooseTerms").val();
        var text = $("#chooseTerms").text();

        if (ct === text) {
            term_info.showChangeClass = false;
        } else {
            // 提示用户当前选择的学期不是当前学期
            term_info.showChangeClass = true;
        }

        // 改变学期名
        term_info.term_name = text;

        // 将改变的学期号给赋值
        term_Id = value;

        // 改变学期就重新生成课程
        queryCourseData.termId = value;
        createCourse();

    });

    // 封装查询课程的方法, 根据登录的老师和选中的学期进行查询
    var queryCourseData = {
        teaNo: teacher["tea_no"],
        termId: term_Id
    };


    var createCourse = function () {
        $.when(
            $.post(
                "./queryCoursesByTeacherIdAndTerm.do",
                queryCourseData,
                function (rtn) {
                    $("#choose_course").jqxDropDownList({
                        placeHolder: "选择课程",
                        source: rtn,
                        selectedIndex: 0,
                        width: '150',
                        height: '25',
                        theme: jqx_default_theme,
                        autoDropDownHeight: true,
                        displayMember: 'key',
                        valueMember: 'value'
                    });
                }
            )).done(function () {

            var courseId = $("#choose_course").val();
            $.post(
                "./queryClasses.do",
                {
                    teaNo: teacher["tea_no"],
                    termId: term_Id,
                    courseId: courseId
                },
                function (rtn) {
                    $("#choose_class").jqxDropDownList({
                        placeHolder: "选择班级",
                        source: rtn,
                        selectedIndex: 0,
                        width: '150',
                        height: '25',
                        theme: jqx_default_theme,
                        autoDropDownHeight: true,
                        displayMember: 'key',
                        valueMember: 'value'
                    });
                }
            )
        })

    };

    createCourse();

    // 作业次数
    var $chooseHomework = $("#choose_homework");

    var homework_count = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10];

    $chooseHomework.jqxDropDownList({
        placeHolder: "作业次数",
        selectedIndex: 0,
        source: homework_count,
        theme: jqx_default_theme,
        width: '70',
        height: "25px",
    });

    var search = function () {

        if (!query_flag) {
            return;
        }
        query_flag = false;

        var $chooseCourse = $("#choose_course");
        var $chooseClass = $("#choose_class");

        var data = {
            teaNo: teacher["tea_no"],
            termId: term_Id,
            courseId: $chooseCourse.val(),
            classId: $chooseClass.val(),
            workIndex: $chooseHomework.val()
        };

        var source = {
            url: './query.do',
            datatype: "json",
            type: "post",
            data: data,
            datafields: [
                // work_id是有编码的
                {name: 'work_id', type: 'String'},
                {name: 'stu_id', type: 'String'},
                {name: 'stu_name', type: 'String'},
                {name: 'work_index', type: 'String'},
                {name: 'work_score', type: 'String'},
            ]
        };

        // 不及格的成绩标红
        var cellrenderer = function (row, columnfield, value, defaulthtml, columnproperties, rowdata) {
            if (value < 60) {
                return '<div style="width: 100%; height: 100%; text-align: center;">' +
                    '<span style="line-height: 28px; color: #ff0000;">' + value + '</span>' +
                    '</div>';
            }
            else {
                return '<div style="width: 100%; height: 100%; text-align: center;">' +
                    '<span style="line-height: 28px;">' + value + '</span>' +
                    '</div>';
            }
        };

        // 数据绑定
        var dataAdapter = new $.jqx.dataAdapter(source);

        var $dataTable = $("#dataTable");

        $dataTable.jqxGrid({
            width: "100%",
            height: jqxUtil.getSearchGridHeight(),
            source: dataAdapter,
            // 设置不可分页
            theme: jqx_default_theme,
            editable: true,
            editmode: 'selectedcell',
            altrows: true,
            showtoolbar: true,
            selectionmode: 'singlerow',
            columns: [
                {
                    text: '学生学号',
                    dataField: 'stu_id',
                    align: "center",
                    cellsAlign: 'center',
                    width: "20%",
                    editable: false
                },
                {
                    text: '学生姓名',
                    dataField: 'stu_name',
                    align: "center",
                    cellsAlign: 'center',
                    width: "30%",
                    editable: false
                },
                {
                    text: '作业成绩',
                    dataField: 'work_score',
                    align: "center",
                    cellsAlign: 'center',
                    width: "10%",
                    cellsrenderer: cellrenderer
                },
                {
                    text: '作业次数',
                    dataField: 'work_index',
                    align: "center",
                    cellsAlign: 'center',
                    width: "10%",
                    editable: false
                },
                {
                    text: '作业id',
                    dataField: 'work_id',
                    align: "center",
                    cellsAlign: 'center',
                    width: "30%",
                    editable: false
                }
            ],
            renderToolbar: function (toolBar) {
                var container = $("<div style='overflow: hidden; position: relative; height: 100%; width: 100%;'></div>");
                container.append("<span style='padding: 3px; margin: 2px;line-height: 33px;'>&nbsp;&nbsp;&nbsp;提示:&nbsp;支持类excel操作,&nbsp;双击成绩单元格进行填写成绩</span></span>");
                toolBar.append(container);

                // 获取表格中选中行的数据
                var rowSelectData;

                $dataTable.on('rowselect', function (event) {
                    var args = event.args;
                    // {work_id: "1H625058H10000", stu_id: "14550123", stu_name: "孙清蓉 ",
                    // work_index: "1", work_score: "40"…}
                    rowSelectData = args.row;
                });

                // 当分数有改变就保存
                $dataTable.on('cellvaluechanged', function (event) {
                    // event arguments.
                    var args = event.args;
                    var value = args.newvalue;

                    $.post(
                        "./save.do",
                        {
                            workid: rowSelectData['work_id'],
                            stuId: rowSelectData['stu_id'],
                            workIndex: rowSelectData['work_index'],
                            score: value
                        },
                        function (rtn) {
                            if (rtn.status === 'error') {
                                // 恢复原来的值
                                $dataTable.jqxGrid('setcellvalue', args.rowindex, "work_score", args.oldvalue);
                                $bs.error(rtn.msg);
                            }
                        }
                    )
                });
            }
        }).on("bindingcomplete", function () {
            $dataTable.jqxGrid('refresh');
            query_flag = true;
        });

    };


    search();

    $("#query_button").click(function () {
        if (query_flag) {
            $("#dataTable").each(function () {
                $(this).jqxGrid("destroy");
            });
            // 不重新append的话, 会出现dataTable找不到的异常
            $("#dataTable-panel").append($("<div id='dataTable'></div>"));
            search();
        }
    });
});

