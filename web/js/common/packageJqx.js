var jqx_default_theme = "bootstrap";

var jqxUtil = {

    getGridHeight : function (rowsheight,columnsheight,filterheight){
        var h =  ( $(document).height() - $("#view-top").height() - $("div.row.rowtop").height() - $("div.grid-toolbar").height() ) * 0.95;
        h -= columnsheight;
        var c = 0;
        while(true){
            if(c > h){
                h = c;
                break;
            }
            c += rowsheight ;
        }
        h = parseInt(( h - filterheight - columnsheight * 2 ) / rowsheight ) * rowsheight + filterheight + columnsheight * 2;
        return h;
    },

    // 带搜索面板的gird的自适应高度
    getSearchGridHeight : function (){
        var h =   $(document).height() - 240;
        return parseInt(h) * 0.95;
    },

    // 销毁Grid
    destroyGrid : function (gridId,css){
        var g = $("#" + gridId);
        var parent = g.parent();
        g.jqxGrid('clearfilters');
        g.jqxGrid("destroy");
        g = $("<div id=" + gridId + "></div>");
        parent.append(g);
        if(css){
            g.css(css);
        }



    },
    passwordStrength : function (password, characters, defaultStrength) {
        var length = password.length;
        var letters = characters.letters;
        var numbers = characters.numbers;
        var specialKeys = characters.specialKeys;
        var strengthCoefficient = letters + numbers + 8 * specialKeys + letters * numbers / 2 + length;
        var strengthValue;
        var color;
        if (length < 6) {
            strengthValue = "密码过短";
            color = "#FF0000";
        } else if (strengthCoefficient < 20) {
            strengthValue = "弱";
            color = "#CE0000";
        } else if (strengthCoefficient < 30) {
            strengthValue = "中";
            color = "#D9B300";
        } else if (strengthCoefficient < 40) {
            strengthValue = "强";
            color = "#005AB5";
        } else {
            strengthValue = "安全";
            color = "#00A600";
        }

        return "<div style='font-family:Microsoft YaHei;font-style: italic; font-weight: bold; color: " + color + ";'>" + strengthValue + "</div>";
    },
    grid : function (){

    }

}

