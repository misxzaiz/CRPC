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
public class ClassInfo implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * 类名
     */
    private String name;
    /**
     * 类路径
     */
    private String path;
    /**
     * 类标识
     */
    private String version;
    /**
     * 方法列表
     */
    private List<MethodInfo> methodList;
}
