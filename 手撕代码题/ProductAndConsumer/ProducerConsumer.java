package ProductAndConsumer;

/**
 * @Description:
 * @Author: wjh
 * @Date: 2025/7/19 14:34
 */
import java.util.LinkedList;
import java.util.Queue;

// ��������
class Buffer {
    private final Queue<Integer> queue = new LinkedList<>();
    private final int capacity;

    public Buffer(int capacity) {
        this.capacity = capacity;
    }

    // ��������
    public synchronized void produce(int item) throws InterruptedException {
        // ��������ʱ�ȴ�
        while (queue.size() == capacity) {
            wait();
        }

        queue.add(item);
        System.out.println(Thread.currentThread().getName() + " ����: " + item);

        // ֪ͨ������
        notifyAll();
    }

    // ���ѷ���
    public synchronized int consume() throws InterruptedException {
        // ��������ʱ�ȴ�
        while (queue.isEmpty()) {
            wait();
        }

        int item = queue.poll();
        System.out.println(Thread.currentThread().getName() + " ����: " + item);

        // ֪ͨ������
        notifyAll();
        return item;
    }
}

// �������߳�
class Producer implements Runnable {
    private final Buffer buffer;

    public Producer(Buffer buffer) {
        this.buffer = buffer;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < 5; i++) {
                buffer.produce(i);
                Thread.sleep(100); // ģ��������ʱ
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

// �������߳�
class Consumer implements Runnable {
    private final Buffer buffer;

    public Consumer(Buffer buffer) {
        this.buffer = buffer;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < 5; i++) {
                buffer.consume();
                Thread.sleep(200); // ģ�����Ѻ�ʱ
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

// ����
public class ProducerConsumer {
    public static void main(String[] args) {
        Buffer buffer = new Buffer(2); // ����������Ϊ2

        Thread producerThread = new Thread(new Producer(buffer), "������");
        Thread consumerThread = new Thread(new Consumer(buffer), "������");

        producerThread.start();
        consumerThread.start();

        try {
            producerThread.join();
            consumerThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("���߳̽���");
    }
}