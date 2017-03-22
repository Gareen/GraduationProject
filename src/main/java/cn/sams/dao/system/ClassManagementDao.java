package cn.sams.dao.system;

import cn.sams.entity.Classes;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Fanpeng on 2017/3/22.
 */
@Repository
public interface ClassManagementDao {

    /**
     * 查找所有的课程
     *
     * @return
     */
    List<Classes> queryClasses();


    /**
     * 根据课程id进行查询响应的课程
     *
     * @param classId
     * @return
     */
    Classes queryClassByClassId(String classId);
}
