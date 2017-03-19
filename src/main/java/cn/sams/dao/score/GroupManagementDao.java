package cn.sams.dao.score;

import cn.sams.entity.Course;
import cn.sams.entity.Group;
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

    /**
     * 根据分组id来查询分组
     * @param groupId
     * @return
     */
    List<Group> queryGroupsByGroupId(String groupId);

    /**
     * 删除分组记录
     * @param groupId
     * @param groupNum
     * @return
     */
    Integer deleteRowByIdAndNum(String groupId, String groupNum);

    /**
     * 保存新增记录
     * @param groupId
     * @param stuLeader
     * @param stuMem
     * @param groupNum
     * @param score
     * @return
     */
    Integer save(String groupId, String stuLeader, String stuMem, String groupNum, String score);
}
