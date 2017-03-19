package cn.sams.entity;

import java.io.Serializable;

/**
 * Created by Fanpeng on 2017/1/14.
 */
public class Student implements Serializable {

    // 学生学号
    private String stu_no;
    // 学生姓名
    private String stu_name;
    // 学生性别
    private String stu_gender;
    // 学生所在班级编号
    private Integer stu_class_id;

    public String getStu_no() {
        return stu_no;
    }

    public void setStu_no(String stu_no) {
        this.stu_no = stu_no;
    }

    public String getStu_name() {
        return stu_name;
    }

    public void setStu_name(String stu_name) {
        this.stu_name = stu_name;
    }

    public String getStu_gender() {
        return stu_gender;
    }

    public void setStu_gender(String stu_gender) {
        this.stu_gender = stu_gender;
    }

    public Integer getStu_class_id() {
        return stu_class_id;
    }

    public void setStu_class_id(Integer stu_class_id) {
        this.stu_class_id = stu_class_id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Student student = (Student) o;

        return stu_no != null ? stu_no.equals(student.stu_no) : student.stu_no == null;
    }

    @Override
    public int hashCode() {
        return stu_no != null ? stu_no.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Student{" +
                "stu_no='" + stu_no + '\'' +
                ", stu_name='" + stu_name + '\'' +
                '}';
    }
}
