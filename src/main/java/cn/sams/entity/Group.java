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
    private String ex_index;
    private String pre_score;
    private String ex_score;
    private String re_score;
    private String score;

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

    public String getEx_index() {
        return ex_index;
    }

    public void setEx_index(String ex_index) {
        this.ex_index = ex_index;
    }

    public String getPre_score() {
        return pre_score;
    }

    public void setPre_score(String pre_score) {
        this.pre_score = pre_score;
    }

    public String getEx_score() {
        return ex_score;
    }

    public void setEx_score(String ex_score) {
        this.ex_score = ex_score;
    }

    public String getRe_score() {
        return re_score;
    }

    public void setRe_score(String re_score) {
        this.re_score = re_score;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
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
                ", group_score='" + '\'' +
                '}';
    }
}
