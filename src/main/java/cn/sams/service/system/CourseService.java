package cn.sams.service.system;

import cn.sams.common.util.Chk;
import cn.sams.common.util.SelectModelUtil;
import cn.sams.dao.system.CourseDao;
import cn.sams.dao.system.TeacherManagerDao;
import cn.sams.entity.Course;
import cn.sams.entity.commons.CourseInfo;
import cn.sams.entity.commons.SelectModel;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by Fanpeng on 2017/2/27.
 */
@Service("courseService")
public class CourseService {

    @Resource
    private CourseDao courseDao;

    @Resource
    private TeacherManagerDao teacherManagerDao;

    public List<Course> queryAllCourses() {
        return courseDao.queryCourses();
    }

    public List<SelectModel> queryCourseInfo() {
        List<CourseInfo> courses = courseDao.queryCourseInfo();
        if (courses == null) {
            return new ArrayList<>();
        }

        List<SelectModel> list = new ArrayList<>();
        for (CourseInfo course : courses) {
            SelectModel model = new SelectModel();
            model.setKey(course.getCourse_id().toString());
            model.setValue(course.getCourse_name());
            list.add(model);
        }
        return list;
    }

    /**
     * 封装在页面上显示的课程信息
     * 显示 课程编号, 课程名称, 上课老师, 上课时间
     *
     * @param req
     * @return
     */
    public List<Map<String, String>> displayCourseInfo(HttpServletRequest req) {

        List<Map<String, String>> list = new ArrayList<>();

        List<Course> courses = queryAllCourses();

        if (Chk.emptyCheck(courses)) {

            for (Course c : courses) {

                Map<String, String> map = new HashMap<>();

                map.put("couId", c.getCourse_id());
                String couName = courseDao.queryCourseInfoByCouId(c.getCourse_id()).getCourse_name();
                map.put("couName", Chk.spaceCheck(couName) ? couName : "");
                String teaName = teacherManagerDao.queryTeaById(c.getCou_tea_no()).getTea_name();
                map.put("teaName", teaName);
                map.put("couTime", queryCoruseTime());

                list.add(map);
            }
            return list;
        } else {
            return new ArrayList<>();
        }

    }

    public String queryCoruseTime() {
        return "";
    }


}
