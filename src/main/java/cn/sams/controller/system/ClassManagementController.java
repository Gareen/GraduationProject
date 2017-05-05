package cn.sams.controller.system;

import cn.sams.entity.Classes;
import cn.sams.entity.commons.ReturnObj;
import cn.sams.service.system.ClassManagementService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by Fanpeng on 2017/3/22.
 */
@Controller
@RequestMapping("/system/classManagement")
public class ClassManagementController {

    @Resource
    private ClassManagementService classManagementService;

    @RequestMapping("toIndex.do")
    public String toIndex() {
        return "system/classManagement";
    }

    @RequestMapping("queryClasses.do")
    @ResponseBody
    public List<Classes> queryClasses() {
        return classManagementService.queryClasses();
    }

    @RequestMapping("queryClassByClzId.do")
    @ResponseBody
    public ReturnObj queryClassByClzId(HttpServletRequest req) {
        return classManagementService.queryClzByClassId(req);
    }

    @RequestMapping("saveOrUpdate.do")
    @ResponseBody
    public ReturnObj saveOrUpdate(HttpServletRequest req) {
        return classManagementService.saveOrUpdate(req);
    }

    @RequestMapping("delete.do")
    @ResponseBody
    public ReturnObj delete(HttpServletRequest req) {
        return classManagementService.delete(req);
    }

}
