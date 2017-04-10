package cn.sams.service.score;

import cn.sams.common.constants.Constant;
import cn.sams.common.constants.DateFormat;
import cn.sams.common.util.Chk;
import cn.sams.common.util.DateUtil;
import cn.sams.dao.score.GroupInitManagementDao;
import cn.sams.dao.system.ClassManagementDao;
import cn.sams.dao.system.CourseDao;
import cn.sams.dao.system.StudentManagementDao;
import cn.sams.dao.system.TermManagementDao;
import cn.sams.entity.*;
import cn.sams.entity.commons.ReturnObj;
import cn.sams.entity.commons.SelectModel;
import cn.sams.service.system.TermManagementService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by Fanpeng on 2017/4/1.
 */
@Service("groupInitManagementService")
public class GroupInitManagementService {

    @Resource
    private TermManagementService termManagementService;

    @Resource
    private GroupInitManagementDao groupInitManagementDao;

    @Resource
    private CourseDao courseDao;

    @Resource
    private ClassManagementDao classManagementDao;

    @Resource
    private StudentManagementDao studentManagementDao;



    public Group queryGroupByGidAndGnm(HttpServletRequest req) {
        String gid = req.getParameter("group_id");
        String gnm = req.getParameter("group_num");

        if (!Chk.spaceCheck(gid) || !Chk.spaceCheck(gnm)) {
            return new Group();
        }

        return groupInitManagementDao.findGroupByGroupIdAndGroupNum(gid, gnm);
    }

    /**
     * 获取全部的学期
     *
     * @return
     */
    public List<SelectModel> queryAllTerms() {
        return termManagementService.queryTermsSelectModels();
    }

    /**
     * 根据老师的工号和学期查询课程
     *
     * @param req
     * @return
     */
    public Set<SelectModel> queryCoursesByTeacherIdAndTerm(HttpServletRequest req) {
        String teaNo = req.getParameter("teaNo");
        String termId = req.getParameter("termId");

        Set<SelectModel> set = new TreeSet<>(Comparator.comparing(SelectModel::getKey));

        if (!Chk.spaceCheck(teaNo) || !Chk.spaceCheck(termId)) {
            return new TreeSet<>();
        }

        List<Course> courses = groupInitManagementDao.queryCoursesByTeacherIdAndTerm(teaNo, termId);

        if (Chk.emptyCheck(courses)) {
            for (Course course : courses) {
                SelectModel selectModel = new SelectModel();
                String id = course.getCourse_id();

                String name = courseDao.queryCourseInfoByCouId(id).getCourse_name();

                selectModel.setValue(id);
                selectModel.setKey(name);

                set.add(selectModel);
            }
        }

        // 如果课程列表不为空, 那么就返回装配好的selectmodel
        if (Chk.emptyCheck(set)) {
            return set;
        }

        // 如果查不到, 放置一个默认值
        SelectModel sm = new SelectModel();
        sm.setKey("");
        sm.setValue("");
        set.add(sm);

        return set;
    }

    public Set<SelectModel> queryClasses(HttpServletRequest req) {
        String teaNo = req.getParameter("teaNo");
        String termId = req.getParameter("termId");
        String courseId = req.getParameter("courseId");

        Set<SelectModel> set = new TreeSet<>(Comparator.comparing(SelectModel::getKey));

        if (!Chk.spaceCheck(teaNo) || !Chk.spaceCheck(termId) || !Chk.spaceCheck(courseId)) {
            return new TreeSet<>();
        }

        // 根据三个条件确定这个老师交的所有的班级
        List<Classes> classes = groupInitManagementDao.queryClasses(teaNo, termId, courseId);

        if (Chk.emptyCheck(classes)) {
            for (Classes c : classes) {
                SelectModel selectModel = new SelectModel();
                String id = c.getClass_id();
                String name = classManagementDao.queryClassByClassId(id).getClass_name();

                selectModel.setKey(name);
                selectModel.setValue(id);

                set.add(selectModel);
            }
        }

        if (Chk.emptyCheck(set)) {
            return set;
        }

        // 如果查不到, 放置一个默认值
        SelectModel sm = new SelectModel();
        sm.setKey("");
        sm.setValue("");
        set.add(sm);

        return new TreeSet<>();
    }

    public List<Group> queryGroups(HttpServletRequest req) {

        String groupId = getEncodeGroupId(req);

        List<Group> list = new ArrayList<>();

        if (!Chk.spaceCheck(groupId)) {
            return new ArrayList<>();
        }

        List<Group> groups = groupInitManagementDao.queryGroupsByGroupId(groupId);

        if (Chk.emptyCheck(groups)) {

            StringBuilder builder = new StringBuilder("");

            for (Group group : groups) {
                Group g = new Group();
                g.setGroup_id(group.getGroup_id());
                g.setGroup_num(group.getGroup_num());

                String leaderId = group.getStu_is_leader();
                Student student = studentManagementDao.queryStudentByStuId(leaderId);
                g.setStu_is_leader(student.getStu_no() + "   " + student.getStu_name());

                String[] ids = group.getStu_is_member().split(",");

                // 如果成员的人数不为空
                if (ids.length != 0) {
                    for (String id : ids) {
                        Student s = studentManagementDao.queryStudentByStuId(id);
                        builder.append(",").append(s.getStu_no()).append("   ").append(s.getStu_name());
                    }
                    // 把首,去除掉
                    String names = builder.toString().substring(1);
                    g.setStu_is_member(names);
                    // 清空StringBuilder
                    builder.delete(0, builder.length());
                } else {
                    g.setStu_is_member("");
                }

                list.add(g);
            }
        }

        return list;
    }


