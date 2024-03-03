package chen.shangquan.crpc.network.netty.handler;

import chen.shangquan.crpc.network.data.RpcRequest;
import chen.shangquan.crpc.network.data.RpcResponse;
import chen.shangquan.crpc.network.thread.RpcRequestLocalThread;
import chen.shangquan.crpc.server.invoke.ServerInvoke;
import cn.hutool.json.JSONUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;

import java.nio.charset.StandardCharsets;

/**
 * @author chenshangquan
 * @date 11/29/2023
 */
public class NettyHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest fullHttpRequest) throws Exception {
        // 获取HTTP请求的内容
        ByteBuf data = fullHttpRequest.content();
        String requestJson = data.toString(StandardCharsets.UTF_8);
        RpcRequest request = JSONUtil.toBean(requestJson, RpcRequest.class);
        RpcRequestLocalThread.saveRpcRequest(request);
        RpcResponse invoke = null;
        try {
             invoke = ServerInvoke.invoke(request);
        } catch (Exception e) {
            invoke = RpcResponse.builder().id(request.getId()).code(400).message("FAIL").build();
        } finally {
            RpcRequestLocalThread.removeRpcRequest();
        }

        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK,
                Unpooled.copiedBuffer(JSONUtil.toJsonStr(invoke), StandardCharsets.UTF_8));
        // 设置响应头信息
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/json; charset=UTF-8");
        response.headers().set(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
        // 发送响应
        ctx.writeAndFlush(response);
    }
}
