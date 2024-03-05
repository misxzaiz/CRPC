package chen.shangquan.crpc.model.po;

import lombok.Data;

import java.util.List;

@Data
public class CrpcInfo {
    private String serverName;
    private String name;
    private String desc;
    private Boolean status;
    private List<ServerInfo> serverList;
}
