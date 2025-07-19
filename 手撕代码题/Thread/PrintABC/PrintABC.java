package Thread.PrintABC;

/**
 * @Description: ʵ�������̷ֱ߳��ӡ "A"��"B"��"C"����ѭ�� 10 �֣������ "ABCABC...ABC" �� 10 �飩
 * @Author: wjh
 * @Date: 2025/7/19 15:27
 */
import java.util.concurrent.Semaphore;

public class PrintABC {
    // �����ź������ֱ����A��B��C�̵߳�ִ��˳��
    private static final Semaphore semA = new Semaphore(1);  // A�̳߳�ʼ����ɣ���ִ��
    private static final Semaphore semB = new Semaphore(0);  // B�̳߳�ʼ�ȴ�
    private static final Semaphore semC = new Semaphore(0);  // C�̳߳�ʼ�ȴ�
    private static final int ROUNDS = 10;  // ��ӡ����

    public static void main(String[] args) {
        // ��ӡA���̣߳�ִ�����ͷ�B�����
        Thread threadA = new Thread(() -> {
            try {
                for (int i = 0; i < ROUNDS; i++) {
                    semA.acquire();  // ��ȡA��ִ�����
                    System.out.print("A");
                    semB.release();  // ����Bִ��
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        // ��ӡB���̣߳�ִ�����ͷ�C�����
        Thread threadB = new Thread(() -> {
            try {
                for (int i = 0; i < ROUNDS; i++) {
                    semB.acquire();  // ��ȡB��ִ�����
                    System.out.print("B");
                    semC.release();  // ����Cִ��
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        // ��ӡC���̣߳�ִ�����ͷ�A����ɣ�������һ�֣�
        Thread threadC = new Thread(() -> {
            try {
                for (int i = 0; i < ROUNDS; i++) {
                    semC.acquire();  // ��ȡC��ִ�����
                    System.out.print("C");
                    semA.release();  // ����A��ʼ��һ��
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