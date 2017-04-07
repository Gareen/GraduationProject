package cn.sams.dao.score;

import cn.sams.entity.Classes;
import cn.sams.entity.Course;
import cn.sams.entity.Group;
import cn.sams.entity.commons.SelectModel;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Fanpeng on 2017/4/4.
 */
@Repository
public interface GroupInitManagementDao {

    /**
     * 根据教师工号和学期号, 查询出这个老师上的课程
     *
     * @param teaNo  教师工号
     * @param termId 学期编号
     * @return
     */
    List<Course> queryCoursesByTeacherIdAndTerm(String teaNo, String termId);

    /**
     * 返回班级列表
     *
     * @param teaNo    教师工号
     * @param termId   学期ID
     * @param courseId 课程编号
     * @return
     */
    List<Classes> queryClasses(String teaNo, String termId, String courseId);

    /**
     * 根据group的encodeId查询出分组列表
     *
     * @param id
     * @return
     */
    List<Group> queryGroupsByGroupId(String id);

    /**
     * 根据小组id和小组组号来找到这个分组
     *
     * @param groupId
     * @param groupNum
     * @return
     */
    Group findGroupByGroupIdAndGroupNum(String groupId, String groupNum);

    /**
     * 根据分组id和小组长的id找到分组
     *
     * @param groupId
     * @param groupLea
     * @return
     */
    List<Group> findGroupByGroupIdAndLeader(String groupId, String groupLea);

    /**
     * 保存分组
     * @param groupId
     * @param groupLeader
     * @param groupMemers
     * @param groupNum
     * @return
     */
    Integer save(String groupId, String groupLeader, String groupMemers, String groupNum);

    /**
     * 更新分组
     *
     * @param groupId
     * @param groupLeader
     * @param groupMemers
     * @param groupNum
     * @return
     */
    Integer update(String groupId, String groupLeader, String groupMemers, String groupNum);

    /**
     * 删除记录
     *
     * @param groupId
     * @param groupNum
     * @return
     */
    Integer delete(String groupId, String groupNum);
}
