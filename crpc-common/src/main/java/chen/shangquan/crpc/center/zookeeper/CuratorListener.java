package chen.shangquan.crpc.center.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.*;

public class CuratorListener {
    public static void create(String path) {
        CuratorFramework client = CuratorClient.getClient();

        // CuratorCache
        CuratorCache cache = CuratorCache.build(client, path);
        CuratorCacheListener cacheListener = CuratorCacheListener.builder()
                .forCreates(CuratorListener::create)
                .forChanges(CuratorListener::change)
                .forDeletes(CuratorListener::delete)
                .forTreeCache(client, CuratorListener::treeCache)
                .forInitialized(CuratorListener::initialized)
                .forAll(CuratorListener::allEvents)
                .forCreatesAndChanges(CuratorListener::createsAndChanges)
                .forPathChildrenCache("",client, CuratorListener::pathChildrenCache)
                .forNodeCache(CuratorListener::nodeCache)
                .afterInitialized()
                .build();

        // 注册监听器
        cache.listenable().addListener(cacheListener);

        // 启动监听器
        cache.start();
    }

    private static void createsAndChanges(ChildData childData, ChildData childData1) {
    }

    private static void allEvents(CuratorCacheListener.Type type, ChildData childData, ChildData childData1) {
    }

    private static void create(ChildData node) {
        System.out.println("节点创建：" + node.getPath());
    }

    private static void change(ChildData oldNode, ChildData node) {
        System.out.println("节点修改：" + node.getPath());
    }

    private static void delete(ChildData oldNode) {
        System.out.println("节点删除：" + oldNode.getPath());
    }

    private static void initialized() {
        // 处理初始化事件
    }

    private static void pathChildrenCache(CuratorFramework client, PathChildrenCacheEvent event) {
        // 处理NodeCache事件
    }

    private static void treeCache(CuratorFramework client, TreeCacheEvent event) {
        // 处理NodeCache事件
    }

    private static void nodeCache() {
        // 处理NodeCache事件
    }
}
