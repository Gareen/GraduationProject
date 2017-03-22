package cn.sams.service.system;

import cn.sams.common.util.Chk;
import cn.sams.dao.system.LoginDao;
import cn.sams.dao.system.ResourcesDao;
import cn.sams.entity.ResourcesPath;
import cn.sams.entity.Teacher;
import cn.sams.entity.commons.ReturnObj;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fanpeng on 2017/1/23.
 */
@Service("resourcesService")
public class ResourcesService {

    @Resource
    private LoginDao loginDao;

    @Resource
    private ResourcesDao resourcesDao;

    public Teacher findById(String no) {
        return loginDao.findTeacherByID(no);
    }

    public Integer updatePwd(String no, String pwd) {
        return resourcesDao.updatePwd(no, pwd);
    }

    public List<ResourcesPath> findNav2ByPid(String pid) {

        List<ResourcesPath> nav2s = resourcesDao.findNav2ByPid(pid);
        // 如果查到的第二根导航栏的集合不为空的话, 那么就直接返回掉
        if (nav2s.size() != 0) {
            return nav2s;
        }
        return new ArrayList<>();
    }

    public List<ResourcesPath> queryResources() {
        return resourcesDao.queryResources();
    }

    public String queryParentName(String id) {
        String name = resourcesDao.queryParentName(id);
        return Chk.spaceCheck(name) ? name : "";
    }

    public ReturnObj queryNodeById(String id) {
        ResourcesPath path = resourcesDao.queryNodeById(id);
        return path == null ? new ReturnObj("error", "该节点可能已被删除, 请刷新页面重试", null) :
                new ReturnObj("success", "", path);
    }

    public ReturnObj delete(HttpServletRequest req, String id) {

        if (!chkPremission(req)) {
            return new ReturnObj("error", "删除失败: 权限不足 !", null);
        }

        if (!Chk.spaceCheck(id)) {
            return new ReturnObj("error", "删除失败: id为空 !", null);
        }
        int count = resourcesDao.delete(id);
        if (count != 0) {
            return new ReturnObj("success", "删除成功, 刷新页面生效 !", null);
        } else {
            return new ReturnObj("error", "删除失败 !", null);
        }
    }

    public ReturnObj saveOrUpdate(HttpServletRequest req, ResourcesPath path) {

        if (!chkPremission(req)) {
            return new ReturnObj("error", "操作失败: 权限不足 !", null);
        }

        String mode = req.getParameter("mode");

        if (!Chk.spaceCheck(mode)) {
            return new ReturnObj("error", "操作失败: 未能获取操作模式!", null);
        }

        if (path == null) {
            return new ReturnObj("error", "操作失败: 数据获取异常!", null);
        }

        if ("add".equalsIgnoreCase(mode)) {

            ResourcesPath p = resourcesDao.queryNodeById(path.getId());

            if (p != null) {
                return new ReturnObj("error", "新增失败: 数据已存在!", null);
            }

            int count = resourcesDao.save(path);

            if (count != 0) {
                return new ReturnObj("success", "新增路径成功, 刷新页面生效 !!", null);
            } else {
                return new ReturnObj("error", "新增失败: 发生错误!", null);
            }

        } else if ("mod".equalsIgnoreCase(mode)) {

            ResourcesPath p = resourcesDao.queryNodeById(path.getId());

            if (p == null) {
                return new ReturnObj("error", "修改失败: 数据不存在!", null);
            }

            int count = resourcesDao.update(path);

            if (count != 0) {
                return new ReturnObj("success", "修改路径成功, 刷新页面生效 !!", null);
            } else {
                return new ReturnObj("error", "修改失败: 发生错误!", null);
            }

        } else {
            return new ReturnObj("error", "操作失败: 操作方式异常!", null);
        }
    }

    private boolean chkPremission(HttpServletRequest req) {
        HttpSession session = req.getSession();
        Teacher t = (Teacher) session.getAttribute("user");

        if (t == null || !"1".equals(t.getTea_permission())) {
            return false;
        } else {
            return true;
        }
    }
}
