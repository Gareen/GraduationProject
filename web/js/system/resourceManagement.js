$(function () {

    var rowk = null;

    var search = function () {
        $(window).on("resize",function (){
            $('#dataTable').jqxTreeGrid({
                height : jqxUtil.getGridHeight(30,30,30)
            });
        });

        sidebar.callback = function (){
            $('#dataTable').jqxTreeGrid('render');

        };

        var source = {
            dataType: "json",
            dataFields: [
                {name: 'id', type: 'string'},
                {name: 'title', type: 'string'},
                {name: 'path', type: 'string'},
                {name: 'icon', type: 'string'},
                {name: 'level', type: 'string'},
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


        var getSelectionId = function (){
            var g = $("#dataTable");
            var selection = g.jqxTreeGrid('getSelection');
            for (var i = 0; i < selection.length; i++) {
                return selection[i]['id'];
            }
            return null;
        };

        $("#dataTable").jqxTreeGrid({
            theme : jqx_default_theme,
            width:"100%",
            source : dataAdapter,
            height :  jqxUtil.getGridHeight(30,30,30),
            selectionMode: "singleRow",
            filterable: true,
            filterMode: 'simple',
            columns : [
                {text: '资源名称',     dataField: 'title',           align: "center",      cellsAlign: 'left',       width: "30%"},
                {text: '路径',      dataField: 'path',            align: "center",      cellsAlign: 'center',     width: "40%"},
                {text: '图标',       dataField: 'icon',            align: "center",      cellsAlign: 'center',     width: "15%"},
                {text: '资源等级',       dataField: 'level',            align: "center",      cellsAlign: 'center',     width: "15%"},
            ],

            rendertoolbar: function () {
               $("#filterdataTable").children().eq(0).html(" 搜索: ");
            },

            ready: function (){

                $("#dataTable").jqxTreeGrid("expandRow", 1);
                var rows = $("#dataTable").jqxTreeGrid('getRows');
                var traverseTree = function(rows) {
                    for(var i = 0; i < rows.length; i++) {
                        $("#dataTable").jqxTreeGrid("expandRow",rows[i].id);
                        if (rows[i].records) {
                            traverseTree(rows[i].records);
                        }
                    }
                };


                $("#add").on("click",function (){
                    rowk = getSelectionId();
                    console.log(rowk);
                    if(rowk == null ){
                        $bs.alert("请选择新增资源的上级节点");
                        return;
                    }

                    openModifywin("add");
                });


                $("#edit").on("click",function (){
                    rowk = getSelectionId();
                    if(rowk == null){
                        $bs.alert("请选择需要修改的资源");
                        return;
                    }
                    if(rowk == 1){
                        $bs.alert("无法修改根目录");
                        return;
                    }
                    openModifywin("edit");
                });



                $("#modifysubmit").on("click",function (){
                    $.post(
                        "./saveOrUpdate",
                        baseUtil.getFormValuesById("form"),
                        function (rtn){
                           /* if(Chk.isNumber(Number(rtn))){
                                //更新或新增行数据在 列表中
                                $.get("../../api/resource/" + rtn,function (res){
                                    if(res && res.info.success){
                                        var g = $("#dataTable");
                                        var data = res.data;
                                        var id = data.id;
                                        if(g.jqxTreeGrid("getRow",id)){
                                            g.jqxTreeGrid('updateRow', id,data);
                                        }else{
                                            g.jqxTreeGrid('addRow', id ,data,'last',rowk);
                                        }
                                    }

                                    $("#modifyWin").modal("hide");

                                    $bs.success("操作成功!");
                                })

                            }else{
                                $bs.error(rtn);
                            }*/

                        }
                    );
                });

                var openModifywin = function(mode){
                    /*$.get("./queryResById?" + rowk ,function (rtn){

                        $(".form .jqxInput").each(function (){
                            $(this).val("");
                        });

                        if(rtn && rtn.info.success){
                            if(mode == "add"){
                                $("#modifyWin .modal-title").text("新增");
                                $("#id").val("");
                                $("#pid").val(rowk);
                                $("#pname").html($("#dataTable").jqxTreeGrid('getCellValue', rowk, 'name'));

                            }else if(mode == "edit"){
                                $("#modifyWin .modal-title").text("修改");
                                $("#id").val(rowk);
                                $("#pid").val("");
                                $("#pname").html($("#dataTable").jqxTreeGrid('getRow', rowk).parent.name);
                                baseUtil.bindByName(rtn.data, $(".form"));
                            }
                            $("#modifyWin").modal("show");
                        }else{
                            $bs.error("该数据可能已被修改或删除,请重新查询");
                        }
                    });*/
                    $("#modifyWin").modal("show");
                };


                $("#delete").on("click",function (){
                    rowk = getSelectionId();
                    if(rowk == null){
                        $bs.alert("请选择需要删除的目录");
                        return;
                    }
                    if(rowk == Global.resourceRootId){
                        $bs.alert("无法删除根目录");
                        return;
                    }

                    $bs.confirm("是否删除该目录?",function (){
                        $.post(
                            "./delete?resourceId=" + rowk,
                            function (rtn){
                                if(rtn == "success"){
                                    $("#dataTable").jqxTreeGrid('deleteRow', rowk);
                                    $bs.success("删除成功!");
                                }else{
                                    $bs.error(rtn);
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
