/**
 * Created by Fanpeng on 2017/2/20.
 */
$(function () {

    var class_info = {
        teacherName: "",
        className: "",
        showClass: true,
        showTea: true,
    };

    new Vue({
        el: "#group_class_info",
        data: class_info
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

    var query_flag = true;


    var searchData = null;

    var enCodeGroup = "";

    // 查询按钮
    $("#query_button").click(function () {


        searchData = {
            courseKey: getListSelectK("group_choose_class"),
            classPlace: getListSelectK("group_choose_class_place"),
            classWeek: getListSelectK("group_choose_class_week"),
            classTime: getListSelectK("group_choose_classTime")
        };
        enCodeGroup = new Date().format('yyyy') + searchData.courseKey + searchData.classPlace
            + searchData.classWeek + searchData.classTime.split(',').join("").trim();

        $.post(
            "./queryTeacherName.do",
            searchData,
            function (rtn) {
                var tea_name = rtn['tea_name'];
                if (tea_name) {
                    // 使用vue.js来改变教师姓名的展示
                    class_info.teacherName = tea_name;
                } else {
                    class_info.teacherName = '';
                    $bs.error('请选择正确的检索信息 !');
                }
            }
        );

        if (query_flag) {
            $("#dataTable").each(function () {
                $(this).jqxGrid("destroy");
            });

            // 不重新append的话, 会出现dataTable找不到的异常
            $("#dataTable-panel").append($("<div id='dataTable'></div>"));
            search(searchData, enCodeGroup);
        }
        // $bs.error("查无教师");
    });

    var search = function (searchData, enCodeGroup) {
        if (!query_flag) {
            return;
        }
        query_flag = false;

        // 数据源
        var source = {
            // 设置查询路径
            url: "./queryGroups.do",
            datatype: "json",
            // id: 'group_id', 此处注释是因为如果是同一个id的就去重了
            data: {
                id: enCodeGroup
            },
            datafields: [
                // 小组编号
                {name: 'group_id', type: 'String'},
                // 组号
                {name: 'group_num', type: 'String'},
                // 小组组长
                {name: 'stu_is_leader', type: 'String'},
                // 组员姓名
                {name: 'stu_is_member', type: 'String'},
                // 小组成绩
                {name: 'group_score', type: 'String'},
            ]
        };

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
                {text: '组号', dataField: 'group_num', align: "center", cellsAlign: 'center', width: "10%"},
                {text: '小组组长', dataField: 'stu_is_leader', align: "center", cellsAlign: 'center', width: "15%"},
                {text: '组员姓名', dataField: 'stu_is_member', align: "center", cellsAlign: 'center', width: "55%"},
                {text: '小组成绩', dataField: 'group_score', align: "center", cellsAlign: 'center', width: "20%"},
            ],
            rendered: function () {
                $(":text").jqxInput({theme: jqx_default_theme, width: '30%', height: "25px"});
                // 分组编号
                var groups = ['1', '2', '3', '4', '5', '6', '7', '8', '9', '10'];

                $("#group_num").jqxDropDownList({
                    placeHolder: "小组号",
                    source: groups,
                    theme: jqx_default_theme,
                    width: '150',
                    height: "25px",
                });

            },
            renderToolbar: function (toolBar) {
                var container = $("<div style='overflow: hidden; position: relative; height: 100%; width: 100%;'></div>");
                var buttonTemplate = "<div style='float: left; padding: 3px; margin: 2px;'><div style='margin: 4px; width: 16px; height: 16px;'></div></div>";
                var addButton = $(buttonTemplate);
                var deleteButton = $(buttonTemplate);
                container.append(addButton);
                container.append(deleteButton);
                container.append("<span style='padding: 3px; margin: 2px;line-height: 33px;'>&nbsp;&nbsp;&nbsp;提示:&nbsp;请双击一条记录进行修改</span>");

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

                function reDrawGrid() {
                    destroyGrid("dataTable");
                    search(searchData, enCodeGroup);
                }

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

                // 查询学生, 需要根据选择这门课程的学生来进行筛选
                $.get(
                    './queryStu.do',
                    function (rtn) {
                        $("#group_leader").jqxDropDownList({
                            placeHolder: "请选择:",
                            source: rtn,
                            width: '150',
                            height: '25',
                            theme: jqx_default_theme,
                            // autoDropDownHeight: true,
                            filterable: true,
                            filterPlaceHolder: "学生学号",
                            displayMember: 'value',
                            valueMember: 'key'
                        });

                        $("#group_member").jqxDropDownList({
                            placeHolder: "请选择:",
                            source: rtn,
                            selectedIndex: 0,
                            width: '250',
                            height: '25',
                            checkboxes: true,
                            theme: jqx_default_theme,
                            // autoDropDownHeight: true,
                            filterable: true,
                            filterPlaceHolder: "学生学号",
                            displayMember: 'value',
                            valueMember: 'key'
                        });
                    }
                )

                // 初始化窗口
                function initWindow() {
                    $(" #group_num").jqxDropDownList({selectedIndex: -1});

                    var selectIndex = $("#group_leader").jqxDropDownList('getSelectedIndex');
                    $("#group_leader").jqxDropDownList('unselectIndex', selectIndex);

                    $("#group_member").jqxDropDownList('uncheckAll');
                    $("#group_score").val("");
                }

                // 创建窗口信息('add'新增, 'mod'修改)
                function createGroupWindow(status) {
                    if (status == 'add') {
                        // 新建窗口初始化
                        $("#jud").val("").val("add");
                        $("#group_win_title").html("新增分组记录");
                        initWindow();

                    } else {
                        // 修改窗口初始化
                        $("#jud").val("").val("mod");
                        $("#group_win_title").html("修改分组记录");

                        // 分组号不允许修改
                        $(" #group_num").jqxDropDownList({disabled: true});

                        initWindow();

                        // 打开窗口后, 将信息回填
                        $.post(
                            './queryGroupInfoByIdAndNum.do',
                            {
                                id: $("#group_id").val(),
                                num: rowSelectData.group_num
                            },
                            function (rtn) {

                                //{"group_id":"20177050584021tue4","stu_is_leader":"14550407",
                                // "stu_is_member":"14550610","group_num":"2","group_score":"81"}
                                if (!rtn) {
                                    $bs.error("查询出错 !");
                                    return;
                                }

                                // 这边是存在问题的, 但是暂时先不做修改, 因为会影响到全局的下拉列表的问题
                                // display应为key, 目前采用的display是value
                                $("#group_num").val(rtn.group_num);
                                $("#group_score").val(rtn.group_score);
                            }
                        )
                    }
                    $("#createWin").modal("show");

                }


                // 点击新增按钮就新增弹出新增窗口
                addButton.unbind('click').click(function () {
                    if (!class_info.teacherName) {
                        $bs.error('请检索到正确课程信息后添加 !');
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
                                reDrawGrid();
                            }
                        );
                    })
                });

                // 修改窗口
                $('#dataTable').on('rowdoubleclick', function (event) {
                    createGroupWindow('mod');
                });

                $("#createSubmit").unbind('click').click(function () {

                    // 获取小组成员的学号, 按照数组的形式传递到后台
                    var $members = $("#group_member").jqxDropDownList('getCheckedItems');
                    var m = new Array();
                    for (var i = 0; i < $members.length; i++) {
                        m.push($members[i].originalItem.key)
                    }

                    var group = {
                        // 新增分组实验成绩记录只能是当年的, 所以采用new Date形式创建当前的时间
                        // 并且获取到年份
                        year: new Date().format('yyyy'),
                        jud: $("#jud").val(),
                        group_num: $("#group_num").val(),
                        group_leader: getListSelectK("group_leader"),
                        group_member: m.join(","),
                        group_score: $("#group_score").val(),
                        group_code: enCodeGroup
                    };


                    $.post(
                        "./saveorupdate.do",
                        group,
                        function (rtn) {
                            if (rtn.status != 'success') {
                                $bs.error(rtn.msg);
                                return;
                            }
                            $bs.success(rtn.msg);
                            reDrawGrid();
                        }
                    );

                    // 点击关闭按钮
                    $("#createSubmit").next().unbind('click').click();

                })
            },
        }).on("bindingcomplete", function () {
            $("#group_id").val(enCodeGroup);
            $('#dataTable').jqxGrid('refresh');
            query_flag = true;
        });

    };

    // 获取选中的item的index
    var class_key;

    // 生成课程列表
    $.post(
        "./queryGroupClassList.do",
        function (rtn) {
            $("#group_choose_class").jqxDropDownList({
                source: rtn,
                selectedIndex: 0,
                width: '150',
                height: '25',
                theme: jqx_default_theme,
                autoDropDownHeight: true,
                filterable: true,
                filterPlaceHolder: "课程名",
                displayMember: 'value',
                valueMember: 'key'
            });
            var key = getListSelectK("group_choose_class");
            createClassPlaceDropdownList(key);
            createClassWeekDropdownList(key);
        }
    );

    // 当课程切换的时候, 进行上课地点的切换
    $("#group_choose_class").on("change", function () {
        class_key = getListSelectK("group_choose_class");
        createClassPlaceDropdownList(class_key);
        createClassWeekDropdownList(class_key);
    });

    // 传入DropDownList的id选择器, 获取当前DropDownList的key
    function getListSelectK(id_selector) {
        var select_index = $("#" + id_selector).jqxDropDownList('getSelectedIndex');
        var selected = $("#" + id_selector).jqxDropDownList('getItem', select_index);
        return selected.originalItem.key;
    }


    // 创建上课地点下拉列表
    function createClassPlaceDropdownList(key) {
        $.post(
            "./queryClassPlaceByCourseKey.do",
            {key: key},
            function (rtn) {
                $("#group_choose_class_place").jqxDropDownList({
                    source: rtn,
                    selectedIndex: 0,
                    width: '150',
                    height: '25',
                    theme: jqx_default_theme,
                    autoDropDownHeight: true,
                    filterable: true,
                    filterPlaceHolder: "上课地点",
                    displayMember: 'value',
                    valueMember: 'key'
                });
            }
        )
    }

    // 创建上课周数下拉列表, 全部根据课程的转换来进行创造
    function createClassWeekDropdownList(key) {
        $.post(
            "./queryClassWeekByCourseKey.do",
            {key: key},
            function (rtn) {
                $("#group_choose_class_week").jqxDropDownList({
                    source: rtn,
                    selectedIndex: 0,
                    width: '100',
                    height: '25',
                    theme: jqx_default_theme,
                    autoDropDownHeight: true,
                    displayMember: 'value',
                    valueMember: 'key'
                });

                var week = getListSelectK("group_choose_class_week");
                createClassWeekAndTimeDropdownList(key, week);
            }
        )
    }


    // 创建上课日期时间段下拉列表
    // 传入key和week
    function createClassWeekAndTimeDropdownList(key, week) {
        $.post(
            "./queryClassTimeByCourseKeyAndWeek.do",
            {
                key: key,
                week: week
            },
            function (rtn) {
                $("#group_choose_classTime").jqxDropDownList({
                    source: rtn,
                    selectedIndex: 0,
                    width: '130',
                    height: '25',
                    theme: jqx_default_theme,
                    autoDropDownHeight: true,
                    displayMember: 'value',
                    valueMember: 'key'
                });
            }
        )
    }


    $("#group_choose_class_week").on("change", function () {
        class_key = getListSelectK("group_choose_class");
        var week = getListSelectK("group_choose_class_week");

        createClassWeekAndTimeDropdownList(class_key, week);
    });

    search();
})