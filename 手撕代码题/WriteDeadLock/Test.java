package WriteDeadLock;

/**
 * @Description:
 * @Author: wjh
 * @Date: 2025/7/19 14:00
 */
public class Test {
    public static void main(String[] args) {

        final Object lock1 = new Object();
        final Object lock2 = new Object();

        Thread t1 = new Thread(() -> {
            synchronized (lock1) {
                System.out.println("�߳�1��õ���1");
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e){
                    e.printStackTrace();
                }
                System.out.println("�߳�1׼����ȡ��2");
                synchronized (lock2) {
                    System.out.println("�߳�1��ȡ��2");
                }
            }
        });

        Thread t2 = new Thread(() -> {
            synchronized (lock2) {
                System.out.println("�߳�2��õ���2");
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e){
                    e.printStackTrace();
                }
                System.out.println("�߳�2׼����ȡ��1");
                synchronized (lock1) {
                    System.out.println("�߳�2��ȡ��1");
                }
            }
        });

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Finally");

    }

}
