package Thread.SequentialThreads;

/**
 * @Description:
 *
 * ���ִ�ӡ���䣺ÿ���̸߳��������� 5 �����֣��� ���߳� 1���߳� 2���߳� 3���߳� 1���� ��˳��ѭ����ֱ����ӡ�� 75��
 * �߳� 1��1-5��16-20��31-35������61-65
 * �߳� 2��6-10��21-25��36-40������66-70
 * �߳� 3��11-15��26-30��36-40������71-75
 * @Author: wjh
 * @Date: 2025/7/19 15:26
 */
public class SequentialThreads {
    // ���Ƶ�ǰ�ֵ��ĸ��߳�ִ�У�1��2��3��
    private int currentThread = 1;
    // �ܴ�ӡ����
    private static final int MAX_NUM = 75;

    public static void main(String[] args) {
        SequentialThreads printer = new SequentialThreads();

        // �߳�1����������1-5��16-20...
        Thread t1 = new Thread(() -> printer.print(1));
        // �߳�2����������6-10��21-25...
        Thread t2 = new Thread(() -> printer.print(2));
        // �߳�3����������11-15��26-30...
        Thread t3 = new Thread(() -> printer.print(3));

        t1.start();
        t2.start();
        t3.start();
    }

    private synchronized void print(int threadId) {
        // ������߳���Ҫ��ӡ����ʼ���֣�ÿ���߳�ÿ�δ�ӡ5����ѭ��ִ�У�
        int start = (threadId - 1) * 5 + 1;
        while (start <= MAX_NUM) {
            // ����û�ֵ���ǰ�̣߳��ȴ�
            while (currentThread != threadId) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }

            // ��ӡ��ǰ�����5������
            for (int i = start; i < start + 5; i++) {
                System.out.println("�߳�" + threadId + "��" + i);
            }

            // ������һ����ִ�е��̣߳�1��2��3��1ѭ����
            currentThread = (threadId % 3) + 1;
            // ���������ȴ����߳�
            notifyAll();

            // ������һ�ֵ���ʼ���֣�ÿ���̼߳��15��������Ϊ3���̸߳���ӡ5����
            start += 15;
        }
    }
}