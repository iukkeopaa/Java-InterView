package Thread.ThreadSafeCounter;

/**
 * @Description:
 * @Author: wjh
 * @Date: 2025/7/19 15:54
 */
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadSafeCounter1 {
    private static final int THREAD_COUNT = 100;
    private static final int INCREMENTS_PER_THREAD = 100;

    // 使用AtomicInteger保证原子性操作
    private static final AtomicInteger counter = new AtomicInteger(0);

    public static void main(String[] args) throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);

        // 提交100个任务
        for (int i = 0; i < THREAD_COUNT; i++) {
            executor.submit(() -> {
                for (int j = 0; j < INCREMENTS_PER_THREAD; j++) {
                    counter.incrementAndGet(); // 原子自增操作
                }
            });
        }

        executor.shutdown();

        executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
//        while (!executor.isTerminated()) {
//            // 等待所有任务完成
//        }

        System.out.println("最终计数结果: " + counter.get()); // 输出应为10000
    }
}