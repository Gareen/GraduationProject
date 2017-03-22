package cn.sams.service.system;

import cn.sams.dao.system.ClassManagementDao;
import cn.sams.entity.Classes;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

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
}
