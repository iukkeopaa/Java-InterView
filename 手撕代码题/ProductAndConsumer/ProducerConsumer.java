package ProductAndConsumer;

/**
 * @Description:
 * @Author: wjh
 * @Date: 2025/7/19 14:34
 */
import java.util.LinkedList;
import java.util.Queue;

// 共享缓冲区
class Buffer {
    private final Queue<Integer> queue = new LinkedList<>();
    private final int capacity;

    public Buffer(int capacity) {
        this.capacity = capacity;
    }

    // 生产方法
    public synchronized void produce(int item) throws InterruptedException {
        // 缓冲区满时等待
        while (queue.size() == capacity) {
            wait();
        }

        queue.add(item);
        System.out.println(Thread.currentThread().getName() + " 生产: " + item);

        // 通知消费者
        notifyAll();
    }

    // 消费方法
    public synchronized int consume() throws InterruptedException {
        // 缓冲区空时等待
        while (queue.isEmpty()) {
            wait();
        }

        int item = queue.poll();
        System.out.println(Thread.currentThread().getName() + " 消费: " + item);

        // 通知生产者
        notifyAll();
        return item;
    }
}

// 生产者线程
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
                Thread.sleep(100); // 模拟生产耗时
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

// 消费者线程
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
                Thread.sleep(200); // 模拟消费耗时
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

// 主类
public class ProducerConsumer {
    public static void main(String[] args) {
        Buffer buffer = new Buffer(2); // 缓冲区容量为2

        Thread producerThread = new Thread(new Producer(buffer), "生产者");
        Thread consumerThread = new Thread(new Consumer(buffer), "消费者");

        producerThread.start();
        consumerThread.start();

        try {
            producerThread.join();
            consumerThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("主线程结束");
    }
}