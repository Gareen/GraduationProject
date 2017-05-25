package cn.sams.dao.score;

import cn.sams.entity.Course;
import cn.sams.entity.Group;
import cn.sams.entity.Student;
import cn.sams.entity.Teacher;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by Fanpeng on 2017/2/27.
 * 对应的mapper为
 */
@Repository
public interface GroupManagementDao {

    /**
     * 通过小组编号和实验次数查询到分组列表
     * @param groupId 小组编号
     * @return
     */
    List<Group> queryGroupsByGroupIdAndExpIndex(String groupId, String expIndex);

    /**
     * 通过分组id查找到该分组编号下面所有的分组信息
     *
     * @param id
     * @return
     */
    List<Group> queryGroupsByGroupId(@Param("groupId") String id);

    /**
     * 动态保存分数
     * @param datafield
     * @param score
     * @param group_id
     * @param groupNum
     * @param exIndex
     * @return
     */
    Integer save(@Param("datafield") String datafield, @Param("score") Double score,
                 @Param("result") Double result_score, @Param("groupId") String group_id,
                 @Param("groupNum") String groupNum, @Param("exIndex") String exIndex);

    /**
     * 查询分组成绩
     *
     * @param groupid
     * @param num
     * @param exindex
     * @return
     */
    Group queryGroupScore(@Param("groupid") String groupid, @Param("groupNum") String num,
                          @Param("expindex") String exindex);

    /**
     * 根据小组编号和小组号进行删除指定分组
     * @param groupId
     * @param groupNum
     * @return
     */
    Integer deleteGroupByIdAndNum(@Param("groupId") String groupId, @Param("groupNum") String groupNum);

    /**
     * 根据分组编号查询成绩次数
     *
     * @param groupId
     * @return
     */
    List<Map<String, String>> queryScoreCount(String groupId);

    /**
     * 重置分数
     *
     * @param id     分组编号
     * @param cindex 次数
     * @return
     */
    Integer reset(String id, String cindex);

    /**
     * 根据次数删除记录
     *
     * @param id     分组编号
     * @param cIndex 次数
     * @return
     */
    Integer deleteScoreByIndex(@Param("id") String id, @Param("index") String cIndex);
}
