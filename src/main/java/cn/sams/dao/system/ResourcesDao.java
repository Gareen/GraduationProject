package cn.sams.dao.system;

import cn.sams.entity.ResourcesPath;
import cn.sams.entity.Teacher;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Fanpeng on 2017/1/23.
 *
 */
@Repository
public interface ResourcesDao {

    /**
     * 修改密码
     * @param no 用户id
     * @param password 新的密码
     * @return
     */
    int updatePwd(String no, String password);

    /**
     * 根据父节点id查找导航标签
     * @param pid 父节点id
     * @return
     */
    List<ResourcesPath> findNav2ByPid(String pid);

    /**
     * 查找所有的资源路径
     * @return
     */
    List<ResourcesPath> queryResources();

    /**
     * 根据id查找根节点的名称
     *
     * @param id
     * @return
     */
    String queryParentName(String id);

    /**
     * 根据id查找节点
     *
     * @param id
     * @return
     */
    ResourcesPath queryNodeById(String id);

    /**
     * 保存资源路径
     *
     * @param path 资源路径对象
     * @return
     */
    int save(ResourcesPath path);

    /**
     * 更新资源
     *
     * @param path 资源路径对象
     * @return
     */
    int update(ResourcesPath path);

    /**
     * 删除资源
     *
     * @param id
     * @return
     */
    int delete(String id);
}
