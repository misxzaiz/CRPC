package chen.shangquan.crpc.center;

import chen.shangquan.crpc.center.zookeeper.CuratorClient;
import org.apache.zookeeper.CreateMode;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

public class CrpcRegisterCenterTest {

    @Test
    public void test() throws Exception {
        CrpcRegisterCenter.connect();
        CrpcRegisterCenter.create("/cprc/test", "test".getBytes(StandardCharsets.UTF_8), CreateMode.EPHEMERAL_SEQUENTIAL);
        while (true) {

        }
    }
}
