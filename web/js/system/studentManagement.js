$(function () {

    // 查询状态
    var query_flag = true;

    var teacher = $getTea();

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
            $('#dataTable').jqxGrid('expandallgroups');
            query_flag = true;
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

    $uploadTool.on('uploadEnd', function (event) {
        var args = event.args;
        var fileName = args.file;
        var serverResponce = args.response;
        console.log(event);
        // todo code
        console.log("upload end");
    });
});
