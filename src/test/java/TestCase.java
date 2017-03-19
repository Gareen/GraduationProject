import cn.sams.dao.system.LoginDao;
import cn.sams.service.system.LoginService;
import org.apache.commons.dbcp.BasicDataSource;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class TestCase {

    private ApplicationContext ac = null;

    @Before
    public void init() {
        ac = new ClassPathXmlApplicationContext("spring-context.xml");
    }

    @Test
    public void test() throws SQLException {
        BasicDataSource ds = ac.getBean("ds", BasicDataSource.class);
        System.out.println(ds.getConnection());
    }

    // 测试lambda表达式
    @Test
    public void test1() {
        String[] atp = {"Rafael Nadal", "Novak Djokovic", "Stanislas Wawrinka", "David Ferrer","Roger Federer", "Andy Murray","Tomas Berdych", "Juan Martin Del Potro"};
        List<String> players =  Arrays.asList(atp);
        System.out.println("before: " + players);
        players.forEach((player) -> System.out.print(player + ", "));

        Arrays.sort(atp, (String o1, String o2) -> (o1.compareTo(o2)));
        players =  Arrays.asList(atp);
        System.out.println("after: " + players);
    }

    @Test
    public void test2() {
        LoginDao loginDao = ac.getBean("loginDao", LoginDao.class);
        System.out.println(loginDao.findTeacherByID("10000"));
    }

    @Test
    public void test3() {
        LoginService loginService = ac.getBean("loginService", LoginService.class);
        System.out.println(loginService.findTeacherByID("10000"));
    }

    @Test
    public void test4() {
        for (long i = 0; i < 300000000; i++) {
            System.out.println(i);
        }

    }



}
