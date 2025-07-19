package ProductAndConsumer;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * @Description:
 * @Author: wjh
 * @Date: 2025/7/19 14:43
 */

class Prducter implements Runnable{

    private final BlockingQueue<Integer> queue;
    public Prducter(BlockingQueue<Integer> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < 10; i++) {
              produce(i);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

    }

    private void produce(int i) throws InterruptedException {
        queue.put(i);
        System.out.println("Produced: " + i +",���д�С"+ queue.size());
    }
}


class Consumer123 implements Runnable {

    private final BlockingQueue<Integer> queue;
    public Consumer123(BlockingQueue<Integer> queue) {
        this.queue = queue;
    }
    @Override
    public void run() {
        try {
            consume();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    private void consume() throws InterruptedException {
        while (true) {
            Integer item = queue.take();
            System.out.println(Thread.currentThread().getName() + " ����: " + item);
        }
    }

}
public class GuidePC {

    public static void main(String[] args) {
        BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(5);
        // ���������ߺ�������
        Prducter producer = new Prducter(queue);
        Consumer123 consumer = new Consumer123(queue);
        Thread producerThread = new Thread(producer);
        Thread consumerThread = new Thread(consumer);
         // �������ɲ��ߺ��������߳�
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
