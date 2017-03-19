package cn.sams.entity;

import java.io.Serializable;

/**
 * Created by Fanpeng on 2017/2/27.
 * Modify by Fanpeng on 2017/3/5.
 * 课程实体类
 */
public class Course implements Serializable {

    private Integer cou_number;
    private String course_id;
    private Integer cou_credit;
    private Integer cou_period;
    private String cou_tea_no;
    private Integer cou_counts;
    private String cou_week;
    private String cou_day_of_week;
    private String cou_time;
    private Integer cou_place_id;
    private String cou_institute; // 开课单位
    private Integer cou_term_id;
    private Integer cou_check_id;
    private Integer cou_work_id;
    private Integer cou_exp_id;
    private Integer cou_final_id;
    private Integer cou_ana_id;

    public Course() {
    }

    public Integer getCou_number() {
        return cou_number;
    }

    public void setCou_number(Integer cou_number) {
        this.cou_number = cou_number;
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

    public Integer getCou_counts() {
        return cou_counts;
    }

    public void setCou_counts(Integer cou_counts) {
        this.cou_counts = cou_counts;
    }

    public String getCou_week() {
        return cou_week;
    }

    public void setCou_week(String cou_week) {
        this.cou_week = cou_week;
    }

    public String getCou_day_of_week() {
        return cou_day_of_week;
    }

    public void setCou_day_of_week(String cou_day_of_week) {
        this.cou_day_of_week = cou_day_of_week;
    }

    public String getCou_time() {
        return cou_time;
    }

    public void setCou_time(String cou_time) {
        this.cou_time = cou_time;
    }

    public Integer getCou_place_id() {
        return cou_place_id;
    }

    public void setCou_place_id(Integer cou_place_id) {
        this.cou_place_id = cou_place_id;
    }

    public String getCou_institute() {
        return cou_institute;
    }

    public void setCou_institute(String cou_institute) {
        this.cou_institute = cou_institute;
    }

    public Integer getCou_term_id() {
        return cou_term_id;
    }

    public void setCou_term_id(Integer cou_term_id) {
        this.cou_term_id = cou_term_id;
    }

    public Integer getCou_check_id() {
        return cou_check_id;
    }

    public void setCou_check_id(Integer cou_check_id) {
        this.cou_check_id = cou_check_id;
    }

    public Integer getCou_work_id() {
        return cou_work_id;
    }

    public void setCou_work_id(Integer cou_work_id) {
        this.cou_work_id = cou_work_id;
    }

    public Integer getCou_exp_id() {
        return cou_exp_id;
    }

    public void setCou_exp_id(Integer cou_exp_id) {
        this.cou_exp_id = cou_exp_id;
    }

    public Integer getCou_final_id() {
        return cou_final_id;
    }

    public void setCou_final_id(Integer cou_final_id) {
        this.cou_final_id = cou_final_id;
    }

    public Integer getCou_ana_id() {
        return cou_ana_id;
    }

    public void setCou_ana_id(Integer cou_ana_id) {
        this.cou_ana_id = cou_ana_id;
    }

    @Override
    public String toString() {
        return "Course{" +
                "cou_number=" + cou_number +
                ", course_id='" + course_id + '\'' +
                ", cou_tea_no='" + cou_tea_no + '\'' +
                ", cou_week='" + cou_week + '\'' +
                ", cou_day_of_week='" + cou_day_of_week + '\'' +
                ", cou_time='" + cou_time + '\'' +
                ", cou_place_id=" + cou_place_id +
                '}';
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
