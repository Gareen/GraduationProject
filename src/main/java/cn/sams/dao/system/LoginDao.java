package cn.sams.dao.system;

import cn.sams.entity.Teacher;
import org.springframework.stereotype.Repository;

/**
 * Created by Fanpeng on 2017/1/14.
 */
@Repository
public interface LoginDao {

    /**
     * 根据教师工号查询到教师
     * @param id 教师工号
     * @return Teacher
     */
    Teacher findTeacherByID(String id);

    Teacher login(String id, String pwd);

}
