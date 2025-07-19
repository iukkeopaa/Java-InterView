package Thread.OddEvenPrinter;

/**
 * @Description:
 * @Author: wjh
 * @Date: 2025/7/19 15:17
 */
import java.util.concurrent.Semaphore;

public class OddEvenPrinterSemaphore {
    private static final Semaphore ODD_SEMAPHORE = new Semaphore(1); // ��ʼ��ɣ������߳���ִ��
    private static final Semaphore EVEN_SEMAPHORE = new Semaphore(0); // ��ʼ�����
    private static int number = 1;
    private static final int MAX = 10;

    public static void main(String[] args) {
        Thread oddThread = new Thread(() -> {
            try {
                while (true) {
                    ODD_SEMAPHORE.acquire(); // ��ȡ�����ź������

                    if (number > MAX) {
                        EVEN_SEMAPHORE.release(); // �ͷ�ż���ź���������ż���߳���ֹ
                        break;
                    }

                    System.out.println(Thread.currentThread().getName() + ": " + number++);
                    EVEN_SEMAPHORE.release(); // �ͷ�ż���ź���������ż���߳�ִ��
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "�����߳�");

        Thread evenThread = new Thread(() -> {
            try {
                while (true) {
                    EVEN_SEMAPHORE.acquire(); // ��ȡż���ź������

                    if (number > MAX) {
                        ODD_SEMAPHORE.release(); // �ͷ������ź��������������߳���ֹ
                        break;
                    }

                    System.out.println(Thread.currentThread().getName() + ": " + number++);
                    ODD_SEMAPHORE.release(); // �ͷ������ź��������������߳�ִ��
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "ż���߳�");

        oddThread.start();
        evenThread.start();

        try {
            oddThread.join();
            evenThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("��ӡ���");
    }
}