package cn.sams.dao.score;

import cn.sams.entity.Classes;
import cn.sams.entity.Course;
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
}
