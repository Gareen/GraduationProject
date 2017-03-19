package cn.sams.dao.score;

import cn.sams.entity.Course;
import cn.sams.entity.Student;
import cn.sams.entity.Teacher;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Fanpeng on 2017/2/27.
 */
@Repository
public interface GroupManagementDao {

    /**
     * 查询所有的学生
     * @return
     */
    List<Student> queryStu();

    /**
     * 查询指定的
     * @param courseKey 课程编号
     * @param classPlace 上课地点编号
     * @param classWeek 上课周
     * @param classTime 上课时间段
     * @return
     */
    Course queryTeacherIdByInfo(String courseKey, String classPlace, String classWeek, String dayOfWeek, String classTime);

    /**
     * 根据教师工号查询教师的信息
     * @param id 教师工号
     * @return
     */
    Teacher queryTeacherName(String id);

    Integer save(String groupId, String stuLeader, String stuMem, String groupNum, String score);
}
