package CustomBlockingQueue;

/**
 * @Description:
 * @Author: wjh
 * @Date: 2025/7/19 16:18
 */
import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class CustomBlockingQueue1<E> {
    private final LinkedList<E> queue;
    private final int capacity;
    private final ReentrantLock lock;
    private final Condition notFull;
    private final Condition notEmpty;

    public CustomBlockingQueue1(int capacity) {
        this.capacity = capacity;
        this.queue = new LinkedList<>();
        this.lock = new ReentrantLock();
        this.notFull = lock.newCondition();
        this.notEmpty = lock.newCondition();
    }

    // 入队操作（阻塞）
    public void put(E element) throws InterruptedException {
        lock.lockInterruptibly();
        try {
            // 队列已满时等待
            while (queue.size() == capacity) {
                notFull.await();
            }
            queue.addLast(element);
            // 通知可能在等待的出队线程
            notEmpty.signal();
        } finally {
            lock.unlock();
        }
    }

    // 出队操作（阻塞）
    public E take() throws InterruptedException {
        lock.lockInterruptibly();
        try {
            // 队列空时等待
            while (queue.isEmpty()) {
                notEmpty.await();
            }
            E element = queue.removeFirst();
            // 通知可能在等待的入队线程
            notFull.signal();
            return element;
        } finally {
            lock.unlock();
        }
    }

    // 非阻塞入队（立即返回）
    public boolean offer(E element) {
        lock.lock();
        try {
            if (queue.size() == capacity) {
                return false;
            }
            queue.addLast(element);
            notEmpty.signal();
            return true;
        } finally {
            lock.unlock();
        }
    }

    // 非阻塞出队（立即返回）
    public E poll() {
        lock.lock();
        try {
            if (queue.isEmpty()) {
                return null;
            }
            E element = queue.removeFirst();
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
            return queue.size();
        } finally {
            lock.unlock();
        }
    }

    // 测试示例
    public static void main(String[] args) {
        CustomBlockingQueue1<Integer> queue = new CustomBlockingQueue1<>(5);

        // 生产者线程
        Thread producer = new Thread(() -> {
            try {
                for (int i = 0; i < 10; i++) {
                    queue.put(i);
                    System.out.println("Produced: " + i);
                    Thread.sleep(200);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        // 消费者线程
        Thread consumer = new Thread(() -> {
            try {
                for (int i = 0; i < 10; i++) {
                    Integer element = queue.take();
                    System.out.println("Consumed: " + element);
                    Thread.sleep(300);
                }
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
            Thread.currentThread().interrupt();
        }
    }
}