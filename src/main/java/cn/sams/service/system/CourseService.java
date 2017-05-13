package cn.sams.service.system;

import cn.sams.common.constants.Constant;
import cn.sams.common.util.Chk;
import cn.sams.common.util.JsonUtil;
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
import cn.sams.service.score.GroupInitManagementService;
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

    public List<SelectModel> queryCourseInfoToSelectModel() {
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

    public List<SelectModel> queryCoursesSelectModel() {
        List<CourseInfo> courseInfos = courseDao.queryCourseInfo();

        if (!Chk.emptyCheck(courseInfos)) {
            return new ArrayList<>();
        }

        List<SelectModel> sms = new ArrayList<>();

        for (CourseInfo ci : courseInfos) {
            SelectModel sm = new SelectModel();

            sm.setValue(ci.getCourse_id() + "");
            sm.setKey(ci.getCourse_name());
            sms.add(sm);
        }

        return sms;
    }

    public CourseInfo queryCourseInfoById(HttpServletRequest req) {

        String courseId = req.getParameter("courseId");

        if (!Chk.spaceCheck(courseId)) {
            return null;
        }

        return courseDao.queryCourseInfoByCouId(courseId);
    }

    public ReturnObj deleteCourseInfoByCouId(HttpServletRequest req) {
        String courseId = req.getParameter("courseId");
        String promis = req.getParameter("promis");

        if (!Chk.spaceCheck(courseId) || !Chk.spaceCheck(promis)) {
            return new ReturnObj(Constant.ERROR, "删除失败：关键信息缺失！", null);
        }

        if (!"1".equalsIgnoreCase(promis)) {
            return new ReturnObj(Constant.ERROR, "删除失败：权限不足！", null);
        }

        int count = courseDao.deleteCourseInfoByCouId(courseId);

        if (count < 1) {
            return new ReturnObj(Constant.ERROR, "删除失败：无此数据，请刷新后重试！", null);
        } else {

            // todo 删除对应的上课信息，暂时预留
            return new ReturnObj(Constant.SUCCESS, "删除成功！", courseId);
        }
    }

    public ReturnObj saveOrUpdateCouInfo(HttpServletRequest req) {
        String data = req.getParameter("data");

        if (!Chk.spaceCheck(data)) {
            return new ReturnObj(Constant.ERROR, "操作失败：关键数据缺失！", null);
        }

        Map<String, String> datas = JsonUtil.toMap(data, String.class, String.class);

        if (!Chk.emptyCheck(datas)) {
            return new ReturnObj(Constant.ERROR, "操作失败，关键数据转换异常！", null);
        }

        if (!"1".equalsIgnoreCase(datas.get("promis"))) {
            return new ReturnObj(Constant.ERROR, "操作失败：权限不足！", null);
        }

        String cno = datas.get("cno");
        String cname = datas.get("cname");
        String cunit = datas.get("cunit");

        if (!Chk.spaceCheck(cno)) {
            return new ReturnObj(Constant.ERROR, "操作失败：课程编号不能为空！", null);
        }

        if ("add".equalsIgnoreCase(datas.get("mode"))) {

            // 首先应该先从数据库中查找是否已有该课程信息
            CourseInfo courseInfo = courseDao.queryCourseInfoByCouId(cno);

            if (courseInfo != null) {
                return new ReturnObj(Constant.ERROR, "操作失败：课程信息已存在！", null);
            }

            int count = courseDao.saveCourseInfo(cno, cname, cunit);

            if (count < 1) {
                return new ReturnObj(Constant.ERROR, "新增失败：数据库异常！", null);
            } else {
                return new ReturnObj(Constant.SUCCESS, "新增成功！", new CourseInfo(cno, cname, cunit));
            }

        }

        if ("mod".equalsIgnoreCase(datas.get("mode"))) {

            // todo


        }

        return new ReturnObj(Constant.ERROR, "操作失败：操作方式异常！", null);

    }
}
