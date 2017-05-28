package cn.sams.controller.common;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Fanpeng on 2017/5/28.
 * 首页
 */
@Controller
@RequestMapping("/common/welcome")
public class WelcomeController {

    @RequestMapping("index.do")
    public String index() {
        return "/common/welcome";
    }

}
