package Thread.ThreadSafeCounter;

/**
 * @Description: ʵ��?���̰߳�ȫ�ļ�������100 ���̣߳�ÿ���߳��ۼ� 100 ��
 * @Author: wjh
 * @Date: 2025/7/19 15:28
 */
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadSafeCounter {
    // ԭ�Ӽ���������ʼֵ0
    private static final AtomicInteger counter = new AtomicInteger(0);
    // �߳�����
    private static final int THREAD_COUNT = 100;
    // ÿ���߳��ۼӴ���
    private static final int ADD_TIMES = 100;

    public static void main(String[] args) throws InterruptedException {
        // ����100���߳�
        Thread[] threads = new Thread[THREAD_COUNT];
        for (int i = 0; i < THREAD_COUNT; i++) {
            threads[i] = new Thread(() -> {
                // ÿ���߳��ۼ�100��
                for (int j = 0; j < ADD_TIMES; j++) {
                    counter.incrementAndGet(); // ԭ���ۼӣ��̰߳�ȫ
                }
            });
        }

        // ���������߳�
        for (Thread thread : threads) {
            thread.start();
        }

        // �ȴ������߳�ִ�����
        for (Thread thread : threads) {
            thread.join();
        }

        // ������ս����Ԥ��10000��
        System.out.println("���ռ�����" + counter.get());
    }
}