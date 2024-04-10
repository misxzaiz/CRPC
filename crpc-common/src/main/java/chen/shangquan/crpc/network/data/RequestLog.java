package chen.shangquan.crpc.network.data;

import cn.hutool.json.JSONUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.jackson.JsonComponent;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestLog extends RpcRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private RpcResponse rpcResponse;
    private List<String> e;
    private String startTime;
    private String endTime;
    private String spendTime;
    @Override
    public String toString() {
        return JSONUtil.toJsonStr(this);
    }
}
