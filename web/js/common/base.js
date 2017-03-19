/** * 对Date的扩展，将 Date 转化为指定格式的String * 月(M)、日(d)、12小时(h)、24小时(H)、分(m)、秒(s)、周(E)、季度(q)
 可以用 1-2 个占位符 * 年(y)可以用 1-4 个占位符，毫秒(S)只能用 1 个占位符(是 1-3 位的数字)
 * eg:
 * (new Date()).pattern("yyyy-MM-dd hh:mm:ss.S")==> 2006-07-02 08:09:04.423
 * (new Date()).pattern("yyyy-MM-dd E HH:mm:ss") ==> 2009-03-10 二 20:09:04
 * (new Date()).pattern("yyyy-MM-dd EE hh:mm:ss") ==> 2009-03-10 周二 08:09:04
 * (new Date()).pattern("yyyy-MM-dd EEE hh:mm:ss") ==> 2009-03-10 星期二 08:09:04
 * (new Date()).pattern("yyyy-M-d h:m:s.S") ==> 2006-7-2 8:9:4.18
 */
Date.prototype.format = function(fmt) {
    var o = {
        "M+" : this.getMonth() + 1, //月份
        "d+" : this.getDate(), //日
        "h+" : this.getHours() % 12 == 0 ? 12 : this.getHours() % 12, //小时
        "H+" : this.getHours(), //小时
        "m+" : this.getMinutes(), //分
        "s+" : this.getSeconds(), //秒
        "q+" : Math.floor((this.getMonth() + 3) / 3), //季度
        "S" : this.getMilliseconds()//毫秒
    };

    var week = {
        "0" : "日",
        "1" : "一",
        "2" : "二",
        "3" : "三",
        "4" : "四",
        "5" : "五",
        "6" : "六"
    };
    if (/(y+)/.test(fmt)) {
        fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "")
            .substr(4 - RegExp.$1.length));
    }
    if (/(E+)/.test(fmt)) {
        fmt = fmt.replace(RegExp.$1,((RegExp.$1.length > 1) ? (RegExp.$1.length > 2 ? "星期": "周"): "")+ week[this.getDay() + ""]);
    }
    for ( var k in o) {
        if (new RegExp("(" + k + ")").test(fmt)) {
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
        }
    }
    return fmt;
}


/**
 * 对String进行trim
 */
String.prototype.trim = function(){
    return this.replace(/(^\s*)|(\s*$)/g, "");
}
String.prototype.ltrim = function(){
    return this.replace(/(^\s*)/g,"");
}
String.prototype.rtrim = function(){
    return this.replace(/(\s*$)/g,"");
}

/**
 * endWith\startWith
 */
String.prototype.endWith = function(s){
    if(!s || s.length > this.length){
        return false;
    }
    return this.substring(this.length - s.length) == s ;

}

String.prototype.startWith = function(s){
    if(!s || s.length > this.length){
        return false;
    }
    return this.substr(0,s.length) == s;
}

Array.prototype.remove = function(obj,once){
    for(var i =0 ; i < this.length ; i++){
        var tmp = this [i];
        if( tmp == obj ){
            for(var j = i; j < this.length ; j++){
                this[j]=this[j+1];
            }
            this.length = this.length-1;
            if(once){
                break;
            }
            i --;
        }
    }
    return this;
}




$(function (){
    var pagebuilder = function (){
        var $d = $(document);
        $d.on("click","ul.nav-pills a",function (){
            $this = $(this);
            $this.parents("ul.nav-pills:first").find("li").removeClass("active");
            $this.parent("li").addClass("active");
        });
    };

    pagebuilder();
});


//$(function (){
//设置浏览器不使用缓存获取数据。防止陆帅峰框架get api 缓存数据
$.ajaxSetup ({cache: false});




var dateFormat = {
    yMd: "yyyy-MM-dd",
    yMd_Hms: "yyyy-MM-dd HH:mm:ss",
    yMd_Hm: "yyyy-MM-dd HH:mm",
    yM: "yyyy-MM",
    Md: "MM-dd",
    Hm: "HH:mm",
    ms: "mm:ss",
    y: "yyyy",
    M: "MM",
    d: "dd",
    h: "HH",
    m: "mm",
    s: "ss"
};


