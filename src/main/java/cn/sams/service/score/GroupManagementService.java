package cn.sams.service.score;

import cn.sams.common.util.BatchUpdateUtil;
import cn.sams.common.util.Chk;
import cn.sams.common.util.NumberUtil;
import cn.sams.dao.score.GroupInitManagementDao;
import cn.sams.dao.score.GroupManagementDao;
import cn.sams.dao.system.StudentManagementDao;
import cn.sams.entity.Group;
import cn.sams.entity.Student;
import cn.sams.entity.commons.ReturnObj;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Fanpeng on 2017/4/09.
 */
@Service("groupManagementService")
public class GroupManagementService {

    @Resource
    private GroupManagementDao groupManagementDao;

    @Resource
    private GroupInitManagementService groupInitManagementService;

    @Resource
    private GroupInitManagementDao groupInitManagementDao;

    @Resource
    private StudentManagementDao studentManagementDao;

    public List<Map<String, String>> query(HttpServletRequest req) {

        String groupId = groupInitManagementService.getEncodeGroupId(req);
        String expIndex = req.getParameter("expIndex");
        String classId = req.getParameter("classId");

        if (!Chk.spaceCheck(expIndex) || !Chk.spaceCheck(groupId)) {
            // 如果传值出问题, 一律返回空集合
            return new ArrayList<>();
        }

        List<Map<String, String>> groupMaps = new ArrayList<>();

        // 查询在当前的groupid下面已经有的分组
        List<Group> g = groupInitManagementDao.queryGroupsByGroupId(groupId);

        if (!Chk.emptyCheck(g)) {
            // 如果这个id在分组管理的时候就没有生成, 那么就需要去分组管理中添加该分组
            System.out.println("未找到该分组信息, 请添加后重试");
            return new ArrayList<>();
        }

        // 首先通过groupid查询有没有分组信息, 有的话, 进行返回信息, 没有的话, 先插入信息
        List<Group> groups = groupManagementDao.queryGroupsByGroupIdAndExpIndex(groupId, expIndex);


        if (!Chk.emptyCheck(groups)) {
            // 即将批量插入的数据
            List<Object[]> args = new ArrayList<>();

            String updateSql = "INSERT INTO sams_group_score VALUES (?, ?, ?, ?, ?, ?, ?)";

            // 如果为空的话, 那就先插入一批数据
            for (Group group : g) {
                Object[] array = {group.getGroup_id(), group.getGroup_num(),
                        expIndex, null, null, null, null};

                args.add(array);
            }
            BatchUpdateUtil.executeBatchUpdate(updateSql, args);

            // 插入后, 再次查询一遍
            groups = groupManagementDao.queryGroupsByGroupIdAndExpIndex(groupId, expIndex);
        }

        // 通过班级编号, 将学生查询出来
        List<Student> students = studentManagementDao.queryStudentsByClassId(classId);

        for (Group group : groups) {

            Map<String, String> m = new HashMap<>();

            m.put("group_id", group.getGroup_id());
            m.put("group_num", group.getGroup_num());
            m.put("ex_index", group.getEx_index());

            m.put("group_leader", "");
            String leaderId = "";
            for (Group group1 : g) {
                if (group1.getGroup_id().equals(group.getGroup_id()) && group1.getGroup_num().equals(group.getGroup_num())) {
                    leaderId = group1.getStu_is_leader();
                    break;
                }
            }
            for (Student student : students) {
                if (leaderId.equals(student.getStu_no())) {
                    m.put("group_leader", student.getStu_no() + " " + student.getStu_name());
                    break;
                }
            }

            m.put("pre_score", group.getPre_score());
            m.put("ex_score", group.getEx_score());
            m.put("re_score", group.getRe_score());

            groupMaps.add(m);
        }

        return groupMaps;
    }

    public synchronized ReturnObj save(HttpServletRequest req) {
        String group_id = req.getParameter("group_id");
        String datafield = req.getParameter("datafield");
        String group_num = req.getParameter("group_num");
        String ex_index = req.getParameter("ex_index");
        String score = req.getParameter("score");

        if (!Chk.spaceCheck(group_id) || !Chk.spaceCheck(datafield) || !Chk.spaceCheck(group_num) || !Chk.spaceCheck(ex_index)) {
            return new ReturnObj("error", "保存失败: 数据不足 !", null);
        }

        // 这个group中只存在预习, 实验, 报告的分数的值
        Group scoreGroup = groupManagementDao.queryGroupScore(group_id, group_num, ex_index);

        double pre = NumberUtil.getStrToDouble(scoreGroup.getPre_score());
        double ex = NumberUtil.getStrToDouble(scoreGroup.getEx_score());
        double re = NumberUtil.getStrToDouble(scoreGroup.getRe_score());

        double s;

        try {
            s = Double.parseDouble(score);
        } catch (Exception e) {
            return new ReturnObj("error", "保存失败: 请填写数值类型的数据 !", null);
        }

        if (s < 0 || s > 100) {
            return new ReturnObj("error", "保存失败: 分数范围0 - 100 !", null);
        }

        if ("pre_score".equalsIgnoreCase(datafield)) {
            pre = s;
        }

        if ("ex_score".equalsIgnoreCase(datafield)) {
            ex = s;
        }

        if ("re_score".equalsIgnoreCase(datafield)) {
            re = s;
        }

        double result_score = (pre + ex + re) / 3;

        int count = groupManagementDao.save(datafield, s, result_score, getArgsStr(group_id), getArgsStr(group_num), getArgsStr(ex_index));

        if (count <= 0) {
            return new ReturnObj("error", "保存失败: 数据库错误 !", null);
        }


        return new ReturnObj("success", "", null);
    }


    /**
     * 使用statement的时候, 需要补充上''
     * @param value
     * @return
     */
    public String getArgsStr(String value) {
        return "'" + value + "'";
    }


}
