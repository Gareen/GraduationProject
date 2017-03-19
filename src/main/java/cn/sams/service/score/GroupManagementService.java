package cn.sams.service.score;

import cn.sams.common.util.Chk;
import cn.sams.common.util.SelectModelUtil;
import cn.sams.dao.score.GroupManagementDao;
import cn.sams.entity.Course;
import cn.sams.entity.Student;
import cn.sams.entity.Teacher;
import cn.sams.entity.commons.ReturnObj;
import cn.sams.entity.commons.SelectModel;
import org.apache.ibatis.jdbc.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

        if (! Chk.spaceCheck(courseKey) || ! Chk.spaceCheck(placeKey) || ! Chk.spaceCheck(date)) {
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

        List<SelectModel> list= new ArrayList<>();
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
        if (!Chk.spaceCheck(courseKey) ||!Chk.spaceCheck(classPlace) ||!Chk.spaceCheck(classWeek) ||!Chk.spaceCheck(classTime)) {
            return null;
        } else {
            String[] arr = classTime.split(",");
            dayOfWeek = arr[0];
            time =  arr[1];
        }
        Course course = groupManagementDao.queryTeacherIdByInfo(courseKey, classPlace, classWeek, dayOfWeek, time);
        if (course == null) {
            return null;
        }
        return groupManagementDao.queryTeacherName(course.getCou_tea_no());
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

        if (! "add".equals(jud)) {
            if (! "mod".equals(jud)) {
                return new ReturnObj("error", "请选择正确的操作 !", null);
            }
        }

        if ("add".equals(jud)) {
            String group_id = getEncodeGroupid(req);

            if (! Chk.spaceCheck(group_id)) {
                System.out.println(group_id);
                return new ReturnObj("", "", null);
            }

            int count = groupManagementDao.save(group_id, group_leader, group_member, group_num, group_score);

            if (count != 0) {
                return new ReturnObj("success", "新增数据成功 !", null);
            } else {
                return new ReturnObj("error", "新增数据失败 !", null);
            }

        } else {

            // todo  更新操作待完成
            return new ReturnObj("", "", null);
        }

    }

    /**
     * 获取新增时候的groupid
     * @param
     * @return
     */
    private String getEncodeGroupid(HttpServletRequest req) {
        String group_id =  req.getParameter("group_code");
        // 保存的时候的分组编号是进行的一个组合, 组合的方式采用一种编码格式
        // 课程所在年份+课程号+课程周数+课程上课时间+课程地点编号+分组编号, 这个需要在页面上对控件的值进行获取
        // 2017 705058 402 1 tue 4
        if (! Chk.spaceCheck(group_id)) {
            return "";
        }
        return group_id;
    }

}
