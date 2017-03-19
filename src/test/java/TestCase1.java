import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fanpeng on 2017/2/13.
 */
public class TestCase1 {

    @Test
    public void test1() {
        int[] arr = {10, 2, 5, 2, 6, 1, 1, 3, 8, 8, 2, 6};
        List<Integer> arrList = new ArrayList<>();
        int temp = 0;
        int cnt = 1;

        for (int i = 1; i < arr.length - 1; i++) {
            int now = arr[i];
            if (arr[i] < arr[i - 1]) {
                temp = now;
            }
            if (now == arr[i + 1]) {
                cnt ++;
            } else if (now < arr[i + 1]) {
                // 需要添加
                for (int j = 0; j < cnt; j++) {
                    arrList.add(temp);
                }
            }
           /* if (arr[i] < arr[i + 1] && arr[i] < arr[i - 1]) {
                arrList.add(arr[i]);
            }*/
        }
        System.out.println(arrList);
    }
}
