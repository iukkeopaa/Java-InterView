package Thread.OddEvenPrinter;

/**
 * @Description:
 * @Author: wjh
 * @Date: 2025/7/19 15:24
 */
import java.util.concurrent.Semaphore;

public class OddEvenPrinter1 {
    // �ź�����oddSem���������̣߳�evenSem����ż���߳�
    private static final Semaphore oddSem = new Semaphore(1);  // ��ʼ���1���������߳���ִ��
    private static final Semaphore evenSem = new Semaphore(0); // ��ʼ���0��ż���߳��ȵȴ�

    public static void main(String[] args) {
        // �����̣߳���ӡ1,3,5...99
        Thread oddThread = new Thread(() -> {
            try {
                for (int i = 1; i <= 99; i += 2) {
                    oddSem.acquire(); // ��ȡ�����߳���ɣ�������������
                    System.out.println("�����̣߳�" + i);
                    evenSem.release(); // �ͷ�ż���߳���ɣ�����ִ��
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        // ż���̣߳���ӡ2,4,6...100
        Thread evenThread = new Thread(() -> {
            try {
                for (int i = 2; i <= 100; i += 2) {
                    evenSem.acquire(); // ��ȡż���߳���ɣ�������������
                    System.out.println("ż���̣߳�" + i);
                    oddSem.release(); // �ͷ������߳���ɣ�����ִ��
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        oddThread.start();
        evenThread.start();
    }
}