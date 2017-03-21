package cn.sams.controller.score;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

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

    @RequestMapping("toIndex.do")
    public String toIndex() {
        return "score/homeWorkManagement";
    }


}
