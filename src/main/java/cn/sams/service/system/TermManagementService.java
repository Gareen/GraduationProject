package cn.sams.service.system;

import cn.sams.common.constants.Constant;
import cn.sams.common.constants.DateFormat;
import cn.sams.common.util.Chk;
import cn.sams.common.util.DateUtil;
import cn.sams.common.util.JsonUtil;
import cn.sams.dao.system.TermManagementDao;
import cn.sams.entity.Term;
import cn.sams.entity.commons.ReturnObj;
import cn.sams.entity.commons.SelectModel;
import org.aspectj.apache.bcel.generic.RET;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

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

    public ReturnObj queryTermByTermId(HttpServletRequest req) {
        String termId = req.getParameter("termId");

        if (!Chk.spaceCheck(termId)) {
            return new ReturnObj(Constant.ERROR, "查询失败！学期编号缺失", null);
        }

        return new ReturnObj(Constant.SUCCESS, "", termManagementDao.queryTermByTermId(termId));
    }

    public ReturnObj deleteTermByTermId(HttpServletRequest req) {
        String termId = req.getParameter("termId");

        if (!Chk.spaceCheck(termId)) {
            return new ReturnObj(Constant.ERROR, "查询失败：学期编号缺失", null);
        }

        int count = termManagementDao.deleteTermByTermId(termId);

        if (count < 1) {

            return new ReturnObj(Constant.ERROR, "删除失败：数据库异常！", null);
        } else {

            return new ReturnObj(Constant.SUCCESS, "删除成功！", termId);

        }
    }

    public ReturnObj saveOrUpdate(HttpServletRequest req) {
        String data = req.getParameter("data");

        if (!Chk.spaceCheck(data)) {
            return new ReturnObj(Constant.ERROR, "保存失败：关键数据缺失！", null);
        }

        Map<String, String> dataMap = JsonUtil.toMap(data, String.class, String.class);

        if (!Chk.emptyCheck(dataMap)) {
            return new ReturnObj(Constant.ERROR, "保存失败：数据转换异常！", null);
        }

        String permission = dataMap.get("tea");

        if (!"1".equalsIgnoreCase(permission)) {
            return new ReturnObj(Constant.ERROR, "操作失败：权限不足！", null);
        }

        String mode = dataMap.get("optMode");

        String termId = dataMap.get("termId");
        String termName = dataMap.get("termName");
        String termYear = dataMap.get("termYear");
        String termMon = dataMap.get("termMon");

        // 新增
        if ("add".equalsIgnoreCase(mode)) {

            // 先查询库中是否已有该学期的信息
            Term term = termManagementDao.queryTermByTermId(termId);

            if (term != null) {
                return new ReturnObj(Constant.ERROR, "新增失败：学期已存在！", null);
            }

            int count = termManagementDao.save(termId, termName, termYear, termMon);

            if (count < 1) {
                return new ReturnObj(Constant.ERROR, "新增失败：保存异常！", null);
            }

            return new ReturnObj(Constant.SUCCESS, "新增成功，刷新页面生效！", null);
        }

        // 修改
        if ("mod".equalsIgnoreCase(mode)) {

            int count = termManagementDao.update(termId, termName, termYear, termMon);

            if (count < 1) {
                return new ReturnObj(Constant.ERROR, "修改失败：保存异常！", null);
            }

            return new ReturnObj(Constant.SUCCESS, "修改成功，刷新页面生效！", null);

        }

        return new ReturnObj(Constant.ERROR, "操作失败：操作类型错误！", null);

    }
}
