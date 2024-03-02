package chen.shangquan.utils.balance;

import chen.shangquan.utils.balance.impl.RandomLoadbalancing;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class RandomLoadbalancingTest {

    @Test
    public void t1() {
        List<String> list = new ArrayList<>();
        list.add("6");
        list.add("7");
        list.add("8");
        list.add("9");
        list.add("0");
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");
        list.add("5");
        LoadBalancing loadBalancing = new RandomLoadbalancing(list);
        for (int i = 0; i < 10; i++) {
            Object balance = loadBalancing.loadBalancing();
            System.out.println(balance);
        }
    }
}
