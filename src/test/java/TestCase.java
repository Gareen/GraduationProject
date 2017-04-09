import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Before;
import org.junit.Test;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.SQLException;

/**
 * Created by Fanpeng on 2017/4/9.
 */
public class TestCase {

    ApplicationContext applicationContext;

    @Before
    public void init() {
        applicationContext = new ClassPathXmlApplicationContext("spring-mybatis.xml");

    }

    @Test
    public void test() throws SQLException {
        JdbcTemplate jt = applicationContext.getBean("jdbcTemplate", JdbcTemplate.class);
        System.out.println(jt.getDataSource().getConnection());
    }

    @Test
    public void test1() {
        Integer.parseInt("");
    }
}
