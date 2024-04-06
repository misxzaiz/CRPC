package chen.shangquan.modules.auth.service;

import chen.shangquan.common.model.po.Result;
import chen.shangquan.crpc.center.CrpcRegisterCenter;
import chen.shangquan.crpc.constant.CrpcConstant;
import chen.shangquan.crpc.network.data.RpcRequest;
import chen.shangquan.crpc.server.annotation.ServerRegister;
import chen.shangquan.modules.auth.model.po.Auth;
import chen.shangquan.utils.time.TimestampUtils;
import cn.hutool.core.util.StrUtil;
import org.apache.zookeeper.KeeperException;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Map;

@Component("AuthService")
@ServerRegister(className = "AuthService", version = "V1")
public class AuthService {

    public Result login(Auth userInfo) throws Exception {
        String account = userInfo.getAccount();
        String password = userInfo.getPassword();
        if (StrUtil.isBlank(account) || StrUtil.isBlank(password)) {
            return Result.fail("账号或密码不能为空");
        } else {
            if (!"admin".equals(account)) {
                return Result.fail("账号错误");
            }
            String token = null;
            try {
                token = String.valueOf(TimestampUtils.generateTimestamp(0,1,0,0,0));
                CrpcRegisterCenter.update(CrpcConstant.AUTH + CrpcConstant.ADMIN_TOKEN, token.getBytes(StandardCharsets.UTF_8));
            } catch (KeeperException.NoNodeException e) {
                CrpcRegisterCenter.create(CrpcConstant.AUTH + CrpcConstant.ADMIN_TOKEN, token.getBytes(StandardCharsets.UTF_8));
            }
            try {
                byte[] bytes = CrpcRegisterCenter.get(CrpcConstant.AUTH + CrpcConstant.ADMIN);
                if (bytes.length != 0) {
                    String psw = new String(bytes, StandardCharsets.UTF_8);
                    if (password.equals(psw)) {
                        return Result.ok(token, "登录成功");
                    } else {
                        return Result.fail("密码错误");
                    }
                } else {
                    CrpcRegisterCenter.update(CrpcConstant.AUTH + CrpcConstant.ADMIN, password.getBytes(StandardCharsets.UTF_8));
                    return Result.ok(token, "登录成功");
                }
            } catch (KeeperException.NoNodeException e) {
                CrpcRegisterCenter.create(CrpcConstant.AUTH + CrpcConstant.ADMIN, password.getBytes(StandardCharsets.UTF_8));
                return Result.ok(token, "登录成功");
            }
        }
    }
}
