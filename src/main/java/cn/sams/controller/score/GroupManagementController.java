package cn.sams.controller.score;

import cn.sams.common.util.Chk;
import cn.sams.entity.Teacher;
import cn.sams.entity.commons.ReturnObj;
import cn.sams.entity.commons.SelectModel;
import cn.sams.service.score.GroupManagementService;
import cn.sams.service.system.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Set;

/**
 * Created by Fanpeng on 2017/2/20.
 */
@Controller
@RequestMapping("/score/groupManagement")
public class GroupManagementController {

    @Resource(name = "courseService")
    private CourseService courseService;

    @Autowired
    private GroupManagementService groupManagementService;

    @RequestMapping("toIndex.do")
    public String toIndex() {
        return "score/groupManagement";
    }

    @RequestMapping("queryGroupClassList.do")
    @ResponseBody
    public List<SelectModel> queryGroupClassList() {
        return courseService.queryCourseInfo();
    }

    @RequestMapping("queryClassPlaceByCourseKey.do")
    @ResponseBody
    public Set<SelectModel> queryClassPlaceByCourseKey(HttpServletRequest req) {
        return courseService.queryClassPlaceByCourseKey(req);
    }

    @RequestMapping("queryCourseTeacher.do")
    @ResponseBody
    public ReturnObj queryCourseTeacher(HttpServletRequest req) {
        return groupManagementService.queryCourseTeacher(req);
    }

    @RequestMapping("queryStu.do")
    @ResponseBody
    public List<SelectModel> queryStu() {
        return groupManagementService.queryStu();
    }

    @RequestMapping("queryClassWeekByCourseKey.do")
    @ResponseBody
    public Set<SelectModel> queryClassWeekByCourseKey(HttpServletRequest req) {
        return courseService.queryClassWeekByCourseKey(req);
    }

    @RequestMapping("queryClassTimeByCourseKeyAndWeek.do")
    @ResponseBody
    public Set<SelectModel> queryClassTimeByCourseKeyAndWeek(HttpServletRequest req) {
        return courseService.queryClassTimeByCourseKeyAndWeek(req);
    }

    @RequestMapping("queryTeacherName.do")
    @ResponseBody
    public Teacher queryTeacherName(HttpServletRequest req) {
        return groupManagementService.queryTeacherByInfo(req);
    }



    @RequestMapping("saveorupdate.do")
    @ResponseBody
    public ReturnObj saveorupdate(HttpServletRequest req) {
        return groupManagementService.saveorupdate(req);
    }
}
