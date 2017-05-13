package cn.sams.controller.system;

import cn.sams.entity.Course;
import cn.sams.entity.commons.CourseInfo;
import cn.sams.entity.commons.ReturnObj;
import cn.sams.entity.commons.SelectModel;
import cn.sams.service.system.ClassManagementService;
import cn.sams.service.system.CourseService;
import cn.sams.service.system.TeacherManagementService;
import cn.sams.service.system.TermManagementService;
import org.aspectj.apache.bcel.generic.RET;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Fanpeng on 2017/3/23.
 */
@Controller
@RequestMapping("/system/courseManagement")
public class CourseManagementController {

    @Resource
    private CourseService courseService;

    @Resource
    private TermManagementService termManagementService;

    @Resource
    private TeacherManagementService teacherManagementService;

    @Resource
    private ClassManagementService classManagementService;

    @RequestMapping("toIndex.do")
    public String toIndex() {
        return "system/courseManagement";
    }


    @RequestMapping("queryCourses.do")
    @ResponseBody
    public List<Map<String, String>> queryCourses(HttpServletRequest req) {
        return courseService.displayCourseInfo(req);
    }

    @RequestMapping("queryCourseInfoById.do")
    @ResponseBody
    public CourseInfo queryCourseInfoById(HttpServletRequest req) {
        return courseService.queryCourseInfoById(req);
    }

    @RequestMapping("queryCourseInfo.do")
    @ResponseBody
    public List<CourseInfo> queryCourseInfo() {
        return courseService.queryCourseInfos();
    }

    @RequestMapping("delete.do")
    @ResponseBody
    public ReturnObj delete(HttpServletRequest req) {
        return courseService.delete(req);
    }

    @RequestMapping("queryTeachers.do")
    @ResponseBody
    public List<SelectModel> queryTeachers() {
        return teacherManagementService.queryTeachersSelectModel();
    }

    @RequestMapping("queryTerms.do")
    @ResponseBody
    public List<SelectModel> queryTerms() {
        return termManagementService.queryTermsSelectModels();
    }

    @RequestMapping("queryClz.do")
    @ResponseBody
    public List<SelectModel> queryClz() {
        return classManagementService.queryClassesToSelectModel();
    }

    @RequestMapping("queryCourseById.do")
    @ResponseBody
    public ReturnObj queryCourseById(HttpServletRequest req) {
        return courseService.queryCourseById(req);
    }

    @RequestMapping("queryCoursesSelectModel.do")
    @ResponseBody
    public List<SelectModel> queryCourseSelectModel() {
        return courseService.queryCoursesSelectModel();
    }

    @RequestMapping("deleteCourseInfoByCouId.do")
    @ResponseBody
    public ReturnObj deleteCourseInfoByCouId(HttpServletRequest req) {
        return courseService.deleteCourseInfoByCouId(req);
    }

    @RequestMapping("saveOrUpdateCouInfo.do")
    @ResponseBody
    public ReturnObj saveOrUpdateCouInfo(HttpServletRequest req) {
        return courseService.saveOrUpdateCouInfo(req);
    }
}
