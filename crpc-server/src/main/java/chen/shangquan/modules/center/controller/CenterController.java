package chen.shangquan.modules.center.controller;

import chen.shangquan.common.model.po.Result;
import chen.shangquan.crpc.balance.ServerBalance;
import chen.shangquan.crpc.constant.CrpcConstant;
import chen.shangquan.crpc.model.po.ServerInfo;
import chen.shangquan.crpc.network.data.RpcRequest;
import chen.shangquan.crpc.network.data.RpcResponse;
import chen.shangquan.utils.generator.UniqueIdGenerator;
import chen.shangquan.utils.net.NetUtils;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/server")
public class CenterController {
    @PostMapping("/dealMethod")
    public Result dealMethod(@RequestBody RpcRequest request) throws Exception {
        String topPath = CrpcConstant.TOP_PATH_SEPARATOR + request.getServerName();
        request.setId(UniqueIdGenerator.generateUniqueId());
        request.setArea("GD");
//        request.setToken("c72d6319-49ab-47d7-bb4a-2ff112db4fbb");
        // TODO 使用缓存
        List<ServerInfo> serverList = ServerBalance.getServersByTopPath(topPath);
        if (serverList.size() == 0) {
            return Result.ok();
        }
        // TODO 负载均衡
        ServerInfo server = serverList.get(0);
        String ip = server.getIp();
        Integer port = server.getPort();
        // TODO NETTY发起HTTP请求，应该可以通过配置发起各种协议请求
        String res = NetUtils.sendHttpRequest(ip, port, request);
        RpcResponse bean = JSONUtil.toBean(res, RpcResponse.class);
        if (bean.getCode() != 200) {
            return Result.fail(bean.getMessage());
        }
        return Result.ok(bean.getData(), bean.getMessage());
    }

}
