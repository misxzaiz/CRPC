package chen.shangquan.crpc.network.netty.handler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * @author chenshangquan
 * @date 11/29/2023
 */
public class NettyInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline().addLast(new HttpServerCodec());  // HTTP编解码器
        ch.pipeline().addLast(new HttpObjectAggregator(65536));  // 聚合HTTP消息
        ch.pipeline().addLast(new ChunkedWriteHandler());  // 支持异步发送大的码流，防止内存溢出
        ch.pipeline().addLast(new NettyHandler());
    }
}
