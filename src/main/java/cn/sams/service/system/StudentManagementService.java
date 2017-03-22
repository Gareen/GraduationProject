package cn.sams.service.system;

import cn.sams.common.util.Chk;
import cn.sams.dao.system.ClassManagementDao;
import cn.sams.dao.system.StudentManagementDao;
import cn.sams.entity.Classes;
import cn.sams.entity.Student;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fanpeng on 2017/3/22.
 */
@Service("studentManagementService")
public class StudentManagementService {

    @Resource
    private StudentManagementDao studentManagementDao;

    @Resource
    private ClassManagementDao classManagementDao;


    public List<Student> queryStudents(HttpServletRequest req) {

        List<Student> students = studentManagementDao.queryStudents();
        List<Student> s = new ArrayList<>();

        if (Chk.emptyCheck(students)) {

            for (Student student : students) {
                Student stu = new Student();

                stu.setStu_no(student.getStu_no());
                stu.setStu_name(student.getStu_name());
                stu.setStu_gender("0".equals(student.getStu_gender()) ? "男" : "女");

                String classId = student.getStu_class_id();
                Classes c = classManagementDao.queryClassByClassId(classId);

                stu.setStu_class_id((c != null && Chk.spaceCheck(c.getClass_name())) ? c.getClass_name() : "");

                s.add(stu);
            }
            return s;
        }

        return new ArrayList<>();
    }
}
