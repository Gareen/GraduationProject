$(function () {

    // 查询状态
    var query_flag = true;

    $(window).on("resize", function () {
        $('#dataTable').jqxGrid({
            height: jqxUtil.getGridHeight(30, 30, 30)
        });
    });

    sidebar.callback = function () {
        $('#dataTable').jqxGrid('render');
    };


    var search = function () {


        if (!query_flag) {
            return;
        }
        query_flag = false;

        var source = {
            dataType: "json",
            dataFields: [
                {name: 'stu_no', type: 'string'},
                {name: 'stu_name', type: 'string'},
                {name: 'stu_gender', type: 'string'},
                {name: 'stu_class_id', type: 'string'},
            ],
            id: 'stu_no',
            url: "./queryStudents.do"
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
                {text: '学号', dataField: 'stu_no', align: "center", cellsAlign: 'center', width: "25%"},
                {text: '姓名', dataField: 'stu_name', align: "center", cellsAlign: 'center', width: "30%"},
                {text: '性别', dataField: 'stu_gender', align: "center", cellsAlign: 'center', width: "10%"},
                {text: '班级', dataField: 'stu_class_id', align: "center", cellsAlign: 'center', width: "35%"}
            ],
        }).on("bindingcomplete", function () {
            query_flag = true;
        });

    };
    search();
})