var $bs = {
    success : function (msg){ $bs.modal(msg,"success");},
    error : function (msg){ $bs.modal(msg,"error");},
    alert : function (msg){ $bs.modal(msg,"alert");},

    confirmCallback : null,
    confirm : function (msg,callback){

        $bs.confirmCallback = callback;


        var a = $("#bs-message-confirm");
        if(a.length == 0){
            a = $('<div id="bs-message-confirm" class="modal fade"  data-backdrop="static">' +
                '<div class="modal-dialog">'+
                '<div class="modal-content">' +
                '<div class="modal-header">' +
                ' <span class="close fa fa-close" data-dismiss="modal" aria-hidden="true" style="float: right; line-height: 30px;"></span>' +
                '<h4 class="modal-title"></button>提示</h4>' +
                '</div>' +
                '<div class="modal-body clearfix">' +
                '<div id="bs-confirm-icon" class="icon" ></div>' +
                '<div id="bs-confirm-msg" class="msg"></div>' +
                '</div>' +
                '<div class="modal-footer">' +
                '<a class="btn btn-success" data-dismiss="modal" id="bs-message-confirm-submit">确定</a>' +
                '<a class="btn btn-primary" data-dismiss="modal">取消</a>' +
                '</div></div></div></div>');
            $("body").append(a);

            $("#bs-message-confirm-submit").on("click",function (){
                if(Chk.isFunction($bs.confirmCallback)){
                    $bs.confirmCallback();
                }
            });
        }

        $("#bs-confirm-icon").attr("class","icon fa fa-question-circle");

        $("#bs-confirm-msg").html(msg);
        a.modal("show");

    },


    modal : function (msg,type){
        var a = $("#bs-message");
        if(a.length == 0){
            a = $('<div id="bs-message" class="modal fade"  data-backdrop="static">' +
                '<div class="modal-dialog">'+
                '<div class="modal-content">' +
                '<div class="modal-header">' +
                ' <span class="close fa fa-close" data-dismiss="modal" aria-hidden="true" style="float: right; line-height: 36px; "></span>' +
                '<h4 class="modal-title"></button>提示</h4>' +
                '</div>' +
                '<div class="modal-body clearfix">' +
                '<div id="bs-alert-icon" class="icon" ></div>' +
                '<div id="bs-alert-msg" class="msg"></div>' +
                '</div>' +
                '<div class="modal-footer"><a class="btn btn-primary" data-dismiss="modal">确定</a></div></div></div></div>');
            $("body").append(a);
        }

        if(type == "success"){
            $("#bs-alert-icon").attr("class","icon fa fa-check-circle");
        }else if(type == "error"){
            $("#bs-alert-icon").attr("class","icon fa fa-times-circle");
        }else if(type == "alert"){
            $("#bs-alert-icon").attr("class","icon fa fa-exclamation-circle");
        }
        //else if(type == "confirm"){
        //    $("#bs-alert-icon").attr("class","icon fa fa-question-circle");
        //}

        $("#bs-alert-msg").html(msg);
        a.modal("show");
    }
};





