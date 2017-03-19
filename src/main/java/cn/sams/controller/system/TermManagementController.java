package cn.sams.controller.system;

import cn.sams.entity.Term;
import cn.sams.service.system.TermManagementService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Fanpeng on 2017/2/6.
 *
 * 学期管理
 */
@Controller
@RequestMapping("/system/termManagement")
public class TermManagementController {

    @Resource
    private TermManagementService termManagementService;

    @RequestMapping("toIndex.do")
    public String index() {
        return "system/termManagement";
    }

    @RequestMapping("queryTerms.do")
    @ResponseBody
    public List<Term> queryTerms() {
        return termManagementService.queryTerms();
    }
}
