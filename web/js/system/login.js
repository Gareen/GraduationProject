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
                if (rtn.status === 'success') {
                    // 将教师信息保存在localStorage, 以便后续调用
                    store.setItem("teacher", JSON.stringify(rtn.data));
                    // 设置默认的跳转页面
                    window.location.href = "../../common/welcome/index.do";
                }else{
                    $bs.error(rtn.msg);
                }
            }
        )
    };

    lb.on("click", login);

    /*键盘监听事件, 按下回车就登陆*/
    $("#password").on("keyup", function (e) {
        if (e.keyCode === 13) {
            login();
        }
    });

    $("#loginName").val('12315601');

    $("#loginName").focus();

});