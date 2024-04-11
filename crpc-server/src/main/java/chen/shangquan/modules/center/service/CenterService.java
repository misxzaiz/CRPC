package chen.shangquan.modules.center.service;

import chen.shangquan.crpc.balance.ServerBalance;
import chen.shangquan.crpc.center.CrpcRegisterCenter;
import chen.shangquan.crpc.center.zookeeper.CuratorClient;
import chen.shangquan.crpc.constant.CrpcConstant;
import chen.shangquan.crpc.model.po.CrpcInfo;
import chen.shangquan.crpc.model.po.ServerInfo;
import chen.shangquan.crpc.network.data.RequestLog;
import chen.shangquan.crpc.network.data.RpcRequest;
import chen.shangquan.crpc.server.annotation.ServerRegister;
import chen.shangquan.utils.balance.BalanceMap;
import chen.shangquan.utils.balance.LoadBalancing;
import chen.shangquan.utils.balance.impl.WeightAreaLoadBalancing;
import chen.shangquan.utils.balance.impl.WeightLoadBalancing;
import chen.shangquan.utils.generator.UniqueIdGenerator;
import chen.shangquan.utils.net.NetUtils;
import chen.shangquan.utils.thread.ThreadPoolUtils;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author chenshangquan
 * @date 3/11/2024
 */
@Slf4j
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

    public ServerInfo getServerBalanceForServerInfo(String serverName) throws Exception {
        String topPath = CrpcConstant.TOP_PATH_SEPARATOR + serverName;
        LoadBalancing loadBalancing = BalanceMap.get(serverName);
        if (loadBalancing == null) {
            List<ServerInfo> serverList = ServerBalance.getServersByTopPath(topPath);
            if (serverList.size() != 0) {
                // 2. 选择负载均衡策略

                loadBalancing = new WeightAreaLoadBalancing(serverList);
                BalanceMap.put(serverName, loadBalancing);
            } else {
                return new ServerInfo();
            }
        }
        ServerInfo ipPort = loadBalancing.loadBalancing();
        return ipPort;
    }

    public Object getServerBalance(String serverName) throws Exception {
        String topPath = CrpcConstant.TOP_PATH_SEPARATOR + serverName;
        LoadBalancing loadBalancing = BalanceMap.get(serverName);
        if (loadBalancing == null) {
            List<ServerInfo> serverList = ServerBalance.getServersByTopPath(topPath);
            if (serverList.size() != 0) {
                // 2. 选择负载均衡策略

                loadBalancing = new WeightAreaLoadBalancing(serverList);
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

    public String getServerWithRpcRequest(RpcRequest request) throws Exception {
        log.info("CenterService.getServerWithRpcRequest request:{}", request);
        return ServerBalance.getServerWithRpcRequest(request);
    }

    public ServerInfo getServer(ServerInfo server) throws Exception {
        log.info("CenterService.getServer server:{}", server);
        return ServerBalance.getServer(server);
    }

//    public String getServerUri(ServerInfo server) throws Exception {
//        return ServerBalance.getServerUri(server);
//    }

    public String getServerUri(String serverName) throws Exception {
        return ServerBalance.getServerUri(serverName);
    }

    // TODO 待优化，如聚合，过期策略等
    // TODO 当日志达到一定长度，复制保存到一个日志文件中
    // TODO 当达到一定时间，也记录到日志文件中
    // TODO 高并发线程安全问题
    private static final List<RequestLog> requestLogs = new ArrayList<>();

    public void saveRequestLog(RequestLog requestLog) {
        requestLogs.add(requestLog);
        ThreadPoolUtils.virtualThreadPool.execute(this::saveLogsIfMoreThanTen);
    }

    public void saveLogsIfMoreThanTen() {
        if (requestLogs.size() > 10) {
            // 创建 log 文件夹（如果不存在）
            File logDir = new File("D:/log");
            if (!logDir.exists()) {
                logDir.mkdirs();
            }

            // 获取当前时间，并格式化为文件名
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            String fileName = dateFormat.format(new Date()) + ".txt";

            // 构建文件路径
            Path filePath = Paths.get("D:/log", fileName);

            try (BufferedWriter writer = Files.newBufferedWriter(filePath)) {
                for (RequestLog log : requestLogs) {
                    writer.write(JSONUtil.toJsonStr(log));
                    writer.newLine(); // 写入新行
                    // TODO 其实也不一定要换行，可以通过编码减少存储空间，使用如“#REQUESTLOGS#”作为日志分隔符
                    writer.newLine(); // 写入新行
                }
                // 保存成功后清空 requestLogs
                requestLogs.clear();
                System.out.println("Logs saved to " + filePath);
            } catch (IOException e) {
                e.printStackTrace(); // 实际应用中应该使用更合适的错误处理
                // 保存失败，不清空 requestLogs
            }
        }
    }

//    public List<RequestLog> getRequestLogs() {
//        return requestLogs;
//    }

    public List<List<RequestLog>> getRequestLogs() {
        // 假设 createTime 字段的格式是 "yyyy-MM-dd'T'HH:mm:ss.SSS"
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

        Map<String, List<RequestLog>> groupedLogs = requestLogs.stream()
                .collect(Collectors.groupingBy(
                        RequestLog::getId,
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                list -> {
                                    // 使用自定义比较器对 createTime 进行排序
                                    list.sort(Comparator.comparing(requestLog -> {
                                        try {
                                            return dateFormat.parse(requestLog.getStartTime());
                                        } catch (ParseException e) {
                                            throw new IllegalStateException("Invalid createTime format", e);
                                        }
                                    }));
                                    return list;
                                }
                        )
                ));

        return new ArrayList<>(groupedLogs.values());
    }
}
