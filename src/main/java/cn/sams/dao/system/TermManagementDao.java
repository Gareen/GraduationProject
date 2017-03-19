package cn.sams.dao.system;

import cn.sams.entity.Term;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Fanpeng on 2017/2/6.
 */
@Repository
public interface TermManagementDao {

    List<Term> queryTerms();
}
