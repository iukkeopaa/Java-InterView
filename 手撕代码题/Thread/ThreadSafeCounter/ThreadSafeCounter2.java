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
                    increment(); // ����ͬ������
                }
            });
        }

        executor.shutdown();
        while (!executor.isTerminated()) {
            // �ȴ������������
        }

        System.out.println("���ռ������: " + counter); // ���ӦΪ10000
    }

    // ͬ������ʵ���̰߳�ȫ
    private static synchronized void increment() {
        counter++;
    }

    // ����ʹ��ͬ����
    private static void incrementWithBlock() {
        synchronized (lock) {
            counter++;
        }
    }
}
