package chen.shangquan.crpc.model.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MethodInfo {
    /**
     * 方法名称
     */
    private String name;
    /**
     * 方法参数列表
     */
    private List<ParameterInfo> parameterList;
}
