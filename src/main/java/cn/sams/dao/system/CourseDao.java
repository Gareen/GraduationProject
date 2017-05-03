package cn.sams.dao.system;

import cn.sams.entity.Course;
import cn.sams.entity.commons.CourseClassPlace;
import cn.sams.entity.commons.CourseInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

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

    /**
     * 根据课程的编号, 查询到课程的信息
     *
     * @param couId
     * @return
     */
    CourseInfo queryCourseInfoByCouId(String couId);

    /**
     * 根据条件查找到唯一的course
     *
     * @param courseId
     * @param teaNo
     * @param termId
     * @return
     */
    Map<String, Object> queryCourseToMap(@Param("courseId") String courseId, @Param("teaNo") String teaNo,
                                         @Param("termId") String termId, @Param("classId") String classId);

    /**
     * 根据教师工号，学期号，班级号查询是否存在这样的班级
     *
     * @param teaId
     * @param termId
     * @param classId
     * @return
     */
    List<Course> queryCoursesByTeaIdAndTermIDAndClassId(@Param("teaId") String teaId, @Param("termId") String termId,
                                                        @Param("classId") String classId);
}
