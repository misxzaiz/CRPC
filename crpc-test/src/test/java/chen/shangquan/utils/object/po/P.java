package chen.shangquan.utils.object.po;

import lombok.Data;

import java.util.HashMap;
import java.util.List;

@Data
public class P {
    private String name;
    private int age;
    private P father;
    private List<P> friend;
    private F f;
    private List<F> flist;
}
