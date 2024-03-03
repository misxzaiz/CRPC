package chen.shangquan.crpc.network;

import chen.shangquan.crpc.network.netty.NettyServer;

public class NetworkServer {
    public static void start(int port) throws InterruptedException {
        NettyServer.start(port);
    }
}
