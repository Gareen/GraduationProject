import cn.sams.entity.commons.SelectModel;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Before;
import org.junit.Test;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.SQLException;
import java.util.Comparator;

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

    @Test
    public void test2() {

        Comparator c = new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                return 0;
            }
        };

        Comparator c1 = (o1, o2) -> 0;

        Comparator.comparing(SelectModel::getValue);

    }


}
