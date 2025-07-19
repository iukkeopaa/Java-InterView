package Thread.ThreadSafeCounter;

/**
 * @Description:
 * @Author: wjh
 * @Date: 2025/7/19 15:56
 */
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadSafeCounter2 {
    private static final int THREAD_COUNT = 100;
    private static final int INCREMENTS_PER_THREAD = 100;
    private static int counter = 0;
    private static final Object lock = new Object();

    public static void main(String[] args) throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);

        for (int i = 0; i < THREAD_COUNT; i++) {
            executor.submit(() -> {
                for (int j = 0; j < INCREMENTS_PER_THREAD; j++) {
                    increment(); // 调用同步方法
                }
            });
        }

        executor.shutdown();
        while (!executor.isTerminated()) {
            // 等待所有任务完成
        }

        System.out.println("最终计数结果: " + counter); // 输出应为10000
    }

    // 同步方法实现线程安全
    private static synchronized void increment() {
        counter++;
    }

    // 或者使用同步块
    private static void incrementWithBlock() {
        synchronized (lock) {
            counter++;
        }
    }
}
