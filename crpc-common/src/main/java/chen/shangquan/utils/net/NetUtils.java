package chen.shangquan.utils.net;

import chen.shangquan.agent.AgentServer;
import chen.shangquan.crpc.model.po.ServerInfo;
import chen.shangquan.crpc.network.data.RpcRequest;
import chen.shangquan.crpc.network.data.RpcResponse;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONUtil;

public class NetUtils {
    public static String sendHttpRequest(String uri, String requestJson) {
        return HttpRequest.post(uri)
                .body(requestJson)
                .execute().body();
    }

    public static String sendHttpRequestWithToken(String uri, String requestJson, String token) {
        return HttpRequest.post(uri)
                .body(requestJson)
                .header("token", token)
                .execute().body();
    }

    public static String sendHttpRequest(String ip, Integer port, RpcRequest request) {
        return NetUtils.sendHttpRequestWithToken(NetUtils.httpUri(ip, port), JSONUtil.toJsonStr(request), request.getToken());
    }

    public static <T> T sendHttpRequest(String uri, String requestJson, Class<T> clazz) {
        return JSONUtil.toBean(sendHttpRequest(uri, requestJson), clazz);
    }

    public static <T> T sendHttpRequest(String uri, Object object, Class<T> clazz) {
        return JSONUtil.toBean(sendHttpRequest(uri, JSONUtil.toJsonStr(object)), clazz);
    }

    public static String getServerUri(String serverName) {
        RpcResponse response = sendHttpRequest(AgentServer.getUri(),
                AgentServer.getAgentServerJson(new ServerInfo(serverName)),
                RpcResponse.class);

        ServerInfo bean = JSONUtil.toBean(String.valueOf(response.getData()), ServerInfo.class);
        return httpUri(bean.getIp(),bean.getPort());
    }

    public static String httpUri(String ip, Integer port) {
        return "http://"+ip+":"+port;
    }
}
