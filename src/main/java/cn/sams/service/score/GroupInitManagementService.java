package cn.sams.service.score;

import cn.sams.common.constants.Constant;
import cn.sams.common.constants.DateFormat;
import cn.sams.common.util.Chk;
import cn.sams.common.util.DateUtil;
import cn.sams.dao.score.GroupInitManagementDao;
import cn.sams.dao.system.ClassManagementDao;
import cn.sams.dao.system.CourseDao;
import cn.sams.dao.system.StudentManagementDao;
import cn.sams.dao.system.TermManagementDao;
import cn.sams.entity.*;
import cn.sams.entity.commons.SelectModel;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by Fanpeng on 2017/4/1.
 */
@Service("groupInitManagementService")
public class GroupInitManagementService {

    @Resource
    private TermManagementDao termManagementDao;

    @Resource
    private ScoreManagementService scoreManagementService;

    @Resource
    private GroupInitManagementDao groupInitManagementDao;

    @Resource
    private CourseDao courseDao;

    @Resource
    private ClassManagementDao classManagementDao;

    @Resource
    private StudentManagementDao studentManagementDao;

    /**
     * 获取当前的系统时间所表示的学期
     *
     * @return term
     */
    public Term queryCurrentTerm() {
        Date date = new Date();
        // 2017-04
        String dateStr = DateUtil.getDateString(date, true, DateFormat.yM);
        String[] d = dateStr.split("-");

        String dateYear = d[0];
        String dateMonth = d[1];

        // 上学期一般是2月份开学, 下学期一般是9月份开学, 所以需要判断是该年的上学期还是下学期
        int end = Integer.parseInt(dateMonth);

        // 2 - 9月份都是上半学期
        if (end >= Constant.FIRST_TERM_MONTH && end < Constant.SECOND_TERM_MONTH) {
            dateMonth = "02";
        }

        // 9 - 2月份都是下半学期, 因为系统时间也不会大于12, 所以不做大于12的判断
        if (end < Constant.FIRST_TERM_MONTH || end >= Constant.SECOND_TERM_MONTH) {
            dateMonth = "09";
        }

        return termManagementDao.queryTermByYearAndMonth(dateYear, dateMonth);
    }

    /**
     * 获取全部的学期
     *
     * @return
     */
    public List<SelectModel> queryAllTerms() {
        return scoreManagementService.queryTermsSelectModels();
    }

    /**
     * 根据老师的工号和学期查询课程
     *
     * @param req
     * @return
     */
    public Set<SelectModel> queryCoursesByTeacherIdAndTerm(HttpServletRequest req) {
        String teaNo = req.getParameter("teaNo");
        String termId = req.getParameter("termId");

        Set<SelectModel> set = new TreeSet<>(Comparator.comparing(SelectModel::getKey));

        if (!Chk.spaceCheck(teaNo) || !Chk.spaceCheck(termId)) {
            return new TreeSet<>();
        }

        List<Course> courses = groupInitManagementDao.queryCoursesByTeacherIdAndTerm(teaNo, termId);

        if (Chk.emptyCheck(courses)) {
            for (Course course : courses) {
                SelectModel selectModel = new SelectModel();
                String id = course.getCourse_id();

                String name = courseDao.queryCourseInfoByCouId(id).getCourse_name();

                selectModel.setValue(id);
                selectModel.setKey(name);

                set.add(selectModel);
            }
        }

        // 如果课程列表不为空, 那么就返回装配好的selectmodel
        if (Chk.emptyCheck(set)) {
            return set;
        }

        // 如果查不到, 放置一个默认值
        SelectModel sm = new SelectModel();
        sm.setKey("");
        sm.setValue("");
        set.add(sm);

        return set;
    }

    public Set<SelectModel> queryClasses(HttpServletRequest req) {
        String teaNo = req.getParameter("teaNo");
        String termId = req.getParameter("termId");
        String courseId = req.getParameter("courseId");

        Set<SelectModel> set = new TreeSet<>(Comparator.comparing(SelectModel::getKey));

        if (!Chk.spaceCheck(teaNo) || !Chk.spaceCheck(termId) || !Chk.spaceCheck(courseId)) {
            return new TreeSet<>();
        }

        // 根据三个条件确定这个老师交的所有的班级
        List<Classes> classes = groupInitManagementDao.queryClasses(teaNo, termId, courseId);

        if (Chk.emptyCheck(classes)) {
            for (Classes c : classes) {
                SelectModel selectModel = new SelectModel();
                String id = c.getClass_id();
                String name = classManagementDao.queryClassByClassId(id).getClass_name();

                selectModel.setKey(name);
                selectModel.setValue(id);

                set.add(selectModel);
            }
        }


        if (Chk.emptyCheck(set)) {
            return set;
        }

        // 如果查不到, 放置一个默认值
        SelectModel sm = new SelectModel();
        sm.setKey("");
        sm.setValue("");
        set.add(sm);

        return new TreeSet<>();
    }

    public List<Group> queryGroups(HttpServletRequest req) {

        String groupId = getEncodeGroupId(req);

        if (!Chk.spaceCheck(groupId)) {
            return new ArrayList<>();
        }

        List<Group> groups = groupInitManagementDao.queryGroupsByGroupId(groupId);

        if (Chk.emptyCheck(groups)) {
            return groups;
        }

        return new ArrayList<>();
    }


    public Set<SelectModel> queryStudentsByClassId(HttpServletRequest req) {
        String classId = req.getParameter("classId");

        Set<SelectModel> set = new TreeSet<>(Comparator.comparing(SelectModel::getValue));

        if (!Chk.spaceCheck(classId)) {
            return new TreeSet<>();
        }

        List<Student> students = studentManagementDao.queryStudentsByClassId(classId);

        if (Chk.emptyCheck(students)) {
            for (Student student : students) {
                SelectModel selectModel = new SelectModel();
                String stuNo = student.getStu_no();

                selectModel.setKey(stuNo + " " + student.getStu_name());
                selectModel.setValue(stuNo);

                set.add(selectModel);
            }
        }

        if (Chk.emptyCheck(set)) {
            return set;
        } else {
            return new TreeSet<>();
        }
    }

    /**
     * 对小组进行编码
     * <p>
     * 编码规则
     * 教师工号+T+课程号+T+班级id+T+学期编号
     *
     * @param req
     * @return 小组id
     */
    private String getEncodeGroupId(HttpServletRequest req) {
        String teaNo = req.getParameter("teaNo");
        String termId = req.getParameter("termId");
        String courseId = req.getParameter("courseId");
        String classId = req.getParameter("classId");

        // 如果一项为空直接返回空
        if (!Chk.spaceCheck(teaNo) || !Chk.spaceCheck(termId) || !Chk.spaceCheck(courseId) || !Chk.spaceCheck(classId)) {
            return "";
        }
        return teaNo + "T" + termId + "T" + courseId + "T" + classId;
    }

    /**
     * 解码小组编号
     *
     * @param groupId 小组编号
     * @return
     */
    private String[] decodeGroupId(String groupId) {
        return groupId.split("T");
    }

}
