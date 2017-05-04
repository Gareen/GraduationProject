package cn.sams.dao.system;

import cn.sams.entity.Classes;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by Fanpeng on 2017/3/22.
 */
@Repository
public interface ClassManagementDao {

    /**
     * 查找所有的课程
     *
     * @return
     */
    List<Classes> queryClasses();

    /**
     * 查找所有的课程
     *
     * @return map
     */
    List<Map<String, String>> queryClassesKVToMap();

    /**
     * 根据课程id进行查询响应的课程
     *
     * @param classId
     * @return
     */
    Classes queryClassByClassId(String classId);

    /**
     * 新增班级记录
     *
     * @param classId
     * @param className
     * @return
     */
    Integer insertClasses(@Param("classId") String classId, @Param("className") String className);

    /**
     * 根据教师ID，查找到该名教师所教的所有班级
     */
    List<Map<String, String>> queryClassesByTeaId(@Param("teaId") String teaId);

    /**
     * 根据教师查找到所有的上课的班级
     *
     * @param teaId
     * @return
     */
    List<String> queryClassIdToListByTeaId(@Param("teaId") String teaId);

    Classes queryClassByClassName(@Param("className") String className);
}
