package cn.sams.entity.commons;

import java.io.Serializable;

/**
 * Created by Fanpeng on 2017/2/27.
 */
public class CourseInfo implements Serializable {

    private String course_id;
    private String course_name;
    private String course_unit;

    public CourseInfo() {
    }

    public CourseInfo(String course_id, String course_name, String course_unit) {
        this.course_id = course_id;
        this.course_name = course_name;
        this.course_unit = course_unit;
    }

    public String getCourse_id() {
        return course_id;
    }

    public void setCourse_id(String course_id) {
        this.course_id = course_id;
    }

    public String getCourse_name() {
        return course_name;
    }

    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }

    public String getCourse_unit() {
        return course_unit;
    }

    public void setCourse_unit(String course_unit) {
        this.course_unit = course_unit;
    }

    @Override
    public String toString() {
        return "CourseInfo{" +
                "course_id=" + course_id +
                ", course_name='" + course_name + '\'' +
                '}';
    }
}
