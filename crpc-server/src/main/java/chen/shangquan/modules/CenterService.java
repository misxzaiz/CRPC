package chen.shangquan.modules;

import chen.shangquan.crpc.server.annotation.ServerRegister;
import org.springframework.stereotype.Component;

@ServerRegister(serverName = "CrpcServer", className = "CenterService", version = "V1")
public interface CenterService {
}
