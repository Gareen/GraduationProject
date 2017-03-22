package cn.sams.controller.system;

import cn.sams.common.util.Chk;
import cn.sams.entity.ResourcesPath;
import cn.sams.entity.Teacher;
import cn.sams.entity.commons.ReturnObj;
import cn.sams.service.system.ResourcesService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fanpeng on 2017/1/23.
 *
 * 资源管理
 * 1. 管理用户的密码修改
 */

@Controller
@RequestMapping("/system/resources")
public class ResourcesController {

    @Resource
    private ResourcesService resourcesService;


    @RequestMapping("toIndex.do")
    public String toIndex() {
        return "system/resourceManagement";
    }


    @RequestMapping("queryResources.do")
    @ResponseBody
    public List<ResourcesPath> queryResources() {
        return resourcesService.queryResources();
    }

    /**
     * 修改密码
     * @param req
     * @return
     */
    @RequestMapping("modifyPwd.do")
    @ResponseBody
    public String modifyPwd(HttpServletRequest req) {
        System.out.println(1);
        String no = req.getParameter("no");
        String old_p = req.getParameter("old");
        String new_p = req.getParameter("new");

        // 空值检测
        if (! Chk.spaceCheck(old_p) || ! Chk.spaceCheck(new_p)) {
            return "error1";
        }
        if (! Chk.spaceCheck(no)) {
            return "error";
        }
        Teacher teacher = resourcesService.findById(no);
        if (! old_p.equals(teacher.getTea_password())) {
            return "error2";
        }
        Integer rows = resourcesService.updatePwd(no, new_p);
        if (rows != null && rows != 0){
            return "success";
        }
        return "error";
    }

    /**
     * 根据一级菜单的点击找到对应的二级菜单的按钮
     */
    @RequestMapping("findNav2.do")
    @ResponseBody
    public List<ResourcesPath> findNav2ByPid(String pid) {
        if (! Chk.spaceCheck(pid)) {
            // 没有找到的话, 那就返回一个空的集合
            return new ArrayList<>();
        }
        return resourcesService.findNav2ByPid(pid);
    }

    @RequestMapping("queryParentName.do")
    @ResponseBody
    public String queryParentName(String id) {
        return resourcesService.queryParentName(id);
    }

    @RequestMapping("queryNodeById.do")
    @ResponseBody
    public ReturnObj queryNodeById(String id) {
        return resourcesService.queryNodeById(id);
    }

    @RequestMapping("saveOrUpdate.do")
    @ResponseBody
    public ReturnObj saveOrUpdate(HttpServletRequest req, ResourcesPath path) {
        return resourcesService.saveOrUpdate(req, path);
    }

    @RequestMapping("delete.do")
    @ResponseBody
    public ReturnObj delete(HttpServletRequest req, String id) {
        return resourcesService.delete(req, id);
    }


}
