package cn.sams.common.util;


import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;

/**
 * Created by 3701 on 2015/10/27.
 */
public class ReflectUtil {


    /**
     * 判断这个类中是否这个属性
     * @param fieldName
     * @param clz
     * @return
     */
    public static boolean hasField(String fieldName,Class clz){
        try {
            Field f = clz.getDeclaredField(fieldName);
            return f != null;
        } catch (NoSuchFieldException e) {
            return false;
        }
    }


    /**
     *通过字段名强制获取值
     * @param propertyName
     * @param obj
     * @return
     */
    public static <T>T getValue(String propertyName, Object obj) {
        if(obj == null){
            return null;
        }
        try {
            Field field = obj.getClass().getDeclaredField(propertyName);
            field.setAccessible(true);
            return (T) field.get(obj);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static <T> T getAnnotation(String propertyName ,Class annotationClass, Object obj) {
        if(obj == null){
            return null;
        }
        try{
            return (T)obj.getClass().getDeclaredField(propertyName).getAnnotation(annotationClass);

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }


    /**
     *通过字段名强制设值
     * @param propertyName
     * @param obj
     * @param value
     * @return
     */
    public static boolean setValue(String propertyName, Object obj,Object value) {
        if(obj == null){
            return false;
        }
        try {
            Field field = obj.getClass().getDeclaredField(propertyName);
            field.setAccessible(true);
            field.set(obj, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 通过get方法获取值
     * @param propertyName
     * @param obj
     * @return
     */
    public static <T> T getValueByGet(String propertyName, Object obj) {
        if(obj == null){
            return null;
        }
        try {
            PropertyDescriptor descriptor = new PropertyDescriptor(propertyName,obj.getClass());
            return (T) descriptor.getReadMethod().invoke(obj);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }


    }

    /**
     *通过set方法设置值
     * @param propertyName
     * @param obj
     * @param value
     * @return
     */
    public static boolean setValueBySet(String propertyName, Object obj,Object value) {
        if(obj == null){
            return false;
        }
        try {
            PropertyDescriptor descriptor = new PropertyDescriptor(propertyName,obj.getClass());
            descriptor.getWriteMethod().invoke(obj,value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }


    }


}
