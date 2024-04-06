package chen.shangquan.jdk;

import org.junit.jupiter.api.Test;

import java.util.concurrent.ThreadLocalRandom;

public class RandomTest {

    @Test
    public void t1() {
        ThreadLocalRandom current = ThreadLocalRandom.current();
        int i = current.nextInt(5);
        System.out.println(i);
    }

}
