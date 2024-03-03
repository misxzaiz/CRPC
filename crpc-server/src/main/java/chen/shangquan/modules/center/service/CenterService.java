package chen.shangquan.modules.center.service;

import chen.shangquan.crpc.center.CrpcRegisterCenter;
import chen.shangquan.crpc.center.zookeeper.CuratorClient;
import chen.shangquan.crpc.constant.CrpcConstant;
import chen.shangquan.crpc.model.po.CrpcInfo;
import chen.shangquan.crpc.server.annotation.ServerRegister;
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

}
