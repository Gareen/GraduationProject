package cn.sams.service.system;

import cn.sams.dao.system.TermManagementDao;
import cn.sams.entity.Term;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Fanpeng on 2017/2/6.
 */
@Service("termManagementService")
public class TermManagementService {

    @Resource
    private TermManagementDao termManagementDao;

    public List<Term> queryTerms() {
        return termManagementDao.queryTerms();
    }
}
