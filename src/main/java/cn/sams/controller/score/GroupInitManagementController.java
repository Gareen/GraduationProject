package cn.sams.controller.score;

import cn.sams.entity.Classes;
import cn.sams.entity.Group;
import cn.sams.entity.Term;
import cn.sams.entity.commons.ReturnObj;
import cn.sams.entity.commons.SelectModel;
import cn.sams.service.score.GroupInitManagementService;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by Fanpeng on 2017/4/1.
 */
@Controller
@RequestMapping("/score/groupInitManagement")
public class GroupInitManagementController {

    @Resource
    private GroupInitManagementService groupInitManagementService;

    @RequestMapping("toIndex.do")
    public String toIndex() {
        return "score/groupInitManagement";
    }


    @RequestMapping("queryCurrentTime.do")
    @ResponseBody
    public Term queryTermTimes(HttpServletRequest req) {

        // 查找当前系统时间的学期
        Term term = groupInitManagementService.queryCurrentTerm();

        if (term != null) {
            return term;
        }

        return null;
    }

    @RequestMapping("queryAllTerms.do")
    @ResponseBody
    public List<SelectModel> queryAllTerms() {
        return groupInitManagementService.queryAllTerms();
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

    @RequestMapping("query.do")
    @ResponseBody
    public List<Group> queryGroups(HttpServletRequest req) {
        return groupInitManagementService.queryGroups(req);
    }

    @RequestMapping("queryStudentsByClassId.do")
    @ResponseBody
    public Set<SelectModel> queryStudentsByClassId(HttpServletRequest req) {
        return groupInitManagementService.queryStudentsByClassId(req);
    }

    @RequestMapping("queryGroupByGidAndGnm.do")
    @ResponseBody
    public Group queryGroupByGidAndGnm(HttpServletRequest req) {
        return groupInitManagementService.queryGroupByGidAndGnm(req);
    }

    @RequestMapping("deleteGroupByIdAndNum.do")
    @ResponseBody
    public ReturnObj deleteGroupByIdAndNum(HttpServletRequest req) {
        return groupInitManagementService.deleteGroupByIdAndNum(req);
    }

    @RequestMapping("saveOrUpdate.do")
    @ResponseBody
    public ReturnObj saveOrUpdate(HttpServletRequest req) {
        return groupInitManagementService.saveOrUpdate(req);
    }
}
