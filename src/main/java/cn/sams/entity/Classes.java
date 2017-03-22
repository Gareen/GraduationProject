package cn.sams.entity;

import java.io.Serializable;

/**
 * Created by Fanpeng on 2017/3/22.
 * <p>
 * 班级实体类
 */
public class Classes implements Serializable {

    private String class_id;
    private String class_name;

    public String getClass_id() {
        return class_id;
    }

    public void setClass_id(String class_id) {
        this.class_id = class_id;
    }

    public String getClass_name() {
        return class_name;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }

    @Override
    public String toString() {
        return "Classes{" +
                "class_id='" + class_id + '\'' +
                ", class_name='" + class_name + '\'' +
                '}';
    }
}
