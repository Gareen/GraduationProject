/**
 * Created by Fanpeng on 2017/2/6.
 */
$(function () {

    // 查询状态
    var query_flag = true;

    var $table = $("#dataTable");

    var teacher = $getTea();

    // 页面调整大小后自动适配
    $(window).on("resize", function (){
        $table.jqxGrid({
            height : jqxUtil.getGridHeight(30,30,30)
        });
    });

    sidebar.callback = function (){
        $table.jqxGrid("render");
    };

    // 此处的search方法是预留
    var search = function() {

        // 此处是为了多次刷新准备的
        if(!query_flag){
            return;
        }
        query_flag = false;

        var rowSelectData;

        // 设置数据源
        var source = {
            url: "./queryTerms.do",
            datatype: "json",
            type: "post",
            id: 'term_id',
            datafields: [
                {name: 'term_id', type: 'String'},
                {name: 'term_name', type: 'String'},
            ],
        };

        var dataAdapter = new $.jqx.dataAdapter(source);

        $table.jqxGrid({
            width: "100%",
            height :  jqxUtil.getGridHeight(30,30,30),
            source : dataAdapter,
            theme:jqx_default_theme,
            altrows: true,
            filterable: true,
            showfilterrow: true,
            columns : [
                {text: '学期编号', 	    dataField: 'term_id', 			align: "center", 		cellsAlign: 'center', 		width: "50%"},
                {text: '学期', 		dataField: 'term_name', 		align: "center", 		cellsAlign: 'center', 		width: "50%"},
            ],
            ready: function () {

                var $btn_add = $("#add");
                var $btn_edit = $("#edit");
                var $btn_del = $("#delete");

                var $termId = $("#termId");
                var $termName = $("#termName");
                var $termYear = $("#termYear");
                var $termMon = $("#termMon");
                var $title = $("#win_title");

                var $tip = $("#modtip");
                // 新增还是修改
                var optmode;

                // 行选中事件
                $table.on('rowselect', function (event) {
                    let args = event.args;
                    rowSelectData = args.row;
                    if (rowSelectData) {
                        $btn_edit.prop('disabled', false);
                        $btn_del.prop('disabled', false);
                    }
                });

                $btn_add.click(function () {
                    $tip.hide();
                    openWin('add');
                });

                $btn_edit.click(function () {
                    $tip.show();
                    openWin('mod');
                });

                $btn_del.click(function () {

                    if ('1' !== teacher["tea_permission"]) {
                        $bs.error('权限不足，请联系管理员！');
                        return;
                    }

                    $bs.confirm("确认删除该学期么？", function () {
                        $.post(
                            './deleteTermByTermId.do',
                            {
                                termId: rowSelectData['term_id']
                            },
                            function (rtn) {
                                if (rtn && rtn.status === 'success') {
                                    $bs.success(rtn.msg);
                                    $table.jqxGrid('deleterow', rtn.data);
                                } else {
                                    $bs.error(rtn.msg);
                                }
                            }
                        )
                    })
                });

                function openWin(opt) {
                    optmode = opt;

                    $termId.jqxNumberInput({
                        theme: jqx_default_theme,
                        digits: 3,
                        decimalDigits: 0,
                        width: 35,
                        min: 0,
                        max: 999
                    });
                    $termName.jqxInput({theme: jqx_default_theme, height: "25px"});
                    $termYear.jqxNumberInput({
                        theme: jqx_default_theme,
                        digits: 4,
                        decimalDigits: 0,
                        width: 55,
                        min: 1900,
                        max: 3000
                    });
                    $termMon.jqxDropDownList({
                        theme: jqx_default_theme,
                        selectedIndex: 0,
                        height: '25',
                        width: 60,
                        autoDropDownHeight: true
                    });

                    if ('1' !== teacher["tea_permission"]) {
                        $bs.error('权限不足，请联系管理员！');
                        return;
                    }

                    if ('add' === opt) {
                        $title.text('新增学期');
                        $termId.jqxNumberInput({disabled: false});

                        $termId.val('');
                        $termName.val('');
                        // 默认为当前年份
                        $termYear.jqxNumberInput('setDecimal', new Number(new Date().getFullYear()));
                        $termMon.jqxDropDownList({selectedIndex: 0});
                        $("#createWin").modal('show');

                    } else {
                        $title.text('修改学期');
                        $termId.jqxNumberInput({disabled: true});

                        $.post(
                            './queryTermByTermId.do',
                            {
                                termId: rowSelectData['term_id']
                            },
                            function (rtn) {

                                var data = rtn.data;
                                if (rtn && rtn["status"] === 'success') {

                                    $termId.jqxNumberInput('setDecimal', new Number(data['term_id']));
                                    $termName.val(data['term_name']);
                                    $termYear.jqxNumberInput('setDecimal', new Number(data['term_year']));
                                    $termMon.val(data['term_month']);
                                    $("#createWin").modal('show');
                                } else {
                                    $bs.error(rtn.msg);
                                }
                            }
                        )
                    }


                }

                $('#submit').unbind('click').click(function () {
                    var postData = {};
                    postData.optMode = optmode;
                    postData.termId = $termId.jqxNumberInput('getDecimal');
                    postData.termName = $termName.val();
                    postData.termYear = $termYear.jqxNumberInput('getDecimal');
                    postData.termMon = $termMon.val();
                    postData.tea = teacher['tea_permission'];

                    $.post(
                        './saveOrUpdate.do',
                        {
                            data: JSON.stringify(postData)
                        },
                        function (rtn) {

                            if (rtn.status === 'success') {
                                $bs.success(rtn.msg);
                            } else {
                                $bs.error(rtn.msg);
                            }
                        }
                    );
                    // 点击关闭按钮
                    $("#submit").next().unbind('click').click();
                })
            }
        }).on("bindingcomplete", function () {
            query_flag = true;
        });
    };

    search();
});