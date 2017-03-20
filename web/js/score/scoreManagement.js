/**
 * Created by VideoMonster on 2017/3/20.
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

    var theme = jqx_default_theme;


    var search = function () {

        // renderer for grid cells.
        var numberrenderer = function (row, column, value) {
            return '<div style="text-align: center; margin-top: 5px;">' + (1 + value) + '</div>';
        }
        // create Grid datafields and columns arrays.
        var datafields = [];

        var columns = [];
        for (var i = 0; i < 35; i++) {
            var text = String.fromCharCode(65 + i);
            if (i == 0) {
                var cssclass = 'jqx-widget-header';
                if (theme != '') cssclass += ' jqx-widget-header-' + theme;
                columns[columns.length] = {
                    pinned: true,
                    exportable: false,
                    text: "",
                    columntype: 'number',
                    cellclassname: cssclass,
                    cellsrenderer: numberrenderer
                };
            }
            datafields[datafields.length] = {name: text};
            columns[columns.length] = {text: text, datafield: text, width: 60, align: 'center'};
        }
        var source = {
            unboundmode: true,
            totalrecords: 100,
            datafields: datafields,
            updaterow: function (rowid, rowdata) {
                // synchronize with the server - send update command
            }
        };

        var dataAdapter = new $.jqx.dataAdapter(source);

        // initialize jqxGrid
        $("#dataTable").jqxGrid({
            width: "100%",
            height: jqxUtil.getSearchGridHeight(),
            source: dataAdapter,
            editable: true,
            theme: jqx_default_theme,
            columnsresize: true,
            selectionmode: 'multiplecellsadvanced',
            columns: columns
        });

    };

    search();

    $("#excelExport").jqxButton({theme: theme});
    $("#excelExport").click(function () {
        $("#dataTable").jqxGrid('exportdata', 'xls', '学生成绩单', false);
    });


})