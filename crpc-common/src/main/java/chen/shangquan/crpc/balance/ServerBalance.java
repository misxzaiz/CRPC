package chen.shangquan.crpc.balance;

import chen.shangquan.crpc.center.CrpcRegisterCenter;
import chen.shangquan.crpc.constant.CrpcConstant;
import chen.shangquan.crpc.model.po.ServerInfo;
import chen.shangquan.crpc.network.data.RpcRequest;
import chen.shangquan.utils.balance.BalanceMap;
import chen.shangquan.utils.balance.LoadBalancing;
import chen.shangquan.utils.balance.impl.WeightAreaLoadBalancing;
import chen.shangquan.utils.balance.impl.WeightLoadBalancing;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.KeeperException;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ServerBalance {

    public static String getServerWithRpcRequest(RpcRequest request) throws Exception {
        log.info("ServerBalance.getServerWithRpcRequest request:{}", request);
        // TODO area 在 request 中
        String serverName = request.getServerName();
        String topPath = CrpcConstant.TOP_PATH_SEPARATOR + serverName;
        LoadBalancing loadBalancing = BalanceMap.get(serverName);
        if (loadBalancing == null) {
            List<ServerInfo> serverList = getServersByTopPath(topPath);
            if (serverList.size() != 0) {
                // 2. 选择负载均衡策略
                loadBalancing = new WeightAreaLoadBalancing(serverList);
                BalanceMap.put(serverName, loadBalancing);
            } else {
                return "";
            }
        }
        ServerInfo bean = BeanUtil.toBean(loadBalancing.loadBalancing(), ServerInfo.class);
        return bean.getIp() + ":" + bean.getPort();
    }

    public static ServerInfo getServer(ServerInfo server) throws Exception {
        String serverName = server.getName();
        String topPath = CrpcConstant.TOP_PATH_SEPARATOR + serverName;
        LoadBalancing loadBalancing = BalanceMap.get(serverName);
        if (loadBalancing == null) {
            List<ServerInfo> serverList = getServersByTopPath(topPath);
            if (serverList.size() != 0) {
                // 2. 选择负载均衡策略
                loadBalancing = new WeightAreaLoadBalancing(serverList);
                BalanceMap.put(serverName, loadBalancing);
            } else {
                return null;
            }
        }
        return BeanUtil.toBean(loadBalancing.loadBalancing(), ServerInfo.class);
    }

    public static String getServerUri(ServerInfo server) throws Exception {
        ServerInfo serverVo = getServer(server);
        if (serverVo == null) {
            return null;
        }
        return serverVo.getIp() + ":" + serverVo.getPort();
    }

    public static String getServerUri(String serverName) throws Exception {
        return getServerUri(new ServerInfo(serverName));
    }

    public static List<ServerInfo> getServersByTopPath(String topPath) throws Exception {
        List<String> serverNamePathList;
        try {
            serverNamePathList = CrpcRegisterCenter.getChildren(topPath);
        } catch (KeeperException.NoNodeException e) {
            return new ArrayList<>();
        }
        List<ServerInfo> serverList = new ArrayList<>();
        for (String serverNamePath : serverNamePathList) {
            byte[] bytes = CrpcRegisterCenter.get(topPath + CrpcConstant.SEPARATOR + serverNamePath);
            String json = new String(bytes, StandardCharsets.UTF_8);
            ServerInfo bean = JSONUtil.toBean(json, ServerInfo.class);
            serverList.add(bean);
        }
        return serverList;
    }
}
