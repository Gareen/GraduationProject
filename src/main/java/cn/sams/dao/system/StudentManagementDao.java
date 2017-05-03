package cn.sams.dao.system;

import cn.sams.entity.Student;
import org.apache.ibatis.annotations.Param;
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

    /**
     * 根据学生学号查找学生
     *
     * @param stuId 学生学号
     * @return
     */
    Student queryStudentByStuId(String stuId);

    /**
     * 根据学号删除学生
     *
     * @param stuId
     * @return
     */
    Integer deleteStudentByStuId(@Param("stuId") String stuId);
}
