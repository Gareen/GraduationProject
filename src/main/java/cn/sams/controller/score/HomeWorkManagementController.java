package cn.sams.controller.score;

import cn.sams.entity.Homework;
import cn.sams.entity.Term;
import cn.sams.entity.commons.ReturnObj;
import cn.sams.entity.commons.SelectModel;
import cn.sams.service.score.GroupInitManagementService;
import cn.sams.service.score.HomeWorkManagementService;
import cn.sams.service.system.TermManagementService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Fanpeng on 2017/3/21.
 * <p>
 * 学生成绩管理
 * <p>
 * 根据学期, 课程, 作业次数,
 */
@Controller
@RequestMapping("/score/homeWorkManagement")
public class HomeWorkManagementController {

    @Resource
    private HomeWorkManagementService homeWorkManagementService;

    @Resource
    private GroupInitManagementService groupInitManagementService;

    @Resource
    private TermManagementService termManagementService;

    @RequestMapping("toIndex.do")
    public String toIndex() {
        return "score/homeWorkManagement";
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
        return homeWorkManagementService.query(req);
    }

    @RequestMapping("save.do")
    @ResponseBody
    public ReturnObj save(HttpServletRequest req) {
        return homeWorkManagementService.save(req);
    }

    @RequestMapping("queryScoreCounts.do")
    @ResponseBody
    public ReturnObj queryScoreCounts(HttpServletRequest req) {
        return homeWorkManagementService.queryScoreCounts(req);
    }

    @RequestMapping("resetScore.do")
    @ResponseBody
    public ReturnObj resetScore(HttpServletRequest req) {
        return homeWorkManagementService.resetScore(req);
    }

    @RequestMapping("deleteScore.do")
    @ResponseBody
    public ReturnObj deleteScore(HttpServletRequest req) {
        return homeWorkManagementService.deleteScore(req);
    }
}
