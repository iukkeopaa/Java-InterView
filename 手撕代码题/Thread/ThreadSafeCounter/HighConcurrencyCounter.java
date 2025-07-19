package Thread.ThreadSafeCounter;

/**
 * @Description:
 * @Author: wjh
 * @Date: 2025/7/19 15:57
 */
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.LongAdder;

public class HighConcurrencyCounter {
    private static final int THREAD_COUNT = 100;
    private static final int INCREMENTS_PER_THREAD = 100;
    private static final int TEST_ROUNDS = 100;

    public static void main(String[] args) throws InterruptedException {
        // 测试AtomicLong
        long atomicTime = testAtomicLong();
        System.out.println("AtomicLong 耗时: " + atomicTime + "ms");

        // 测试LongAdder
        long adderTime = testLongAdder();
        System.out.println("LongAdder 耗时: " + adderTime + "ms");

        // 输出性能提升比例
        double improvement = (double)(atomicTime - adderTime) / atomicTime * 100;
        System.out.printf("LongAdder 性能提升: %.2f%%\n", improvement);
    }

    private static long testAtomicLong() throws InterruptedException {
        long startTime = System.currentTimeMillis();

        for (int round = 0; round < TEST_ROUNDS; round++) {
            java.util.concurrent.atomic.AtomicLong counter = new java.util.concurrent.atomic.AtomicLong(0);
            ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);

            for (int i = 0; i < THREAD_COUNT; i++) {
                executor.submit(() -> {
                    for (int j = 0; j < INCREMENTS_PER_THREAD; j++) {
                        counter.incrementAndGet();
                    }
                });
            }

            executor.shutdown();
            while (!executor.isTerminated()) {}

            if (counter.get() != THREAD_COUNT * INCREMENTS_PER_THREAD) {
                System.err.println("AtomicLong 计数错误: " + counter.get());
            }
        }

        return System.currentTimeMillis() - startTime;
    }

    private static long testLongAdder() throws InterruptedException {
        long startTime = System.currentTimeMillis();

        for (int round = 0; round < TEST_ROUNDS; round++) {
            LongAdder counter = new LongAdder();
            ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);

            for (int i = 0; i < THREAD_COUNT; i++) {
                executor.submit(() -> {
                    for (int j = 0; j < INCREMENTS_PER_THREAD; j++) {
                        counter.increment();
                    }
                });
            }

            executor.shutdown();
            while (!executor.isTerminated()) {}

            if (counter.sum() != THREAD_COUNT * INCREMENTS_PER_THREAD) {
                System.err.println("LongAdder 计数错误: " + counter.sum());
            }
        }

        return System.currentTimeMillis() - startTime;
    }
}