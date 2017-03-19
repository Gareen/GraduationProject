/**
 * Created by Fanpeng on 2017/2/16.
 */
public class Test {

    public static void main(String[] args) {

        long now = System.currentTimeMillis();
        for (long i = 0; i < 3000000; i++) {

            System.out.println(i);
        }
        long end = System.currentTimeMillis();
        System.out.println((end - now)/1000 * 90 );
    }
}
