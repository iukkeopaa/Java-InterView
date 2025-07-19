package Thread.SequentialThreads;

/**
 * @Description:
 * @Author: wjh
 * @Date: 2025/7/19 15:26
 */
import java.util.concurrent.Semaphore;

public class SequentialThreadsWithSemaphore {
    // �����ź������ֱ���������̵߳�ִ��Ȩ��
    private static final Semaphore sem1 = new Semaphore(1); // �߳�1��ʼ�����
    private static final Semaphore sem2 = new Semaphore(0); // �߳�2��ʼ�����
    private static final Semaphore sem3 = new Semaphore(0); // �߳�3��ʼ�����
    private static final int MAX_NUM = 75;

    public static void main(String[] args) {
        // �߳�1����ӡ1-5��16-20...61-65
        Thread t1 = new Thread(() -> printRange(1, sem1, sem2));
        // �߳�2����ӡ6-10��21-25...66-70
        Thread t2 = new Thread(() -> printRange(2, sem2, sem3));
        // �߳�3����ӡ11-15��26-30...71-75
        Thread t3 = new Thread(() -> printRange(3, sem3, sem1)); // �߳�3ִ���껽���߳�1

        t1.start();
        t2.start();
        t3.start();
    }

    /**
     * ��ӡָ���̸߳��������
     * @param threadId �̱߳�ţ�1-3��
     * @param currentSem ��ǰ�̵߳��ź�������ȡ��ɲ���ִ�У�
     * @param nextSem ��һ���̵߳��ź�������ǰ�߳�ִ������ͷţ�
     */
    private static void printRange(int threadId, Semaphore currentSem, Semaphore nextSem) {
        int start = (threadId - 1) * 5 + 1; // �����ʼ��ʼֵ
        while (start <= MAX_NUM) {
            try {
                currentSem.acquire(); // ��ȡ��ǰ�̵߳�ִ����ɣ���û����������

                // ��ӡ��ǰ�����5������
                for (int i = start; i < start + 5; i++) {
                    System.out.println("�߳�" + threadId + "��" + i);
                }

                start += 15; // ��һ����ʼֵ�����3���̡߳�5����=15��
                nextSem.release(); // �ͷ���һ���̵߳����
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }
}
