package Thread.OddEvenPrinter;

/**
 * @Description:
 * @Author: wjh
 * @Date: 2025/7/19 15:15
 */
public class OddEvenPrinter {
    private static final Object lock = new Object();
    private static int number = 1;
    private static final int MAX = 10; // 打印上限

    public static void main(String[] args) {
        // 偶数线程
        Thread evenThread = new Thread(() -> {
            while (true) {
                synchronized (lock) {
                    // 当数字超过最大值或为奇数时等待
                    while (number <= MAX && number % 2 != 0) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            return;
                        }
                    }

                    // 检查是否已达到最大值
                    if (number > MAX) {
                        lock.notify(); // 唤醒另一个线程
                        break;
                    }

                    System.out.println(Thread.currentThread().getName() + ": " + number++);
                    lock.notify(); // 唤醒另一个线程
                }
            }
        }, "偶数线程");

        // 奇数线程
        Thread oddThread = new Thread(() -> {
            while (true) {
                synchronized (lock) {
                    // 当数字超过最大值或为偶数时等待
                    while (number <= MAX && number % 2 == 0) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            return;
                        }
                    }

                    // 检查是否已达到最大值
                    if (number > MAX) {
                        lock.notify(); // 唤醒另一个线程
                        break;
                    }

                    System.out.println(Thread.currentThread().getName() + ": " + number++);
                    lock.notify(); // 唤醒另一个线程
                }
            }
        }, "奇数线程");

        evenThread.start();
        oddThread.start();

        try {
            evenThread.join();
            oddThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("打印完成");
    }
}