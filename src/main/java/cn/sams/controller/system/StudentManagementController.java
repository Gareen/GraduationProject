package cn.sams.controller.system;

import cn.sams.entity.Student;

import cn.sams.entity.commons.ReturnObj;
import cn.sams.entity.commons.SelectModel;
import cn.sams.service.system.ClassManagementService;
import cn.sams.service.system.StudentManagementService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.List;

/**
 * Created by Fanpeng on 2017/3/22.
 */
@Controller
@RequestMapping("system/studentManagement")
public class StudentManagementController {

    @Resource
    private StudentManagementService studentManagementService;

    @Resource
    private ClassManagementService classManagementService;

    @RequestMapping("toIndex.do")
    public String toIndex() {
        return "system/studentManagement";
    }

    @RequestMapping("queryStudents.do")
    @ResponseBody
    public List<Student> queryStudents(HttpServletRequest req) {
        return studentManagementService.queryStudents(req);
    }

    @RequestMapping("importStudents.do")
    @ResponseBody
    public ReturnObj importStudents(HttpServletRequest req, HttpServletResponse res) {
        return studentManagementService.importStudents(req, res);
    }

    @RequestMapping("export.do")
    @ResponseBody
    public void export(HttpServletRequest req, HttpServletResponse response) {
        try {
            studentManagementService.export(req, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("queryClasses.do")
    @ResponseBody
    public List<SelectModel> queryClasses() {
        return classManagementService.queryClassesToSelectModel();
    }

    @RequestMapping("delete.do")
    @ResponseBody
    public ReturnObj delete(HttpServletRequest req) {
        return studentManagementService.delete(req);
    }

    @RequestMapping("saveOrUpdate.do")
    @ResponseBody
    public ReturnObj saveOrUpdate(HttpServletRequest req) {
        return studentManagementService.saveOrUpdate(req);
    }

    @RequestMapping("queryClassesByTeaId.do")
    @ResponseBody
    public List<SelectModel> queryClassesByTeaId(HttpServletRequest req) {
        return studentManagementService.queryClassesByTeaId(req);
    }

    @RequestMapping("delStuByClassId.do")
    @ResponseBody
    public ReturnObj delStuByClassId(HttpServletRequest req) {
        return studentManagementService.delStuByClassId(req);
    }

    @RequestMapping("queryStudent.do")
    @ResponseBody
    public ReturnObj queryStudent(HttpServletRequest req) {
        return studentManagementService.queryStudent(req);
    }
}
