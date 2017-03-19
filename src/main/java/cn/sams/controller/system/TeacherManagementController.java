package cn.sams.controller.system;

import cn.sams.entity.commons.ReturnObj;
import cn.sams.entity.Teacher;
import cn.sams.service.system.TeacherManagementService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by Fanpeng on 2017/1/15.
 */
@Controller
@RequestMapping("/system/teacherManagement")
public class TeacherManagementController {

    @Resource
    private TeacherManagementService teacherManagementService;

    @RequestMapping("toIndex.do")
    public String index() {
        return "/system/teacherManagement";
    }

    @RequestMapping("queryTeachers.do")
    @ResponseBody
    public List<Teacher> queryTeachers() {
        return teacherManagementService.queryTeachers();
    }

    @RequestMapping("saveTeacher.do")
    @ResponseBody
    public ReturnObj saveTeacher(HttpServletRequest req) {
        return teacherManagementService.saveTeacher(req);
    }

    @RequestMapping("deleteTeacherById.do")
    @ResponseBody
    public ReturnObj deleteTeacherById(HttpServletRequest req, String id) {
        return teacherManagementService.deleteTeacherById(req, id);
    }

    @RequestMapping("queryTeacherById.do")
    @ResponseBody
    public Teacher queryTeacherById(String id) {
        return teacherManagementService.queryTeaById(id);
    }
}
