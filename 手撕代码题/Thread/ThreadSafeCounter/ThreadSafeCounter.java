package Thread.ThreadSafeCounter;

/**
 * @Description: 实现?个线程安全的计数器，100 个线程，每个线程累加 100 次
 * @Author: wjh
 * @Date: 2025/7/19 15:28
 */
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadSafeCounter {
    // 原子计数器，初始值0
    private static final AtomicInteger counter = new AtomicInteger(0);
    // 线程数量
    private static final int THREAD_COUNT = 100;
    // 每个线程累加次数
    private static final int ADD_TIMES = 100;

    public static void main(String[] args) throws InterruptedException {
        // 创建100个线程
        Thread[] threads = new Thread[THREAD_COUNT];
        for (int i = 0; i < THREAD_COUNT; i++) {
            threads[i] = new Thread(() -> {
                // 每个线程累加100次
                for (int j = 0; j < ADD_TIMES; j++) {
                    counter.incrementAndGet(); // 原子累加，线程安全
                }
            });
        }

        // 启动所有线程
        for (Thread thread : threads) {
            thread.start();
        }

        // 等待所有线程执行完毕
        for (Thread thread : threads) {
            thread.join();
        }

        // 输出最终结果（预期10000）
        System.out.println("最终计数：" + counter.get());
    }
}