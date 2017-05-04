package cn.sams.dao.system;

import cn.sams.entity.Term;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Fanpeng on 2017/2/6.
 */
@Repository
public interface TermManagementDao {

    List<Term> queryTerms();

    Term queryTermByYearAndMonth(String year, String month);

    /**
     * 根据学期的id进行查找学期
     *
     * @param termId
     * @return
     */
    Term queryTermByTermId(@Param("termId") String termId);

    /**
     * 根据学期ID删除学期
     *
     * @param termId
     * @return
     */
    Integer deleteTermByTermId(@Param("termId") String termId);

    /**
     * 保存学期信息
     *
     * @param termId
     * @param termName
     * @param termYear
     * @param termMon
     * @return
     */
    Integer save(@Param("termId") String termId, @Param("termName") String termName,
                 @Param("termYear") String termYear, @Param("termMon") String termMon);

    /**
     * 修改学期信息
     *
     * @param termId
     * @param termName
     * @param termYear
     * @param termMon
     * @return
     */
    Integer update(@Param("termId") String termId, @Param("termName") String termName,
                   @Param("termYear") String termYear, @Param("termMon") String termMon);
}
