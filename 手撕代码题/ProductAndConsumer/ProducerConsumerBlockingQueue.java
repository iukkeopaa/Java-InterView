package ProductAndConsumer;

/**
 * @Description:
 * @Author: wjh
 * @Date: 2025/7/19 14:41
 */
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

// 生产者线程
class BlockingQueueProducer implements Runnable {
    private final BlockingQueue<Integer> queue;
    private final int maxItems;

    public BlockingQueueProducer(BlockingQueue<Integer> queue, int maxItems) {
        this.queue = queue;
        this.maxItems = maxItems;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < maxItems; i++) {
                queue.put(i); // 自动处理队列满的情况
                System.out.println(Thread.currentThread().getName() + " 生产: " + i);
                Thread.sleep(100); // 模拟生产耗时
            }
            // 发送结束标志
            queue.put(-1); // 假设-1为结束标志
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

// 消费者线程
class BlockingQueueConsumer implements Runnable {
    private final BlockingQueue<Integer> queue;

    public BlockingQueueConsumer(BlockingQueue<Integer> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Integer item = queue.take(); // 自动处理队列空的情况

                // 检查结束标志
                if (item == -1) {
                    queue.put(-1); // 传递结束标志给其他消费者
                    break;
                }

                System.out.println(Thread.currentThread().getName() + " 消费: " + item);
                Thread.sleep(200); // 模拟消费耗时
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

// 主类
public class ProducerConsumerBlockingQueue {
    public static void main(String[] args) {
        BlockingQueue<Integer> queue = new LinkedBlockingQueue<>(2); // 容量为2的阻塞队列
        int maxItems = 5; // 生产/消费的最大数量

        Thread producerThread = new Thread(new BlockingQueueProducer(queue, maxItems), "生产者");
        Thread consumerThread = new Thread(new BlockingQueueConsumer(queue), "消费者");

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