package PriorityBlockingQueue;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Description:
 * @Author: wjh
 * @Date: 2025/7/21 17:20
 */


public class PriorityBlockingQueue<E extends Comparable<? super E>> {
    private final E[] queue;
    private int size;
    private final ReentrantLock lock;
    private final Condition notEmpty;

    @SuppressWarnings("unchecked")
    public PriorityBlockingQueue(int capacity, boolean fair) {
        this.queue = (E[]) new Comparable[capacity];
        this.size = 0;
        this.lock = new ReentrantLock(fair);
        this.notEmpty = lock.newCondition();
    }

    // 入队操作（带优先级）
    public void enqueue(E element) throws InterruptedException {
        if (element == null) throw new NullPointerException();
        lock.lockInterruptibly();
        try {
            // 队列满时等待
            while (size == queue.length) {
                notEmpty.await(); // 此处简化，实际应使用notFull条件
            }
            insert(element);
            notEmpty.signalAll(); // 唤醒等待的出队线程
        } finally {
            lock.unlock();
        }
    }

    // 出队操作（获取优先级最高的元素）
    public E dequeue() throws InterruptedException {
        lock.lockInterruptibly();
        try {
            // 队列空时等待
            while (size == 0) {
                notEmpty.await();
            }
            E result = extractMin();
            notEmpty.signalAll(); // 唤醒等待的入队线程
            return result;
        } finally {
            lock.unlock();
        }
    }

    // 插入元素到堆中
    private void insert(E element) {
        // 扩容检查（简化版，实际需实现动态扩容）
        if (size >= queue.length) {
            throw new IllegalStateException("Queue is full");
        }

        // 将元素添加到堆末尾
        int i = size;
        queue[i] = element;
        size++;

        // 向上调整堆
        while (i > 0) {
            int parent = (i - 1) / 2;
            if (queue[i].compareTo(queue[parent]) >= 0) {
                break;
            }
            swap(i, parent);
            i = parent;
        }
    }

    // 提取堆顶元素（最小值）
    private E extractMin() {
        if (size <= 0) {
            return null;
        }

        E min = queue[0];
        E last = queue[size - 1];
        queue[0] = last;
        size--;

        // 向下调整堆
        int i = 0;
        while (true) {
            int left = 2 * i + 1;
            int right = 2 * i + 2;
            int smallest = i;

            if (left < size && queue[left].compareTo(queue[smallest]) < 0) {
                smallest = left;
            }

            if (right < size && queue[right].compareTo(queue[smallest]) < 0) {
                smallest = right;
            }

            if (smallest == i) {
                break;
            }

            swap(i, smallest);
            i = smallest;
        }

        return min;
    }

    // 交换数组元素
    private void swap(int i, int j) {
        E temp = queue[i];
        queue[i] = queue[j];
        queue[j] = temp;
    }

    // 获取队列大小
    public int size() {
        lock.lock();
        try {
            return size;
        } finally {
            lock.unlock();
        }
    }

    // 测试示例
//    public static void main(String[] args) throws InterruptedException {
//        PriorityBlockingQueue<Integer> queue = new PriorityBlockingQueue<>(10, false);
//
//        // 生产者线程（优先级：3, 1, 2）
//        Thread producer = new Thread(() -> {
//            try {
//                queue.enqueue(3);
//                queue.enqueue(1);
//                queue.enqueue(2);
//            } catch (InterruptedException e) {
//                Thread.currentThread().interrupt();
//            }
//        });
//
//        // 消费者线程
//        Thread consumer = new Thread(() -> {
//            try {
//                Thread.sleep(100); // 等待生产者添加元素
//                System.out.println(queue.dequeue()); // 输出: 1
//                System.out.println(queue.dequeue()); // 输出: 2
//                System.out.println(queue.dequeue()); // 输出: 3
//            } catch (InterruptedException e) {
//                Thread.currentThread().interrupt();
//            }
//        });
//
//        producer.start();
//        consumer.start();
//        producer.join();
//        consumer.join();
//    }
}

