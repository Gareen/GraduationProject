package cn.sams.dao.score;

import cn.sams.entity.FinalGrade;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by Fanpeng on 2017/4/15.
 */
@Repository
public interface ScoreManagementDao {

    /**
     * 根据成绩单编号查询所有的成绩
     *
     * @param finalId
     * @return
     */
    List<FinalGrade> queryFinalsByFinalId(String finalId);

    /**
     * 查询实验进行了多少次
     *
     * @param groupId 分组实验编号
     * @return 实验次数
     */
    Integer queryExpIndex(@Param("groupId") String groupId);

    /**
     * 根据分组编号和小组号计算这个小组的平均分组实验成绩总和
     *
     * @param groupId
     * @param groupNum
     * @return
     */
    Double countGroupScoreByGroupIdAndGroupNum(@Param("groupId") String groupId, @Param("groupNum") String groupNum);

    /**
     * 计算作业成绩
     *
     * @param workId
     * @return
     */
    List<Map<String, Double>> countHomeworkScoreByWorkId(String workId);

    /**
     * 根据finalId清空表
     *
     * @param finalId
     */
    void delDataByFinalId(@Param("finalId") String finalId);
}
