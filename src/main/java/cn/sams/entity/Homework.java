package cn.sams.entity;

import java.io.Serializable;

/**
 * Created by Fanpeng on 2017/4/9.
 */
public class Homework implements Serializable {

    private String work_id;
    private String work_stu_id;
    private String work_index;
    private String work_score;

    public String getWork_id() {
        return work_id;
    }

    public void setWork_id(String work_id) {
        this.work_id = work_id;
    }

    public String getWork_stu_id() {
        return work_stu_id;
    }

    public void setWork_stu_id(String work_stu_id) {
        this.work_stu_id = work_stu_id;
    }

    public String getWork_index() {
        return work_index;
    }

    public void setWork_index(String work_index) {
        this.work_index = work_index;
    }

    public String getWork_score() {
        return work_score;
    }

    public void setWork_score(String work_score) {
        this.work_score = work_score;
    }

    @Override
    public String toString() {
        return "Homework{" +
                "work_id='" + work_id + '\'' +
                ", work_stu_id='" + work_stu_id + '\'' +
                ", work_index='" + work_index + '\'' +
                ", work_score='" + work_score + '\'' +
                '}';
    }
}