var jqx = {
    loader :{
        object : null,
        open : function (){
            if(this.object == null){
                var l = $("<div id='jqx-loader-element'></div>");
                $("body").append(l);
                this.object = l;
            }

            this.object.jqxLoader("open");
        } ,
        close : function (){
            this.object.jqxLoader("close");
        }
    },


    createDataAdapter : function (source,ext){
        var dataAdapter = new $.jqx.dataAdapter(source, $.extend(
            {
//		自定义load方法 ,在这种情况下可返回自定义对象等 进行各种操作
                loadServerData: function (data,source,callback){
                    $.ajax({
                        url : source.url,
                        type : source.type ? source.type : "GET",
                        data : data,
                        dataType : source.datatype,
                        success : function (rtn){
                            alert(JSON.stringify(rtn))
                            if(rtn){
                                if(Chk.isArray(rtn)){
                                    callback({records:rtn});
                                }else if(Chk.isObject(rtn)) {
                                    if(rtn.records){
                                        callback({records: rtn.records, totalCount: rtn.totalCount});
                                    }else{
                                        //兼容陆帅峰框架的返回值
                                        callback({records: rtn.data, totalCount: rtn.totalCount});
                                    }
                                }else{
                                    callback({records : []});
                                }
                            }else{
                                callback({records : []});
                            }
//					注意 data 中必须有 records 属性、否则需要用以下方式调用参数
                        }
                    });
                },
                //		该方法在请求数据前触发,
                //		formatData: function (data) {
                //			return data;
                //		},
                //		},
                //     该方法在数据传输完成后触发但在使用自定义方法请求数据时无效、故不使用
                //        downloadComplete: function (data, status, xhr) {
                //        	source.totalRecords = data.totalCount;
                //        },
                //     该方法在load完成后触发、触发时间晚于downloadComplete
                //      用于设置totalCount;
                loadComplete: function (data, status, xhr) {
                    source.totalRecords = data.totalCount;
                }
            },
            ext ? ext : {}
        ));
        return dataAdapter;
    },

    widgets : {
        jqxButton: {
            dom: "button",
            props: {
                disabled: {
                    type: "boolean", value: false
                },
                width: {
                    type: "string", value: "100px"
                },
                height: {
                    type: "string", value: "32px"
                },
                textPosition: {
                    type: "string",
                    value: "",
                    possibleValues: ["", "left", "top", "center", "bottom", "right", "topLeft", "bottomLeft", "topRight", "bottomRight"]
                },
                theme: {
                    type: "string", value: jqx_default_theme
                },
                template: {
                    type: "string",
                    value: "default",
                    possibleValues: ["default", "primary", "success", "warning", "danger", "inverse", "info", "link"]
                }
            },
            events: {
                click: true
            }
        },
//--------------------------------------------------------------------

        jqxToggleButton: $.extend(
            {
                dom : "button",
                props: {
                    toggled: {type: "boolean", value: false}
                }
            },
            this.jqxButton
        ),
//--------------------------------------------------------------------

        jqxSwitchButton: {
            dom: "button",
            props: {
                disabled: {
                    type: "boolean", value: false
                },
                checked: {
                    type: "boolean", value: false
                },
                width: {
                    type: "string", value: "100px"
                },
                height: {
                    type: "string", value: "32px"
                },
                orientation: {
                    type: "string", value: "horizontal", possibleValues: ["horizontal", "vertical"]
                },
                onLabel: {
                    type: "string", value: "On"
                },
                offLabel: {
                    type: "string", value: "Off"
                },
                thumbSize: {
                    type: "string", value: "40%"
                },
                theme: {
                    type: "string", value: jqx_default_theme
                }
            },
            events: {
                checked: true,
                unchecked: true,
                change: true
            }
        },
//--------------------------------------------------------------------
        jqxLinkButton: $.extend(
            {
                dom: "a",
                event: {
                    click: true
                }
            },
            this.jqxButton
        ),
//--------------------------------------------------------------------

        jqxCheckBox: {
            dom: "div",
            props: {
                boxSize: {
                    type: "string", value: "15px"
                },
                disabled: {
                    type: "boolean", value: false
                },
                checked: {
                    type: "boolean", value: false
                },
                groupName: {
                    type: "boolean", value: false
                },
                height: {
                    type: "string", value: null
                },
                hasThreeStates: {
                    type: "boolean", value: false
                },
                theme: {
                    type: "string", value : jqx_default_theme
                }
            },
            events: {
                checked: true,
                change: true,
                indeterminate: true,
                unchecked: true
            }
        },

//--------------------------------------------------------------------
        jqxDropDownList : {
            dom : "div",
            props : {
                autoOpen: {
                    type: "boolean", value: false
                },
                autoItemsHeight: {
                    type: "boolean", value: false
                },
                autoDropDownHeight: {
                    type: "boolean", value: false
                },
                checkboxes: {
                    type: "boolean", value: false
                },
                animationType: {
                    type: "string", value: "none", possibleValues:  ["fade", "slide","none"]
                },
                displayMember: {
                    type: "string", value: ""
                },
                valueMember: {
                    type: "string", value: ""
                },
                dropDownHeight: {
                    type: "number", value: 200
                },
                dropDownWidth: {
                    type: "number", value: 200
                },
                filterable : {
                    type: "boolean", value: false
                },
                filterPlaceHolder: {
                    type: "string", value: ""
                },
                placeHolder : {
                    type: "string", value: ""
                },
                width : {
                    type: "string", value: null
                },
                height : {
                    type: "string", value: "32px"
                },
                itemHeight : {
                    type: "number", value: -1
                },
                popupZIndex : {
                    type: "number", value: 20000
                },
                renderer: {
                    type: "function", value: null
                },
                searchMode : {
                    type: "string", value: "containsignorecase", possibleValues: ["none", "contains", "containsignorecase", "equals", "equalsignorecase", "startswithignorecase", "startswith", "endswithignorecase", "endswith"]
                },
                selectedIndex : {
                    type: "number", value: -1,afterBinding : true
                },
                source : {
                    type : "source" , value : null
                },
                theme: {
                    type: "string", value: jqx_default_theme
                }
            },
            events: {
                bindingComplete: true,
                close: true,
                checkChange: true,
                change: true,
                open: true,
                select: true,
                unselect: true
            }
        },

        //--------------------------------------------------------------------

        jqxInput : {
            dom : "input[type=text]",
            props : {
                displayMember: {
                    type: "string", value: ""
                },
                displayMember: {
                    type: "string", value: ""
                },
                valueMember: {
                    type: "string", value: ""
                },
                dropDownWidth: {
                    type: "string", value: ""
                },
                placeHolder : {
                    type: "string", value: ""
                },
                minLength : {
                    type: "number", value:1
                },
                maxLength : {
                    type: "number", value: null
                },
                popupZIndex : {
                    type: "number", value: 20000
                },
                renderer: {
                    type: "function", value: null
                },
                width : {
                    type: "string", value: "80"
                },
                height : {
                    type: "string", value: "32px"
                },
                items : {
                    type: "number", value: 10
                },
                source : {
                    type : "source" , value : null
                },
                theme: {
                    type: "string", value: jqx_default_theme
                }
            },
            events: {
                change: true,
                close: true,
                open: true,
                select: true
            }
        },
        //--------------------------------------------------------------------

        jqxPasswordInput : {
            dom : "input[type=password]",
            props : {
                placeHolder : {
                    type: "string", value: null
                },
                maxLength : {
                    type: "number", value: 100
                },
                localization : {
                    type: "object", value: { passwordStrengthString: "密码强度", tooShort: "密码过短", weak: "弱", fair: "中等", good: "强", strong: "安全" }
                },
                width : {
                    type: "string", value: null
                },
                height : {
                    type: "string", value: "32px"
                },
                passwordStrength : {
                    type: "function", value: null
                },
                strengthTypeRenderer : {
                    type: "function", value: jqxUtil.passwordStrength
                },
                strengthColors : {
                    type : "object" , value : { tooShort: "rgb(170, 0, 51)", weak: "rgb(170, 0, 51)", fair: "rgb(255, 204, 51)", good: "rgb(45, 152, 243)", strong: "rgb(118, 194, 97)" }
                },
                showStrength: {
                    type: "boolean", value: false
                },
                showPasswordIcon: {
                    type: "boolean", value: true
                },
                showStrengthPosition: {
                    type: "string", value: "right" , possibleValues : ["top","bottom","left","right"]
                },
                theme: {
                    type: "string", value: jqx_default_theme
                }
            },
            events: {
                change: true
            }
        },

        //--------------------------------------------------------------------
        jqxDateTimeInput : {
            dom : "div",
            props : {
                animationType: {
                    type: "string", value: "none", possibleValues: ["none", "slide","fade"]
                },
                allowNullDate: {
                    type: "boolean", value: true
                },
                allowKeyboardDelete: {
                    type: "boolean", value: true
                },
                clearString: {
                    type: "string", value: "清除"
                },
                culture : {
                    type: "string", value: "zh-CN"
                },
                disabled : {
                    type: "boolean", value:false
                },
                firstDayOfWeek : {
                    type: "number", value: 0
                },
                formatString : {
                    type: "string", value: "yyyy-MM-dd"
                },
                height: {
                    type: "string", value: "32px"
                },
                min : {
                    type: "date", value: new Date(1900, 0, 1)
                },
                max : {
                    type: "date", value: new Date(2100, 11, 31)
                },
                placeHolder : {
                    type: "string", value: ""
                },
                popupZIndex : {
                    type : "number" , value : 20000
                },
                readonly : {
                    type: "boolean", value: false
                },
                showFooter : {
                    type : "boolean" , value : true
                },
                selectionMode : {
                    type: "string", value: "default",possibleValues : ["none","default","range"]
                },
                showWeekNumbers : {
                    type : "boolean" , value : true
                },
                showTimeButton : {
                    type : "boolean" , value : false
                },
                showCalendarButton : {
                    type : "boolean" , value : true
                },
                template : {
                    type: "string", value: "default",possibleValues : ["default","primary","success","warning","danger","info"]
                },
                todayString: {
                    type: "string", value: "今天"
                },
                width: {
                    type: "string", value: 100
                },
                value: {
                    type: "Date", value: new Date()
                },
                theme: {
                    type: "string", value: jqx_default_theme
                }
            },
            events: {
                change: true,
                close: true,
                open: true,
                textchanged: true,
                valueChanged: true
            }

        },

        //--------------------------------------------------------------------

        jqxTextArea : {
            dom : "textarea",
            props : {
                disabled : {
                    type: "boolean", value:false
                },
                displayMember : {
                    type: "string", value: ""
                },
                valueMember : {
                    type: "string", value: ""
                },
                dropDownWidth: {
                    type: "string", value: null
                },
                height : {
                    type: "string", value: "100px"
                },
                items : {
                    type: "number", value: 8
                },
                maxLength : {
                    type: "number", value: 1000
                },
                minLength : {
                    type: "number", value: 1
                },
                placeHolder : {
                    type: "string", value: ""
                },
                popupZIndex : {
                    type : "number" , value : 20000
                },
                searchMode : {
                    type: "string", value: "containsignorecase", possibleValues: ["none", "contains", "containsignorecase", "equals", "equalsignorecase", "startswithignorecase", "startswith", "endswithignorecase", "endswith"]
                },
                source : {
                    type : "source" , value : []
                },
                width: {
                    type: "string", value: null
                },

                theme: {
                    type: "string", value: jqx_default_theme
                }
            },
            events: {
                change: true,
                close: true,
                open: true,
                select: true
            }
        },


        //-------------------------------------------------------------------


        jqxTabs : {
            dom : "div",
            props : {
                animationType: {
                    type: "string", value: "none", possibleValues: ["none","fade"]
                },
                autoHeight : {
                    type: "boolean", value:true
                },
                disabled : {
                    type: "boolean", value:false
                },
                collapsible : {
                    type: "boolean", value:false
                },
                showCloseButtons : {
                    type: "boolean", value:false
                },
                initTabContent : {
                    type: "function", value: null
                },
                position: {
                    type: "string", value: "top",possibleValues : ["top","bottom"]
                },

                selectedItem : {
                    type: "number", value: 0
                },
                toggleMode : {
                    type: "string", value: "click",possibleValues : ["click","mouseenter","dblclick","none"]
                },
                width : {
                    type: "string", value: "auto"
                },
                theme: {
                    type: "string", value: jqx_default_theme
                }
            },
            events: {
                add: true,
                created: true,
                collapsed: true,
                dragStart: true,
                dragEnd: true,
                expanded: true,
                removed: true,
                selecting: true,
                selected: true,
                tabclick: true,
                unselecting: true,
                unselected: true
            }
        }

        //-------------------------------------------------------------------


    },


    build : function (){
        var widgetsPool = {};
        var temporaryPool = {};
        for(var w in jqx.widgets){
            var widget = jqx.widgets[w];
            var objs = $(widget.dom + "." + w);


            objs.each(function(){
                var uid = baseUtil.uuid();

                var $this = $(this);
                widgetsPool[uid] = $this;
                temporaryPool[uid] = {};
                var execStr = 'widgetsPool["' + uid + '"].' + w + '(';
                //记录属性
                var args = {};
                //记录function属性
                var fargs = {};

                for(var p in widget.props){
                    var prop = widget.props[p];
                    var attrValue = baseUtil.getAttrValue(this,p);
                    if(attrValue){
                        if(prop.type == "boolean") { //布尔值类型需要对字符串转换
                            attrValue = eval(attrValue);
                        }else if(prop.type == "date"){
                            //attrValue = new Date(attrValue);
                        } else if(prop.type == "number"){//数字类型需要对字符串转换
                            attrValue = Number(attrValue);
                        }else if(prop.type == "function"){
                            attrValue = eval(attrValue);//JSON.stringify在序列化时会过滤掉function类型的变量 此处无需处理
                            if(attrValue){
                                fargs[p] = attrValue;
                            }
                        }else if(prop.type == "source") {
                            //类型1 = url
                            var evalError = false;
                            try{
                                attrValue = eval(attrValue);
                                if(Chk.isFunction(attrValue)){
                                    fargs[p] = attrValue;
                                }
                            }catch (e){
                                evalError = true;
                            }
                            if(evalError || Chk.isString(attrValue)){
                                var $t = $this;
                                var $uid = uid;
                                var $p = p;
                                var $execStr = execStr;
                                $.ajax({
                                    url: attrValue,
                                    type: $t.attr("method") ? $t.attr("method") : "post",
                                    dataType: "json",
                                    success: function (rtn) {
                                        var args = {};
                                        args[$p] = rtn;
                                        $.extend(args,temporaryPool[$uid]);
                                        delete temporaryPool[$uid];
                                        eval($execStr + JSON.stringify(args) + ');');
                                    }
                                });
                                attrValue = [];
                            }
                        }
                    }else{
                        attrValue = prop.value;
                        // 避免控件对null的格式化异常  在function类型的属性 如果初始值为null时 跳过这个属性
                        //如果值不为null 则也需要通过fargs对象进行配置
                        if(prop.type == "function" || prop.type == "source" ){
                            if(attrValue){
                                fargs[p] = attrValue;
                            }else{
                                continue;
                            }
                        }
                    }
                    if(prop.afterBinding){
                        temporaryPool[uid][p] = attrValue;
                    }
                    args[p] = attrValue;
                }

                eval(execStr + JSON.stringify(args) + ");");
                for(var f in fargs){
                    eval(execStr + "{" + f + ":" + fargs[f] + "});");
                }
                for(var e in widget.events){
                    var f = widget.events[e];
                    if(f == true){
                        var event = baseUtil.getAttrValue(this,e);
                        if(event){
                            $this.on(e,eval(event));
                        }
                    }
                }
            });
        }
    }







};

/*$(function (){
    jqx.build();
});*/


