package WriteDeadLock;

/**
 * @Description:
 * @Author: wjh
 * @Date: 2025/7/19 13:58
 */
public class DeadlockExample {
    public static void main(String[] args) {
        final Object lock1 = new Object();
        final Object lock2 = new Object();

        // 线程1：先获取lock1，再获取lock2
        Thread thread1 = new Thread(() -> {
            synchronized (lock1) {
                System.out.println("线程1已获取锁1");
                try {
                    Thread.sleep(100); // 确保线程2有机会获取lock2
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("线程1尝试获取锁2");
                synchronized (lock2) {
                    System.out.println("线程1已获取锁2");
                }
            }
        });

        // 线程2：先获取lock2，再获取lock1
        Thread thread2 = new Thread(() -> {
            synchronized (lock2) {
                System.out.println("线程2已获取锁2");
                try {
                    Thread.sleep(100); // 确保线程1有机会获取lock1
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("线程2尝试获取锁1");
                synchronized (lock1) {
                    System.out.println("线程2已获取锁1");
                }
            }
        });

        // 启动两个线程
        thread1.start();
        thread2.start();

        // 等待两个线程结束（实际上会因为死锁而永远等待）
        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("程序正常结束"); // 这行永远不会被执行
    }
}