package Thread.PrintNumbers;

/**
 * @Description: 三个线程按顺序打印特定规律的数字：A 线程打印 3n+1（如 1、4、7...），B 线程打印 3n+2（如 2、5、8...），C 线程打印 3n（如 3、6、9...），且按 A→B→C 的顺序循环执行，直到某个范围（比如打印到 30）。
 * @Author: wjh
 * @Date: 2025/7/19 15:28
 */
import java.util.concurrent.Semaphore;

public class PrintNumbers {
    // 信号量控制顺序：A→B→C→A
    private static final Semaphore semA = new Semaphore(1); // A先执行
    private static final Semaphore semB = new Semaphore(0);
    private static final Semaphore semC = new Semaphore(0);
    private static final int MAX = 30; // 打印到30为止

    public static void main(String[] args) {
        // A线程：打印3n+1（1,4,7...）
        new Thread(() -> {
            try {
                for (int n = 0; ; n++) {
                    int num = 3 * n + 1;
                    if (num > MAX) break;

                    semA.acquire();
                    System.out.println("A: " + num);
                    semB.release(); // 唤醒B
                }
                semB.release(); // 确保B能退出
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();

        // B线程：打印3n+2（2,5,8...）
        new Thread(() -> {
            try {
                for (int n = 0; ; n++) {
                    int num = 3 * n + 2;
                    if (num > MAX) break;

                    semB.acquire();
                    System.out.println("B: " + num);
                    semC.release(); // 唤醒C
                }
                semC.release(); // 确保C能退出
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();

        // C线程：打印3n（3,6,9...）
        new Thread(() -> {
            try {
                for (int n = 1; ; n++) {
                    int num = 3 * n;
                    if (num > MAX) break;

                    semC.acquire();
                    System.out.println("C: " + num);
                    semA.release(); // 唤醒A，开始下一轮
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
    }
}