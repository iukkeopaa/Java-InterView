package BlockingQueue;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Description:
 * @Author: wjh
 * @Date: 2025/7/21 16:27
 */


public class MyBlockingQueue<E> {
    private final E[] array;          // 存储元素的数组
    private int head;               // 队首指针
    private int tail;               // 队尾指针
    private int size;               // 当前元素数量
    private final ReentrantLock lock; // 可重入锁
    private final Condition notEmpty; // 非空条件
    private final Condition notFull;  // 非满条件

    @SuppressWarnings("unchecked")
    public MyBlockingQueue(int capacity) {
        array = (E[]) new Object[capacity];
        head = 0;
        tail = 0;
        size = 0;
        lock = new ReentrantLock();
        notEmpty = lock.newCondition();
        notFull = lock.newCondition();
    }

    // 入队操作（阻塞）
    public void enqueue(E element) throws InterruptedException {
        lock.lockInterruptibly();
        try {
            // 队列已满，等待
            while (size == array.length) {
                notFull.await();
            }
            // 入队操作
            array[tail] = element;
            tail = (tail + 1) % array.length; // 循环数组
            size++;
            // 唤醒等待出队的线程
            notEmpty.signal();
        } finally {
            lock.unlock();
        }
    }

    // 出队操作（阻塞）
    public E dequeue() throws InterruptedException {
        lock.lockInterruptibly();
        try {
            // 队列已空，等待
            while (size == 0) {
                notEmpty.await();
            }
            // 出队操作
            E element = array[head];
            head = (head + 1) % array.length; // 循环数组
            size--;
            // 唤醒等待入队的线程
            notFull.signal();
            return element;
        } finally {
            lock.unlock();
        }
    }

    // 获取当前队列大小
    public int size() {
        lock.lock();
        try {
            return size;
        } finally {
            lock.unlock();
        }
    }

    // 测试示例
    public static void main(String[] args) {
        MyBlockingQueue<Integer> queue = new MyBlockingQueue<>(2);

        // 生产者线程
        Thread producer = new Thread(() -> {
            try {
                queue.enqueue(1);
                System.out.println("Produced: 1");
                queue.enqueue(2);
                System.out.println("Produced: 2");
                queue.enqueue(3); // 队列已满，此线程会阻塞
                System.out.println("Produced: 3");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        // 消费者线程
        Thread consumer = new Thread(() -> {
            try {
                Thread.sleep(1000); // 延迟消费
                Integer val = queue.dequeue();
                System.out.println("Consumed: " + val);
                val = queue.dequeue();
                System.out.println("Consumed: " + val);
                val = queue.dequeue(); // 队列已空，此线程会阻塞
                System.out.println("Consumed: " + val);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        producer.start();
        consumer.start();

        try {
            producer.join();
            consumer.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

