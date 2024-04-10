package chen.shangquan.utils.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPoolUtils {
    public static final ExecutorService virtualThreadPool = Executors.newVirtualThreadPerTaskExecutor();
}
