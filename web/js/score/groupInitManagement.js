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

    var mod = "";

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

        var $groupNum = $("#group_num");
        var $chooseCourse = $("#choose_course");
        var $chooseClass = $("#choose_class");

        // 分组编号  目前最多支持分十五组
        var groups = ['1', '2', '3', '4', '5', '6', '7', '8', '9', '10', '11', '12', '13', '14', '15'];

        $groupNum.jqxDropDownList({
            placeHolder: "小组号",
            source: groups,
            theme: jqx_default_theme,
            width: '100',
            height: "25px",
        });

        var data = {
            teaNo: teacher["tea_no"],
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
                {name: 'group_num', type: 'String'},
                {name: 'stu_is_leader', type: 'String'},
                {name: 'stu_is_member', type: 'String'},
            ]
        };

        // 生成学生信息下拉列表
        $.post(
            "./queryStudentsByClassId.do",
            {
                classId: $chooseClass.val()
            },
            function (rtn) {
                console.log(JSON.stringify(rtn));
                $("#group_leader").jqxDropDownList({
                    placeHolder: "选择组长",
                    source: rtn,
                    filterPlaceHolder: "学生学号",
                    selectedIndex: 0,
                    width: '170',
                    height: '25',
                    theme: jqx_default_theme,
                    filterable: true,
                    displayMember: 'key',
                    valueMember: 'value'
                });
                $("#group_members").jqxDropDownList({
                    placeHolder: "选择组员",
                    source: rtn,
                    filterPlaceHolder: "学生学号",
                    selectedIndex: 0,
                    checkboxes: true,
                    width: '200',
                    height: '25',
                    theme: jqx_default_theme,
                    filterable: true,
                    displayMember: 'key',
                    valueMember: 'value'
                });
            }
        )

        // 数据绑定
        var dataAdapter = new $.jqx.dataAdapter(source);

        $("#dataTable").jqxGrid({
            width: "100%",
            height: jqxUtil.getSearchGridHeight(),
            source: dataAdapter,
            // 设置不可分页
            theme: jqx_default_theme,
            editable: false,
            altrows: true,
            showtoolbar: true,
            selectionmode: 'singlerow',
            columns: [
                {text: '小组编号', dataField: 'group_num', align: "center", cellsAlign: 'center', width: "10%"},
                {text: '小组组长', dataField: 'stu_is_leader', align: "center", cellsAlign: 'center', width: "20%"},
                {text: '组员姓名', dataField: 'stu_is_member', align: "center", cellsAlign: 'center', width: "70%"}
            ],
            renderToolbar: function (toolBar) {
                var container = $("<div style='overflow: hidden; position: relative; height: 100%; width: 100%;'></div>");
                var buttonTemplate = "<div style='float: left; padding: 3px; margin: 2px;'><div style='margin: 4px; width: 16px; height: 16px;'></div></div>";
                var addButton = $(buttonTemplate);
                var deleteButton = $(buttonTemplate);
                container.append(addButton);
                container.append(deleteButton);
                container.append("<span style='padding: 3px; margin: 2px;line-height: 33px;'>&nbsp;&nbsp;&nbsp;提示:&nbsp;1.请先设置好搜索条件进行搜索;&nbsp;2.请双击一条记录进行修改;</span>");

                toolBar.append(container);

                addButton.jqxButton({cursor: "pointer", enableDefault: false, height: 25, width: 25});
                addButton.find('div:first').addClass('jqx-icon-plus');
                addButton.jqxTooltip({position: 'bottom', content: "添加"});
                addButton.jqxButton({disabled: false});

                deleteButton.jqxButton({
                    cursor: "pointer",
                    disabled: true,
                    enableDefault: false,
                    height: 25,
                    width: 25
                });

                deleteButton.find('div:first').addClass('jqx-icon-delete');
                deleteButton.jqxTooltip({position: 'bottom', content: "删除"});

                // 获取表格中选中行的数据
                var rowSelectData;

                $('#dataTable').on('rowselect', function (event) {
                    var args = event.args;
                    rowSelectData = args.row;
                    // 当没有选中状态的时候, 删除按钮不可用
                    if (rowSelectData.boundindex + 1) {
                        deleteButton.jqxButton({disabled: false});
                    } else {
                        deleteButton.jqxButton({disabled: true});
                    }
                });

                // 初始化窗口
                function initWindow() {
                    $(" #group_num").jqxDropDownList({selectedIndex: -1});
                    $("#group_members").jqxDropDownList('uncheckAll');
                    var selectIndex = $("#group_leader").jqxDropDownList('getSelectedIndex');
                    $("#group_leader").jqxDropDownList('unselectIndex', selectIndex);
                }

                // 创建窗口信息('add'新增, 'mod'修改)
                function createGroupWindow(status) {
                    if (status === 'add') {
                        // 新建窗口初始化
                        mod = "add";
                        $("#group_win_title").html("新增分组记录");
                        initWindow();

                    } else {
                        // 修改窗口初始化
                        mod = "mod";
                        $("#group_win_title").html("修改分组记录");

                        initWindow();

                        // 打开窗口后, 将信息回填

                    }
                    $("#groupWin").modal("show");
                }


                // 点击新增按钮就新增弹出新增窗口
                addButton.unbind('click').click(function () {
                    // 管理员不能对当前的面板进行操作
                    if (teacher["tea_no"] === '10000') {
                        $bs.error('请登录具体老师后重试 !');
                        return;
                    }
                    createGroupWindow('add');
                });

                deleteButton.unbind('click').click(function () {
                    if (deleteButton.jqxButton('disabled')) {
                        return;
                    }
                    $bs.confirm("确定删除该条记录吗 ?", function () {
                        $.post(
                            './deleteRowByIdAndNum.do',
                            {
                                id: $("#group_id").val(),
                                num: rowSelectData.group_num
                            },
                            function (rtn) {
                                if (rtn.status == 'success') {
                                    $bs.success(rtn.msg);
                                } else {
                                    $bs.error(rtn.msg);
                                }
                            }
                        );
                    })
                });

                // 修改窗口
                $('#dataTable').on('rowdoubleclick', function (event) {
                    createGroupWindow('mod');
                });

                $("#submit").unbind('click').click(function () {

                    console.log(teacher["tea_no"])

                    // 获取小组成员的学号, 按照数组的形式传递到后台
                    $.post(
                        "./saveOrUpdate.do",
                        {
                            jud: mod,
                            group_num: $groupNum.val(),
                            group_leader: $("#group_leader").val(),
                            group_member: $("#group_members").val(),
                            teaNo: teacher["tea_no"],
                            termId: term_Id,
                            courseId: $chooseCourse.val(),
                            classId: $chooseClass.val()
                        },
                        function (rtn) {
                            if (rtn.status === 'success') {
                                $bs.success(rtn.msg);
                            } else {
                                $bs.error(rtn.msg);
                            }
                            destroyGrid('dataTable');
                            search();
                        }
                    );

                    // 点击关闭按钮
                    $("#createSubmit").next().unbind('click').click();
                })
            },
        }).on("bindingcomplete", function () {
            $('#dataTable').jqxGrid('refresh');
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

