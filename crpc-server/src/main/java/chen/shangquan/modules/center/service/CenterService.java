package chen.shangquan.modules.center.service;

import chen.shangquan.common.model.po.Result;
import chen.shangquan.crpc.balance.ServerBalance;
import chen.shangquan.crpc.center.CrpcRegisterCenter;
import chen.shangquan.crpc.center.zookeeper.CuratorClient;
import chen.shangquan.crpc.constant.CrpcConstant;
import chen.shangquan.crpc.model.po.CrpcInfo;
import chen.shangquan.crpc.model.po.ServerInfo;
import chen.shangquan.crpc.network.data.RpcRequest;
import chen.shangquan.crpc.server.annotation.ServerRegister;
import chen.shangquan.utils.balance.BalanceMap;
import chen.shangquan.utils.balance.LoadBalancing;
import chen.shangquan.utils.balance.impl.WeightLoadBalancing;
import chen.shangquan.utils.generator.UniqueIdGenerator;
import chen.shangquan.utils.net.NetUtils;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component("CenterService")
@ServerRegister(className = "CenterService", version = "V1")
public class CenterService {
    public Object getTopServerList() throws Exception {
        List<CrpcInfo> serverDetails = new ArrayList<>();

        List<String> pathList = CrpcRegisterCenter.getChildren(CrpcConstant.TOP_PATH);
        for (String path : pathList) {
            byte[] bytes = CrpcRegisterCenter.get(CrpcConstant.TOP_PATH_SEPARATOR + path);
            if (bytes.length != 0) {
                String json = new String(bytes, StandardCharsets.UTF_8);
                CrpcInfo bean = JSONUtil.toBean(json, CrpcInfo.class);
                serverDetails.add(bean);
            } else {
                CrpcInfo bean = new CrpcInfo();
                bean.setServerName(path);
                serverDetails.add(bean);
            }
        }
        for (CrpcInfo serverDetail : serverDetails) {
            List<String> list = CuratorClient.getClient().getChildren().forPath(CrpcConstant.TOP_PATH_SEPARATOR + serverDetail.getServerName());
            serverDetail.setStatus(list.size() > 0);
        }
        return serverDetails;
    }

    public Object getServerBalance(String serverName) throws Exception {
        String topPath = CrpcConstant.TOP_PATH_SEPARATOR + serverName;
        LoadBalancing loadBalancing = BalanceMap.get(serverName);
        if (loadBalancing == null) {
            List<ServerInfo> serverList = ServerBalance.getServersByTopPath(topPath);
            System.out.println("serverList:" + serverList);
            if (serverList.size() != 0) {
                // 2. 选择负载均衡策略

                loadBalancing = new WeightLoadBalancing(serverList);
                BalanceMap.put(serverName, loadBalancing);
            } else {
                return new ArrayList<>();
            }
        }
        ServerInfo ipPort = loadBalancing.loadBalancing();
        return Collections.singletonList(ipPort);
    }

    public Object getServerList(String serverName) throws Exception {
        String topPath = CrpcConstant.TOP_PATH_SEPARATOR + serverName;
        return ServerBalance.getServersByTopPath(topPath);
    }

    public Object setServerDetail(CrpcInfo serverDetail) throws Exception {
        String topPath = CrpcConstant.TOP_PATH_SEPARATOR + serverDetail.getServerName();
        CuratorClient.update(topPath, JSONUtil.toJsonStr(serverDetail).getBytes(StandardCharsets.UTF_8));
        return true;
    }

    public Object dealMethod(RpcRequest request) throws Exception {
        String topPath = CrpcConstant.TOP_PATH_SEPARATOR + request.getServerName();
        request.setId(UniqueIdGenerator.generateUniqueId());
        request.setArea("GD");
//        request.setToken("c72d6319-49ab-47d7-bb4a-2ff112db4fbb");
        // TODO 使用缓存
        List<ServerInfo> serverList = ServerBalance.getServersByTopPath(topPath);
        if (serverList.size() == 0) {
            return "";
        }
        // TODO 负载均衡
        ServerInfo server = serverList.get(0);
        String ip = server.getIp();
        Integer port = server.getPort();
        // TODO NETTY发起HTTP请求，应该可以通过配置发起各种协议请求
        return NetUtils.sendHttpRequest(ip, port, request);
    }

    public Object deal(RpcRequest request) throws Exception {
        String topPath = CrpcConstant.TOP_PATH_SEPARATOR + request.getServerName();
        request.setId(UniqueIdGenerator.generateUniqueId());
        request.setArea("GD");
        request.setToken(request.getToken());
        // TODO 使用缓存
        List<ServerInfo> serverList = ServerBalance.getServersByTopPath(topPath);
        if (serverList.size() == 0) {
            return "";
        }
        // TODO 负载均衡
        ServerInfo server = serverList.get(0);
        String ip = server.getIp();
        Integer port = server.getPort();
        // TODO NETTY发起HTTP请求，应该可以通过配置发起各种协议请求
        String response = NetUtils.sendHttpRequest(ip, port, request);
        JSONObject jsonObject = JSONUtil.parseObj(response);
        return jsonObject.getObj("data");
    }
}
