package chen.shangquan.crpc.model.po;

import lombok.Data;

import java.util.List;

@Data
public class ParameterInfo {
    private String name;
    private List<String> parameterList;
    private String parameterJson;
}
