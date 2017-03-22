$(function () {

    let tip = {
        tipText: "",
        showTip: false
    };

    new Vue({
        el: "#vue-ctl",
        data: tip
    });

    var rowk = null;

    var search = function () {
        $(window).on("resize", function () {
            $('#dataTable').jqxTreeGrid({
                height: jqxUtil.getGridHeight(30, 30, 30)
            });
        });

        sidebar.callback = function () {
            $('#dataTable').jqxTreeGrid('render');

        };

        var source = {
            dataType: "json",
            dataFields: [
                {name: 'id', type: 'string'},
                {name: 'title', type: 'string'},
                {name: 'path', type: 'string'},
                {name: 'icon', type: 'string'},
                {name: 'plevel', type: 'string'},
                {name: 'pid', type: 'string'}
            ],
            hierarchy: {
                keyDataField: {name: 'id'},
                parentDataField: {name: 'pid'}
            },
            id: 'id',
            url: "./queryResources.do"
        };

        var dataAdapter = new $.jqx.dataAdapter(source);


        var getSelectionId = function () {
            var g = $("#dataTable");
            var selection = g.jqxTreeGrid('getSelection');
            for (var i = 0; i < selection.length; i++) {
                return selection[i]['id'];
            }
            return null;
        };


        $("#dataTable").jqxTreeGrid({
            theme: jqx_default_theme,
            width: "100%",
            source: dataAdapter,
            height: jqxUtil.getGridHeight(30, 30, 30),
            selectionMode: "singleRow",
            filterable: true,
            filterMode: 'simple',
            columns: [
                {text: '资源名称', dataField: 'title', align: "center", cellsAlign: 'left', width: "20%"},
                {text: '编号', dataField: 'id', align: "center", cellsAlign: 'center', width: "10%"},
                {text: '路径', dataField: 'path', align: "center", cellsAlign: 'center', width: "40%"},
                {text: '图标', dataField: 'icon', align: "center", cellsAlign: 'center', width: "15%"},
                {text: '资源等级', dataField: 'plevel', align: "center", cellsAlign: 'center', width: "15%"},
            ],

            rendertoolbar: function () {
                // 修改搜索框label标识
                $("#filterdataTable").children().eq(0).html(" 搜索: ");
            },

            ready: function () {

                // 选中行的数据
                var rowData;
                var mode;

                $("#createWin :input").jqxInput({theme: jqx_default_theme, height: "25px"});
                $("#nodeNo").jqxInput({width: "10%"});
                $("#nodeName").jqxInput({width: "30%"});
                $("#nodePath").jqxInput({width: "70%"});
                $("#icon").jqxInput({width: "20%"});
                $("#level").jqxDropDownList({
                    theme: jqx_default_theme,
                    width: "20%",
                    height: '25',
                    autoDropDownHeight: true
                });

                $("#dataTable").jqxTreeGrid("expandRow", 1);
                var rows = $("#dataTable").jqxTreeGrid('getRows');

                var traverseTree = function (rows) {
                    for (var i = 0; i < rows.length; i++) {
                        $("#dataTable").jqxTreeGrid("expandRow", rows[i].id);
                        if (rows[i].records) {
                            traverseTree(rows[i].records);
                        }
                    }
                };


                // 当资源目录为根目录的时候, 不可以修改 删除, 但可以新增, 而其他的节点不可以新增, 可以修改和删除
                $('#dataTable').on('rowSelect', function (event) {
                    // event args.
                    var args = event.args;
                    // row data.
                    rowData = args.row;

                    if (getSelectionId() == 100 || getSelectionId() == 200) {
                        $('#edit, #delete').prop('disabled', true);
                        $('#add').prop('disabled', false);
                    } else {
                        $('#edit, #delete').prop('disabled', false);
                        $('#add').prop('disabled', true);

                    }
                });

                $("#add").on("click", function () {
                    rowk = getSelectionId();
                    if (rowk == null) {
                        $bs.alert("请选择新增资源的根节点");
                        return;
                    }
                    if (rowData.id == 100) {
                        tip.tipText = "(输入格式: 100, 101...)"
                    }
                    if (rowData.id == 200) {
                        tip.tipText = "(输入格式: 200, 201...)"
                    }
                    tip.showTip = true;
                    $("#nodeNo").jqxInput({disabled: false});
                    $("#nodeNo").val("");
                    $("#nodeName").val("");
                    $("#nodePath").val("");
                    $("#icon").val("");
                    $("#level").val("");
                    $("#pid").val("");
                    openModifywin("add");
                });


                $("#edit").on("click", function () {
                    rowk = getSelectionId();
                    if (rowk == null) {
                        $bs.alert("请选择需要修改的资源");
                        return;
                    }
                    if (rowk == 1) {
                        $bs.alert("无法修改根目录");
                        return;
                    }
                    $("#nodeNo").jqxInput({disabled: true});

                    tip.showTip = false;
                    $.post(
                        "./queryNodeById.do",
                        {
                            id: rowData.id
                        },
                        function (rtn) {
                            if ("success" == rtn.status) {
                                var data = rtn['data'];
                                $("#nodeNo").val(data.id);
                                $("#nodeName").val(data.title);
                                $("#nodePath").val(data.path);
                                $("#icon").val(data.icon);
                                $("#level").val(data.plevel);
                                $("#pid").val(data.pid);
                            } else {
                                $bs.error(rtn.msg);
                                return;
                            }
                            openModifywin("mod");
                        }
                    )
                });


                $("#submit").unbind('click').on("click", function () {

                    $.post(
                        "./saveOrUpdate.do?mode=" + mode,
                        {
                            id: $("#nodeNo").val(),
                            title: $("#nodeName").val(),
                            path: $("#nodePath").val(),
                            icon: $("#icon").val(),
                            plevel: $("#level").val("1"),
                            pid: $("#pid").val(),
                        },
                        function (rtn) {
                            // 点击关闭按钮
                            $("#submit").next().unbind('click').click();

                            if (rtn.status == "success") {
                                $bs.success(rtn.msg);
                            } else {
                                $bs.error(rtn.msg);
                            }

                            destroyGrid("dataTable");
                            search();
                        }
                    );
                });

                var openModifywin = function (m) {

                    mode = m;

                    if ('add' == mode) {

                        if (rowData.id != 100 && rowData.id != 200) {
                            $bs.error("请选择根资源进行新增操作 !");
                            return;
                        }
                        // 新增
                        $("#win_title").html("新增资源节点");
                        $("#parentNodeName").html("").html(rowData.title);
                        $("#pid").val(rowData.id);
                        $("#jud").val("add");

                    } else {

                        // 修改
                        $("#win_title").html("修改资源节点");
                        $("#pid").val(rowData.pid);
                        $("#jud").val("mod");

                        if (rowData.id == 100 || rowData.id == 200) {
                            $bs.error("请选择非根资源进行修改操作 !");
                            return;
                        }
                        $.post(
                            './queryParentName.do',
                            {
                                id: rowData.pid
                            },
                            function (rtn) {
                                $("#parentNodeName").html("").html(rtn);
                            }
                        )

                    }

                    $("#createWin").modal("show");
                };


                $("#delete").on("click", function () {
                    rowk = getSelectionId();
                    if (rowk == null) {
                        $bs.alert("请选择需要删除的资源");
                        return;
                    }
                    if (rowk == 100 || rowk == 200) {
                        $bs.alert("无法删除根路径");
                        return;
                    }

                    $bs.confirm("是否删除该资源?", function () {
                        $.post(
                            "./delete.do",
                            {
                                id: rowData.id
                            },
                            function (rtn) {
                                if (rtn.status == "success") {
                                    $("#dataTable").jqxTreeGrid('deleteRow', rowk);
                                    $bs.success(rtn.msg);
                                } else {
                                    $bs.error(rtn.msg);
                                }
                            }
                        );
                    });

                });

                traverseTree(rows);
            }


        });
    }

    search()
})
