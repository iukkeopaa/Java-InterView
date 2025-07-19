package Thread.SequentialThreads;

/**
 * @Description:
 * @Author: wjh
 * @Date: 2025/7/19 15:38
 */


/**
 * @Description: ʹ���ź������������߳�˳���ӡ1-75
 * @Author: wjh
 * @Date: 2025/7/19 15:26
 */
import java.util.concurrent.Semaphore;

public class SequentialThreadsWithSemaphore1 {
    private static final Semaphore SEMAPHORE_1 = new Semaphore(1);
    private static final Semaphore SEMAPHORE_2 = new Semaphore(0);
    private static final Semaphore SEMAPHORE_3 = new Semaphore(0);
    private static final int MAX_NUMBER = 75;

    public static void main(String[] args) {
        Thread thread1 = new Thread(() -> printNumbers(1));
        Thread thread2 = new Thread(() -> printNumbers(2));
        Thread thread3 = new Thread(() -> printNumbers(3));

        thread1.start();
        thread2.start();
        thread3.start();
    }

    private static void printNumbers(int threadId) {
        int baseNumber = (threadId - 1) * 5 + 1;
        int cycle = 0;

        while (true) {
            int startNumber = baseNumber + cycle * 15;
            if (startNumber > MAX_NUMBER) break;

            try {
                // ֻʹ�õ�ǰ�̵߳��ź���
                getCurrentSemaphore(threadId).acquire();

                // ��ӡ5����������
                for (int i = 0; i < 5; i++) {
                    System.out.println("�߳�" + threadId + "��" + (startNumber + i));
                }

                // ����жϲ��ͷ���һ���̵߳��ź���
                if (threadId == 1) {
                    SEMAPHORE_2.release();
                } else if (threadId == 2) {
                    SEMAPHORE_3.release();
                } else if (threadId == 3) {
                    SEMAPHORE_1.release();
                }

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }

            cycle++;
        }
    }

    private static Semaphore getCurrentSemaphore(int threadId) {
        switch (threadId) {
            case 1: return SEMAPHORE_1;
            case 2: return SEMAPHORE_2;
            case 3: return SEMAPHORE_3;
            default: throw new IllegalArgumentException("Invalid thread ID: " + threadId);
        }
    }
}