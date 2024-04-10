package chen.shangquan.agent;

import chen.shangquan.crpc.network.data.RpcRequest;
import chen.shangquan.crpc.network.thread.RpcRequestLocalThread;
import cn.hutool.json.JSONUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

/**
 * @author chenshangquan
 * @date 1/8/2024
 */
@Data
@Slf4j
public class AgentServer {
    private static RpcRequest centerServer = null;
    private static String protocol = "http";
    private static String ip = "127.0.0.1";
    private static int port = 7999;

    public static void setIpAndPort(String ip, int port) {
        AgentServer.ip = ip;
        AgentServer.port = port;
    }

    public static void setProtocolAndIpAndPort(String protocol, String ip, int port) {
        AgentServer.protocol = protocol;
        AgentServer.ip = ip;
        AgentServer.port = port;
    }

    public static String getUri() {
        return protocol + "://" + ip + ":" + port;
    }

    public static RpcRequest getCenterServer(String methodName) {
        if (centerServer == null) {
            RpcRequest request = new RpcRequest();
            request.setServerName("CrpcServer");
            request.setClassName("CenterService");
            request.setVersion("V1");
            RpcRequest rpcRequest = RpcRequestLocalThread.getRpcRequest();
            log.info("AgentServer.getCenterServer rpcRequest:{}", rpcRequest);
            if (rpcRequest != null) {
                request.setArea(rpcRequest.getArea());
                request.setId(rpcRequest.getId());
                request.setToken(rpcRequest.getToken());
            }
            centerServer = request;
        }
        centerServer.setMethodName(Objects.requireNonNullElse(methodName, "getServerWithRpcRequest"));
        return centerServer;
    }

    public static RpcRequest getAgentServer(String methodName, Object data) {
        RpcRequest request = getCenterServer(methodName);
        request.setData(data);
        return request;
    }

    /**
     *
     * @param methodName 为空时，调用中心服务的 getServerWithRpcRequest 方法，获取服务 ip:port
     */
    public static String getAgentServerJson(String methodName, Object data) {
        return JSONUtil.toJsonStr(getAgentServer(methodName, data));
    }
}
