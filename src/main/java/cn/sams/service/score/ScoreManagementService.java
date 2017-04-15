package cn.sams.service.score;

import cn.sams.common.util.Chk;
import cn.sams.dao.score.ScoreManagementDao;
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
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Fanpeng on 2017/4/15.
 */
@Service("scoreManagementService")
public class ScoreManagementService {

    @Resource
    private GroupInitManagementService groupInitManagementService;

    @Resource
    private HomeWorkManagementService homeWorkManagementService;

    @Resource
    private ScoreManagementDao scoreManagementDao;

    public List<Map<String, String>> query(HttpServletRequest req) {

        // 获得这学期分组的id
        String groupId = groupInitManagementService.getEncodeGroupId(req);

        // 获得这学期作业的id
        String homeWorkId = homeWorkManagementService.getWorkId(req);

        if (!Chk.spaceCheck(groupId) || !Chk.spaceCheck(homeWorkId)) {
            return new ArrayList<>();
        }

        System.out.println(groupId);
        System.out.println(homeWorkId);

        return new ArrayList<>();
    }
}
