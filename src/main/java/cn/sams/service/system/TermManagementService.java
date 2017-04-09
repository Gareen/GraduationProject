package cn.sams.service.system;

import cn.sams.common.constants.Constant;
import cn.sams.common.constants.DateFormat;
import cn.sams.common.util.Chk;
import cn.sams.common.util.DateUtil;
import cn.sams.dao.system.TermManagementDao;
import cn.sams.entity.Term;
import cn.sams.entity.commons.SelectModel;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
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

    /**
     * 获取当前的系统时间所表示的学期
     *
     * @return term
     */
    public Term queryCurrentTerm() {
        Date date = new Date();
        // 2017-04
        String dateStr = DateUtil.getDateString(date, true, DateFormat.yM);
        String[] d = dateStr.split("-");

        String dateYear = d[0];
        String dateMonth = d[1];

        // 上学期一般是2月份开学, 下学期一般是9月份开学, 所以需要判断是该年的上学期还是下学期
        int end = Integer.parseInt(dateMonth);

        // 2 - 9月份都是上半学期
        if (end >= Constant.FIRST_TERM_MONTH && end < Constant.SECOND_TERM_MONTH) {
            dateMonth = "02";
        }

        // 9 - 2月份都是下半学期, 因为系统时间也不会大于12, 所以不做大于12的判断
        if (end < Constant.FIRST_TERM_MONTH || end >= Constant.SECOND_TERM_MONTH) {
            dateMonth = "09";
        }

        return termManagementDao.queryTermByYearAndMonth(dateYear, dateMonth);
    }

    /**
     * 返回学期的SelectModel
     *
     * @return
     */
    public List<SelectModel> queryTermsSelectModels() {
        List<Term> terms = queryTerms();

        List<SelectModel> s = new ArrayList<>();

        if (!Chk.emptyCheck(terms)) {
            return new ArrayList<>();
        }

        for (Term t : terms) {
            SelectModel selectModel = new SelectModel();

            selectModel.setKey(t.getTerm_name());
            selectModel.setValue(t.getTerm_id());
            s.add(selectModel);
        }
        return s;
    }
}
