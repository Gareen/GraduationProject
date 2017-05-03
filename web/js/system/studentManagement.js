$(function () {

    // 查询状态
    var query_flag = true;

    var teacher = $getTea();

    var $table = $("#dataTable");

    $(window).on("resize", function () {
        $table.jqxGrid({
            height: jqxUtil.getGridHeight(30, 30, 30)
        });
    });

    sidebar.callback = function () {
        $table.jqxGrid('render');
    };


    var search = function () {

        // 行选中数据
        let rowSelectData;

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

        $table.jqxGrid({
            width: "100%",
            height: jqxUtil.getGridHeight(30, 30, 30),
            source: dataAdapter,
            theme: jqx_default_theme,
            altrows: true,
            groupable: true,
            showgroupsheader: false,
            filterable: true,
            showfilterrow: true,
            columns: [
                {text: '学号', dataField: 'stu_no', align: "center", cellsAlign: 'center', width: "25%"},
                {text: '姓名', dataField: 'stu_name', align: "center", cellsAlign: 'center', width: "30%"},
                {text: '性别', dataField: 'stu_gender', align: "center", cellsAlign: 'center', width: "10%"},
                {text: '班级', dataField: 'stu_class_id', align: "center", cellsAlign: 'center', width: "35%"}
            ],
            groups: ['stu_class_id']

        }).on("bindingcomplete", function () {
            $table.jqxGrid('expandallgroups');
            query_flag = true;

            // 行选中事件
            $table.on('rowselect', function (event) {
                var args = event.args;
                rowSelectData = args.row;
                // {stu_no: "10002", stu_name: "test2", stu_gender: "女", stu_class_id: "测试班级", uid: "10002"}
                if (rowSelectData) {
                    $("#edit").prop('disabled', false);
                    $("#delete").prop('disabled', false);
                }
            });



        });

    };

    search();

    // 导出学生信息模版
    $("#export-stuTemp").click(function () {
        excelExport("export.do");
    });

    // 学生名册上传
    let $stuUpload = $("#import-students");
    let $uploadTool = $("#stu-upload");
    // 文件上传请求路径
    let uploadUrl = "./importStudents.do";

    $("#import_stu").click(function () {
        // todo 完成后需要修改成管理员进行添加
        if (teacher["tea_no"] === '10000') {
            $bs.error("您无权导入学生信息，请联系管理员 ！");
            return;
        }
        initUpload();
        $stuUpload.modal("show");
    });

    // 上传初始化
    function initUpload() {
        // 先对队列中全部的待上传进行清空
        $uploadTool.jqxFileUpload('cancelAll');

        $uploadTool.jqxFileUpload({
            theme: jqx_default_theme,
            height: 150,
            uploadUrl: uploadUrl,
            fileInputName: 'studentsInfo',
            localization: {
                browseButton: '浏览文件',
                uploadButton: '全部上传',
                cancelButton: '全部取消',
                uploadFileTooltip: '点击上传',
                cancelFileTooltip: '点击取消'
            },
            multipleFilesUpload: true,
            uploadTemplate: 'warning',
            // 默认只找寻excel文件
            accept: ['.xlsx', '.xls']
        });

    }

    /*$uploadTool.on('uploadEnd', function (event) {
        var args = event.args;
        var serverResponce = args.response;
     let rtn = JSON.parse($(serverResponce).text());

     /!*if (rtn && rtn['status'] === 'success') {
     $('#dataTable').jqxGrid('render');
     }*!/
     console.log(111)
     });*/

});
