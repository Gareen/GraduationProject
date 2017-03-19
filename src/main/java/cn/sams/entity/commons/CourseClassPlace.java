package cn.sams.entity.commons;

import java.io.Serializable;

/**
 * Created by Fanpeng on 2017/2/27.
 */
public class CourseClassPlace implements Serializable {

    private Integer place_id;
    private String place_name;
    private Integer place_pid;

    public Integer getPlace_id() {
        return place_id;
    }

    public void setPlace_id(Integer place_id) {
        this.place_id = place_id;
    }

    public String getPlace_name() {
        return place_name;
    }

    public void setPlace_name(String place_name) {
        this.place_name = place_name;
    }

    public Integer getPlace_pid() {
        return place_pid;
    }

    public void setPlace_pid(Integer place_pid) {
        this.place_pid = place_pid;
    }

    @Override
    public String toString() {
        return "CourseClassPlace{" +
                "place_id=" + place_id +
                ", place_name='" + place_name + '\'' +
                ", place_pid=" + place_pid +
                '}';
    }
}
