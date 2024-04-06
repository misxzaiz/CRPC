package chen.shangquan.mix;

import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicLong;

public class MainTest {
    @Test
    public void t2() {
        AtomicLong atomicLong = new AtomicLong();
    }

    @Test
    public void t1() {
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4);
        byteBuffer.put((byte)'1');
        byteBuffer.put((byte)'2');
        byteBuffer.put((byte)'3');
        byteBuffer.put((byte)'4');
        byteBuffer.flip();
        while (byteBuffer.hasRemaining()) {
            System.out.println((char) byteBuffer.get());
        }
    }

}
