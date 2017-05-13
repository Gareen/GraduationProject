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

    /**
     * 根据班级编号删除学生
     *
     * @param classId
     * @return
     */
    Integer deleteStuByClassId(@Param("classId") String classId);

    /**
     * 保存学生
     *
     * @param stuNo
     * @param stuName
     * @param gender
     * @param classId
     * @return
     */
    Integer save(@Param("stuNo") String stuNo, @Param("stuName") String stuName,
                 @Param("gender") String gender, @Param("classId") String classId);

    /**
     * 更新学生
     *
     * @param stuNo
     * @param stuName
     * @param gender
     * @param classId
     * @return
     */
    Integer update(@Param("stuNo") String stuNo, @Param("stuName") String stuName,
                   @Param("gender") String gender, @Param("classId") String classId);

}
