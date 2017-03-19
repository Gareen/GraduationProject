package cn.sams.entity;

import java.io.Serializable;

/**
 * Created by Fanpeng on 2017/1/24.
 *
 * 页面资源管理
 */
public class ResourcesPath implements Serializable {

    private Integer id;
    private String title;
    private String path;
    private String icon;

    // level分为 1: 超级管理员可以访问, 2:教师访问 , 3:预留
    private Integer level;
    private Integer pid;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    @Override
    public String toString() {
        return "ResourcesPath{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", path='" + path + '\'' +
                ", icon='" + icon + '\'' +
                ", level=" + level +
                ", pid=" + pid +
                '}';
    }
}
