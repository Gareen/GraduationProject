$(function (){
    $("h3").click(function (){


        var h3a = $.makeArray($("h3"));

        h3a.splice($.inArray($(this)[0],h3a),1);
        $(h3a).siblings("div").slideUp();

        $(this).siblings("div").slideToggle();
    })
});