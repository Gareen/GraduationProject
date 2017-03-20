package cn.sams.controller.score;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Fanpeng on 2017/3/20.
 * <p>
 * 学生成绩管理
 */
@Controller
@RequestMapping("/score/scoreManagement")
public class ScoreManagementController {


    @RequestMapping("toIndex.do")
    public String index(HttpServletRequest req) {
        return "score/scoreManagement";
    }


}
