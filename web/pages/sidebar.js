
var sidebar = {
    callback : function (){}
};

$(function (){


    $("#view-top div.topbar-nav").has("ul").find("div>a>.nav-title").append('<span class="fa fa-caret-down"></span><span class="fa fa-caret-up"></span>');

    var root = baseUtil.getRoot();
    alert(root);
    $("ul.nav1 .nav-item a").on("click",function (){
        var url = $(this).attr("href").replace("#","");
        if(/^[1-9]+[0-9]*$/.test(url)){
            $.post(
                root + "/common/loader/resourcePath?id=" + url,
                function (rtn){
                    if(rtn){
                        //alert(ctx + rtn);
                        window.location.href = ctx + rtn;
                    }
                }
            );
        }
    });

    var ulnav2 = $("ul.nav2");
    ulnav2.find(".nav-item a").on("click",function (){
        var $this = $(this);
        var $navItem = $this.parent("li.nav-item");
        var itemId = $navItem.attr("id");
        var hasChildren = $navItem.hasClass("parent");
        if(hasChildren){
            $this.find("div.nav-icon").toggleClass("fa-caret-right");
            var c = ulnav2.find("li.nav-item[pid=" + itemId + "]");
            c.toggle();
            if(c.filter(".active").length > 0){
                $navItem.toggleClass("active");
            }
            return;
        }
        var h = $this.attr("href").replace("#","");
        if(h){
            window.location.href = root + h;
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

        var setSubTitle = function (){
            var title = $(".nav1 li.nav-item.active .nav-title").text();
            $("#left-sub-title").html(title);

        };

        setSubTitle();


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
        //
        //var sn1sw= baseUtil.getCookie("sideNav1sw");
        var sn1sw = false;


        //if( ! sn1sw){
        //    closeSideNav1();
        //    nav1sw.toggleClass("glyphicon-indent-left");
        //}


        nav1sw.on("click",function (){



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

        //var closeSideNav2 = function (){
        //  leftAlteration(vls,-130);
        //  leftAlteration(vm,-130);
        //  vl.width("-=130");
        //};
        //
        //var openSideNav2 = function (){
        //  leftAlteration(vls,130);
        //  leftAlteration(vm,130);
        //  vl.width("+=130");
        //};



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

});
