package chen.shangquan.crpc.center;

import chen.shangquan.crpc.center.zookeeper.CuratorClient;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

public class CrpcRegisterCenter {
    public static String getNameSpace() {
        return CuratorClient.getNameSpace();
    }
    public static void connect() {
        connect("127.0.0.1:2181");
    }

    public static void connect(String... connectStrings) {
        CuratorClient.connect("crpc",
                5000,
                5000,
                new ExponentialBackoffRetry(1000, 3),
                connectStrings
        );
    }

    public static CuratorFramework getClient() {
        return CuratorClient.getClient();
    }

    public static void close() {
        CuratorClient.close();
    }

    public static String createTemporaryOrder(String path) throws Exception {
        return CuratorClient.create(path, CreateMode.EPHEMERAL_SEQUENTIAL);
    }

    public static String createTemporaryOrder(String path, byte[] data) throws Exception {
        return CuratorClient.create(path, data, CreateMode.EPHEMERAL_SEQUENTIAL);
    }

    public static void update(String path, byte[] data) throws Exception {
        CuratorClient.update(path, data);
    }

    public static byte[] get(String path) throws Exception {
        return CuratorClient.get(path);
    }

    public static boolean checkExists(String path) throws Exception {
        Stat stat = CuratorClient.checkExists(path);
        return stat != null;
    }

    public static void delete(String path) throws Exception {
        CuratorClient.delete(path);
    }
}
