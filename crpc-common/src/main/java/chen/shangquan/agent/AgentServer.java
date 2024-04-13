package chen.shangquan.agent;

import chen.shangquan.crpc.model.po.ServerInfo;
import chen.shangquan.crpc.network.data.RpcRequest;
import chen.shangquan.crpc.network.thread.RpcRequestLocalThread;
import chen.shangquan.crpc.runner.ServerApplicationRunner;
import cn.hutool.core.util.StrUtil;
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

    public static String getUri() {
        ServerInfo applicationServerInfo = ServerApplicationRunner.APPLICATION_SERVER_INFO;
        if (applicationServerInfo != null && StrUtil.isNotBlank(applicationServerInfo.getCenter())) {
            return applicationServerInfo.getCenter();
        }
        return "http://127.0.0.1:8000";
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
