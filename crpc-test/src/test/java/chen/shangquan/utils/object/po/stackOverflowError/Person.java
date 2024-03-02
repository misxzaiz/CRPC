package chen.shangquan.utils.object.po.stackOverflowError;


import lombok.Data;

import java.util.List;

@Data
public class Person {
    private String name;
    private int age;
    private Integer code;
    private List<Friend> friends;
    private Person father;
}
