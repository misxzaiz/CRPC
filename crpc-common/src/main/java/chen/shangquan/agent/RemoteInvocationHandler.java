package chen.shangquan.agent;

import chen.shangquan.crpc.network.data.RpcRequest;
import chen.shangquan.crpc.network.data.RpcResponse;
import chen.shangquan.crpc.network.thread.RpcRequestLocalThread;
import chen.shangquan.crpc.server.annotation.ServerRegister;
import chen.shangquan.utils.net.NetUtils;
import chen.shangquan.utils.type.ClassTypeUtils;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;


import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author chenshangquan
 * @date 12/28/2023
 */
@Slf4j
public class RemoteInvocationHandler implements InvocationHandler {

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        // 获取注解信息
        RpcResponse rpcResponse = sendRequestForResponse(method, args);
        Class<?> returnType = method.getReturnType();
        Object data = rpcResponse.getData();
        // 返回响应结果
        // TODO 异常处理②
        if (ClassTypeUtils.isBaseType(returnType)) {
            return data;
        }
        return BeanUtil.toBean(data, returnType);
    }

    private static RpcResponse sendRequestForResponse(Method method, Object[] args) {
        ServerRegister annotation = method.getDeclaringClass().getAnnotation(ServerRegister.class);
        String serverName = annotation.serverName();
        String className = annotation.className();
        String version = annotation.version();
        String area = annotation.area();
        String methodName = method.getName();
        RpcRequest request = new RpcRequest();
        request.setServerName(serverName);
        request.setClassName(className);
        request.setMethodName(methodName);
        request.setVersion(version);
        RpcRequest rpcRequest = RpcRequestLocalThread.getRpcRequest();
        log.info("RemoteInvocationHandler.sendRequestForResponse rpcRequest:{}", rpcRequest);
        if (rpcRequest != null) {
            request.setArea(rpcRequest.getArea());
            request.setId(rpcRequest.getId());
            request.setToken(rpcRequest.getToken());
        }

        log.info("RemoteInvocationHandler.sendRequestForResponse area:{}", area);
        // 注解存在的时候优先使用注解的地区
        if (!"default".equals(area)) {
            request.setArea(area);
        }

        if (args != null && args.length > 0) {
            request.setData(args[0]);
        }

        // 获取服务uri
        // TODO 异常处理①
        String serverUri = NetUtils.getServerUri(serverName);

        // 发起请求
        return NetUtils.sendHttpRequest(serverUri, request, RpcResponse.class);
    }
}
