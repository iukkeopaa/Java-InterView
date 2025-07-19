package Thread.OddEvenPrinter;

/**
 * @Description:
 * @Author: wjh
 * @Date: 2025/7/19 15:15
 */
public class OddEvenPrinter {
    private static final Object lock = new Object();
    private static int number = 1;
    private static final int MAX = 10; // ��ӡ����

    public static void main(String[] args) {
        // ż���߳�
        Thread evenThread = new Thread(() -> {
            while (true) {
                synchronized (lock) {
                    // �����ֳ������ֵ��Ϊ����ʱ�ȴ�
                    while (number <= MAX && number % 2 != 0) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            return;
                        }
                    }

                    // ����Ƿ��Ѵﵽ���ֵ
                    if (number > MAX) {
                        lock.notify(); // ������һ���߳�
                        break;
                    }

                    System.out.println(Thread.currentThread().getName() + ": " + number++);
                    lock.notify(); // ������һ���߳�
                }
            }
        }, "ż���߳�");

        // �����߳�
        Thread oddThread = new Thread(() -> {
            while (true) {
                synchronized (lock) {
                    // �����ֳ������ֵ��Ϊż��ʱ�ȴ�
                    while (number <= MAX && number % 2 == 0) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            return;
                        }
                    }

                    // ����Ƿ��Ѵﵽ���ֵ
                    if (number > MAX) {
                        lock.notify(); // ������һ���߳�
                        break;
                    }

                    System.out.println(Thread.currentThread().getName() + ": " + number++);
                    lock.notify(); // ������һ���߳�
                }
            }
        }, "�����߳�");

        evenThread.start();
        oddThread.start();

        try {
            evenThread.join();
            oddThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("��ӡ���");
    }
}