package chen.shangquan.modules.center.service;

import chen.shangquan.crpc.balance.ServerBalance;
import chen.shangquan.crpc.center.CrpcRegisterCenter;
import chen.shangquan.crpc.constant.CrpcConstant;
import chen.shangquan.crpc.model.po.ClassInfo;
import chen.shangquan.crpc.model.po.CrpcInfo;
import chen.shangquan.crpc.model.po.ServerInfo;
import chen.shangquan.crpc.server.annotation.ServerRegister;
import cn.hutool.json.JSONUtil;
import org.apache.zookeeper.KeeperException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Component("InterfaceService")
@ServerRegister(className = "InterfaceService", version = "V1")
public class InterfaceService {
    static String TOP_PATH_SEPARATOR = CrpcConstant.SEPARATOR + CrpcConstant.INTERFACE + CrpcConstant.SEPARATOR;

    public Object getTopServerList() throws Exception {
        return getTopList();
    }

    private static List<CrpcInfo> getTopList() throws Exception {
        List<CrpcInfo> serverDetails = new ArrayList<>();
        List<String> pathTopList = CrpcRegisterCenter.getChildren(CrpcConstant.SEPARATOR + CrpcConstant.INTERFACE);
        for (String path : pathTopList) {
            byte[] bytes = CrpcRegisterCenter.get(TOP_PATH_SEPARATOR + path);
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
            String serverName = serverDetail.getServerName();

            List<ServerInfo> serverList = new ArrayList<>();
            List<String> pathList = CrpcRegisterCenter.getChildren(CrpcConstant.SEPARATOR + CrpcConstant.INTERFACE);

            for (String topPath : pathList) {
                String path = TOP_PATH_SEPARATOR + topPath;
                List<String> serverNamePathList;
                try {
                    serverNamePathList = CrpcRegisterCenter.getChildren(path);
                } catch (KeeperException.NoNodeException e) {
                    return new ArrayList<>();
                }

                for (String serverNamePath : serverNamePathList) {
                    byte[] bytes = CrpcRegisterCenter.get(path + CrpcConstant.SEPARATOR + serverNamePath);
                    String json = new String(bytes, StandardCharsets.UTF_8);
                    ServerInfo bean = JSONUtil.toBean(json, ServerInfo.class);
                    if (bean.getClassList().size() != 0) {
                        boolean equals = false;
                        for (ClassInfo classItem : bean.getClassList()) {
                            if (serverName.equals(classItem.getServerName())) {
                                equals = true;
                                break;
                            }
                        }
                        if (equals) {
                            serverList.add(bean);
                        }
                    }
                }

            }

            serverDetail.setStatus(serverList.size() > 0);
            serverDetail.setServerList(serverList);
        }
        return serverDetails;
    }

    public Object getServerUsed(String serverName) throws Exception {
        List<String> pathList = CrpcRegisterCenter.getChildren(CrpcConstant.SEPARATOR + CrpcConstant.INTERFACE);
        List<ServerInfo> serverList = new ArrayList<>();
        for (String topPath : pathList) {
            String path = TOP_PATH_SEPARATOR + topPath;
            List<String> serverNamePathList;
            try {
                serverNamePathList = CrpcRegisterCenter.getChildren(path);
            } catch (KeeperException.NoNodeException e) {
                return new ArrayList<>();
            }

            for (String serverNamePath : serverNamePathList) {
                byte[] bytes = CrpcRegisterCenter.get(path + CrpcConstant.SEPARATOR + serverNamePath);
                String json = new String(bytes, StandardCharsets.UTF_8);
                ServerInfo bean = JSONUtil.toBean(json, ServerInfo.class);
                if (bean.getClassList().size() != 0) {
                    boolean equals = false;
                    for (ClassInfo classItem : bean.getClassList()) {
                        if (serverName.equals(classItem.getServerName())) {
                            equals = true;
                            break;
                        }
                    }
                    if (equals) {
                        serverList.add(bean);
                    }
                }
            }

        }
        return serverList;
    }

    public Object getServerList(String serverName) throws Exception {
        String topPath = TOP_PATH_SEPARATOR + serverName;
        return ServerBalance.getServersByTopPath(topPath);
    }

    public Object setServerDetail(CrpcInfo serverDetail) throws Exception {
        String topPath = TOP_PATH_SEPARATOR + serverDetail.getServerName();
        CrpcRegisterCenter.update(topPath, JSONUtil.toJsonStr(serverDetail).getBytes(StandardCharsets.UTF_8));
        return true;
    }
}
