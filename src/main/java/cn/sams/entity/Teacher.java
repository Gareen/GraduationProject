package cn.sams.entity;

import java.io.Serializable;

/**
 * Created by Fanpeng on 2017/1/14.
 */
public class Teacher implements Serializable {

    // 教师工号
    private String tea_no;
    // 登录密码
    private String tea_password;
    // 教师姓名
    private String tea_name;
    // 控制权限
    private String tea_permission;

    public String getTea_no() {
        return tea_no;
    }

    public void setTea_no(String tea_no) {
        this.tea_no = tea_no;
    }

    public String getTea_password() {
        return tea_password;
    }

    public void setTea_password(String tea_password) {
        this.tea_password = tea_password;
    }

    public String getTea_name() {
        return tea_name;
    }

    public void setTea_name(String tea_name) {
        this.tea_name = tea_name;
    }

    public String getTea_permission() {
        return tea_permission;
    }

    public void setTea_permission(String tea_permission) {
        this.tea_permission = tea_permission;
    }

    @Override
    public String toString() {
        return "Teacher{" +
                "tea_no='" + tea_no + '\'' +
                ", tea_password='" + tea_password + '\'' +
                ", tea_name='" + tea_name + '\'' +
                ", tea_permission='" + tea_permission + '\'' +
                '}';
    }
}
