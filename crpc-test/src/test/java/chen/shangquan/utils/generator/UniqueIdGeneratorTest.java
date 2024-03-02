package chen.shangquan.utils.generator;

import org.junit.jupiter.api.Test;

public class UniqueIdGeneratorTest {

    @Test
    public void Test11() {
        UniqueIdGenerator.setWorkerId(33L);
        System.out.println(~(-1L << 10L));;;
    }

    @Test
    public void test() {
        Long l = 31L;
        System.out.println(Long.toBinaryString(l));
        String s1 = UniqueIdGenerator.generateUniqueIdBinary();
        System.out.println(s1);
        String s22 = UniqueIdGenerator.generateUniqueIdBinary();
        System.out.println(s22);
        String s221 = UniqueIdGenerator.generateUniqueIdBinary();
        System.out.println(s221);
        String s2221 = UniqueIdGenerator.generateUniqueIdBinary();
        System.out.println(s2221);
        UniqueIdGenerator.setWorkerId(31L);
        String s = UniqueIdGenerator.generateUniqueIdBinary();
        System.out.println(s);
        String s2 = UniqueIdGenerator.generateUniqueIdBinary();
        System.out.println(s2);
        String s3 = UniqueIdGenerator.generateUniqueIdBinary();
        System.out.println(s3);
        UniqueIdGenerator.setWorkerId(1L);
        String s12 = UniqueIdGenerator.generateUniqueIdBinary();
        System.out.println(s12);
        String s122 = UniqueIdGenerator.generateUniqueIdBinary();
        System.out.println(s122);
        String s123 = UniqueIdGenerator.generateUniqueIdBinary();
        System.out.println(s123);
    }

    @Test
    public void tes1t() {
        Long l = 31L;
        String s1 = UniqueIdGenerator.generateUniqueId();
        System.out.println(s1);
        UniqueIdGenerator.setWorkerId(31L);
        String s = UniqueIdGenerator.generateUniqueId();
        System.out.println(s);
        String s2 = UniqueIdGenerator.generateUniqueId();
        System.out.println(s2);
        String s3 = UniqueIdGenerator.generateUniqueId();
        System.out.println(s3);
    }

    @Test
    public void tes1t2() {
        Long l = 31L;
        String s1 = UniqueIdGenerator.generateUniqueIdHex();
        System.out.println(s1);
        UniqueIdGenerator.setWorkerId(31L);
        String s = UniqueIdGenerator.generateUniqueIdHex();
        System.out.println(s);
        String s2 = UniqueIdGenerator.generateUniqueIdHex();
        System.out.println(s2);
        String s3 = UniqueIdGenerator.generateUniqueIdHex();
        System.out.println(s3);
    }
}
