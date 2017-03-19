package cn.sams.dao.system;

import cn.sams.entity.Course;
import cn.sams.entity.commons.CourseClassPlace;
import cn.sams.entity.commons.CourseInfo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Fanpeng on 2017/2/27.
 */
@Repository
public interface CourseDao {

    /**
     * 返回全部课程的详细信息
     * @return
     */
    List<Course> queryCourses();

    /**
     * 返回全部课程对应的课程名
     * @return
     */
    List<CourseInfo> queryCourseInfo();

    /**
     * 返回课程
     * @param key 课程编号
     * @return
     */
    List<Course> queryCoursesByCourseKey(String key);

    /**
     * 返回上课地点
     * @param id 教室编号
     * @return
     */
    CourseClassPlace queryCoursePlaceById(String id);

    /**
     * 返回课程列表
     * @param key 课程编号
     * @param week 上课周数
     * @return
     */
    List<Course> queryCoursesByCourseKeyAndWeek(String key, String week);
}