    public Set<SelectModel> queryStudentsByClassId(HttpServletRequest req) {
        String classId = req.getParameter("classId");

        Set<SelectModel> set = new TreeSet<>(Comparator.comparing(SelectModel::getValue));

        if (!Chk.spaceCheck(classId)) {
            return new TreeSet<>();
        }

        List<Student> students = studentManagementDao.queryStudentsByClassId(classId);

        if (Chk.emptyCheck(students)) {
            for (Student student : students) {
                SelectModel selectModel = new SelectModel();
                String stuNo = student.getStu_no();

                selectModel.setKey(stuNo + " " + student.getStu_name());
                selectModel.setValue(stuNo);

                set.add(selectModel);
            }
        }

        if (Chk.emptyCheck(set)) {
            return set;
        } else {
            return new TreeSet<>();
        }
    }

    public synchronized ReturnObj saveOrUpdate(HttpServletRequest req) {
        String groupId = getEncodeGroupId(req);
        String jud = req.getParameter("jud");
        String groupNum = req.getParameter("group_num");
        String groupLea = req.getParameter("group_leader");
        String groupMem = req.getParameter("group_member");

        if (!Chk.spaceCheck(groupId)) {
            return new ReturnObj("error", "编码id失败, 请先进行搜索 !", null);
        }

        if (!Chk.spaceCheck(jud) || !Chk.spaceCheck(groupNum)
                || !Chk.spaceCheck(groupLea) || !Chk.spaceCheck(groupMem)) {
            return new ReturnObj("error", "各项不可为空 !", null);

        }

        List<Group> g1 = groupInitManagementDao.findGroupByGroupIdAndLeader(groupId, groupLea);

        if ("add".equals(jud)) {

            // 先进行判断, 如果id和小组编号重复就提示不可以新增
            Group g = groupInitManagementDao.findGroupByGroupIdAndGroupNum(groupId, groupNum);

            if (g != null) {
                return new ReturnObj("error", "新增分组失败: 分组已存在 !", null);
            }

            // 查找小组长, 如果小组长存在, 也返回不可以新增
            if (Chk.emptyCheck(g1)) {
                return new ReturnObj("error", "新增分组失败: 小组长已存在 !", null);
            }

            int count = groupInitManagementDao.save(groupId, groupLea, groupMem, groupNum);

            if (count != 0) {
                return new ReturnObj("success", "新增分组成功 !", null);
            } else {
                return new ReturnObj("error", "新增分组失败: 数据库异常 !", null);
            }
        }

        if ("mod".equals(jud)) {
            //todo 修改方法待完成
            // 根据groupid和groupnum来确定唯一的分组, 所以修改分组号就是需要删除再新增
            // 所以先进行查询, 如果查找不到, 那就返回没有找到分组
            Group g = groupInitManagementDao.findGroupByGroupIdAndGroupNum(groupId, groupNum);

            if (g == null) {
                return new ReturnObj("error", "修改失败: 没有找到指定的分组 !", null);
            }

            if (!Chk.emptyCheck(g1) && g1.size() > 1) {
                return new ReturnObj("error", "修改分组失败: 小组长已存在 !", null);
            }

            int count = groupInitManagementDao.update(groupId, groupLea, groupMem, groupNum);

            if (count != 0) {
                return new ReturnObj("success", "修改分组成功 !", null);
            } else {
                return new ReturnObj("error", "修改分组失败: 数据库异常 !", null);
            }
        }

        // 如果不是add或者mod, 那就是被人篡改了
        return new ReturnObj("error", "请选择正确的操作模式 !", null);
    }

    public ReturnObj deleteGroupByIdAndNum(HttpServletRequest req) {
        String gid = req.getParameter("group_id");
        String gnm = req.getParameter("group_num");

        if (!Chk.spaceCheck(gid) || !Chk.spaceCheck(gnm)) {
            return new ReturnObj("error", "删除失败: 数据不足 !", null);
        }

        int count = groupInitManagementDao.delete(gid, gnm);

        if (count != 0) {
            return new ReturnObj("success", "删除成功 !", null);
        } else {
            return new ReturnObj("error", "删除失败: 数据库异常 !", null);
        }
    }


    /**
     * 对小组进行编码
     * <p>
     * 编码规则
     * 教师工号+T+学期号+T+课程编号+T+班级编号
     *
     * @param req
     * @return 小组id
     */
    public String getEncodeGroupId(HttpServletRequest req) {
        String teaNo = req.getParameter("teaNo");
        String termId = req.getParameter("termId");
        String courseId = req.getParameter("courseId");
        String classId = req.getParameter("classId");

        // 如果一项为空直接返回空
        if (!Chk.spaceCheck(teaNo) || !Chk.spaceCheck(termId) || !Chk.spaceCheck(courseId) || !Chk.spaceCheck(classId)) {
            return "";
        }
        return teaNo + "T" + termId + "T" + courseId + "T" + classId;
    }

    /**
     * 解码小组编号
     *
     * @param groupId 小组编号
     * @return
     */
    private String[] decodeGroupId(String groupId) {
        return groupId.split("T");
    }

}
