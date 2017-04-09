package cn.sams.service.score;

import cn.sams.common.util.BatchUpdateUtil;
import cn.sams.common.util.Chk;
import cn.sams.dao.score.HomeworkManagementDao;
import cn.sams.dao.system.StudentManagementDao;
import cn.sams.entity.Homework;
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
 * Created by Fanpeng on 2017/4/8.
 */
@Service("homeWorkManagementService")
public class HomeWorkManagementService {

    @Resource
    private HomeworkManagementDao homeworkManagementDao;

    @Resource
    private StudentManagementDao studentManagementDao;

    /**
     * 查询符合条件的所有作业的记录
     * 1. 先拿着workid去进行查询, 如果存在就将查询到的返回出来;
     * 2. 如果查询不到, 先在数据库中插入相应的数据, 再进行查询
     *
     * @param req
     * @return map 是因为不在是实体类中加上学生姓名的字段, 如果需要返回的话, 采用Map的形式可能更加靠谱
     */
    public List<Map<String, String>> query(HttpServletRequest req) {

        String id = getWorkId(req);
        String classId = req.getParameter("classId");
        String workIndex = req.getParameter("workIndex");

        List<Homework> hws = homeworkManagementDao.queryHomeworkByworkIdAndIndex(id, workIndex);

        // 将要返回的数据结构
        List<Map<String, String>> infos = new ArrayList<>();

        // 封装批量插入的参数
        List<Object[]> args = new ArrayList<>();

        // 插入语句
        String batchSql = "INSERT INTO sams_homework VALUES (?, ?, ?, ?)";

        List<Student> students = studentManagementDao.queryStudentsByClassId(classId);

        if (!Chk.emptyCheck(hws)) {
            // 首先先对这个表进行插入, work_id, stu_id, work_index

            if (Chk.emptyCheck(students)) {

                for (Student s : students) {
                    Object[] arr = {id, s.getStu_no(), workIndex, null};
                    args.add(arr);
                }

                // 执行批量更新
                BatchUpdateUtil.executeBatchUpdate(batchSql, args);

                // 插入完后再次进行查询
                hws = homeworkManagementDao.queryHomeworkByworkIdAndIndex(id, workIndex);

                // 如果还是为空的话, 那就直接返回空的集合, 表示没有查询到
                if (!Chk.emptyCheck(hws)) {
                    return infos;
                }
            }
        }

        // 封装Map, 将学生的姓名给封装到返回的数据结构中
        for (Homework homework : hws) {
            Map<String, String> map = new HashMap<>();
            map.put("work_id", homework.getWork_id());

            String stuNo = homework.getWork_stu_id();
            map.put("stu_id", stuNo);

            // todo 待优化, 是采用查出数据list进行遍历快还是每次都去数据库查询快
            // Student student = studentManagementDao.queryStudentByStuId(stuNo);

            map.put("stu_name", "");
            for (Student student : students) {
                String no = student.getStu_no();

                if (no.equals(homework.getWork_stu_id())) {
                    map.put("stu_name", student.getStu_name());
                    break;
                }
            }

            map.put("work_index", homework.getWork_index());
            map.put("work_score", homework.getWork_score());

            infos.add(map);
        }

        return infos;
    }

    public ReturnObj save(HttpServletRequest req) {
        String workId = req.getParameter("workid");
        String stuId = req.getParameter("stuId");
        String workIndex = req.getParameter("workIndex");
        String score = req.getParameter("score");

        if (!Chk.spaceCheck(score)) {
            score = "0";
        }

        // 成绩
        double s;

        try {
            s = Double.parseDouble(score);
        } catch (Exception e) {
            return new ReturnObj("error", "请填写数字 !", null);
        }

        Map<String, Object> args = new HashMap<>();

        args.put("workId", workId);
        args.put("stuId", stuId);
        args.put("workIndex", workIndex);
        args.put("score", s);

        int count = homeworkManagementDao.save(args);

        if (count == 0) {
            return new ReturnObj("error", "保存出错 !", null);
        }

        return new ReturnObj("success", "", null);
    }

    /**
     * 获取平时作业的id
     * <p>
     * 学期号+H+课程编号+H+班级编号
     *
     * @param req
     * @return
     */
    private String getWorkId(HttpServletRequest req) {
        String termId = req.getParameter("termId");
        String courseId = req.getParameter("courseId");
        String classId = req.getParameter("classId");

        // 如果一项为空直接返回空
        if (!Chk.spaceCheck(termId) || !Chk.spaceCheck(courseId) || !Chk.spaceCheck(classId)) {
            return "";
        }
        return termId + "H" + courseId + "H" + classId;
    }

}
