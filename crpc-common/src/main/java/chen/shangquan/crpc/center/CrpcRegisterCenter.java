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
        CuratorClient.getClient().close();
    }

    public static String create(String path, CreateMode createMode) throws Exception {
        return CuratorClient.getClient().create().creatingParentsIfNeeded()
                .withMode(createMode)
                .forPath(path);
    }

    public static String create(String path, byte[] data, CreateMode createMode) throws Exception {
        return CuratorClient.getClient().create().creatingParentsIfNeeded()
                .withMode(createMode)
                .forPath(path, data);
    }

    public static void update(String path, byte[] data) throws Exception {
        CuratorClient.getClient().setData().forPath(path, data);
    }

    public static byte[] get(String path) throws Exception {
        return CuratorClient.getClient().getData().forPath(path);
    }

    public static Stat checkExists(String path) throws Exception {
        return CuratorClient.getClient().checkExists().forPath(path);
    }

    public static Void delete(String path) throws Exception {
        return CuratorClient.getClient().delete().forPath(path);
    }
}
