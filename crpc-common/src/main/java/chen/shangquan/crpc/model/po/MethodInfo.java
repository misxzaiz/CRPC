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
    private String name;
    private List<ParameterInfo> parameterList;
}
