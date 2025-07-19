package Thread.PrintABC;

/**
 * @Description: 实现三个线程分别打印 "A"、"B"、"C"，并循环 10 轮（即输出 "ABCABC...ABC" 共 10 组）
 * @Author: wjh
 * @Date: 2025/7/19 15:27
 */
import java.util.concurrent.Semaphore;

public class PrintABC {
    // 三个信号量，分别控制A、B、C线程的执行顺序
    private static final Semaphore semA = new Semaphore(1);  // A线程初始有许可，先执行
    private static final Semaphore semB = new Semaphore(0);  // B线程初始等待
    private static final Semaphore semC = new Semaphore(0);  // C线程初始等待
    private static final int ROUNDS = 10;  // 打印轮数

    public static void main(String[] args) {
        // 打印A的线程：执行完释放B的许可
        Thread threadA = new Thread(() -> {
            try {
                for (int i = 0; i < ROUNDS; i++) {
                    semA.acquire();  // 获取A的执行许可
                    System.out.print("A");
                    semB.release();  // 允许B执行
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        // 打印B的线程：执行完释放C的许可
        Thread threadB = new Thread(() -> {
            try {
                for (int i = 0; i < ROUNDS; i++) {
                    semB.acquire();  // 获取B的执行许可
                    System.out.print("B");
                    semC.release();  // 允许C执行
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        // 打印C的线程：执行完释放A的许可（开启下一轮）
        Thread threadC = new Thread(() -> {
            try {
                for (int i = 0; i < ROUNDS; i++) {
                    semC.acquire();  // 获取C的执行许可
                    System.out.print("C");
                    semA.release();  // 允许A开始下一轮
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        threadA.start();
        threadB.start();
        threadC.start();
    }
}