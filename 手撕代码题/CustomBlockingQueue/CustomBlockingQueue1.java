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

    // ��Ӳ�����������
    public void put(E element) throws InterruptedException {
        lock.lockInterruptibly();
        try {
            // ��������ʱ�ȴ�
            while (queue.size() == capacity) {
                notFull.await();
            }
            queue.addLast(element);
            // ֪ͨ�����ڵȴ��ĳ����߳�
            notEmpty.signal();
        } finally {
            lock.unlock();
        }
    }

    // ���Ӳ�����������
    public E take() throws InterruptedException {
        lock.lockInterruptibly();
        try {
            // ���п�ʱ�ȴ�
            while (queue.isEmpty()) {
                notEmpty.await();
            }
            E element = queue.removeFirst();
            // ֪ͨ�����ڵȴ�������߳�
            notFull.signal();
            return element;
        } finally {
            lock.unlock();
        }
    }

    // ��������ӣ��������أ�
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

    // ���������ӣ��������أ�
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

    // ��ȡ��ǰ���д�С
    public int size() {
        lock.lock();
        try {
            return queue.size();
        } finally {
            lock.unlock();
        }
    }

    // ����ʾ��
    public static void main(String[] args) {
        CustomBlockingQueue1<Integer> queue = new CustomBlockingQueue1<>(5);

        // �������߳�
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

        // �������߳�
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