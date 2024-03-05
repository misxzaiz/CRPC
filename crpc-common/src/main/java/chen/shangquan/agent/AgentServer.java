package chen.shangquan.agent;

import chen.shangquan.crpc.network.data.RpcRequest;
import cn.hutool.json.JSONUtil;
import lombok.Data;

/**
 * @author chenshangquan
 * @date 1/8/2024
 */
@Data
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

    public static RpcRequest getCenterServer() {
        if (centerServer == null) {
            RpcRequest request = new RpcRequest();
            request.setServerName("CrpcServer");
            request.setClassName("CenterService");
            request.setMethodName("getServer");
            request.setVersion("V1");
            centerServer = request;
        }
        return centerServer;
    }

    public static RpcRequest getAgentServer(Object data) {
        RpcRequest request = getCenterServer();
        request.setData(data);
        return request;
    }

    public static String getAgentServerJson(Object data) {
        return JSONUtil.toJsonStr(getAgentServer(data));
    }
}
