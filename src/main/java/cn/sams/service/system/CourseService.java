package cn.sams.service.system;

import cn.sams.common.constants.Constant;
import cn.sams.common.util.Chk;
import cn.sams.common.util.SelectModelUtil;
import cn.sams.dao.system.ClassManagementDao;
import cn.sams.dao.system.CourseDao;
import cn.sams.dao.system.TeacherManagerDao;
import cn.sams.dao.system.TermManagementDao;
import cn.sams.entity.Course;
import cn.sams.entity.Teacher;
import cn.sams.entity.commons.CourseInfo;
import cn.sams.entity.commons.ReturnObj;
import cn.sams.entity.commons.SelectModel;
import com.sun.org.apache.regexp.internal.RE;
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

    @Resource
    private ClassManagementDao classManagementDao;

    @Resource
    private TermManagementDao termManagementDao;

    public List<Map<String, String>> queryAllCourses() {
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

    public List<CourseInfo> queryCourseInfos() {
        return courseDao.queryCourseInfo();
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

        List<Map<String, String>> courses = queryAllCourses();

        if (Chk.emptyCheck(courses)) {

            for (Map c : courses) {

                Map<String, String> map = new HashMap<>();
                map.put("couNum", c.get("cou_number").toString());
                map.put("couId", c.get("course_id").toString());
                map.put("couName", c.get("course_name") + "");
                map.put("teaName", getTeacherName(c.get("cou_tea_no") + ""));
                map.put("couTimePlace", c.get("class_time_place") + "");
                map.put("couCount", c.get("cou_counts") + "");
                map.put("couTerm", getTermName(c.get("cou_term_id") + ""));
                map.put("couClass", getClassName(c.get("class_id") + ""));

                list.add(map);
            }
            return list;

        } else {
            return new ArrayList<>();
        }

    }

    private String getTeacherName(String teaId) {
        return teacherManagerDao.queryTeaById(teaId).getTea_name();
    }

    private String getCoruseTimeAndPlace(String couNum) {
        return courseDao.queryCourseByCouNum(couNum).getClass_time_place();
    }

    private String getClassName(String classId) {
        return classManagementDao.queryClassByClassId(classId).getClass_name();
    }

    private String getTermName(String termId) {
        return termManagementDao.queryTermByTermId(termId).getTerm_name();
    }


    public ReturnObj delete(HttpServletRequest req) {

        String couNum = req.getParameter("couNum");
        String promis = req.getParameter("promis");

        if (!Chk.spaceCheck(couNum) || !Chk.spaceCheck(promis)) {
            return new ReturnObj(Constant.ERROR, "课程唯一码/教师权限为空！", null);
        }

        if (!"1".equalsIgnoreCase(promis)) {
            return new ReturnObj(Constant.ERROR, "权限不足，请联系管理员！", null);
        }

        int count = courseDao.delete(couNum);

        if (count < 1) {

            return new ReturnObj(Constant.ERROR, "删除失败：数据库异常！", null);
        }
        return new ReturnObj(Constant.SUCCESS, "删除成功！", couNum);

    }

    public ReturnObj queryCourseById(HttpServletRequest req) {
        String couNum = req.getParameter("couNum");

        if (!Chk.spaceCheck(couNum)) {
            return new ReturnObj(Constant.ERROR, "查询失败：关键数据缺失！", null);
        }

        Course course = courseDao.queryCourseByCouNum(couNum);

        return new ReturnObj(Constant.SUCCESS, "", course);
    }

}
