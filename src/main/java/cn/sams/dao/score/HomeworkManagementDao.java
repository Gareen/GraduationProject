package cn.sams.dao.score;

import cn.sams.entity.Homework;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by Fanpeng on 2017/4/9.
 */
@Repository
public interface HomeworkManagementDao {

    /**
     * 查询所有的平时作业
     *
     * @return
     */
    List<Homework> queryAllWorks();

    /**
     * 根据workid查询平时作业的记录
     *
     * @param id
     * @return
     */
    List<Homework> queryHomeworkByworkIdAndIndex(String id, String index);

    /**
     * 保存
     *
     * @param args 参数
     * @return
     */
    Integer save(Map<String, Object> args);

    /**
     * 根据工作id查找到所有的作业
     *
     * @param id
     * @return
     */
    List<Homework> queryHomeworkByworkId(String id);
}
