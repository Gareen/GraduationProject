$(function (){

    var nav1li= $("#sideNav1>li");

    nav1li.click(function () {
        // 获取点击的是那个div
        var index = nav1li.index(this);
        var a = nav1li.eq(index).children("a");
        var title = $(this).children("a").children("div[class='nav-title']").text();
        $("#left-sub-title").html(title);
        var p_id = a.attr("p_id");
        $.post(
            "../../system/resources/findNav2.do",
            { "pid" : p_id},
            function(rtn) {
                createUlnav2(rtn);

            }
        )

    });

    (function () {
        var h = window.location.href;

        var i = h.indexOf('score');
        if (i && i != -1) {
            nav1li.eq(0).click().addClass('active');
        } else {
            nav1li.eq(1).click().addClass('active');
        }

    }());


    // 根据一级菜单进行二级菜单的获取 vue重构
    var createUlnav2 = function(paths) {
        new Vue({
            el:"#sideNav2",
            data:{
                pathObj:paths
            }
        })
    };

    //设置页面上的时钟
    setInterval(function() {
        var d = new Date();
        var t = d.format("yyyy年MM月dd HH:mm:ss EEE").split(" ");
        $("#clock").html(t[1]);
        $("#YYYYMMDD").html(t[0] + "日");
        $("#EEE").html(t[2]);
    }, 1000);

    $("#view-top div.topbar-nav").has("ul").find("div>a>.nav-title").append('<span class="fa fa-caret-down"></span><span class="fa fa-caret-up"></span>');

    $("ul.nav1 .nav-item a").on("click",function (){
        // location.href =;
        var url = $(this).attr("href").replace("#","");
        console.log(url);
    });

    var ulnav2 = $("ul.nav2");
    ulnav2.find(".nav-item a").on("click",function (){
        alert(2);
        var $this = $(this);
        var $navItem = $this.parent("li.nav-item");
        // var itemId = $navItem.attr("id");
        var hasChildren = $navItem.hasClass("parent");
        if(hasChildren){
            $this.find("div.nav-icon").toggleClass("fa-caret-right");

            $navItem.toggleClass("active");

        }

    });

    var buildSideNav = function (){
        var sideNav1Item = $(".nav1 li.nav-item");//第一层菜单所有项
        var sideNav2Item = $(".nav2 li.nav-item");//第二层菜单所有项
        var vl = $("#view-content-left");                 //左侧视图面板
        var vls = $("#view-content-left-sub");        //左侧子视图面板
        var vm = $("#view-content-main");          //中间面板
        var nav1sw = $("#nav1-switch");               //第一层菜单开关
        var nav2swl = $("#nav2-switch-left").hide();              //第二层菜单右边开关
        var nav2swr = $("#nav2-switch-right");            //第二层菜单左边开关

        var iconTooltips = $(".nav1 .iconTooltip"); // 第一层但中的图标

        var leftAlteration = function (o,n,time,callback){
            var ov = o.css("left");
            var css  = {
                "left" : ( ov ? ( Number(ov.toUpperCase().replace("PX","")) + n ) : n )
            };
            o.animate(css,time,function(){
                if(Chk.isFunction(callback)){
                    callback();
                }
            });
        };


        var createIconTooltips = function (){
            iconTooltips.tooltip({
                container : "body",
                html : true
            });
        };
        if(sn1sw){
            createIconTooltips();
        }

        var destroyIconTooltips = function (){
            iconTooltips.tooltip('destroy');
        };


        var closeSideNav1 = function (callback){
            leftAlteration(vls,-130,0);
            leftAlteration(vm,-130,0,callback);
            vl.width("-=130");
            createIconTooltips();
        };

        var openSideNav1 = function (callback){
            leftAlteration(vls,130,0);
            leftAlteration(vm,130,0,callback);
            vl.width("+=130");
            destroyIconTooltips();
        };

        var sn1sw = new Object;
        nav1sw.on("click",function (){

            /*$.post(
                root + "/common/commonConfig/setSidebar?sw=" + ! sn1sw
            );*/

            if(sn1sw){
                openSideNav1( sidebar.callback(nav1sw));
            }else{
                closeSideNav1( sidebar.callback(nav1sw));

            }
            $(this).toggleClass("fa-minus");
            $(this).toggleClass("fa-bars");
            sn1sw = ! sn1sw;
            //baseUtil.setCookie("sideNav1sw",sn1sw);

        });

        nav2swr.on("click",function (){
            leftAlteration(vm ,-180,150,function(){
                nav2swr.hide();
                nav2swl.show();
                if(sidebar.callback && Chk.isFunction(sidebar.callback)){
                    sidebar.callback(nav2swr);
                }
            });

        });

        nav2swl.on("click",function (){
            leftAlteration(vm ,180,150,function (){
                nav2swl.hide();
                nav2swr.show();
                if(sidebar.callback && Chk.isFunction(sidebar.callback)){
                    sidebar.callback(nav2swl);
                }
            });

        });

    };

    buildSideNav();

    // 登陆者可以进行密码修改
    $("#old_pwd, #new_pwd").jqxPasswordInput({theme: jqx_default_theme, width:'45%', height:"25px"});
    $("#modify_pwd").click(function () {
        $("#old_pwd").val("");
        $("#new_pwd").val("");
        $("#modifywin").modal("show");
    });

    var modifysubmit =  $("#modifysubmit");
    modifysubmit.click(function () {
        var tea_no = $("#user_no").val();
        var old_pwd = $("#old_pwd").val();
        var new_pwd = $("#new_pwd").val();
        modifysubmit.next('a').click();
        $.post(
            "../../system/resources/modifyPwd.do",
            {
                "no":tea_no,
                "old":old_pwd,
                "new":new_pwd
            },
            function (rtn) {
                if(rtn == "success") {
                    $bs.success("密码修改成功 !");
                } else if(rtn == "error1") {
                    $bs.error("新/旧密码不可为空 !");
                } else if(rtn == "error2") {
                    $bs.error("旧密码输入出错 !");
                } else {
                    $bs.error("发生异常 !");
                }
            }
        )

    })

    $("#login_out").click(function () {
        $bs.confirm("确认退出 ?", function() {
            $.post(
                "../../system/login/loginOut.do",
                function (rtn) {
                    if (rtn === "success") {
                        window.location.href = "../../system/login/index.do";
                        // 从localStorage中移除teacher
                        store.removeItem("teacher");
                    } else {
                        $bs.error(rtn);
                    }
                }
            )
        });
    })
});

var sidebar = {
    callback : function (){}
};
