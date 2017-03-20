package cn.sams.service.score;

import cn.sams.common.util.Chk;
import cn.sams.common.util.CollectionUtil;
import cn.sams.common.util.SelectModelUtil;
import cn.sams.dao.score.GroupManagementDao;
import cn.sams.entity.Course;
import cn.sams.entity.Group;
import cn.sams.entity.Student;
import cn.sams.entity.Teacher;
import cn.sams.entity.commons.ReturnObj;
import cn.sams.entity.commons.SelectModel;
import org.apache.ibatis.jdbc.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by Fanpeng on 2017/2/27.
 */
@Service("groupManagementService")
public class GroupManagementService {

    @Autowired
    private GroupManagementDao groupManagementDao;

    public ReturnObj queryCourseTeacher(HttpServletRequest req) {
        String courseKey = req.getParameter("courseKey");
        String placeKey = req.getParameter("placeKey");

        // 2017-02-13 一 00:00:00
        String date = req.getParameter("date");

        if (!Chk.spaceCheck(courseKey) || !Chk.spaceCheck(placeKey) || !Chk.spaceCheck(date)) {
            return new ReturnObj("error", "", null);
        }

        // 此处为写死的第一周
        String week = "1";

        String[] time = date.split(" ");
        String weekday = time[1];
        String classtime = time[2];


        return new ReturnObj("", "", null);
    }

    public List<SelectModel> queryStu() {

        List<Student> students = groupManagementDao.queryStu();

        if (students == null) {
            return new ArrayList<>();
        }

        List<SelectModel> list = new ArrayList<>();
        for (Student s : students) {
            SelectModel selectModel = new SelectModel();
            selectModel.setKey(s.getStu_no());
            selectModel.setValue(s.getStu_no() + " " + s.getStu_name());
            list.add(selectModel);
        }
        return list;
    }


    /**
     * 此处暂时返回教师的姓名, 以后需要返回的是一个学生列表
     *
     * @param req
     * @return
     */
    public Teacher queryTeacherByInfo(HttpServletRequest req) {
        String courseKey = req.getParameter("courseKey");
        String classPlace = req.getParameter("classPlace");
        String classWeek = req.getParameter("classWeek");
        String classTime = req.getParameter("classTime");

        // day_of_week
        String dayOfWeek;
        // class_time
        String time;
        if (!Chk.spaceCheck(courseKey) || !Chk.spaceCheck(classPlace) || !Chk.spaceCheck(classWeek) || !Chk.spaceCheck(classTime)) {
            return null;
        } else {
            String[] arr = classTime.split(",");
            dayOfWeek = arr[0];
            time = arr[1];
        }
        Course course = groupManagementDao.queryTeacherIdByInfo(courseKey, classPlace, classWeek, dayOfWeek, time);
        if (course == null) {
            return null;
        }
        return groupManagementDao.queryTeacherName(course.getCou_tea_no());
    }

    public List<Group> queryGroups(HttpServletRequest req) {
        String group_id = req.getParameter("id");

        if (!Chk.spaceCheck(group_id)) {
            return new ArrayList<>();
        }
        List<Group> groups = groupManagementDao.queryGroupsByGroupId(group_id);
        return showStuNameGroups(groups);
    }


    public ReturnObj deleteRowByIdAndNum(HttpServletRequest req) {
        String groupId = req.getParameter("id");
        String groupNum = req.getParameter("num");

        if (!Chk.spaceCheck(groupId) || !Chk.spaceCheck(groupNum)) {
            return new ReturnObj("error", "删除异常 !", null);
        }

        int count = groupManagementDao.deleteRowByIdAndNum(groupId, groupNum);

        if (count == 0) {
            return new ReturnObj("error", "该条记录可能已被删除, 请刷新重试 !", null);
        } else {
            return new ReturnObj("success", "删除记录成功 !", null);
        }

    }

