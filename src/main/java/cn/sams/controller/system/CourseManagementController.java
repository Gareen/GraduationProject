package cn.sams.controller.system;

import cn.sams.entity.Course;
import cn.sams.service.system.CourseService;
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

    @RequestMapping("toIndex.do")
    public String toIndex() {
        return "system/courseManagement";
    }


    @RequestMapping("queryCourses.do")
    @ResponseBody
    public List<Map<String, String>> queryCourses(HttpServletRequest req) {
        return courseService.displayCourseInfo(req);
    }
}
