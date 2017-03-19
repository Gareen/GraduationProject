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
    private String group_score;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Group group = (Group) o;

        return group_id != null ? group_id.equals(group.group_id) : group.group_id == null;
    }

    @Override
    public int hashCode() {
        return group_id != null ? group_id.hashCode() : 0;
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