var baseUtil = {
    setCookie : function (c_name,value,expiredays){
        var exdate=new Date();
        exdate.setDate(exdate.getDate() + expiredays)
        document.cookie=c_name+ "=" +escape(value)+ ((expiredays==null) ? "" : ";expires="+exdate.toGMTString())
    },

    getCookie : function (c_name){
    if (document.cookie.length>0) {
            var c_start = document.cookie.indexOf(c_name + "=")
            if (c_start != -1) {
                c_start=c_start + c_name.length + 1
                var c_end=document.cookie.indexOf(";",c_start)
                if (c_end == -1){
                    c_end=document.cookie.length;
                }
                return unescape(document.cookie.substring(c_start,c_end));
            }
        }
        return null;
    },

    getAttrValue : function (el,key){
        if(el && key){
            var attr = el.attributes[key];
            if(attr){
                return attr.nodeValue;
            }
        }
    },
    getRoot : function (){
        //获取当前网址，如： http://localhost:8080/acdm/xxx/xxx.jsp
        var path=window.document.location.href;
        //获取主机地址之后的目录，如： acdm/xxx/xxx.jsp
        var pathName=window.document.location.pathname;
        //获取主机地址，如： http://localhost:8080
        var localhostPath = path.substring(0,path.indexOf(pathName));
        //获取带"/"的项目名，如：/acdm
        var projectName=pathName.substring(0,pathName.substr(1).indexOf('/')+1);
        return localhostPath + projectName;
    },

    getWebsocketRoot :  function(){
        return baseUtil.getRoot().replace(/^http[s]?:/i,"ws:");
    },


    openWindow : function (url,windowName,w,h,top,left){
            w = Number((w + "").toLowerCase().replace("px",""));
            h = Number((h + "").toLowerCase().replace("px",""));
            var top = typeof top == "undefined" ?  ( window.screen.height - h ) / 2 : top;
            var left = typeof left == "undefined" ? ( window.screen.width - w ) / 2 : left;
            var win = window.open(url,windowName,"width=" + w + ",height=" + h + ",top=" + top + ",left=" + left + ",resizable=no,scrollbars=no,status=no,toolbar=no,menubar=no,location=no");
            return win;
    },

    bind : function (type,data,$area,pre,suf){
        if(! pre){
            pre = "";
        }
        if( ! suf){
            suf = "";
        }

        if( ! $area){
            $area = $("body");
        }

        for(var key in data){
            var obj = null;
            var k = pre + key + suf;
            if(type == "class"){
                obj = $area.find("." + k);
            }else if(type == "id"){
                obj = $area.find("#" + k);
            }else if(type == "name"){
                obj = $area.find("[name=" + k + "]");
            }
            obj.val(data[key]);
        }
    },

    bindByName : function (data,$area,pre,suf) {
        baseUtil.bind("name", data, $area,pre, suf);
    },


     toJSONString : function (o,replacer,space){
        if( ! Chk.isObject(o) && ! Chk.isArray(o)){
            return "";
        }
        if( ! replacer){
            replacer = function (key,value){
                return value == null ? "" : value;
            }
        }
        return JSON.stringify(o,replacer,space);
     },

    toJSON : function(str,replacer){
        try {
            return JSON.parse(str);
        } catch (e) {
            //alert("exception in function [ commons.js -> toJSON ]  \n " + e.name + ": " + e.message);
            return replacer;
        }
    },



    formSerialize : function (id){
        var r = {};
        var f = $("#" + id);
        if(f.length == 1){
            var a = f.serializeArray();
            for(var i = 0 ; i < a.length ; i ++){
                var b = a[i];
                r[b['name']] = b['value'];
            }
        }

        return r;
    },

    download : function (url,data){
        $("#temporary-download-form,#temporary-download-iframe").remove();
        var form = $("<form id='temporary-download-form' method='post' target='temporary-download-iframe'>" +
            "<input type='hidden' name='data'>" +
            "</form>");
        form.attr("action",url);
        form.children("input").val(data);
        var iframe = $("<iframe id='temporary-download-iframe' style='display:none' name='temporary-download-iframe'></iframe>")
        $("body").append(iframe).append(form);
        form.submit();
    },
    min : function (arr,key,defaultValue){
        var numbers = [defaultValue];
        if(isArray(arr) && isString(key)){
            for(var i in arr){
                numbers.push(arr[i][key]);
            }
        }
        return Math.min.apply(Math,numbers);
    },

    max : function (arr,key,defaultValue){
        var numbers = [defaultValue];
        if(Chk.isArray(arr) && Chk.isString(key)){
            for(var i in arr){
                numbers.push(arr[i][key]);
            }
        }
        return Math.max.apply(Math, numbers);
    },

    uuid : function (){
        var s = [];
        var hexDigits = "0123456789abcdef";
        for (var i = 0; i < 36; i++) {
            s[i] = hexDigits.substr(Math.floor(Math.random() * 0x10), 1);
        }
        s[14] = "4"; // bits 12-15 of the time_hi_and_version field to 0010
        s[19] = hexDigits.substr((s[19] & 0x3) | 0x8, 1); // bits 6-7 of the clock_seq_hi_and_reserved to 01
        s[8] = s[13] = s[18] = s[23] = "-";
        return s.join("");
    },


    createTemporaryForm : function (url,args){
        var f = $("<form/>");
        f.attr("action",url);
        f.attr("method","post");
        if(args){
            for(var key in args){
                var input = $("<input/>");
                input.attr({
                    "type" : "hidden","name" : key , "value" : args[key]
                });
                f.append(input);
            }
        }

        $("body").append(f);
        return f;
    },

     submitTemporaryForm : function (url,args){
        baseUtil.createTemporaryForm(url,args).submit();
    },

    getTrueLength : function (str) {
        return str.replace(/[^\x00-\xff]/g, "ww").length;
    },

    getFormValuesById : function (id,replacer){
        var args = {};
        var  f = $("#" + id);
        f.find("input,textarea,div.jqx-dropdownlist-state-normal").each(function (){
            var key = $(this).attr("name") || $(this).attr("id");
            if(replacer && key){
                key = key.replace(replacer,"");
            }
            args[key] = $(this).val();
        })
        return args;

    }
};

