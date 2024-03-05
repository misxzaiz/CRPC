package chen.shangquan.utils.balance;

import chen.shangquan.utils.balance.impl.RandomLoadbalancing;
import chen.shangquan.utils.balance.impl.WeightLoadBalancing;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WeightLoadBalancingTest {
//    @Test
//    public void t1() {
//        Map<String, Integer> map = new HashMap<>();
//        map.put("127.0.0.1:8001", 1);
//        map.put("127.0.0.1:8002", 2);
//        map.put("127.0.0.1:8003", 3);
//        map.put("127.0.0.1:8004", 4);
//        map.put("127.0.0.1:8005", 5);
//        map.put("127.0.0.1:8006", 6);
//        map.put("127.0.0.1:8007", 7);
//        map.put("127.0.0.1:8008", 8);
//        map.put("127.0.0.1:8009", 9);
//        map.put("127.0.0.1:8010", 10);
//        LoadBalancing loadBalancing = new WeightLoadBalancing(map);
//        for (int i = 0; i < 55; i++) {
//            String s = loadBalancing.loadBalancing();
//            System.out.println(s);
//        }
//
//    }
}
