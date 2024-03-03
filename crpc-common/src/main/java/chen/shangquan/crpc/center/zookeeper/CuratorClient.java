package chen.shangquan.crpc.center.zookeeper;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

public class CuratorClient {

    private static volatile CuratorFramework CLIENT = null;

    private static String NAME_SPACE;

    public static String getNameSpace() {
        return NAME_SPACE;
    }

    public static void connect() {
        connect("127.0.0.1:2181");
    }

    public static void connect(String... connectStrings) {
        connect("crpc",
                5000,
                5000,
                new ExponentialBackoffRetry(1000, 3),
                connectStrings
        );
    }

    public static void connect(String namespace,
                               int connectionTimeoutMs,
                               int sessionTimeoutMs,
                               RetryPolicy retryPolicy,
                               String... connectStrings
    ) {
        if (CLIENT == null) {
            synchronized (CuratorClient.class) {
                if (CLIENT == null) {
                    NAME_SPACE = namespace;
                    CLIENT = CuratorFrameworkFactory.builder()
                            .connectString(String.join(",", connectStrings))
                            .connectionTimeoutMs(connectionTimeoutMs)
                            .sessionTimeoutMs(sessionTimeoutMs)
                            .retryPolicy(retryPolicy)
                            .namespace(namespace)
                            .build();
                    CLIENT.start();
                }
            }
        }
    }

    public static CuratorFramework getClient() {
        return CLIENT;
    }

    public static void close() {
        CLIENT.close();
    }

    public static String create(String path, CreateMode createMode) throws Exception {
        return CLIENT.create().creatingParentsIfNeeded()
                .withMode(createMode)
                .forPath(path);
    }

    public static String create(String path, byte[] data, CreateMode createMode) throws Exception {
        return CLIENT.create().creatingParentsIfNeeded()
                .withMode(createMode)
                .forPath(path, data);
    }

    public static void update(String path, byte[] data) throws Exception {
        CLIENT.setData().forPath(path, data);
    }

    public static byte[] get(String path) throws Exception {
        return CLIENT.getData().forPath(path);
    }

    public static Stat checkExists(String path) throws Exception {
        return CLIENT.checkExists().forPath(path);
    }

    public static Void delete(String path) throws Exception {
        return CLIENT.delete().forPath(path);
    }
}
