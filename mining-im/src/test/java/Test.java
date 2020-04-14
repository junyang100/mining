import java.util.HashMap;
import java.util.Map;

/**
 * Created by admin on 2020/4/14.
 */
public class Test {
    public static void main(String[] args) {
        Map map = new HashMap<>();
        map.put(1,1);
        System.out.println(map.remove(1));
        System.out.println(map.remove(1));
    }
}
