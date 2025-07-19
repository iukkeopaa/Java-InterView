package Thread.PrintNumbers;

/**
 * @Description: �����̰߳�˳���ӡ�ض����ɵ����֣�A �̴߳�ӡ 3n+1���� 1��4��7...����B �̴߳�ӡ 3n+2���� 2��5��8...����C �̴߳�ӡ 3n���� 3��6��9...�����Ұ� A��B��C ��˳��ѭ��ִ�У�ֱ��ĳ����Χ�������ӡ�� 30����
 * @Author: wjh
 * @Date: 2025/7/19 15:28
 */
import java.util.concurrent.Semaphore;

public class PrintNumbers {
    // �ź�������˳��A��B��C��A
    private static final Semaphore semA = new Semaphore(1); // A��ִ��
    private static final Semaphore semB = new Semaphore(0);
    private static final Semaphore semC = new Semaphore(0);
    private static final int MAX = 30; // ��ӡ��30Ϊֹ

    public static void main(String[] args) {
        // A�̣߳���ӡ3n+1��1,4,7...��
        new Thread(() -> {
            try {
                for (int n = 0; ; n++) {
                    int num = 3 * n + 1;
                    if (num > MAX) break;

                    semA.acquire();
                    System.out.println("A: " + num);
                    semB.release(); // ����B
                }
                semB.release(); // ȷ��B���˳�
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();

        // B�̣߳���ӡ3n+2��2,5,8...��
        new Thread(() -> {
            try {
                for (int n = 0; ; n++) {
                    int num = 3 * n + 2;
                    if (num > MAX) break;

                    semB.acquire();
                    System.out.println("B: " + num);
                    semC.release(); // ����C
                }
                semC.release(); // ȷ��C���˳�
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();

        // C�̣߳���ӡ3n��3,6,9...��
        new Thread(() -> {
            try {
                for (int n = 1; ; n++) {
                    int num = 3 * n;
                    if (num > MAX) break;

                    semC.acquire();
                    System.out.println("C: " + num);
                    semA.release(); // ����A����ʼ��һ��
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
    }
}