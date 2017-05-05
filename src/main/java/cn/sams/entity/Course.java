package cn.sams.entity;

import java.io.Serializable;

/**
 * Created by Fanpeng on 2017/2/27.
 * Modify by Fanpeng on 2017/3/5.
 * Modify by Fanpeng on 2017/5/5.
 * 课程实体类
 */
public class Course implements Serializable {

    private String cou_number;
    private String course_id;
    private Integer cou_credit;
    private Integer cou_period;
    private String cou_tea_no;
    private String cou_counts;
    private String class_time_place;
    private String cou_term_id;
    private String class_id;

    public Course() {
    }


    public String getCourse_id() {
        return course_id;
    }

    public void setCourse_id(String course_id) {
        this.course_id = course_id;
    }

    public Integer getCou_credit() {
        return cou_credit;
    }

    public void setCou_credit(Integer cou_credit) {
        this.cou_credit = cou_credit;
    }

    public Integer getCou_period() {
        return cou_period;
    }

    public void setCou_period(Integer cou_period) {
        this.cou_period = cou_period;
    }

    public String getCou_tea_no() {
        return cou_tea_no;
    }

    public void setCou_tea_no(String cou_tea_no) {
        this.cou_tea_no = cou_tea_no;
    }

    public String getCou_number() {
        return cou_number;
    }

    public void setCou_number(String cou_number) {
        this.cou_number = cou_number;
    }

    public String getCou_counts() {
        return cou_counts;
    }

    public void setCou_counts(String cou_counts) {
        this.cou_counts = cou_counts;
    }

    public String getCou_term_id() {
        return cou_term_id;
    }

    public void setCou_term_id(String cou_term_id) {
        this.cou_term_id = cou_term_id;
    }

    public String getClass_id() {
        return class_id;
    }

    public void setClass_id(String class_id) {
        this.class_id = class_id;
    }

    public String getClass_time_place() {
        return class_time_place;
    }

    public void setClass_time_place(String class_time_place) {
        this.class_time_place = class_time_place;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Course course = (Course) o;

        return course_id != null ? course_id.equals(course.course_id) : course.course_id == null;
    }

    @Override
    public int hashCode() {
        return course_id != null ? course_id.hashCode() : 0;
    }


}
