package cn.sams.service.score;

import cn.sams.common.util.Chk;
import cn.sams.dao.system.CourseDao;
import cn.sams.entity.Course;
import cn.sams.entity.Term;
import cn.sams.entity.commons.CourseInfo;
import cn.sams.entity.commons.SelectModel;
import cn.sams.service.system.CourseService;
import cn.sams.service.system.TermManagementService;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fanpeng on 2017/3/21.
 */
@Service("scoreManagementService")
public class ScoreManagementService {

    @Resource
    private TermManagementService termManagementService;

    @Resource
    private CourseDao courseDao;

    @Resource
    private CourseService courseService;

    public List<SelectModel> queryTermsSelectModels() {
        List<Term> terms = termManagementService.queryTerms();

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

    public List<SelectModel> queryCoursesSelectModels() {
        List<CourseInfo> courseInfos = courseDao.queryCourseInfo();

        List<SelectModel> s = new ArrayList<>();

        if (!Chk.emptyCheck(courseInfos)) {
            return new ArrayList<>();
        }

        for (CourseInfo c : courseInfos) {
            SelectModel selectModel = new SelectModel();

            selectModel.setKey(c.getCourse_name());
            selectModel.setValue(c.getCourse_id().toString());
            s.add(selectModel);
        }
        return s;
    }
}
