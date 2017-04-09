package cn.sams.common.util;

import cn.sams.entity.commons.SelectModel;

import java.util.*;

/**
 * Created by Fanpeng on 2017/2/26.
 */
public class SelectModelUtil {

    /**
     * 返回一个解析好的键值对组合
     * @param fileName 文件名
     * @param reload  是否加入缓存
     * @return
     */
    public static List<SelectModel> getSelectModels(String fileName, boolean reload) {
        // false将文件加载入缓存, 下次再使用的时候, 先从缓存中找, 如果缓存中没有的话, 将从文件中再次读取
        LinkedHashMap<String,String> pros = new LinkedHashMap<String,String>((Map) ResourceUtil.getProperties(fileName, reload));

        Set<Map.Entry<String, String>> entries = pros.entrySet();
        List<SelectModel> models = new ArrayList<SelectModel>();

        for (Map.Entry<String, String> entry : entries) {
            SelectModel model = new SelectModel();
            model.setKey(entry.getKey());
            model.setValue(entry.getValue());
            models.add(model);
        }

        return models;
    }

    /**
     * 返回一个键值对模型
     * @param key model的key
     * @param value model的value
     * @return
     */
    public static SelectModel getSelectModel(String key, String value) {
        SelectModel selectModel = new SelectModel();
        selectModel.setKey(key);
        selectModel.setValue(value);
        return selectModel;
    }

    /**
     * 获得周数
     * @return
     */
    public static List<SelectModel> getWeeks() {
        return SelectModelUtil.getSelectModels("config/week.properties", false);
    }

    /**
     * 获得星期
     * @return
     */
    public static List<SelectModel> getDaysOfWeek() {
        return SelectModelUtil.getSelectModels("config/day_of_week.properties", false);
    }

    /**
     * 获得上课时间段
     * @return
     */
    public static List<SelectModel> getClassTime() {
        return SelectModelUtil.getSelectModels("config/class_time.properties", false);
    }

    /**
     * 将一个普通的对象list集合转换成键值对集合
     * <p>
     * 暂时还未完成
     *
     * @param list   普通对象集合
     * @param keyStr 对象中指定的key的属性名
     * @param valStr 对象中指定作为value的属性名
     * @return List<SelectModel>
     */
    public static List<SelectModel> objectListToSelectModelList(List<Object> list, Class<?> clz, String keyStr, String valStr) {

        if (clz == null) {

        }

        if (list != null && list.size() != 0) {

            // 先判断是不是该类的实例
            if (!list.get(0).getClass().getClassLoader().getClass().getName().equals(
                    clz.getClass().getClassLoader().getClass().getName())) {

                return new ArrayList<>();
            }
            System.out.println(1);

            for (Object obj : list) {

            }

        }
        return new ArrayList<>();
    }
}
