package cn.sams.entity;

import java.io.Serializable;

/**
 * Created by Fanpeng on 2017/4/15.
 * <p>
 * 期末成绩
 */
public class FinalGrade implements Serializable {

    // 期末成绩编码
    private String final_id;
    // 本学期作业分数
    private String final_work_score;
    // 本学期实验成绩
    private String final_exp_score;
    // 本学期考试成绩
    private String final_exam_score;
    // 最终成绩
    private String final_score;
    // 学生学号
    private String final_stu_id;
    // 其他情况
    private String final_remark;

    public String getFinal_id() {
        return final_id;
    }

    public void setFinal_id(String final_id) {
        this.final_id = final_id;
    }

    public String getFinal_work_score() {
        return final_work_score;
    }

    public void setFinal_work_score(String final_work_score) {
        this.final_work_score = final_work_score;
    }

    public String getFinal_exp_score() {
        return final_exp_score;
    }

    public void setFinal_exp_score(String final_exp_score) {
        this.final_exp_score = final_exp_score;
    }

    public String getFinal_exam_score() {
        return final_exam_score;
    }

    public void setFinal_exam_score(String final_exam_score) {
        this.final_exam_score = final_exam_score;
    }

    public String getFinal_score() {
        return final_score;
    }

    public void setFinal_score(String final_score) {
        this.final_score = final_score;
    }

    public String getFinal_stu_id() {
        return final_stu_id;
    }

    public void setFinal_stu_id(String final_stu_id) {
        this.final_stu_id = final_stu_id;
    }

    public String getFinal_remark() {
        return final_remark;
    }

    public void setFinal_remark(String final_remark) {
        this.final_remark = final_remark;
    }

    @Override
    public String toString() {
        return "FinalGrade{" +
                "final_id='" + final_id + '\'' +
                ", final_stu_id='" + final_stu_id + '\'' +
                '}';
    }
}
