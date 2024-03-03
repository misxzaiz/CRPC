package chen.shangquan.crpc.center;

import chen.shangquan.crpc.center.zookeeper.CuratorClient;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

public class CrpcRegisterCenterTest {

    @Test
    public void test() throws Exception {
        CrpcRegisterCenter.connect();
        String temporaryOrder = CrpcRegisterCenter.createTemporaryOrder("/cprc/test", "test".getBytes(StandardCharsets.UTF_8));
        System.out.println(temporaryOrder);
//        while (true) {
//
//        }
    }

    @Test
    public void test1() throws Exception {
        CrpcRegisterCenter.connect();
        boolean stat = CrpcRegisterCenter.checkExists("/cprc/test");
        String temporaryOrder = CrpcRegisterCenter.createTemporaryOrder("/cprc/test", "test".getBytes(StandardCharsets.UTF_8));
        boolean stat1 = CrpcRegisterCenter.checkExists(temporaryOrder);
        CrpcRegisterCenter.delete(temporaryOrder);
        boolean stat2 = CrpcRegisterCenter.checkExists(temporaryOrder);
        while (true) {

        }
    }

    @Test
    public void test2() throws Exception {
        CrpcRegisterCenter.connect();
        String temporaryOrder = CrpcRegisterCenter.createTemporaryOrder("/cprc/test", "test".getBytes(StandardCharsets.UTF_8));
        System.out.println(temporaryOrder);
//        while (true) {
//
//        }
    }
}
