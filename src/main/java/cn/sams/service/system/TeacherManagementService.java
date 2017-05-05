package cn.sams.service.system;

import cn.sams.common.util.Chk;
import cn.sams.dao.system.TeacherManagerDao;
import cn.sams.entity.Teacher;
import cn.sams.entity.commons.ReturnObj;
import cn.sams.entity.commons.SelectModel;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Fanpeng on 2017/1/25.
 */
@Service("organManagementService")
public class TeacherManagementService {

    @Resource
    private TeacherManagerDao teacherManagerDao;

    public Teacher queryTeaById(String id) {
        return teacherManagerDao.queryTeaById(id);
    }

    public List<Teacher> queryTeachers() {
        return teacherManagerDao.findTeachers();
    }

    public ReturnObj saveTeacher(HttpServletRequest req) {
       HttpSession session = req.getSession();
       Teacher user = (Teacher)session.getAttribute("user");
       if (user == null || ! "1".equals(user.getTea_permission())) {
           return new ReturnObj("error", "您无权进行此操作, 请联系管理员 !", null);
       }

       String jud = req.getParameter("jud");
       String id  = req.getParameter("id");
       String name = req.getParameter("name");
       String pwd = req.getParameter("pwd");
       String pre = req.getParameter("pre");

       if (! Chk.spaceCheck(id) || ! Chk.spaceCheck(name) || ! Chk.spaceCheck(pwd)) {
           return new ReturnObj("error", "各项均不可为空 !", null);
       }
       try {
           Integer.parseInt(id);
       } catch (Exception e) {
           return new ReturnObj("error", e.getMessage(), null);
       }
       Teacher teacher = queryTeaById(id);
       if("add".equals(jud) && teacher != null) {
           return new ReturnObj("error", "不可重复添加教师 !", null);
       }
       // 如果该名教师目前不存在那就是新增, 如果存在那就是修改
       if (teacher == null) {
           try {
               Integer row = teacherManagerDao.saveTeacher(id, pwd, name, pre);
               if (row != null && row > 0) {
                   return new ReturnObj("success", "保存成功 !", null);
               } else {
                   return new ReturnObj("error", "保存失败 !", null);
               }
           } catch (Exception e) {
               return new ReturnObj("error", "保存发生错误 !", null);
           }
       } else {
           try {
               Integer row = teacherManagerDao.updateTeacher(id, pwd, name, pre);
               if (row != null && row > 0) {
                   return new ReturnObj("success", "更新成功 !", null);
               } else {
                   return new ReturnObj("error", "更新失败 !", null);
               }
           } catch (Exception e) {
               e.printStackTrace();
               return new ReturnObj("error", "更新发生错误 !", null);
           }
       }
    }

    public ReturnObj deleteTeacherById(HttpServletRequest req, String id) {
        HttpSession session = req.getSession();
        Teacher user = (Teacher)session.getAttribute("user");
        if (user == null || ! "1".equals(user.getTea_permission())) {
            return new ReturnObj("error", "您无权进行此操作, 请联系管理员 !", null);
        }
        try {
            if ("10000".equals(id)) {
                return new ReturnObj("error", "管理员不可以删除 !", null);
            }
            if (queryTeaById(id) == null) {
                return new ReturnObj("error", "该记录不存在, 请刷新页面重试 !", null);
            }
            Integer rows = teacherManagerDao.deleteTeaById(id);
            if (rows != null && rows > 0) {
                return new ReturnObj("success", "删除成功 !", null);
            } else {
                return new ReturnObj("error", "删除失败 !", null);
            }
        } catch (Exception e) {
            return new ReturnObj("error", e.getMessage(), null);
        }
    }

    public List<SelectModel> queryTeachersSelectModel() {
        List<Teacher> teachers = teacherManagerDao.findTeachers();

        if (!Chk.emptyCheck(teachers)) {
            return new ArrayList<>();
        }

        List<SelectModel> sms = new ArrayList<>();

        for (Teacher teacher : teachers) {
            SelectModel sm = new SelectModel();

            sm.setKey(teacher.getTea_no() + " " + teacher.getTea_name());
            sm.setValue(teacher.getTea_no());

            sms.add(sm);
        }

        return sms;
    }
}
