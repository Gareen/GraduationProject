package cn.sams.service.score;

import cn.sams.common.constants.Constant;
import cn.sams.common.util.BatchUpdateUtil;
import cn.sams.common.util.Chk;
import cn.sams.common.util.JsonUtil;
import cn.sams.common.util.NumberUtil;
import cn.sams.dao.score.GroupInitManagementDao;
import cn.sams.dao.score.GroupManagementDao;
import cn.sams.dao.system.StudentManagementDao;
import cn.sams.entity.Group;
import cn.sams.entity.Student;
import cn.sams.entity.commons.ReturnObj;
import cn.sams.entity.commons.SelectModel;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;


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

        // 一个是实验成绩分组全部为空的情况，一个是实验成绩分组不全部为空，但是实验分组的数目和实验成绩分组的数目不一致的情况
        // 实验成绩分组全部为空
        String updateSql = "INSERT INTO sams_group_score VALUES (?, ?, ?, ?, ?, ?, ?)";

        if (!Chk.emptyCheck(groups)) {
            // 即将批量插入的数据
            List<Object[]> args = new ArrayList<>();

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

        // 一个是实验成绩分组不全部为空，但是实验分组的数目和实验成绩分组的数目不一致的情况
        if (Chk.emptyCheck(groups) && g.size() != groups.size()) {
            // 封装参数列表
            List<Object[]> argsList = new ArrayList<>();
            // 当分组管理模块(groupInit)中的分组数 < 分组实验中的分组数的时候, 应该将分组实验中的对应分组进行删除
            if (g.size() < groups.size()) {
                // 分组数小一般不可能
                System.out.println(">>>>分组数小");
            }

            if (g.size() > groups.size()) {

                g.removeAll(groups);

                // 新增一条的情况
                if (g.size() == 1) {
                    Group group = g.get(0);
                    Object[] args = {group.getGroup_id(), group.getGroup_num(), expIndex, null, null, null, null};
                    argsList.add(args);
                }

                // 新增为多条的情况
                if (g.size() > 1) {
                    for (Group group : g) {
                        Object[] args = {group.getGroup_id(), group.getGroup_num(), expIndex, null, null, null, null};
                        argsList.add(args);
                    }
                }
                BatchUpdateUtil.executeBatchUpdate(updateSql, argsList);
            }

            // 插入后, 再次查询一遍
            groups = groupManagementDao.queryGroupsByGroupIdAndExpIndex(groupId, expIndex);

            // 分组也要再次查找一遍, 不然会出现removeAll后不全的问题, Mybatis默认开启一级缓存, 会从缓存中调去
            g = groupInitManagementDao.queryGroupsByGroupId(groupId);
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

        // scoreGroup中只存在预习, 实验, 报告的分数的值
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

        // 根据不同的分数字段动态保存分数值
        int count = groupManagementDao.save(datafield, s, result_score, group_id, group_num, ex_index);

        if (count <= 0) {
            return new ReturnObj("error", "保存失败: 数据库错误 !", null);
        }


        return new ReturnObj("success", "", null);
    }


   /* *
     * 使用statement的时候, 需要补充上''
     * @param value
     * @return
    private String getArgsStr(String value) {
        return "'" + value + "'";
    }*/

    /**
     * 查询实验成绩次数
     *
     * @param req
     * @return
     */
    public ReturnObj queryScoreCounts(HttpServletRequest req) {
        String data = req.getParameter("data");

        if (!Chk.spaceCheck(data)) {
            return new ReturnObj(Constant.ERROR, "查询成绩次数失败, 关键数据缺失!", null);
        }

        Map<String, String> map = JsonUtil.toMap(data, String.class, String.class);

        if (!Chk.emptyCheck(map)) {
            return new ReturnObj(Constant.ERROR, "查询成绩次数失败, 关键数据转换失败!", null);
        }
        String id = map.get("teaNo") + "T" + map.get("termId") +
                "T" + map.get("courseId") + "T" + map.get("classId");

        // 根据groupId查找次数
        List<Map<String, String>> counts = groupManagementDao.queryScoreCount(id);

        if (!Chk.emptyCheck(counts)) {
            return new ReturnObj(Constant.ERROR, "查询成绩次数失败, 关键数据转换失败!", null);
        }

        List<SelectModel> csm = new ArrayList<>();

        for (Map m : counts) {
            SelectModel sm = new SelectModel();

            sm.setKey(m.get("counts").toString());
            sm.setValue(m.get("counts").toString());

            csm.add(sm);
        }

        return new ReturnObj(Constant.SUCCESS, "", csm);
    }

    public ReturnObj resetScore(HttpServletRequest req) {
        String data = req.getParameter("data");

        if (!Chk.spaceCheck(data)) {
            return new ReturnObj(Constant.ERROR, "重置失败, 关键数据缺失!", null);
        }

        Map<String, String> map = JsonUtil.toMap(data, String.class, String.class);

        if (!Chk.emptyCheck(map)) {
            return new ReturnObj(Constant.ERROR, "重置失败, 关键数据转换失败!", null);
        }

        String id = map.get("teaNo") + "T" + map.get("termId") +
                "T" + map.get("courseId") + "T" + map.get("classId");

        String cindex = map.get("cindex");

        int count = groupManagementDao.reset(id, cindex);

        if (count < 1) {
            return new ReturnObj(Constant.ERROR, "重置失败, 数据库错误!", null);
        }

        return new ReturnObj(Constant.SUCCESS, "重置成功!", null);

    }

    public ReturnObj deleteScore(HttpServletRequest req) {

        String data = req.getParameter("data");

        if (!Chk.spaceCheck(data)) {
            return new ReturnObj(Constant.ERROR, "重置失败, 关键数据缺失!", null);
        }

        Map<String, String> map = JsonUtil.toMap(data, String.class, String.class);

        if (!Chk.emptyCheck(map)) {
            return new ReturnObj(Constant.ERROR, "重置失败, 关键数据转换失败!", null);
        }

        String id = map.get("teaNo") + "T" + map.get("termId") +
                "T" + map.get("courseId") + "T" + map.get("classId");

        String cIndex = map.get("cIndex");

        int count = groupManagementDao.deleteScoreByIndex(id, cIndex);

        if (count < 1) {
            return new ReturnObj(Constant.ERROR, "删除失败, 数据库错误!", null);
        }

        return new ReturnObj(Constant.SUCCESS, "删除第" + cIndex + "次成绩成功, 请刷新页面!", null);

    }
}
