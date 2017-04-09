package cn.sams.common.util;


import cn.sams.common.constants.Constant;
import cn.sams.common.constants.DateFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

    /**
     * 计算两个日期之间的时间差
     *
     * @param date1
     * @param date2
     * @param format 格式
     * @return
     */
    public static Long getDaysBetween(String date1, String date2, String format) {
        return (parse(date1, format).getTime() - parse(date2, format).getTime()) / (1000 * 60 * 60 * 24);
    }

    /**
     * 将格式化日期重新格式化
     *
     * @param date
     * @param fromFormat 旧格式
     * @param toFormat   新格式
     * @return
     */
    public static String convertFormat(String date, String fromFormat, String toFormat) {
        if(Chk.spaceCheck(date) && Chk.spaceCheck(fromFormat) && Chk.spaceCheck(toFormat)){
            return getDateString(parse(date, fromFormat), toFormat);
        }else{
            return "";
        }
    }


    /**
     * 获取
     *
     * @param date
     * @param format
     * @return
     */
    public static String getDateString(Date date, String format) {
        if(date == null || !Chk.spaceCheck(format)){
            return "";
        }
        String d = "";
        try{
            d = getFormat(format).format(date);
        }catch(Exception e){
            e.printStackTrace();
        }
        return d;
    }

    /**
     *
     * @param date
     * @param format
     * @param offset [0]=day [1]=hour [2]=min [3]=sec
     * @return
     */
    public static String getOffsetDateString(Date date,String format,int [] offset,boolean ...calWithZeroPoint){
        Date d = getOffsetDate(date, offset[0],  offset[1],  offset[2],  offset[3], calWithZeroPoint);
        return getDateString(d, format);
    }

    /**
     * 返回时间格式化字符串, 如果isFormat为false, 那么就默认为"yyyy-MM-dd HH:mm:ss" 格式
     *
     * @param date
     * @param isFormat
     * @param format
     * @return
     */
    public static String getDateString(Date date, boolean isFormat, String format) {
        if(date == null){
            return null;
        }
        if (isFormat) {
            return getFormat(format).format(date);
        }
        return getFormat(DateFormat.yMd_Hms).format(date);
    }

    /**
     * 返回格式化后的日期字符串 yyyy-MM-dd
     *
     * @param date
     * @return
     */
    public static String getYyyymmdd(Date date) {
        return getFormat(DateFormat.yMd).format(date);
    }

    /**
     * 返回格式化后的时间字符串 HH:mm:ss
     *
     * @param date
     * @return
     */
    public static String getHHmmss(Date date) {
        return getFormat(DateFormat.Hms).format(date);
    }

    public static String formatStartDate(String yyyymmdd){
        if( ! Chk.dateCheck(yyyymmdd)){
            return yyyymmdd;
        }
        return yyyymmdd + " 00:00:00";
    }

    public static String formatEndDate(String yyyymmdd){
        if( ! Chk.dateCheck(yyyymmdd)){
            return yyyymmdd;
        }
        return yyyymmdd + " 23:59:59";
    }

    public static String getDateString() {
        return getDateString(new Date(), false, "");
    }

    /**
     * 获得当前日期的格式化后的字符串
     * @return
     */
    public static String getYyyymmdd(){
        return getYyyymmdd(new Date());
    }

    /**
     * 获得当前时间的格式化后的字符串
     * @return
     */
    public static String getHHmmss(){
        return getHHmmss(new Date());
    }

    private static SimpleDateFormat getFormat(String format){
        return new SimpleDateFormat(format);
    }

    /**
     * 将日期字符串根据指定的格式转换成日期对象
     *
     * @param date   日期字符串
     * @param format 格式
     * @return
     */
    public static Date parse(String date, String format){
        try {
            return new SimpleDateFormat(format).parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 获得当前日期偏移量中的全部格式化日期字符串
     *
     * @param format     格式
     * @param offSetDay
     * @param offSetHour
     * @return
     */
    public static String[] getBetweenDate(String format, String offSetDay, String offSetHour){

        String [] rtn = new String [] {"",""};
        if(!Chk.spaceCheck(format) || !Chk.integerCheck(offSetHour) || !Chk.integerCheck(offSetHour)){
            return rtn;
        }


        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();

        Date currentDate = new Date();
        c1.setTime(currentDate);
        c2.setTime(currentDate);

        int d = Integer.parseInt(offSetDay);
        int h = Integer.parseInt(offSetHour);

        c1.set(Calendar.HOUR_OF_DAY, 0);
        c1.set(Calendar.MINUTE, 0);
        c1.set(Calendar.SECOND, 0);

        c2.set(Calendar.HOUR_OF_DAY, 0);
        c2.set(Calendar.MINUTE, 0);
        c2.set(Calendar.SECOND, 0);

        c1.add(Calendar.DAY_OF_MONTH, d);
        c2.add(Calendar.DAY_OF_MONTH, d);

        c1.add(Calendar.HOUR_OF_DAY, h * -1);
        c2.add(Calendar.HOUR_OF_DAY, 24 + h);

        c2.add(Calendar.SECOND	,-1);

        rtn[0] = getDateString(c1.getTime(), format);
        rtn[1] = getDateString(c2.getTime(), format);

        return rtn;
    }

    /**
     * 获取指定日期 偏移天后的时间
     * @param date 		日期
     * @param offset 	偏移天数
     * @param zeroPoint 	是否零点
     * @return
     */
    public static Date getOffsetDate(Date date,int offset,boolean ...zeroPoint ){
        Calendar c =Calendar.getInstance();

        Date d = null;
        if(zeroPoint == null || zeroPoint.length == 0 || ! zeroPoint[0]){
            d = date;
        }else{
            d = getZeroPointDate(date);
        }
        c.setTime(d);
        c.set(Calendar.DATE,c.get(Calendar.DATE) + offset);
        return c.getTime();
    }

    /**
     * 获取指定日期 偏移天后的时间
     * @param date 		日期
     * @param offsetDay 	偏移天数
     * @param offsetMin 	偏移小时数
     * @param offsetSec 	偏移秒数
     * @param zeroPoint 	是否零点
     * @return
     */
    public static Date getOffsetDate(Date date,int offsetDay,int offsetHour,int offsetMin,int offsetSec,boolean ...zeroPoint ){
        Calendar c =Calendar.getInstance();

        Date d = null;
        if(zeroPoint == null || zeroPoint.length == 0 || ! zeroPoint[0]){
            d = date;
        }else{
            d = getZeroPointDate(date);
        }
        c.setTime(d);
        c.set(Calendar.DATE,c.get(Calendar.DATE) + offsetDay);
        c.set(Calendar.HOUR,c.get(Calendar.HOUR) + offsetHour);
        c.set(Calendar.MINUTE, c.get(Calendar.MINUTE) + offsetMin);
        c.set(Calendar.SECOND,c.get(Calendar.SECOND) + offsetSec);
        return c.getTime();
    }

    /**
     *获取当前日期偏移天后的的时间
     * @param offset 	偏移天数
     * @param zeroPoint 	是否零点
     * @return
     */
    public static Date getOffsetDate(int offset,boolean ...zeroPoint){
        return getOffsetDate(new Date(), offset, zeroPoint);
    }


    public static Date getZeroPointDate(Date ... date){
        Calendar c =Calendar.getInstance();
        Date d  = null;
        if(date != null && date.length > 0){
            d = date[0];
        }else{
            d = new Date();
        }
        c.setTime(d);
        //设置当前时刻的小时为0
        c.set(Calendar.HOUR_OF_DAY, 0);
        //设置当前时刻的分钟为0
        c.set(Calendar.MINUTE, 0);
        //设置当前时刻的秒钟为0
        c.set(Calendar.SECOND, 0);
        //设置当前的毫秒钟为0
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }


    public static String add(String date,int type,int value,String format){
        Date d = parse(date, format);
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        c.add(type, value);
        return getDateString(c.getTime(), format);
    }

    public static String addHour(String date,int h,String format){
        return add(date, Calendar.HOUR_OF_DAY, h, format);
    }

    public static String addDay(String date,int d,String format){
        return add(date, Calendar.DAY_OF_MONTH, d, format);
    }

    public static String addMin(String date,int min,String format){
        return add(date, Calendar.MINUTE, min, format);
    }

    public static String  getFormatDateBetween(String startTime,String endTime,String ...format){
        String f = DateFormat.yMd_Hms;
        if(format != null && format.length != 0){
            f = format[0];
        }


        Calendar s = Calendar.getInstance();
        Calendar e = Calendar.getInstance();

        s.setTime(DateUtil.parse(startTime,f));
        e.setTime(DateUtil.parse(endTime,f));


        Long st = DateUtil.parse(startTime,f).getTime();
        Long et = DateUtil.parse(endTime,f).getTime();

        Long bw = ( et - st ) / 1000;

        if(bw < 60){
            return bw + "秒前";
        }
        if(( bw /= 60 )< 60){
            return bw + "分钟前";
        }
        if(( bw /= 60 ) < 24){
            return bw + "小时前";
        }
        if(( bw /= 24 ) < 30){
            return bw + "天前";
        }
        if(( bw /= 30 ) < 12){
            return bw + "月前";
        }
        bw/= 12;
        return bw + "年前";





//
//		int bw = e.get(Calendar.YEAR) - s.get(Calendar.YEAR);
//
//		if(bw > 0){
//			return bw + "年前";
//		}
//
//		bw = e.get(Calendar.MONTH) - s.get(Calendar.MONTH);
//		if (bw > 0){
//			return bw + "月前";
//		}
//
//		bw = e.get(Calendar.WEEK_OF_MONTH) - s.get(Calendar.WEEK_OF_MONTH);
//		if (bw > 0){
//			return bw + "周前";
//		}
//
//		bw = e.get(Calendar.DAY_OF_WEEK) - s.get(Calendar.DAY_OF_WEEK);
//		if (bw > 0){
//			return bw + "天前";
//		}
//
//		bw = e.get(Calendar.HOUR_OF_DAY) - s.get(Calendar.HOUR_OF_DAY);
//		if (bw > 0){
//			return bw + "小时前";
//		}
//
//		bw = e.get(Calendar.MINUTE) - s.get(Calendar.MINUTE);
//		if (bw > 0){
//			return bw + "分钟前";
//		}
//
//		bw = e.get(Calendar.SECOND) - s.get(Calendar.SECOND);
//		return bw + "秒前";



    }


    public static String formatDateInSysdate(String date,String format){
        if(date.startsWith(Constant.SYSDATE)){
            int  [] dms = new int [4];
            String [] str = date.split(Constant.SEPARATOR);

            for(int i = 1 ; i < str.length ; i ++){
                if(Chk.integerCheck(str[i])){
                    dms[i - 1] = Integer.parseInt(str[i]);
                }
            }
            date = DateUtil.getDateString(DateUtil.getOffsetDate(new Date(), dms[0], dms[1], dms[2],dms[3], true), format);
            return date;
        }else{
            return date;
        }
    }

    public static void main(String[] args) {
        String x = getFormatDateBetween("2015-10-25 18:30:00","2015-11-23 19:10:33");
        System.out.println(x);

    }

}
