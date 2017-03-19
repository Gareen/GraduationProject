package cn.sams.dao.system;

import cn.sams.entity.Teacher;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Fanpeng on 2017/1/29.
 */
@Repository
public interface TeacherManagerDao {

    /**
     * 查找所有的教师
     * @return List<Teacher>
     */
    List<Teacher> findTeachers();

    /**
     * 根据工号查找到教师
     * @param id 教师工号
     * @return
     */
    Teacher queryTeaById(String id);

    /**
     * 新增教师
     * @param no 教师工号
     * @param name 教师姓名
     * @param pwd 密码
     * @param pre 角色控制权限
     * @return
     */
    Integer saveTeacher(String no, String pwd, String name, String pre);

    /**
     * 更新教师
     * @param no 教师工号
     * @param name 教师姓名
     * @param pwd 密码
     * @param pre 角色控制权限
     * @return
     */
    Integer updateTeacher(String no, String pwd, String name, String pre);

    /**
     * 删除教师
     * @param id 教师工号
     * @return
     */
    Integer deleteTeaById(String id);
}
