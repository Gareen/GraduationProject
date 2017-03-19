$(function (){

    var lb = $("#login_btn");

    var login = function (){
        lb.prop("disabled","disabled");
        $.post(
            "./login.do",
            {
                "loginName" : $("#loginName").val(),
                "password" : $("#password").val()
            },
            function (rtn){
                lb.removeAttr("disabled");
                if("success" == rtn){
                    // 设置默认的跳转页面
                    window.location.href = "../../score/groupManagement/toIndex.do";
                }else{
                    if("empty" == rtn) {
                        $bs.error("工号/密码不能为空 !");
                    } else if("notFound" == rtn) {
                        $bs.error("工号错误 !");
                    } else if("pwdError" == rtn) {
                        $bs.error("密码错误 !");
                    }
                }
            }
        )
    };

    lb.on("click", login);

    /*键盘监听事件, 按下回车就登陆*/
    $("#password").on("keyup",function (e){
        if(e.keyCode == 13){
            login();
        }
    });

    $("#loginName").focus();

});