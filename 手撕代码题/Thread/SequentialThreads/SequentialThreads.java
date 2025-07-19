package Thread.SequentialThreads;

/**
 * @Description:
 *
 * 划分打印区间：每个线程负责连续的 5 个数字，按 “线程 1→线程 2→线程 3→线程 1…” 的顺序循环，直到打印到 75。
 * 线程 1：1-5、16-20、31-35、…、61-65
 * 线程 2：6-10、21-25、36-40、…、66-70
 * 线程 3：11-15、26-30、36-40、…、71-75
 * @Author: wjh
 * @Date: 2025/7/19 15:26
 */
public class SequentialThreads {
    // 控制当前轮到哪个线程执行（1、2、3）
    private int currentThread = 1;
    // 总打印上限
    private static final int MAX_NUM = 75;

    public static void main(String[] args) {
        SequentialThreads printer = new SequentialThreads();

        // 线程1：负责区间1-5、16-20...
        Thread t1 = new Thread(() -> printer.print(1));
        // 线程2：负责区间6-10、21-25...
        Thread t2 = new Thread(() -> printer.print(2));
        // 线程3：负责区间11-15、26-30...
        Thread t3 = new Thread(() -> printer.print(3));

        t1.start();
        t2.start();
        t3.start();
    }

    private synchronized void print(int threadId) {
        // 计算该线程需要打印的起始数字（每个线程每次打印5个，循环执行）
        int start = (threadId - 1) * 5 + 1;
        while (start <= MAX_NUM) {
            // 若还没轮到当前线程，等待
            while (currentThread != threadId) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }

            // 打印当前区间的5个数字
            for (int i = start; i < start + 5; i++) {
                System.out.println("线程" + threadId + "：" + i);
            }

            // 更新下一个该执行的线程（1→2→3→1循环）
            currentThread = (threadId % 3) + 1;
            // 唤醒其他等待的线程
            notifyAll();

            // 计算下一轮的起始数字（每个线程间隔15个数，因为3个线程各打印5个）
            start += 15;
        }
    }
}