package chen.shangquan.utils.generator;

/**
 * 唯一 id 生成器
 * 最多支持 32 个节点 （0 - 31）
 * 每个节点每毫秒最多生成 4096 个 ID
 */
public class UniqueIdGenerator {

    /**
     * 需要生成一个 64 位字节（8 字节）的唯一 ID
     * 并将其转换为十六进制字符串表示
     */
    private static final long EPOCH = 1609459200000L; // 2021-01-01 00:00:00 UTC

    // TODO 支持主动配置机器码和顺序号的长度
    private static final long WORKER_ID_BITS = 5L;
    private static final long SEQUENCE_BITS = 12L;

    private static final long MAX_WORKER_ID = ~(-1L << WORKER_ID_BITS);
    private static final long MAX_SEQUENCE = ~(-1L << SEQUENCE_BITS);

    private static long workerId = 0L;
    private static long sequence = 0L;
    private static long lastTimestamp = -1L;

    public static synchronized void setWorkerId(long workerId) {
        if (workerId < 0 || workerId > MAX_WORKER_ID) {
            throw new IllegalArgumentException(String.format("机器 ID 必须在 0 和 %s 之间", MAX_WORKER_ID));
        }
        UniqueIdGenerator.workerId = workerId;
    }

    public static synchronized long generateUniqueIdLong() {
        long timestamp = System.currentTimeMillis();

        if (timestamp < lastTimestamp) {
            throw new RuntimeException("Invalid System Clock!");
        }

        if (timestamp == lastTimestamp) {
            sequence = (sequence + 1) & MAX_SEQUENCE;
            if (sequence == 0) {
                timestamp = tilNextMillis(lastTimestamp);
            }
        } else {
            sequence = 0L;
        }

        lastTimestamp = timestamp;

        return ((timestamp - EPOCH) << (WORKER_ID_BITS + SEQUENCE_BITS))
                | (workerId << SEQUENCE_BITS)
                | sequence;
    }


    private static long tilNextMillis(long lastTimestamp) {
        long timestamp = System.currentTimeMillis();
        while (timestamp <= lastTimestamp) {
            timestamp = System.currentTimeMillis();
        }
        return timestamp;
    }

    public static String generateUniqueIdHex() {
        long id = generateUniqueIdLong(); // 同上，假设这是一个返回长整型ID的方法
        return Long.toHexString(id);
    }

    public static String generateUniqueIdBinary() {
        long id = generateUniqueIdLong(); // 假设这是一个修改后的方法，直接返回长整型ID
        return Long.toBinaryString(id);
    }

    public static String generateUniqueId() {
        long id = generateUniqueIdLong(); // 同上，假设这是一个返回长整型ID的方法
        return Long.toString(id);
    }

}
