$(function () {

    // 查询状态
    var query_flag = true;

    // 页面调整大小后自动适配
    $(window).on("resize", function () {
        $('#dataTable').jqxGrid({
            height: jqxUtil.getGridHeight(30, 30, 30)
        });
    });

    sidebar.callback = function () {
        $('#dataTable').jqxGrid("render");
    };

    // 此处的search方法是预留
    var search = function () {

        // 此处是为了多次刷新准备的
        if (!query_flag) {
            return;
        }

        query_flag = false;

        // 设置数据源
        var source = {
            url: "./queryCourses.do",
            datatype: "json",
            type: "post",
            datafields: [
                {name: 'couId', type: 'String'},
                {name: 'couName', type: 'String'},
                {name: 'teaName', type: 'String'},
                {name: 'couTime', type: 'String'},
            ],
        };

        var dataAdapter = new $.jqx.dataAdapter(source);

        $("#dataTable").jqxGrid({
            width: "100%",
            height: jqxUtil.getGridHeight(30, 30, 30),
            source: dataAdapter,
            theme: jqx_default_theme,
            altrows: true,
            filterable: true,
            showfilterrow: true,
            columns: [
                {text: '课程编号', dataField: 'couId', align: "center", cellsAlign: 'center', width: "10%"},
                {text: '课程名', dataField: 'couName', align: "center", cellsAlign: 'center', width: "20%"},
                {text: '上课教师', dataField: 'teaName', align: "center", cellsAlign: 'center', width: "10%"},
                {text: '上课时间', dataField: 'couTime', align: "center", cellsAlign: 'center', width: "60%"},
            ],
        }).on("bindingcomplete", function () {
            console.log('bindingcomplete');
            query_flag = true;
        });
    };

    search();
})