package chen.shangquan.crpc.model.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServerInfo implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * 服务名称
     */
    private String name;
    /**
     * ip
     */
    private String ip;
    /**
     * 端口
     */
    private Integer port;
    /**
     * 权重
     */
    private Integer weight = 5;
    /**
     * 区域
     */
    private String area;
    /**
     * 中心服务地址
     */
    private String center;
    /**
     * 类列表
     */
    private List<ClassInfo> classList;
    public ServerInfo(String serverName) {
        this.name = serverName;
    }

    public String toString() {
        return
                name + "={" +
                "weight=" + weight +
                ", area='" + area + "}";
    }
}
