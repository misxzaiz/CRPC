package chen.shangquan.utils.object.po.underpresentation;

import lombok.Data;

import java.util.List;

@Data
public class P {
    private String name;
    private Integer age;
    private Integer code;
    private P father;
    private List<P> friend;
}
