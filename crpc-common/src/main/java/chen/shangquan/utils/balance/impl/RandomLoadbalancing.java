package chen.shangquan.utils.balance.impl;

import chen.shangquan.utils.balance.LoadBalancing;

import java.util.List;
import java.util.Random;

public class RandomLoadbalancing implements LoadBalancing {
    /**
     * 元素：唯一标识，如 CRPC 中是 ip + port
     */
    private List<String> list = null;

    private final Random random = new Random();

    public RandomLoadbalancing(List<String> list) {
        this.list = list;
    }

    @Override
    public String loadBalancing() {
        int randomNumber = random.nextInt(list.size());
        // TODO 关于这块的优化
        // 1. 使用 Clone 代替 New，经测试，Clone 不如 New 效果好，创建 1000000000 个对象，New 9 ms，Clone 170 ms
        // 2. 使用 对象池，优点：实现简单，缺点，使用完后需要及时归还
        return list.get(randomNumber);
    }
}
