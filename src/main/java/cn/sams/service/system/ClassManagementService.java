package cn.sams.service.system;

import cn.sams.common.constants.Constant;
import cn.sams.common.util.Chk;
import cn.sams.common.util.JsonUtil;
import cn.sams.dao.system.ClassManagementDao;
import cn.sams.entity.Classes;
import cn.sams.entity.Term;
import cn.sams.entity.commons.ReturnObj;
import cn.sams.entity.commons.SelectModel;
import com.sun.xml.internal.xsom.impl.Const;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by Fanpeng on 2017/3/22.
 */
@Service("classManagementService")
public class ClassManagementService {

    @Resource
    private ClassManagementDao classManagementDao;

    public List<Classes> queryClasses() {
        return classManagementDao.queryClasses();
    }

    public ReturnObj queryClzByClassId(HttpServletRequest req) {
        String classId = req.getParameter("clzId");

        if (!Chk.spaceCheck(classId)) {
            return new ReturnObj(Constant.ERROR, "查询出错：课程号为空！", null);
        }

        Classes c = classManagementDao.queryClassByClassId(classId);

        if (c == null) {
            return new ReturnObj(Constant.ERROR, "查询出错：课程不存在！", null);
        }

        return new ReturnObj(Constant.SUCCESS, "", c);
    }


    public ReturnObj saveOrUpdate(HttpServletRequest req) {
        String data = req.getParameter("data");

        if (!Chk.spaceCheck(data)) {
            return new ReturnObj(Constant.ERROR, "保存失败：关键数据缺失！", null);
        }

        Map<String, String> dataMap = JsonUtil.toMap(data, String.class, String.class);

        if (!Chk.emptyCheck(dataMap)) {
            return new ReturnObj(Constant.ERROR, "保存失败：数据转换异常！", null);
        }

        String permission = dataMap.get("tea");

        if (!"1".equalsIgnoreCase(permission)) {
            return new ReturnObj(Constant.ERROR, "操作失败：权限不足！", null);
        }

        String mode = dataMap.get("optMode");

        String clzId = dataMap.get("clzId");
        String clzName = dataMap.get("clzName");

        // 新增
        if ("add".equalsIgnoreCase(mode)) {

            // 先查询库中是否已有该学期的信息
            Classes clz = classManagementDao.queryClassByClassId(clzId);

            if (clz != null) {
                return new ReturnObj(Constant.ERROR, "新增失败：班级已存在！", null);
            }

            int count = classManagementDao.save(clzId, clzName);

            if (count < 1) {
                return new ReturnObj(Constant.ERROR, "新增失败：保存异常！", null);
            }

            return new ReturnObj(Constant.SUCCESS, "新增成功，刷新页面生效！", null);
        }

        // 修改
        if ("mod".equalsIgnoreCase(mode)) {

            int count = classManagementDao.update(clzId, clzName);

            if (count < 1) {
                return new ReturnObj(Constant.ERROR, "修改失败：保存异常！", null);
            }

            return new ReturnObj(Constant.SUCCESS, "修改成功，刷新页面生效！", null);

        }

        return new ReturnObj(Constant.ERROR, "操作失败：操作类型错误！", null);


    }

    public ReturnObj delete(HttpServletRequest req) {
        String clzId = req.getParameter("clzId");
        String promis = req.getParameter("promis");

        if (!Chk.spaceCheck(clzId) || !Chk.spaceCheck(promis)) {
            return new ReturnObj(Constant.ERROR, "删除失败：关键信息为空 ！", null);
        }

        if (!"1".equalsIgnoreCase(promis)) {
            return new ReturnObj(Constant.ERROR, "权限不足，请联系管理员 ！", null);
        }

        int count = classManagementDao.delete(clzId);

        if (count < 1) {
            return new ReturnObj(Constant.ERROR, "删除失败：数据库异常！", null);
        }

        return new ReturnObj(Constant.SUCCESS, "删除成功！", clzId);
    }
}
