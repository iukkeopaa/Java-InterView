package Thread.OddEvenPrinter;

/**
 * @Description:
 * @Author: wjh
 * @Date: 2025/7/19 15:16
 */
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class OddEvenPrinterLock {
    private static final Lock lock = new ReentrantLock();
    private static final Condition ODD_CONDITION = lock.newCondition();
    private static final Condition EVEN_CONDITION = lock.newCondition();
    private static int number = 1;
    private static final int MAX = 10;

    public static void main(String[] args) {
        Thread oddThread = new Thread(() -> {
            while (true) {
                lock.lock();
                try {
                    // �ȴ���������
                    while (number <= MAX && number % 2 == 0) {
                        ODD_CONDITION.await();
                    }

                    if (number > MAX) {
                        EVEN_CONDITION.signal(); // ����ż���߳�
                        break;
                    }

                    System.out.println(Thread.currentThread().getName() + ": " + number++);
                    EVEN_CONDITION.signal(); // ����ż���߳�
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                } finally {
                    lock.unlock();
                }
            }
        }, "�����߳�");

        Thread evenThread = new Thread(() -> {
            while (true) {
                lock.lock();
                try {
                    // �ȴ�ż������
                    while (number <= MAX && number % 2 != 0) {
                        EVEN_CONDITION.await();
                    }

                    if (number > MAX) {
                        ODD_CONDITION.signal(); // ���������߳�
                        break;
                    }

                    System.out.println(Thread.currentThread().getName() + ": " + number++);
                    ODD_CONDITION.signal(); // ���������߳�
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                } finally {
                    lock.unlock();
                }
            }
        }, "ż���߳�");

        oddThread.start();
        evenThread.start();

        try {
            oddThread.join();
            evenThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("��ӡ���");
    }
}