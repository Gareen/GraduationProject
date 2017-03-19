package cn.sams.service.system;

import cn.sams.dao.system.LoginDao;
import cn.sams.dao.system.ResourcesDao;
import cn.sams.entity.ResourcesPath;
import cn.sams.entity.Teacher;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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
}
