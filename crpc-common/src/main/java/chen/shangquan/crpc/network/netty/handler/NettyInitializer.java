package chen.shangquan.crpc.network.netty.handler;

import chen.shangquan.crpc.network.data.RpcRequest;
import chen.shangquan.crpc.network.data.RpcResponse;
import chen.shangquan.crpc.network.thread.RpcRequestLocalThread;
import cn.hutool.json.JSONUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;

import java.nio.charset.StandardCharsets;

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
