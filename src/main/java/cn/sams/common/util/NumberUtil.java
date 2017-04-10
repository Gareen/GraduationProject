package cn.sams.common.util;

/**
 * Created by Fanpeng on 2017/4/10.
 */
public class NumberUtil {

    public static int INT_ZERO = 0;
    public static double DOUBLE_ZERO = 0.0;


    /**
     * 将数字格式字符串转换成double类
     * 如果转换出错, 默认为0.0
     *
     * @param numberStr 数字字符
     * @return
     */
    public static Double getStrToDouble(String numberStr) {
        Double d;
        try {
            d = Double.parseDouble(numberStr);
        } catch (Exception e) {
            d = DOUBLE_ZERO;
        }

        return d;
    }

}
