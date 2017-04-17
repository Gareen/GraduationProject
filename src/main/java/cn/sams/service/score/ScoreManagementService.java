package cn.sams.service.score;

import cn.sams.common.util.BatchUpdateUtil;
import cn.sams.common.util.Chk;
import cn.sams.dao.score.GroupInitManagementDao;

import cn.sams.dao.score.ScoreManagementDao;
import cn.sams.dao.system.StudentManagementDao;
import cn.sams.entity.*;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
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
    private GroupInitManagementDao groupInitManagementDao;

    @Resource
    private StudentManagementDao studentManagementDao;

    @Resource
    private ScoreManagementDao scoreManagementDao;

    public List<Map<String, String>> query(HttpServletRequest req) {

        String classId = req.getParameter("classId");
        String teaNo = req.getParameter("teaNo");

        // 获得这学期分组的id
        String groupId = groupInitManagementService.getEncodeGroupId(req);

        // 获得这学期作业的id
        String homeWorkId = homeWorkManagementService.getWorkId(req);

        // 获得这学期的期末成绩id
        String finalId = getEncodeFinalId(req);

        if (!Chk.spaceCheck(groupId) || !Chk.spaceCheck(homeWorkId) ||
                !Chk.spaceCheck(finalId) || !Chk.spaceCheck(teaNo) || !Chk.spaceCheck(classId)) {
            return new ArrayList<>();
        }

        // !important 此处先对当前查询条件下的表中数据进行一次清空, 意思就是每次打开的时候都要进行一次重新的数据插入
        delDataByFinalId(finalId);

        // 根据班级的id查询所有的学生
        List<Student> students = studentManagementDao.queryStudentsByClassId(classId);

        if (!Chk.emptyCheck(students)) {
            return new ArrayList<>();
        }

        // 根据分组编号查询出所有的分组成绩情况
        Map<String, Double> group_scores = countGroupStuScore(groupId);

        // 根据作业编号查询到每个学生对应的平均作业成绩
        Map<String, Double> work_scores = countHomeworkStuScore(homeWorkId);

        // 批量插入参数列表
        List<Object[]> argsList = new ArrayList<>();

        String updateSQL = "INSERT INTO sams_finalgrade VALUES (?, ?, ?, ?, ?, ?, ?)";

        for (Student student : students) {
            String stuNo = student.getStu_no();
            Object[] args = {finalId, work_scores.get(stuNo), group_scores.get(stuNo), null, null, stuNo, ""};
            argsList.add(args);
        }

        BatchUpdateUtil.executeBatchUpdate(updateSQL, argsList);

        return getResultMap(students, finalId);
    }

    /**
     * 计算实验分组中每个成员的分数
     *
     * @param groupId 分组号
     * @return 每位学生的对应的实验分数 Map<学生学号, 实验分数>
     */
    private Map<String, Double> countGroupStuScore(String groupId) {

        // 从分组表中查询到一共多少个分组, 不从分组成绩表中进行查询, 因为存在大量的重复
        List<Group> groups = groupInitManagementDao.queryGroupsByGroupId(groupId);

        if (!Chk.emptyCheck(groups)) {
            return new HashMap<>();
        }

        // 查询实验次数, 实验次数为0, 说明没有实验成绩
        Integer expIndex = scoreManagementDao.queryExpIndex(groupId);

        if (expIndex <= 0) {
            return new HashMap<>();
        }

        // 存放学生学号和实验成绩的map
        Map<String, Double> map = new HashMap<>();
        for (Group group : groups) {

            String groupNum = group.getGroup_num();

            Double score = scoreManagementDao.countGroupScoreByGroupIdAndGroupNum(groupId, groupNum) / expIndex;

            map.put(group.getStu_is_leader(), score);

            String[] ids = group.getStu_is_member().split(",");

            for (String id : ids) {
                map.put(id, score);
            }
        }

        return map;
    }

    /**
     * 计算每个学生的平时作业的平均成绩
     *
     * @param workId 作业编号
     * @return Map<学生学号, 作业分数>
     */
    private Map<String, Double> countHomeworkStuScore(String workId) {
        // 先查一下次数, 如果次数为0的话, 直接返回一个空的集合
        List<Map<String, Double>> hws = scoreManagementDao.countHomeworkScoreByWorkId(workId);
        if (!Chk.emptyCheck(hws)) {
            return new HashMap<>();
        }

        Map<String, Double> maps = new HashMap<>();

        for (Map map : hws) {
            maps.put((String) map.get("stuNo"), (Double) map.get("sum"));
        }

        return maps;
    }

    /**
     * 组装最后返回给页面的结果集
     *
     * @param
     * @param students
     * @return
     */
    private List<Map<String, String>> getResultMap(List<Student> students, String finalId) {
        // 每次进行页面显示的时候都需要查询下最新的
        List<FinalGrade> fgs = scoreManagementDao.queryFinalsByFinalId(finalId);

        if (!Chk.emptyCheck(fgs)) {
            return new ArrayList<>();
        }

        List<Map<String, String>> resultList = new ArrayList<>();
        Map<String, String> map;

        for (FinalGrade fg : fgs) {
            map = new HashMap<>();

            map.put("finalId", fg.getFinal_id());
            map.put("stuNo", fg.getFinal_stu_id());

            map.put("stuName", "");
            for (Student student : students) {
                if (fg.getFinal_stu_id().equalsIgnoreCase(student.getStu_no())) {
                    map.put("stuName", student.getStu_name());
                    break;
                }
            }

            map.put("hScore", fg.getFinal_work_score());
            map.put("eScore", fg.getFinal_exp_score());
            map.put("fScore", fg.getFinal_exam_score());
            map.put("remark", fg.getFinal_remark());
            map.put("score", fg.getFinal_score());

            resultList.add(map);
        }

        return resultList;
    }

    /**
     * 先对表中的数据进行一次清空
     *
     * @param finalId
     * @return
     */
    private synchronized void delDataByFinalId(String finalId) {
        scoreManagementDao.delDataByFinalId(finalId);
    }

    /**
     * 获得学期的编号
     *
     * @param req
     * @return
     */
    public String getEncodeFinalId(HttpServletRequest req) {
        String termId = req.getParameter("termId");
        String courseId = req.getParameter("courseId");
        String classId = req.getParameter("classId");

        // 如果一项为空直接返回空
        if (!Chk.spaceCheck(termId) || !Chk.spaceCheck(courseId) || !Chk.spaceCheck(classId)) {
            return "";
        }
        return termId + "F" + courseId + "F" + classId;
    }
}
