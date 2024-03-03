package chen.shangquan.crpc.network.netty.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author chenshangquan
 * @date 11/29/2023
 */
public class NettyHandler extends SimpleChannelInboundHandler<String> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String json) throws Exception {

    }
}
