package cn.sams.dao.system;

import cn.sams.entity.Course;
import cn.sams.entity.commons.CourseClassPlace;
import cn.sams.entity.commons.CourseInfo;
import com.sun.jdi.IntegerType;
import org.apache.ibatis.annotations.Param;
import org.apache.poi.util.Internal;
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
    List<Map<String, String>> queryCourses();

    /**
     * 返回全部课程对应的课程名
     * @return
     */
    List<CourseInfo> queryCourseInfo();

    /**
     * 查询课程
     *
     * @param couNum
     * @return
     */
    Course queryCourseByCouNum(@Param("couNum") String couNum);

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

    /**
     * 删除上课信息
     *
     * @param num
     * @return
     */
    Integer delete(@Param("couNum") String num);

    /**
     * 根据课程号，删除课程信息
     *
     * @param couId
     * @return
     */
    Integer deleteCourseInfoByCouId(@Param("couId") String couId);

    /**
     * 保存课程信息
     *
     * @return
     */
    Integer saveCourseInfo(@Param("cno") String cno, @Param("cname") String cname, @Param("cunit") String cunit);

    /**
     * 更新课程信息
     *
     * @param cno
     * @param cname
     * @param cunit
     */
    Integer updateCourseInfo(@Param("cno") String cno, @Param("cname") String cname, @Param("cunit") String cunit);

    /**
     * 保存上课信息
     *
     * @param courseId  课程号
     * @param couCredit 课程学分
     * @param couPeriod 课程学时
     * @param couCounts 选课人数
     * @param couTea    上课教师
     * @param couClz    上课班级
     * @param couTerm   上课学期
     * @param timePlace 上课时间和地点
     * @return
     */
    Integer save(@Param("cid") String courseId, @Param("cre") String couCredit, @Param("per") String couPeriod,
                 @Param("counts") String couCounts, @Param("tea") String couTea, @Param("clz") String couClz,
                 @Param("term") String couTerm, @Param("tp") String timePlace);

    /**
     * 更新上课信息
     *
     * @param couNum    key
     * @param courseId  课程号
     * @param couCredit 课程学分
     * @param couPeriod 课程学时
     * @param couCounts 选课人数
     * @param couTea    上课教师
     * @param couClz    上课班级
     * @param couTerm   上课学期
     * @param timePlace 上课时间和地点
     * @return
     */
    Integer update(@Param("cno") String couNum, @Param("cid") String courseId, @Param("cre") String couCredit,
                   @Param("per") String couPeriod, @Param("counts") String couCounts, @Param("tea") String couTea,
                   @Param("clz") String couClz, @Param("term") String couTerm, @Param("tp") String timePlace);

    /**
     * 根据课程号删除上课信息
     *
     * @param courseId
     */
    void deleteCourseByCourseId(String courseId);

    int getLastInsertId();
}