    public ReturnObj saveorupdate(HttpServletRequest req) {
        // jud判断是新增还是修改后保存
        String jud = req.getParameter("jud");
        // 分组编号
        String group_num = req.getParameter("group_num");
        // 小组长id
        String group_leader = req.getParameter("group_leader");
        // 组员id
        String group_member = req.getParameter("group_member");
        // 小组分数
        String group_score = req.getParameter("group_score");

        if (!"add".equals(jud)) {
            if (!"mod".equals(jud)) {
                return new ReturnObj("error", "请选择正确的操作 !", null);
            }
        }

        // 获取分组的id编码
        String group_id = getEncodeGroupid(req);

        if (!Chk.spaceCheck(group_id)) {
            return new ReturnObj("", "", null);
        }
        List<Group> groups = groupManagementDao.queryGroupsByGroupId(group_id);

        if ("add".equals(jud)) {

            for (Group p : groups) {
                if (group_num.equals(p.getGroup_num())) {
                    return new ReturnObj("error", "添加失败: 小组号重复 !", null);
                }
                if (group_leader.equals(p.getStu_is_leader())) {
                    return new ReturnObj("error", "添加失败: 小组长重复 !", null);
                }
            }

            int count = groupManagementDao.save(group_id, group_leader, group_member, group_num, group_score);

            if (count != 0) {
                return new ReturnObj("success", "新增数据成功 !", null);
            } else {
                return new ReturnObj("error", "新增数据失败 !", null);
            }

        } else {
            // 更新分组操作

            Group group = groupManagementDao.queryGroupInfoByIdAndNum(group_id, group_num);

            if (group == null) {
                return new ReturnObj("error", "更新失败: 查无此数据 !", null);
            }

            for (Group p : groups) {
                if (group_leader.equals(p.getStu_is_leader())) {

                    // 如果修改的不是本条记录时, 出现别的小组的小组长, 就报错
                    if (!group_leader.equals(group.getStu_is_leader())) {
                        return new ReturnObj("error", "更新失败: 和其他小组长重复 !", null);
                    }
                }
            }

            for (Group p : groups) {
                // 只能够更新已有的分组, 没有的分组提示报错信息
                // 也就是说要修改分组号, 只能删除重建分组
                if (group_num.equals(p.getGroup_num())) {

                    int count = groupManagementDao.update(group_id, group_leader, group_member, group_num, group_score);

                    if (count != 0) {
                        return new ReturnObj("success", "更新成功 !", null);
                    } else {
                        return new ReturnObj("error", "记录可能被删除, 请刷新后操作 !", null);
                    }
                }
            }
            return new ReturnObj("error", "请选择已有的分组号进行修改操作 !", null);
        }

    }

    public Group queryGroupInfoByIdAndNum(HttpServletRequest req) {
        String id = req.getParameter("id");
        String num = req.getParameter("num");

        if (!Chk.spaceCheck(id) || !Chk.spaceCheck(num)) {
            return new Group();
        }

        return groupManagementDao.queryGroupInfoByIdAndNum(id, num);
    }

    /**
     * 获取新增时候的groupid
     *
     * @param
     * @return
     */
    private String getEncodeGroupid(HttpServletRequest req) {
        String group_id = req.getParameter("group_code");
        // 保存的时候的分组编号是进行的一个组合, 组合的方式采用一种编码格式
        // 课程所在年份+课程号+课程周数+课程上课时间+课程地点编号+分组编号, 这个需要在页面上对控件的值进行获取
        // 2017 705058 402 1 tue 4
        if (!Chk.spaceCheck(group_id)) {
            return "";
        }
        return group_id;
    }

    /**
     * 封装在页面上展示学生姓名的方法
     *
     * @param groups 查询出的分组序列
     * @return
     */
    private List<Group> showStuNameGroups(List<Group> groups) {

        if (groups == null || groups.size() == 0) {
            return new ArrayList<>();
        }

        List<Group> newGroups = new ArrayList<>();

        for (Group g : groups) {
            Group group = new Group();
            // 先将一些不需要变动的属性进行赋值
            group.setGroup_id(g.getGroup_id());
            group.setGroup_num(g.getGroup_num());
            group.setGroup_score(g.getGroup_score());
            group.setStu_is_leader(getStuNameByStuId(g.getStu_is_leader()));

            String[] memberIds = g.getStu_is_member().split(",");
            List<String> names = new ArrayList<>();

            for (String id : memberIds) {
                names.add(getStuNameByStuId(id));
            }

            String memberNames = String.join(", ", names);
            group.setStu_is_member(memberNames);

            // 将赋值成功的对象添加到序列中
            newGroups.add(group);
        }

        // 将新的分组序列进行排序
        Collections.sort(newGroups, (Group g1, Group g2) -> g1.getGroup_num().compareTo(g2.getGroup_num()));

        return newGroups;
    }

    /**
     * 通过学号查找到学生的姓名
     *
     * @param stuId 学生学号
     * @return 学生姓名
     */
    private String getStuNameByStuId(String stuId) {
        Student student = groupManagementDao.findStuById(stuId);
        if (student == null) {
            return "";
        }
        return Chk.spaceCheck(student.getStu_name()) ? student.getStu_name() : "";
    }

}
