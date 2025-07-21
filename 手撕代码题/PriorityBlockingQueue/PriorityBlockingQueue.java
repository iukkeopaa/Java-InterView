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

    // ��Ӳ����������ȼ���
    public void enqueue(E element) throws InterruptedException {
        if (element == null) throw new NullPointerException();
        lock.lockInterruptibly();
        try {
            // ������ʱ�ȴ�
            while (size == queue.length) {
                notEmpty.await(); // �˴��򻯣�ʵ��Ӧʹ��notFull����
            }
            insert(element);
            notEmpty.signalAll(); // ���ѵȴ��ĳ����߳�
        } finally {
            lock.unlock();
        }
    }

    // ���Ӳ�������ȡ���ȼ���ߵ�Ԫ�أ�
    public E dequeue() throws InterruptedException {
        lock.lockInterruptibly();
        try {
            // ���п�ʱ�ȴ�
            while (size == 0) {
                notEmpty.await();
            }
            E result = extractMin();
            notEmpty.signalAll(); // ���ѵȴ�������߳�
            return result;
        } finally {
            lock.unlock();
        }
    }

    // ����Ԫ�ص�����
    private void insert(E element) {
        // ���ݼ�飨�򻯰棬ʵ����ʵ�ֶ�̬���ݣ�
        if (size >= queue.length) {
            throw new IllegalStateException("Queue is full");
        }

        // ��Ԫ����ӵ���ĩβ
        int i = size;
        queue[i] = element;
        size++;

        // ���ϵ�����
        while (i > 0) {
            int parent = (i - 1) / 2;
            if (queue[i].compareTo(queue[parent]) >= 0) {
                break;
            }
            swap(i, parent);
            i = parent;
        }
    }

    // ��ȡ�Ѷ�Ԫ�أ���Сֵ��
    private E extractMin() {
        if (size <= 0) {
            return null;
        }

        E min = queue[0];
        E last = queue[size - 1];
        queue[0] = last;
        size--;

        // ���µ�����
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

    // ��������Ԫ��
    private void swap(int i, int j) {
        E temp = queue[i];
        queue[i] = queue[j];
        queue[j] = temp;
    }

    // ��ȡ���д�С
    public int size() {
        lock.lock();
        try {
            return size;
        } finally {
            lock.unlock();
        }
    }

    // ����ʾ��
//    public static void main(String[] args) throws InterruptedException {
//        PriorityBlockingQueue<Integer> queue = new PriorityBlockingQueue<>(10, false);
//
//        // �������̣߳����ȼ���3, 1, 2��
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
//        // �������߳�
//        Thread consumer = new Thread(() -> {
//            try {
//                Thread.sleep(100); // �ȴ����������Ԫ��
//                System.out.println(queue.dequeue()); // ���: 1
//                System.out.println(queue.dequeue()); // ���: 2
//                System.out.println(queue.dequeue()); // ���: 3
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

