package ProductAndConsumer;

/**
 * @Description:
 * @Author: wjh
 * @Date: 2025/7/19 14:41
 */
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

// �������߳�
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
                queue.put(i); // �Զ���������������
                System.out.println(Thread.currentThread().getName() + " ����: " + i);
                Thread.sleep(100); // ģ��������ʱ
            }
            // ���ͽ�����־
            queue.put(-1); // ����-1Ϊ������־
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

// �������߳�
class BlockingQueueConsumer implements Runnable {
    private final BlockingQueue<Integer> queue;

    public BlockingQueueConsumer(BlockingQueue<Integer> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Integer item = queue.take(); // �Զ�������пյ����

                // ��������־
                if (item == -1) {
                    queue.put(-1); // ���ݽ�����־������������
                    break;
                }

                System.out.println(Thread.currentThread().getName() + " ����: " + item);
                Thread.sleep(200); // ģ�����Ѻ�ʱ
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

// ����
public class ProducerConsumerBlockingQueue {
    public static void main(String[] args) {
        BlockingQueue<Integer> queue = new LinkedBlockingQueue<>(2); // ����Ϊ2����������
        int maxItems = 5; // ����/���ѵ��������

        Thread producerThread = new Thread(new BlockingQueueProducer(queue, maxItems), "������");
        Thread consumerThread = new Thread(new BlockingQueueConsumer(queue), "������");

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