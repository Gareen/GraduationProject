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
            return new ReturnObj(Constant.ERROR, "上课信息唯一码/教师权限为空！", null);
        }

        if (!"1".equalsIgnoreCase(promis)) {
            return new ReturnObj(Constant.ERROR, "权限不足，请联系管理员！", null);
        }

        int count = courseDao.delete(couNum);

        if (count < 1) {

            return new ReturnObj(Constant.ERROR, "删除失败：数据库异常或无此数据！", null);
        }
        return new ReturnObj(Constant.SUCCESS, "删除成功, 刷新页面生效！", couNum);

    }

    /**
     * 根据key查找到上课信息
     *
     * @param req
     * @return
     */
    public ReturnObj queryCourseByCouNum(HttpServletRequest req) {
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

            courseDao.deleteCourseByCourseId(courseId);
            return new ReturnObj(Constant.SUCCESS, "删除成功！", courseId);
        }
    }

    /**
     * 保存/更新课程信息
     * @param req
     * @return
     */
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

            int count = courseDao.updateCourseInfo(cno, cname, cunit);

            if (count < 1) {
                return new ReturnObj(Constant.ERROR, "更新失败：数据库异常或无此数据！", null);
            } else {
                return new ReturnObj(Constant.SUCCESS, "更新成功, 刷新页面生效！", new CourseInfo(cno, cname, cunit));
            }

        }

        return new ReturnObj(Constant.ERROR, "操作失败：操作方式异常！", null);
    }

    /**
     * 保存/更新上课信息
     *
     * @param req
     * @return
     */
    public ReturnObj saveOrUpdate(HttpServletRequest req) {
        String data = req.getParameter("data");

        if (!Chk.spaceCheck(data)) {
            return new ReturnObj(Constant.ERROR, "保存/更新上课信息失败: 关键数据缺失!", null);
        }

        // {"optMode":"mod","couNum":"7","courseId":"705058",
        // "couCredit":3,"couPeriod":56,"couCounts":45,"couTea":"12315601",
        // "couClz":"10002","couTerm":"1","timePlace":"hh","promisssion":"1"}
        Map<String, String> dataMap = JsonUtil.toMap(data, String.class, String.class);

        if (!Chk.emptyCheck(dataMap)) {
            return new ReturnObj(Constant.ERROR, "保存/更新上课信息失败: 数据转换失败!", null);
        }

        // 判断登陆的用户权限
        String promiss = dataMap.get("promisssion");

        if (!"1".equals(promiss)) {
            return new ReturnObj(Constant.ERROR, "保存/更新上课信息失败: 权限不足!", null);
        }

        // 判断操作方式
        String mode = dataMap.get("optMode");

        // 获取传过来的参数
        String courseId = dataMap.get("courseId");
        String couCredit = dataMap.get("couCredit");
        String couPeriod = dataMap.get("couPeriod");
        String couCounts = dataMap.get("couCounts");
        String couTea = dataMap.get("couTea");
        String couClz = dataMap.get("couClz");
        String couTerm = dataMap.get("couTerm");
        String timePlace = dataMap.get("timePlace");

        int count;

        if ("add".equalsIgnoreCase(mode)) {
            // 因为couNum为自增, 所以不需要判断是否已存在
            count = courseDao.save(courseId, couCredit, couPeriod, couCounts, couTea,
                    couClz, couTerm, timePlace);

            if (count < 0) {
                return new ReturnObj(Constant.ERROR, "保存上课信息失败: 数据库错误!", null);
            } else {
                return new ReturnObj(Constant.SUCCESS, "保存上课信息成功, 刷新页面生效!", null);
            }
        }

        if ("mod".equalsIgnoreCase(mode)) {
            // 此为记录的key, 只有在修改的时候用到
            String couNum = dataMap.get("couNum");

            count = courseDao.update(couNum, courseId, couCredit, couPeriod, couCounts, couTea,
                    couClz, couTerm, timePlace);

            if (count < 0) {
                return new ReturnObj(Constant.ERROR, "更新上课信息失败: 数据库错误或无此记录!", null);
            } else {
                return new ReturnObj(Constant.SUCCESS, "更新上课信息成功, 刷新页面生效!", null);
            }
        }

        return new ReturnObj(Constant.ERROR, "保存/更新上课信息失败: 操作错误!", null);

    }

    /**
     * 获取最新插入上课信息记录的id
     *
     * @return id 自增主键, 只适用于MySQL数据库
     */
    public int getLastInsertId() {
        return courseDao.getLastInsertId();
    }
}
