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

    // 获取教师的工号
    var teaNo = teacher["tea_no"];

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
        teaNo: teaNo, // teacher["tea_no"]
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


    var search = function () {

        if (!query_flag) {
            return;
        }
        query_flag = false;

        var $chooseCourse = $("#choose_course");
        var $chooseClass = $("#choose_class");

        var pscj = sessionStorage.getItem(teaNo + "pss");
        var excj = sessionStorage.getItem(teaNo + "exs");
        var ficj = sessionStorage.getItem(teaNo + "fis");

        // 用session保存的比重值初始化
        if (!pscj || !excj || !ficj) {
            pscj = 30;
            excj = 30;
            ficj = 40;
        }

        var data = {
            teaNo: teaNo,
            termId: term_Id,
            courseId: $chooseCourse.val(),
            classId: $chooseClass.val()
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

        var toolbarHtml = "<span style='padding: 3px; margin: 2px;line-height: 33px;'>&nbsp;&nbsp;" +
            "<i class='fa Example of pie-chart fa-pie-chart' style='margin-right: 20px;'>成绩比重</i>" +
            "平时成绩:&nbsp;<span id='pingshi' style='color: #0a73a7; font-weight: bold'>" + pscj + "</span>%&nbsp;&nbsp;&nbsp;" +
            "实验成绩:&nbsp;<span id='shiyan' style='color: #0a73a7; font-weight: bold'>" + excj + "</span>%&nbsp;&nbsp;&nbsp;" +
            "期末成绩:&nbsp;<span id='qimo' style='color: #0a73a7; font-weight: bold'>" + ficj + "</span>%&nbsp;&nbsp;&nbsp;" +
            "<a id='modify_percent' style='margin-left: 20px;'>设置成绩比重</a></span></span>";

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
                    cellsrenderer: cellrenderer,
                    columntype: 'numberinput',
                    validation: function (cell, value) {
                        if (value < 0 || value > 100) {
                            return {result: false, message: "输入的值请在0 - 100之间 !"};
                        }
                        return true;
                    }
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
                container.append(toolbarHtml);

                toolBar.append(container);

                // 修改成绩比重模块
                $("#modify_percent").unbind('click').click(function () {
                    $("#modify_per .numInput").jqxNumberInput({
                        width: '50px',
                        height: '25px',
                        theme: jqx_default_theme,
                        symbol: "%",
                        min: 0,
                        max: 99,
                        // 不允许小数
                        decimalDigits: 0,
                        digits: 2,
                        spinButtons: false,
                        symbolPosition: 'right'
                    });

                    $("#pss").val(sessionStorage.getItem(teaNo + "pss") ? sessionStorage.getItem(teaNo + "pss") : 30);
                    $("#exs").val(sessionStorage.getItem(teaNo + "exs") ? sessionStorage.getItem(teaNo + "exs") : 30);
                    $("#fis").val(sessionStorage.getItem(teaNo + "fis") ? sessionStorage.getItem(teaNo + "fis") : 40);
                    $("#modify_per").modal('show');
                });

                $("#modifySub").unbind('click').click(function () {
                    var pss = $("#pss").val();
                    var exs = $("#exs").val();
                    var fis = $("#fis").val();
                    var count = pss + exs + fis;

                    if (count !== 100) {
                        $bs.error('设置无效: 三项成绩的比重之和必须为100 !');
                        return;
                    }

                    // 将设置好的值暂存在session中, 分成不同的老师自己的比重
                    sessionStorage.setItem(teaNo + 'pss', pss);
                    sessionStorage.setItem(teaNo + 'exs', exs);
                    sessionStorage.setItem(teaNo + 'fis', fis);

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
                    // {finalId: "1F625058F10000", stuNo: "14550407", stuName: "李壮壮", hScore: "71.5", eScore: "89.35"…}
                });

                // 当分数有改变就保存
                $dataTable.unbind('cellvaluechanged').on('cellvaluechanged', function (event) {
                    // event arguments.
                    var args = event.args;
                    var value = args.newvalue;
                    var datafield = args.datafield;
                    var rowIndex = args.rowindex;

                    // 各项成绩的比重
                    var pss_cent = $("#pingshi").text() / 100;
                    var exs_cent = $("#shiyan").text() / 100;
                    var fis_cent = $("#qimo").text() / 100;


                    if (datafield === 'fScore' || datafield === 'remark') {
                        $.post(
                            "./save.do",
                            {
                                finalId: rowSelectData['finalId'],
                                stuNo: rowSelectData['stuNo'],
                                datafield: datafield,
                                value: value,
                                p: pss_cent,
                                e: exs_cent,
                                f: fis_cent
                            },
                            function (rtn) {
                                if (rtn.status === 'error') {
                                    $bs.error(rtn.msg);
                                    return;
                                }
                                // 如果保存的是分数的话, 要进行更新格子
                                if (datafield === "fScore") {
                                    $dataTable.jqxGrid('setcellvalue', rowIndex, 'score', rtn.data);
                                }
                            }
                        )
                    }
                });

                // 导出excel
                $("#export").unbind('click').click(function () {
                    download("./export.do?termId=" + term_Id + "&courseId=" + $chooseCourse.val() + "&classId=" + $chooseClass.val());
                })
            }
        }).on("bindingcomplete", function () {
            $dataTable.jqxGrid('refresh');
            query_flag = true;
        });

    };


    search();

    $("#query_button").click(function () {
        if (query_flag) {
            // 使打印按钮生效
            $("#export").prop('disabled', false);

            $("#dataTable").each(function () {
                $(this).jqxGrid("destroy");
            });
            // 不重新append的话, 会出现dataTable找不到的异常
            $("#dataTable-panel").append($("<div id='dataTable'></div>"));
            search();
        }
    });

    function download(url, data) {
        $("#tmp-dl-form").remove();
        $("#tmp-dl-iframe").remove();

        var form = $("<form id='tmp-dl-form' method='post' target='tmp-dl-iframe'>" +
            "<input type='hidden' name='data'>" +
            "</form>");
        form.attr("action", url);
        form.children("input").val(data);
        var iframe = $("<iframe id='tmp-dl-iframe' style='display:none' name='tmp-dl-iframe'></iframe>")
        $("body").append(iframe).append(form);
        form.submit();
    }
});
