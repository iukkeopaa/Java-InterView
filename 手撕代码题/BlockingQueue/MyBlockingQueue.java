package BlockingQueue;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Description:
 * @Author: wjh
 * @Date: 2025/7/21 16:27
 */


public class MyBlockingQueue<E> {
    private final E[] array;          // �洢Ԫ�ص�����
    private int head;               // ����ָ��
    private int tail;               // ��βָ��
    private int size;               // ��ǰԪ������
    private final ReentrantLock lock; // ��������
    private final Condition notEmpty; // �ǿ�����
    private final Condition notFull;  // ��������

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

    // ��Ӳ�����������
    public void enqueue(E element) throws InterruptedException {
        lock.lockInterruptibly();
        try {
            // �����������ȴ�
            while (size == array.length) {
                notFull.await();
            }
            // ��Ӳ���
            array[tail] = element;
            tail = (tail + 1) % array.length; // ѭ������
            size++;
            // ���ѵȴ����ӵ��߳�
            notEmpty.signal();
        } finally {
            lock.unlock();
        }
    }

    // ���Ӳ�����������
    public E dequeue() throws InterruptedException {
        lock.lockInterruptibly();
        try {
            // �����ѿգ��ȴ�
            while (size == 0) {
                notEmpty.await();
            }
            // ���Ӳ���
            E element = array[head];
            head = (head + 1) % array.length; // ѭ������
            size--;
            // ���ѵȴ���ӵ��߳�
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
            return size;
        } finally {
            lock.unlock();
        }
    }

    // ����ʾ��
    public static void main(String[] args) {
        MyBlockingQueue<Integer> queue = new MyBlockingQueue<>(2);

        // �������߳�
        Thread producer = new Thread(() -> {
            try {
                queue.enqueue(1);
                System.out.println("Produced: 1");
                queue.enqueue(2);
                System.out.println("Produced: 2");
                queue.enqueue(3); // �������������̻߳�����
                System.out.println("Produced: 3");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        // �������߳�
        Thread consumer = new Thread(() -> {
            try {
                Thread.sleep(1000); // �ӳ�����
                Integer val = queue.dequeue();
                System.out.println("Consumed: " + val);
                val = queue.dequeue();
                System.out.println("Consumed: " + val);
                val = queue.dequeue(); // �����ѿգ����̻߳�����
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

