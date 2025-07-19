package Thread.OddEvenPrinter;

/**
 * @Description:
 * @Author: wjh
 * @Date: 2025/7/19 15:17
 */
import java.util.concurrent.Semaphore;

public class OddEvenPrinterSemaphore {
    private static final Semaphore ODD_SEMAPHORE = new Semaphore(1); // 初始许可：奇数线程先执行
    private static final Semaphore EVEN_SEMAPHORE = new Semaphore(0); // 初始无许可
    private static int number = 1;
    private static final int MAX = 10;

    public static void main(String[] args) {
        Thread oddThread = new Thread(() -> {
            try {
                while (true) {
                    ODD_SEMAPHORE.acquire(); // 获取奇数信号量许可

                    if (number > MAX) {
                        EVEN_SEMAPHORE.release(); // 释放偶数信号量，允许偶数线程终止
                        break;
                    }

                    System.out.println(Thread.currentThread().getName() + ": " + number++);
                    EVEN_SEMAPHORE.release(); // 释放偶数信号量，允许偶数线程执行
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "奇数线程");

        Thread evenThread = new Thread(() -> {
            try {
                while (true) {
                    EVEN_SEMAPHORE.acquire(); // 获取偶数信号量许可

                    if (number > MAX) {
                        ODD_SEMAPHORE.release(); // 释放奇数信号量，允许奇数线程终止
                        break;
                    }

                    System.out.println(Thread.currentThread().getName() + ": " + number++);
                    ODD_SEMAPHORE.release(); // 释放奇数信号量，允许奇数线程执行
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
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