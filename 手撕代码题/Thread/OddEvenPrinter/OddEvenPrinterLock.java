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
                    // 等待奇数条件
                    while (number <= MAX && number % 2 == 0) {
                        ODD_CONDITION.await();
                    }

                    if (number > MAX) {
                        EVEN_CONDITION.signal(); // 唤醒偶数线程
                        break;
                    }

                    System.out.println(Thread.currentThread().getName() + ": " + number++);
                    EVEN_CONDITION.signal(); // 唤醒偶数线程
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                } finally {
                    lock.unlock();
                }
            }
        }, "奇数线程");

        Thread evenThread = new Thread(() -> {
            while (true) {
                lock.lock();
                try {
                    // 等待偶数条件
                    while (number <= MAX && number % 2 != 0) {
                        EVEN_CONDITION.await();
                    }

                    if (number > MAX) {
                        ODD_CONDITION.signal(); // 唤醒奇数线程
                        break;
                    }

                    System.out.println(Thread.currentThread().getName() + ": " + number++);
                    ODD_CONDITION.signal(); // 唤醒奇数线程
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                } finally {
                    lock.unlock();
                }
            }
        }, "偶数线程");

        oddThread.start();
        evenThread.start();

        try {
            oddThread.join();
            evenThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("打印完成");
    }
}