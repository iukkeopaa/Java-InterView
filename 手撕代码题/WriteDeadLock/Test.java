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
                System.out.println("线程1获得到锁1");
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e){
                    e.printStackTrace();
                }
                System.out.println("线程1准备获取锁2");
                synchronized (lock2) {
                    System.out.println("线程1获取锁2");
                }
            }
        });

        Thread t2 = new Thread(() -> {
            synchronized (lock2) {
                System.out.println("线程2获得到锁2");
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e){
                    e.printStackTrace();
                }
                System.out.println("线程2准备获取锁1");
                synchronized (lock1) {
                    System.out.println("线程2获取锁1");
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
