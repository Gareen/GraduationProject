package cn.sams.controller.score;

import cn.sams.common.util.Chk;
import cn.sams.entity.Term;
import cn.sams.entity.commons.SelectModel;
import cn.sams.service.score.ScoreManagementService;
import cn.sams.service.system.TermManagementService;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fanpeng on 2017/3/20.
 * <p>
 * 学生成绩管理
 */
@Controller
@RequestMapping("/score/scoreManagement")
public class ScoreManagementController {

    @Resource
    private ScoreManagementService scoreManagementService;

    @RequestMapping("toIndex.do")
    public String index(HttpServletRequest req) {
        return "score/scoreManagement";
    }


    @RequestMapping("queryTermSelectModels.do")
    @ResponseBody
    public List<SelectModel> queryTerms() {
        return scoreManagementService.queryTermsSelectModels();
    }

    @RequestMapping("queryCourseSelectModels.do")
    @ResponseBody
    public List<SelectModel> queryCourseSelectModels() {
        return scoreManagementService.queryCoursesSelectModels();
    }
}
