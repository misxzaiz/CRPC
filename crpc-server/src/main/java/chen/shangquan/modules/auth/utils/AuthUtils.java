package chen.shangquan.modules.auth.utils;

import chen.shangquan.crpc.center.CrpcRegisterCenter;
import chen.shangquan.crpc.constant.CrpcConstant;
import chen.shangquan.crpc.network.data.RpcRequest;
import chen.shangquan.utils.time.TimestampUtils;
import cn.hutool.core.util.StrUtil;

import java.nio.charset.StandardCharsets;

public class AuthUtils {
    public static boolean checkAuth(RpcRequest request) throws Exception {
        String uri = request.getServerName() + "/" + request.getClassName() + "/" + request.getMethodName();
        if (uri.equals("CrpcServer/AuthService/login")) {
            return true;
        }
        if (StrUtil.isBlank(request.getToken())) {
            return false;
        }
        if (TimestampUtils.isExpired(Long.valueOf(request.getToken()))) {
            return false;
        }
        if (!CrpcRegisterCenter.checkExists(CrpcConstant.AUTH + CrpcConstant.ADMIN_TOKEN)) {
            return false;
        }
        byte[] bytes = CrpcRegisterCenter.get(CrpcConstant.AUTH + CrpcConstant.ADMIN_TOKEN);
        String token = new String(bytes, StandardCharsets.UTF_8);
        if (token.equals(request.getToken())) {
            return true;
        } else {
            return false;
        }
    }
}
