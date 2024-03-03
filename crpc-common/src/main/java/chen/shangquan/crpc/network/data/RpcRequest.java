package chen.shangquan.crpc.network.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RpcRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private String id;
    private String token;
    private String serverName;
    private String className;
    private String methodName;
    private String version = "V1";
    private String area;
    private Object data;
}

