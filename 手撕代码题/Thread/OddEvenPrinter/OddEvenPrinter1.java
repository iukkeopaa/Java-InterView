package Thread.OddEvenPrinter;

/**
 * @Description:
 * @Author: wjh
 * @Date: 2025/7/19 15:24
 */
import java.util.concurrent.Semaphore;

public class OddEvenPrinter1 {
    // 信号量：oddSem控制奇数线程，evenSem控制偶数线程
    private static final Semaphore oddSem = new Semaphore(1);  // 初始许可1，让奇数线程先执行
    private static final Semaphore evenSem = new Semaphore(0); // 初始许可0，偶数线程先等待

    public static void main(String[] args) {
        // 奇数线程：打印1,3,5...99
        Thread oddThread = new Thread(() -> {
            try {
                for (int i = 1; i <= 99; i += 2) {
                    oddSem.acquire(); // 获取奇数线程许可（若无则阻塞）
                    System.out.println("奇数线程：" + i);
                    evenSem.release(); // 释放偶数线程许可，让其执行
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        // 偶数线程：打印2,4,6...100
        Thread evenThread = new Thread(() -> {
            try {
                for (int i = 2; i <= 100; i += 2) {
                    evenSem.acquire(); // 获取偶数线程许可（若无则阻塞）
                    System.out.println("偶数线程：" + i);
                    oddSem.release(); // 释放奇数线程许可，让其执行
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        oddThread.start();
        evenThread.start();
    }
}