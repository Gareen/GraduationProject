package cn.sams.entity;

import java.io.Serializable;

/**
 * Created by Fanpeng on 2017/3/19.
 */
public class Group implements Serializable {

    private String group_id;
    private String stu_is_leader;
    private String stu_is_member;
    private String group_num;
    private String group_pre_score;
    private String group_score;
    private String group_re_score;
    private String score_result;

    public Group() {
    }

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public String getStu_is_leader() {
        return stu_is_leader;
    }

    public void setStu_is_leader(String stu_is_leader) {
        this.stu_is_leader = stu_is_leader;
    }

    public String getStu_is_member() {
        return stu_is_member;
    }

    public void setStu_is_member(String stu_is_member) {
        this.stu_is_member = stu_is_member;
    }

    public String getGroup_num() {
        return group_num;
    }

    public void setGroup_num(String group_num) {
        this.group_num = group_num;
    }

    public String getGroup_score() {
        return group_score;
    }

    public void setGroup_score(String group_score) {
        this.group_score = group_score;
    }

    public String getGroup_pre_score() {
        return group_pre_score;
    }

    public void setGroup_pre_score(String group_pre_score) {
        this.group_pre_score = group_pre_score;
    }

    public String getGroup_re_score() {
        return group_re_score;
    }

    public void setGroup_re_score(String group_re_score) {
        this.group_re_score = group_re_score;
    }

    public String getScore_result() {
        return score_result;
    }

    public void setScore_result(String score_result) {
        this.score_result = score_result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Group group = (Group) o;

        if (group_id != null ? !group_id.equals(group.group_id) : group.group_id != null) return false;
        return group_num != null ? group_num.equals(group.group_num) : group.group_num == null;
    }

    @Override
    public int hashCode() {
        int result = group_id != null ? group_id.hashCode() : 0;
        result = 31 * result + (group_num != null ? group_num.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Group{" +
                "group_id='" + group_id + '\'' +
                ", stu_is_leader='" + stu_is_leader + '\'' +
                ", stu_is_member='" + stu_is_member + '\'' +
                ", group_num='" + group_num + '\'' +
                ", group_score='" + group_score + '\'' +
                '}';
    }
}
