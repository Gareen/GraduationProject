package cn.sams.service.system;

import cn.sams.common.util.Chk;
import cn.sams.dao.system.LoginDao;
import cn.sams.entity.Teacher;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by Fanpeng on 2017/1/14.
 */
@Service
public class LoginService {

    @Resource
    private LoginDao loginDao;


    // 根据工号查询
    public Teacher findTeacherByID(String id) {
        return loginDao.findTeacherByID(id);
    }

    // 登录
    public Teacher login(String id, String pwd) {
        return loginDao.login(id, pwd);
    }
}
