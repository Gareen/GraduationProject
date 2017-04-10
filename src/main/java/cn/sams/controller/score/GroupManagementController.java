package cn.sams.controller.score;

import cn.sams.common.util.Chk;
import cn.sams.entity.Group;
import cn.sams.entity.Teacher;
import cn.sams.entity.Term;
import cn.sams.entity.commons.ReturnObj;
import cn.sams.entity.commons.SelectModel;
import cn.sams.service.score.GroupInitManagementService;
import cn.sams.service.score.GroupManagementService;
import cn.sams.service.system.CourseService;
import cn.sams.service.system.TermManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Fanpeng on 2017/2/20.
 */
@Controller
@RequestMapping("/score/groupManagement")
public class GroupManagementController {

    @Resource(name = "courseService")
    private CourseService courseService;

    @Resource
    private GroupInitManagementService groupInitManagementService;

    @Resource
    private TermManagementService termManagementService;

    @Resource
    private GroupManagementService groupManagementService;

    @RequestMapping("toIndex.do")
    public String toIndex() {
        return "score/groupManagement";
    }

    @RequestMapping("queryCurrentTime.do")
    @ResponseBody
    public Term queryCurrentTime() {

        // 查找当前系统时间的学期
        Term term = termManagementService.queryCurrentTerm();

        if (term != null) {
            return term;
        }

        return null;
    }

    @RequestMapping("queryCoursesByTeacherIdAndTerm.do")
    @ResponseBody
    public Set<SelectModel> queryCoursesByTeacherIdAndTerm(HttpServletRequest req) {
        return groupInitManagementService.queryCoursesByTeacherIdAndTerm(req);
    }

    @RequestMapping("queryClasses.do")
    @ResponseBody
    public Set<SelectModel> queryClasses(HttpServletRequest req) {
        return groupInitManagementService.queryClasses(req);
    }

    @RequestMapping("queryStudentsByClassId.do")
    @ResponseBody
    public Set<SelectModel> queryStudentsByClassId(HttpServletRequest req) {
        return groupInitManagementService.queryStudentsByClassId(req);
    }

    @RequestMapping("queryAllTerms.do")
    @ResponseBody
    public List<SelectModel> queryAllTerms() {
        return termManagementService.queryTermsSelectModels();
    }

    @RequestMapping("query.do")
    @ResponseBody
    public List<Map<String, String>> query(HttpServletRequest req) {
        return groupManagementService.query(req);
    }

    @RequestMapping("save.do")
    @ResponseBody
    public ReturnObj save(HttpServletRequest req) {
        return groupManagementService.save(req);
    }
}
