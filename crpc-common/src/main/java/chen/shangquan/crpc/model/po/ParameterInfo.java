package chen.shangquan.crpc.model.po;

import lombok.Data;

import java.util.List;

@Data
public class ParameterInfo {
    /**
     * 参数名称
     */
    private String name;
    /**
     * 参数类型列表
     */
    private List<String> parameterList;
    /**
     * 参数JSON格式
     */
    private String parameterJson;
}
