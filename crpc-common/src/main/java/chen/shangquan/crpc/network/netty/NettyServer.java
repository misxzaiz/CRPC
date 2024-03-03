package chen.shangquan.crpc.network.netty;

import chen.shangquan.crpc.network.data.RpcRequest;
import chen.shangquan.crpc.network.data.RpcResponse;
import chen.shangquan.crpc.network.netty.handler.NettyInitializer;
import chen.shangquan.crpc.network.thread.RpcRequestLocalThread;
import cn.hutool.json.JSONUtil;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.stream.ChunkedWriteHandler;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class NettyServer {

//    public static void startWithInterface(List<Class<?>> serverRegisterClasses) throws Exception {
//        Server server = YmlUtils.getServer();
//        ServerRegister.registerServerClass(server, serverRegisterClasses);
//        start(server.getPort());
//    }

    public static void start(int port) throws InterruptedException {
        // 创建主线程组和工作线程组
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            // 创建服务端启动器
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new NettyInitializer());
            // 绑定端口并启动服务
            ChannelFuture future = serverBootstrap.bind(port).sync();
            // 等待服务端监听端口关闭
            future.channel().closeFuture().sync();
        } finally {
            // 优雅地关闭线程组
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
