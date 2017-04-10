package cn.sams.dao.score;

import cn.sams.entity.Course;
import cn.sams.entity.Group;
import cn.sams.entity.Student;
import cn.sams.entity.Teacher;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

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

    Group queryGroupScore(@Param("groupid") String groupid, @Param("groupNum") String num,
                          @Param("expindex") String exindex);
}
