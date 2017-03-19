var $time = {
    recentClientTime : null,
    difference : null,
    readjust : true,
    frequency : 1000,	//获取频率 默认1秒
    getTimeDifferenceWithServer : function (){
        var times = 5;
        var arrs = [];
        for(var i = 0 ; i < times ; i ++){
            $.ajax({
                async : false,
                type : "POST",
                url : baseUtil.getRoot() + "/common/time/getSystemTime",
                success : function (serverTime){
                    var t = ( serverTime - new Date().getTime() + times);
                    arrs.push(t);
                }
            });
        }
        arrs.remove(Math.max.apply(Math,arrs),true).remove(Math.min.apply(Math,arrs),true);
        var total = 0;
        for(var i = 0 ; i < arrs.length ; i ++){
            total += arrs[i];
        }

        return Math.floor(total/arrs.length);
    },
    resetDifferenceTask : setInterval(function (){
        $time.readjust = true;
    },1000 * 60 * 10),

    get : function(format){
        var t = new Date().getTime();
        //如果获取的时间与上次获取的时间 超过获取频率+1秒 则重新向服务器请求时间差
        if($time.recentClientTime != null){
            if(Math.abs(t - $time.recentClientTime) > ($time.frequency + 1000)){
                $time.readjust = true;
            }
        }

        if($time.readjust){
            $time.difference = $time.getTimeDifferenceWithServer();
            $time.readjust = false;
        }

        $time.recentClientTime = t;
        return new Date(t + $time.difference).format(format ? format : "yyyy-MM-dd EEE HH:mm:ss") ;
    }

};



