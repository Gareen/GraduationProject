$(function () {
    layui.use('element', function () {
        let element = layui.element();

    });

    $("#signOut").unbind('click').click(function () {
        $bs.confirm("确认退出 ?", function () {
            $.post(
                "../../system/login/loginOut.do",
                function (rtn) {
                    if (rtn === "success") {
                        window.location.href = "../../system/login/index.do";
                        store.removeItem("teacher");
                    } else {
                        $bs.error(rtn);
                    }
                }
            )
        });
    });

    let a = $('#stu').attr('pid');
    let b = $('#blk').attr('pid');

    let getPath = function (pid) {
        return new Promise(function (success) {

            $.post(
                "../../system/resources/findNav2.do",
                {"pid": pid},
                function (path) {

                    success(path)
                }
            )
        })
    };


    function createStuBar(rtn) {
        new Vue({
            el: "#stuBar",
            data: {
                pathObj: rtn
            }
        });
    }

    function createBlkBar(rtn) {
        new Vue({
            el: "#backBar",
            data: {
                pathObj: rtn
            }
        });
    }


    for (let i = 0; i < 2; i++) {

        if (i === 1) {
            getPath(b).then(function (rtn) {
                createBlkBar(rtn);
            });
            break;
        }

        getPath(a).then(function (rtn) {
            createStuBar(rtn);
        })

    }
});
