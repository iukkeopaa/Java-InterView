package Thread.SequentialThreads;

/**
 * @Description:
 * @Author: wjh
 * @Date: 2025/7/19 15:26
 */
import java.util.concurrent.Semaphore;

public class SequentialThreadsWithSemaphore {
    // 三个信号量，分别控制三个线程的执行权限
    private static final Semaphore sem1 = new Semaphore(1); // 线程1初始有许可
    private static final Semaphore sem2 = new Semaphore(0); // 线程2初始无许可
    private static final Semaphore sem3 = new Semaphore(0); // 线程3初始无许可
    private static final int MAX_NUM = 75;

    public static void main(String[] args) {
        // 线程1：打印1-5、16-20...61-65
        Thread t1 = new Thread(() -> printRange(1, sem1, sem2));
        // 线程2：打印6-10、21-25...66-70
        Thread t2 = new Thread(() -> printRange(2, sem2, sem3));
        // 线程3：打印11-15、26-30...71-75
        Thread t3 = new Thread(() -> printRange(3, sem3, sem1)); // 线程3执行完唤醒线程1

        t1.start();
        t2.start();
        t3.start();
    }

    /**
     * 打印指定线程负责的区间
     * @param threadId 线程编号（1-3）
     * @param currentSem 当前线程的信号量（获取许可才能执行）
     * @param nextSem 下一个线程的信号量（当前线程执行完后释放）
     */
    private static void printRange(int threadId, Semaphore currentSem, Semaphore nextSem) {
        int start = (threadId - 1) * 5 + 1; // 计算初始起始值
        while (start <= MAX_NUM) {
            try {
                currentSem.acquire(); // 获取当前线程的执行许可（若没有则阻塞）

                // 打印当前区间的5个数字
                for (int i = start; i < start + 5; i++) {
                    System.out.println("线程" + threadId + "：" + i);
                }

                start += 15; // 下一轮起始值（间隔3个线程×5个数=15）
                nextSem.release(); // 释放下一个线程的许可
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }
}
