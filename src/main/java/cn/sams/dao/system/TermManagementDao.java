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
}
