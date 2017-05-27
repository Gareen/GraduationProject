package cn.sams.dao.score;

import cn.sams.entity.Homework;
import org.apache.ibatis.annotations.Param;
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

    /**
     * 返回作业次数列表
     *
     * @param id
     * @return
     */
    List<Map<String, String>> queryScoreCounts(String id);

    /**
     * 重置次数
     *
     * @param id
     * @param windex
     * @return
     */
    Integer resetScoreByIndex(@Param("id") String id, @Param("windex") String windex);

    /**
     * 删除次数
     *
     * @param id
     * @param windex
     * @return
     */
    Integer deleteScoreByIndex(@Param("id") String id, @Param("windex") String windex);
}
