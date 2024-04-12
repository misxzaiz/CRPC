package chen.shangquan.modules.center.controller;

import chen.shangquan.crpc.network.thread.RpcRequestLocalThread;
import chen.shangquan.crpc.runner.ServerApplicationRunner;
import chen.shangquan.result.Result;
import chen.shangquan.crpc.balance.ServerBalance;
import chen.shangquan.crpc.constant.CrpcConstant;
import chen.shangquan.crpc.model.po.ServerInfo;
import chen.shangquan.crpc.network.data.RpcRequest;
import chen.shangquan.crpc.network.data.RpcResponse;
import chen.shangquan.modules.auth.utils.AuthUtils;
import chen.shangquan.utils.generator.UniqueIdGenerator;
import chen.shangquan.utils.net.NetUtils;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/server")
public class CenterController {
    @PostMapping("/dealMethod")
    public Result dealMethod(@RequestBody RpcRequest request) throws Exception {
        log.info("CenterController.dealMethod request:{}", request);
        if (!AuthUtils.checkAuth(request)) {
            return Result.authFail("认证失败！");
        }

        ServerInfo applicationServerInfo = ServerApplicationRunner.APPLICATION_SERVER_INFO;
        if (StrUtil.isBlank(request.getArea()) && applicationServerInfo != null && StrUtil.isNotBlank(applicationServerInfo.getArea())) {
            request.setArea(applicationServerInfo.getArea());
        }

        RpcRequestLocalThread.saveRpcRequest(request);
        request.setId(UniqueIdGenerator.generateUniqueId());
        String serverUri = ServerBalance.getServerUri(request.getServerName());
        log.info("CenterController.dealMethod serverUri:{}", serverUri);
        if (serverUri == null) {
            return Result.ok();
        }
        String[] ipPort = serverUri.split(":");
        String ip = ipPort[0];
        Integer port = Integer.parseInt(ipPort[1]);

        // TODO NETTY发起HTTP请求，应该可以通过配置发起各种协议请求
        String res = NetUtils.sendHttpRequest(ip, port, request);
        RpcResponse bean = JSONUtil.toBean(res, RpcResponse.class);
        if (bean.getCode() != 200) {
            return Result.fail(bean.getMessage());
        }
        return Result.ok(bean.getData(), bean.getMessage());
    }

}
