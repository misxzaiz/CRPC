package chen.shangquan.crpc.network.netty.handler;

import chen.shangquan.crpc.exp.ServerInvokeException;
import chen.shangquan.crpc.network.data.RequestLog;
import chen.shangquan.crpc.network.data.RpcRequest;
import chen.shangquan.crpc.network.data.RpcResponse;
import chen.shangquan.crpc.network.thread.RpcRequestLocalThread;
import chen.shangquan.crpc.server.invoke.ServerInvoke;
import chen.shangquan.utils.exp.ExceptionUtils;
import chen.shangquan.utils.net.NetUtils;
import chen.shangquan.utils.thread.ThreadPoolUtils;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.common.NettyUtils;
import org.springframework.context.annotation.Bean;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static chen.shangquan.crpc.network.constant.ResponseCodeConstant.SERVER_ERROR;
import static chen.shangquan.utils.thread.ThreadPoolUtils.virtualThreadPool;

/**
 * @author chenshangquan
 * @date 11/29/2023
 */
@Slf4j
public class NettyHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest fullHttpRequest) throws Exception {
        // 获取HTTP请求的内容
        ByteBuf data = fullHttpRequest.content();
        String requestJson = data.toString(StandardCharsets.UTF_8);
        RpcRequest request = JSONUtil.toBean(requestJson, RpcRequest.class);
        log.info("NettyHandler.channelRead0:{}",request);
        // TODO 记录请求日志
        RpcRequestLocalThread.saveRpcRequest(request);
        RequestLog requestLog = BeanUtil.toBean(request, RequestLog.class);
        RpcResponse invoke = null;
        LocalDateTime start = LocalDateTime.now();
        requestLog.setStartTime(start.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")));
        try {
             invoke = ServerInvoke.invoke(request);
        } catch (Exception e) {
            if (e instanceof ServerInvokeException) {
                requestLog.setE(((ServerInvokeException) e).getErrorMessages());
            } else {
                requestLog.setE(ExceptionUtils.getExceptionMessage(e));
            }
            e.printStackTrace();
            invoke = RpcResponse.builder()
                    .id(request.getId())
                    .code(SERVER_ERROR)
                    .message(e.getMessage())
                    .build();

        }
        RpcRequestLocalThread.removeRpcRequest();
        LocalDateTime end = LocalDateTime.now();
        requestLog.setEndTime(end.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")));
        Duration duration = Duration.between(start, end);
        long spendTimeMillis = duration.toMillis();
        requestLog.setSpendTime(String.valueOf(spendTimeMillis));
        requestLog.setRpcResponse(invoke);
        log.info("NettyHandler.channelRead0 requestLog:{}", requestLog);
        // TODO 将日志保存到中心服务，但是要注意，不要保存日志的日志
        // TODO 这里有个问题，如果中心服务采用集群模式，就需要将数据汇总
        // TODO 应该补充通过配置文件选择记录那些日志
        if (!("CrpcServer".equals(request.getServerName()) && "CenterService".equals(request.getClassName()) && ("saveRequestLog".equals(request.getMethodName()) || "getRequestLogs".equals(request.getMethodName()) || "getServerWithRpcRequest".equals(request.getMethodName())))) {
            ThreadPoolUtils.virtualThreadPool.submit(() -> {
                NetUtils.saveRequestLog(requestLog);
            });
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
