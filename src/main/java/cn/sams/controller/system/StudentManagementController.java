package cn.sams.controller.system;

import cn.sams.entity.Student;
import cn.sams.service.system.StudentManagementService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fanpeng on 2017/3/22.
 */
@Controller
@RequestMapping("system/studentManagement")
public class StudentManagementController {

    @Resource
    private StudentManagementService studentManagementService;

    @RequestMapping("toIndex.do")
    public String toIndex() {
        return "system/studentManagement";
    }

    @RequestMapping("queryStudents.do")
    @ResponseBody
    public List<Student> queryStudents(HttpServletRequest req) {
        return studentManagementService.queryStudents(req);
    }
}
