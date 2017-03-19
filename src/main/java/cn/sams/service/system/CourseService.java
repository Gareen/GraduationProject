package cn.sams.service.system;

import cn.sams.common.util.Chk;
import cn.sams.common.util.SelectModelUtil;
import cn.sams.dao.system.CourseDao;
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

    public Set<SelectModel> queryClassPlaceByCourseKey(HttpServletRequest req) {
        String key = req.getParameter("key");
        if (! Chk.spaceCheck(key)) {
            return new HashSet<>();
        }

        Set<SelectModel> set = new HashSet<>();
        List<Course> courses = courseDao.queryCoursesByCourseKey(key);
        for (Course course : courses) {
            SelectModel selectModel = SelectModelUtil.getSelectModel(course.getCou_place_id().toString(),
                    courseDao.queryCoursePlaceById(course.getCou_place_id().toString()).getPlace_name());
            set.add(selectModel);
        }
        return set;
    }

    /**
     * 根据课程查找到该门课程的上课周
     * 形成如: 第一周
     * @param req
     * @return TreeSet 对结果进行排序
     */
    public Set<SelectModel> queryClassWeekByCourseKey(HttpServletRequest req) {
        String key = req.getParameter("key");
        if (! Chk.spaceCheck(key)) {
            return new TreeSet<>();
        }
        Set<SelectModel> set = new TreeSet<SelectModel>((o1, o2) -> o1.getKey().compareTo(o2.getKey()));
        List<Course> courses = courseDao.queryCoursesByCourseKey(key);
        List<SelectModel> selectModels = SelectModelUtil.getWeeks();
        if (selectModels == null || selectModels.size() == 0) {
            return new TreeSet<>();
        }
        for (Course course : courses) {
            for (SelectModel model : selectModels) {
                if (course.getCou_week().equals(model.getKey())) {
                    set.add(SelectModelUtil.getSelectModel(model.getKey(), model.getValue()));
                }
            }
        }
        return set;
    }

    /**
     * 根据课程和选中的上课周来查找上课的时间列表
     * 形成如: 周五 一二节课 格式
     * db字段: cou_day_of_week  cou_time
     * pro文件: day_of_week, class_time
     *
     * @param req
     * @return
     */
    public Set<SelectModel> queryClassTimeByCourseKeyAndWeek(HttpServletRequest req) {
        String key = req.getParameter("key");
        String week = req.getParameter("week");
        if (! Chk.spaceCheck(key) || ! Chk.spaceCheck(week)) {
            return new TreeSet<>();
        }
        List<SelectModel> classTimes = SelectModelUtil.getClassTime();

        List<SelectModel> daysOfWeeks = SelectModelUtil.getDaysOfWeek();

        List<Course> courses = courseDao.queryCoursesByCourseKeyAndWeek(key, week);
        Set<SelectModel> set = new TreeSet<SelectModel>((o1, o2) -> o1.getKey().compareTo(o2.getKey()));

        for (Course c : courses) {
            // 定义显示值和key
            String displayValue = "";
            String displayKey = "";

            SelectModel selectModel = new SelectModel();

            for (SelectModel daysOfWeek : daysOfWeeks) {
                if (daysOfWeek.getKey().equals(c.getCou_day_of_week())) {
                    displayValue += daysOfWeek.getValue() + " ";
                    // key用,进行分隔
                    displayKey += daysOfWeek.getKey() + ",";
                }
            }

            for (SelectModel classTime : classTimes) {
                if (classTime.getKey().equals(c.getCou_time())) {
                    displayValue += classTime.getValue();
                    displayKey += classTime.getKey();
                }
            }

            selectModel.setKey(displayKey);
            selectModel.setValue(displayValue);

            set.add(selectModel);
        }

        return set;
    }
}
