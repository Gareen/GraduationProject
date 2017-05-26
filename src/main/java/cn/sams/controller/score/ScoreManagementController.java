package cn.sams.controller.score;

import cn.sams.common.util.Chk;
import cn.sams.entity.Term;
import cn.sams.entity.commons.ReturnObj;
import cn.sams.entity.commons.SelectModel;
import cn.sams.service.score.GroupInitManagementService;
import cn.sams.service.score.ScoreManagementService;
import cn.sams.service.system.TermManagementService;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 学生成绩管理
 *
 * @author fanpeng
 * @time 2017.4.15
 */
@Controller
@RequestMapping("/score/scoreManagement")
public class ScoreManagementController {

    @Resource
    private ScoreManagementService scoreManagementService;

    @Resource
    private GroupInitManagementService groupInitManagementService;

    @Resource
    private TermManagementService termManagementService;

    @RequestMapping("toIndex.do")
    public String index(HttpServletRequest req) {
        return "score/scoreManagement";
    }

    @RequestMapping("queryCurrentTime.do")
    @ResponseBody
    public Term queryTermTimes(HttpServletRequest req) {

        // 查找当前系统时间的学期
        Term term = termManagementService.queryCurrentTerm();

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
    public List<Map<String, String>> query(HttpServletRequest req) {
        return scoreManagementService.query(req);
    }

    @RequestMapping("save.do")
    @ResponseBody
    public ReturnObj save(HttpServletRequest req) {
        return scoreManagementService.save(req);
    }

    @RequestMapping("export.do")
    @ResponseBody
    public void export(HttpServletRequest req, HttpServletResponse response) throws IOException {
        scoreManagementService.export(req, response);
    }

    @RequestMapping("resetScore.do")
    @ResponseBody
    public ReturnObj resetScore(HttpServletRequest req) {
        return scoreManagementService.resetScore(req);
    }


}
