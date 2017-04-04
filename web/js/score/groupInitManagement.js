$(function () {

    var term_info = {
        term_name: "",
        showChangeClass: false,
        showTerm: false
    };

    new Vue({
        el: "#term_info",
        data: term_info
    });

    var teacher = $getTea();

    var term_Id = 1;

    $.post(
        './queryCurrentTime.do',
        function (rtn) {
            if (rtn) {
                store.setItem("currentTime", rtn['term_name']);
                term_info.term_name = rtn["term_name"];
                term_info.showTerm = true;
                $("#term_id").val(rtn['term_id']);
            }
        }
    );

    $("#changeTerm").click(function () {
        $.post(
            './queryAllTerms.do',
            function (rtn) {
                if (rtn) {
                    $("#chooseTerms").jqxDropDownList({
                        source: rtn,
                        selectedIndex: 0,
                        width: '170',
                        height: '25',
                        theme: jqx_default_theme,
                        autoDropDownHeight: true,
                        displayMember: 'key',
                        valueMember: 'value'
                    });
                    $("#termWin").modal('show');
                } else {
                    $bs.error('发生错误 !');
                }
            }
        );
    });

    $("#termSub").click(function () {
        var ct = store.getItem('currentTime');
        var value = $("#chooseTerms").val();
        var text = $("#chooseTerms").text();

        if (ct === text) {
            term_info.showChangeClass = false;
        } else {
            // 提示用户当前选择的学期不是当前学期
            term_info.showChangeClass = true;
        }

        // 改变学期名
        term_info.term_name = text;

        // 将当前选中的学期的序号记录
        $("#term_id").val(value);

        // 改变学期就重新生成课程
        queryCourseData.termId = value;
        createCourse();
    });

    // 封装查询课程的方法, 根据登录的老师和选中的学期进行查询
    var queryCourseData = {
        teaNo: teacher["tea_no"],
        termId: term_Id
    };


    var createCourse = function () {
        $.when(
            $.post(
                "./queryCoursesByTeacherIdAndTerm.do",
                queryCourseData,
                function (rtn) {
                    $("#choose_course").jqxDropDownList({
                        source: rtn,
                        selectedIndex: 0,
                        width: '150',
                        height: '25',
                        theme: jqx_default_theme,
                        autoDropDownHeight: true,
                        displayMember: 'key',
                        valueMember: 'value'
                    });
                }
            )).done(function () {
            var courseId = $("#choose_course").val();
            $.post(
                "./queryClasses.do",
                {
                    teaNo: teacher["tea_no"],
                    termId: term_Id,
                    courseId: courseId
                },
                function (rtn) {
                    $("#choose_class").jqxDropDownList({
                        source: rtn,
                        selectedIndex: 0,
                        width: '150',
                        height: '25',
                        theme: jqx_default_theme,
                        autoDropDownHeight: true,
                        displayMember: 'key',
                        valueMember: 'value'
                    });
                }
            )
        })

    };

    createCourse();


    var search = function () {

    }
});