var Chk = {
    isNumber : function (o){
        return '[object Number]' === Object.prototype.toString.call(o) && isFinite(o);
    },
    isArray : function(o){
        return '[object Array]' === Object.prototype.toString.call(o);
    },
    isDate : function(o){
        return "[object Date]" === Object.prototype.toString.call(o)  && o.toString() !== 'Invalid Date'  &&  !isNaN(o);
    },
    isFunction : function(o) {
        return '[object Function]' === Object.prototype.toString.call(o);
    },
    isObject : function (o) {
        return '[object Object]' === Object.prototype.toString.call(o);
    },
    isString : function(o) {
        return '[object String]' == Object.prototype.toString.call(o);
    },
    isEmpty : function (o){
        if( ! Chk.isObject(o)){
            return false;
        }
        for(var k in o){
            return false;
        }
        return true;
    },
    isTelephone : function (str){
        var re = /^(\d{8})|(0\d{2,3}-?\d{7,8})$/;
        return re.test(str);
    },
    isSpace : function (str){
        return typeof(str) == "undefined" ? false : $.trim(str).length > 0;
    }
}




//重写jquery的ajax方法 用作异步请求的 用户登录状态判断
//jQuery(function($) {
//备份jquery的ajax方法
/*var _ajax = $.ajax;
var ajaxErrorHandel = true;
$.ajax = function(opt) {
    var _success = opt && opt.success || function(a, b) {};
    var errorHandle = function (t,textStatus){
        if(t == Constant.UNLOGIN_STATUS_CODE){
            if(ajaxErrorHandel){
                ajaxErrorHandel = false;
                alert("登录超时, 请重新登录 !");
                toIndex();
                return true;
            }
        }else if(t == Constant.NO_PERMISSION_STATUS_CODE){
            //alert("无访问权限!");
            _success(null,textStatus);
            return true;
        }else{
            return false;
        }
    };

    var _opt = $.extend(opt, {

        error : function (req,textStatus){
            if( errorHandle(req.responseText,textStatus) ){

            }else{
                //alert("请求返回异常 : " + textStatus);
            }

        },

        success : function(d, textStatus) {
            // 如果后台将请求重定向到了登录页，则data里面存放的就是登录页的源码，这里需要找到data是登录页的证据(标记)
            if(Chk.isString(d)){
                if( errorHandle( d , textStatus) ){
                    return;
                }
            }
            _success(d, textStatus);
        }
    });

    return _ajax(_opt);
};*/












//$.browser = {};
////$.browser.mozilla = /firefox/.test(navigator.userAgent.toLowerCase());
//$.browser.webkit = /webkit/.test(navigator.userAgent.toLowerCase());
////$.browser.opera = /opera/.test(navigator.userAgent.toLowerCase());
////$.browser.msie = /msie/.test(navigator.userAgent.toLowerCase());
//
//if( ! $.browser.webkit){
//    window.location.href = getRoot() + "/browser.jsp";
//}
