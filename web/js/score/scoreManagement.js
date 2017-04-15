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

    // 一开始禁用导出
    $("#export").prop('disabled', true);

    // 侧边栏点击后自动适配
    sidebar.callback = function () {
        $('#dataTable').jqxGrid("render");
    };


    var teacher = $getTea();

    // 默认的学期的id为1
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

    // 实验次数

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
        };

        var source = {
            url: './query.do',
            datatype: "json",
            type: "post",
            data: data,
            datafields: [
                // 学期成绩单编码
                {name: 'finalId', type: 'String'},
                // 学生学号
                {name: 'stuNo', type: 'String'},
                // 学生姓名
                {name: 'stuName', type: 'String'},
                // 学生本学期作业成绩
                {name: 'hScore', type: 'String'},
                // 学生本学期实验成绩
                {name: 'eScore', type: 'String'},
                // 学生本学期期末成绩, 这个是手动输入的
                {name: 'fScore', type: 'String'},
                // 其他情况
                {name: 'remark', type: 'String'},
                // 总成绩
                {name: 'score', type: 'String'}
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
            filterable: true,
            showfilterrow: true,
            selectionmode: 'singlerow',
            columns: [
                {
                    text: '学生学号',
                    dataField: 'stuNo',
                    align: "center",
                    cellsAlign: 'center',
                    width: "15%",
                    editable: false,
                },
                {
                    text: '学生姓名',
                    dataField: 'stuName',
                    align: "center",
                    cellsAlign: 'center',
                    width: "15%",
                    editable: false,
                },
                {
                    text: '作业成绩',
                    dataField: 'hScore',
                    align: "center",
                    cellsAlign: 'center',
                    width: "10%",
                    editable: false,
                    cellsrenderer: cellrenderer
                },
                {
                    text: '实验成绩',
                    dataField: 'eScore',
                    align: "center",
                    cellsAlign: 'center',
                    width: "10%",
                    editable: false,
                    cellsrenderer: cellrenderer
                },
                {
                    text: '期末成绩',
                    dataField: 'fScore',
                    align: "center",
                    cellsAlign: 'center',
                    width: "10%",
                    editable: true,
                    cellsrenderer: cellrenderer
                },
                {
                    text: '总成绩',
                    dataField: 'score',
                    align: "center",
                    cellsAlign: 'center',
                    width: "10%",
                    editable: false
                },
                {
                    text: '其他情况',
                    dataField: 'remark',
                    align: "center",
                    cellsAlign: 'center',
                    width: "30%",
                    editable: true
                }
            ],
            renderToolbar: function (toolBar) {
                var container = $("<div style='overflow: hidden; position: relative; height: 100%; width: 100%;'></div>");

                container.append("<span style='padding: 3px; margin: 2px;line-height: 33px;'>&nbsp;&nbsp;" +
                    "<i class='fa Example of pie-chart fa-pie-chart' style='margin-right: 20px;'>成绩比重</i>" +
                    "平时成绩:&nbsp;<span id='pingshi' style='color: #0a73a7; font-weight: bold'>30</span>%&nbsp;&nbsp;&nbsp;" +
                    "实验成绩:&nbsp;<span id='shiyan' style='color: #0a73a7; font-weight: bold'>30</span>%&nbsp;&nbsp;&nbsp;" +
                    "期末成绩:&nbsp;<span id='qimo' style='color: #0a73a7; font-weight: bold'>40</span>%&nbsp;&nbsp;&nbsp;" +
                    "<a id='modify_percent' style='margin-left: 20px;'>修改成绩比重</a></span></span>");

                toolBar.append(container);

                // 修改成绩比重模块
                $("#modify_percent").unbind('click').click(function () {
                    $("#modify_per .numInput").jqxNumberInput({
                        width: '50px',
                        height: '25px',
                        symbol: "%",
                        min: 0,
                        max: 99,
                        // 不允许小数
                        decimalDigits: 0,
                        digits: 2,
                        spinButtons: false,
                        symbolPosition: 'right'
                    });
                    $("#modify_per").modal('show');
                });
                $("#modifySub").unbind('click').click(function () {
                    var pss = $("#pss").val();
                    var exs = $("#exs").val();
                    var fis = $("#fis").val();
                    var count = pss + exs + fis;

                    if (count !== 100) {
                        $bs.error('设置无效: 三项比重的比例和必须为100 !');
                        return;
                    }
                    // 设置比重显示
                    $("#pingshi").text(pss);
                    $("#shiyan").text(exs);
                    $("#qimo").text(fis);
                });

                // 获取表格中选中行的数据
                var rowSelectData;

                $dataTable.on('rowselect', function (event) {
                    var args = event.args;
                    rowSelectData = args.row;
                });

                // 当分数有改变就保存
                $dataTable.on('cellvaluechanged', function (event) {
                    // event arguments.
                    var args = event.args;
                    var value = args.newvalue;
                    var datafield = args.datafield;

                    $.post(
                        "./save.do",
                        {
                            group_id: rowSelectData['group_id'],
                            datafield: datafield,
                            group_num: rowSelectData['group_num'],
                            ex_index: rowSelectData['ex_index'],
                            score: value
                        },
                        function (rtn) {
                            if (rtn.status === 'error') {
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
            $("#export").prop('disabled', false);
            $("#dataTable").each(function () {
                $(this).jqxGrid("destroy");
            });
            // 不重新append的话, 会出现dataTable找不到的异常
            $("#dataTable-panel").append($("<div id='dataTable'></div>"));
            search();
        }
    });
});
