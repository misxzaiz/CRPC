package chen.shangquan.utils.time;

import java.time.Duration;
import java.time.Instant;

public class TimestampUtils {
    // 生成指定时间后的毫秒级时间戳
    public static long generateTimestamp(long days, long hours, long minutes, long seconds, long milliseconds) {
        Instant now = Instant.now();
        Duration duration = Duration.ofDays(days)
                .plusHours(hours)
                .plusMinutes(minutes)
                .plusSeconds(seconds)
                .plusMillis(milliseconds);
        Instant future = now.plus(duration);
        return future.toEpochMilli();
    }

    // 检查时间戳是否已经过期
    public static boolean isExpired(long timestamp) {
        Instant now = Instant.now();
        Instant targetTime = Instant.ofEpochMilli(timestamp);
        return now.isAfter(targetTime);
    }

    public static void main(String[] args) {
        boolean expired = isExpired(1712397357855L);
        System.out.println(expired);
    }
}
