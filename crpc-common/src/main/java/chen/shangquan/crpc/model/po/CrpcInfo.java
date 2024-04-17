package chen.shangquan.crpc.model.po;

import lombok.Data;

import java.util.List;

/**
 * 项目服务信息
 */
@Data
public class CrpcInfo {
    /**
     * 服务名
     */
    private String serverName;
    /**
     * 服务昵称
     */
    private String name;
    /**
     * 服务描述
     */
    private String desc;
    /**
     * 服务状态：是否有启动的服务
     */
    private Boolean status;
    /**
     * 服务列表：服务名相同，类名不同的服务列表
     */
    private List<ServerInfo> serverList;
}
