package cn.sams.common.util;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * jdbcTemplate
 *
 * @author fanpeng
 */
public class BatchUpdateUtil {

    private static JdbcTemplate jdbcTemplate;

    static {
        jdbcTemplate = new ClassPathXmlApplicationContext("spring-mybatis.xml").getBean("jdbcTemplate", JdbcTemplate.class);
    }

    /**
     * 执行批量更新
     *
     * 参数列表: 需要用数组的形式封装一条要插入的信息所有的数据
     *
     * @param updateSQL 更新的sql
     * @param batchArgs 参数列表
     */
    public static void executeBatchUpdate(String updateSQL, List<Object[]> batchArgs) {

        Connection con = null;
        try {
            con = jdbcTemplate.getDataSource().getConnection();
            // 设置不自动提交
            con.setAutoCommit(false);
            // 如果要插入的参数超过9000条, 则开始进行分批次批量更新
            int s = batchArgs.size() / 9000 + 1;
            // 缓存待插入的参数列表
            List<Object[]> batchArgsList;
            for (int i = 1; i <= s; i++) {
                if (i <= s - 1) {
                    batchArgsList = batchArgs.subList((i - 1) * 9000, i * 9000);
                } else {
                    batchArgsList = batchArgs.subList((i - 1) * 9000, batchArgs.size());
                }
                // 执行批量更新
                jdbcTemplate.batchUpdate(updateSQL, batchArgsList);
            }
            //手动提交
            con.commit();
            //还原自动提交
            con.setAutoCommit(true);
        } catch (Exception e) {
            e.printStackTrace();
            try {
                //事务回滚
                if (con != null) {
                    con.rollback();
                    con.setAutoCommit(true);
                }
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        } finally {
            try {
                if (con != null) {
                    //关闭连接
                    con.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * 获取jdbcTemplate
     *
     * @return
     */
    public static JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

}
