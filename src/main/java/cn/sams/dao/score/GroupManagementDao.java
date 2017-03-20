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
     * 查询分组的信息
     *
     * @param groupId  分组id
     * @param groupNum 分组编号
     * @return
     */
    Group queryGroupInfoByIdAndNum(String groupId, String groupNum);

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


    /**
     * 更新分组记录
     *
     * @param groupId   分组id
     * @param stuLeader 小组组长
     * @param stuMem    小组成员
     * @param groupNum  小组组号
     * @param score     小组分数
     * @return
     */
    Integer update(String groupId, String stuLeader, String stuMem, String groupNum, String score);

    /**
     * 通过学生学号查找到学生
     *
     * @param stuId
     * @return
     */
    Student findStuById(String stuId);
}
