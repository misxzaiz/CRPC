package chen.shangquan.utils.balance;

import chen.shangquan.crpc.model.po.ServerInfo;
import chen.shangquan.utils.balance.impl.WeightAreaLoadBalancing;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class WeightAreaLoadBalancingTest {
    @Test
    public void test() {
        List<ServerInfo> list = new ArrayList<>();
        ServerInfo s1 = new ServerInfo();
        s1.setWeight(1);
        s1.setArea("GD");
        list.add(s1);
        ServerInfo s2 = new ServerInfo();
        s2.setWeight(2);
        s2.setArea("GD");
        list.add(s2);
        ServerInfo s3 = new ServerInfo();
        s3.setWeight(3);
        s3.setArea("GD");
        list.add(s3);
        ServerInfo s4 = new ServerInfo();
        s4.setWeight(4);
        s4.setArea("SH");
        list.add(s4);
        ServerInfo s5 = new ServerInfo();
        s5.setWeight(5);
        s5.setArea("SH");
        list.add(s5);
        WeightAreaLoadBalancing loadBalancing = new WeightAreaLoadBalancing(list);
        loadBalancing.show();
    }
}
