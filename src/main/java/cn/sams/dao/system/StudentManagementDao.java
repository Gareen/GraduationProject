package cn.sams.dao.system;

import cn.sams.entity.Student;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Fanpeng on 2017/3/22.
 */
@Repository
public interface StudentManagementDao {

    List<Student> queryStudents();

    /**
     * 根据班级的id查找到所有的学生
     *
     * @param classId
     * @return
     */
    List<Student> queryStudentsByClassId(String classId);
}
