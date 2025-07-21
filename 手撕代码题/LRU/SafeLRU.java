package LRU;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Description:
 * @Author: wjh
 * @Date: 2025/7/21 16:34
 */


public class SafeLRU<K, V> {
    private final int capacity;
    private final Map<K, Node<K, V>> cache;
    private final Node<K, V> head;  // �ڱ�ͷ�ڵ�
    private final Node<K, V> tail;  // �ڱ�β�ڵ�
    private final ReentrantLock lock = new ReentrantLock();

    // ˫������ڵ�
    private static class Node<K, V> {
        K key;
        V value;
        Node<K, V> prev;
        Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    public SafeLRU(int capacity) {
        this.capacity = capacity;
        this.cache = new HashMap<>(capacity);
        this.head = new Node<>(null, null);
        this.tail = new Node<>(null, null);
        head.next = tail;
        tail.prev = head;
    }

    // ��ȡ������
    public V get(K key) {
        lock.lock();
        try {
            Node<K, V> node = cache.get(key);
            if (node == null) {
                return null;
            }
            // ���ʽڵ�󣬽�����������ͷ�������ʹ�ã�
            moveToHead(node);
            return node.value;
        } finally {
            lock.unlock();
        }
    }

    // ����/���»�����
    public void put(K key, V value) {
        lock.lock();
        try {
            Node<K, V> node = cache.get(key);
            if (node != null) {
                // ���Ѵ��ڣ�����ֵ������ͷ��
                node.value = value;
                moveToHead(node);
            } else {
                // �������ڣ������½ڵ�
                Node<K, V> newNode = new Node<>(key, value);
                cache.put(key, newNode);
                addToHead(newNode);

                // ���������������ɾ��β���ڵ㣨���δʹ�ã�
                if (cache.size() > capacity) {
                    Node<K, V> removed = removeTail();
                    cache.remove(removed.key);
                }
            }
        } finally {
            lock.unlock();
        }
    }

    // ɾ��������
    public void remove(K key) {
        lock.lock();
        try {
            Node<K, V> node = cache.get(key);
            if (node != null) {
                removeNode(node);
                cache.remove(key);
            }
        } finally {
            lock.unlock();
        }
    }

    // ���ڵ���������ͷ��
    private void moveToHead(Node<K, V> node) {
        removeNode(node);
        addToHead(node);
    }

    // ��ӽڵ㵽����ͷ��
    private void addToHead(Node<K, V> node) {
        node.prev = head;
        node.next = head.next;
        head.next.prev = node;
        head.next = node;
    }

    // ��������ɾ���ڵ�
    private void removeNode(Node<K, V> node) {
        node.prev.next = node.next;
        node.next.prev = node.prev;
    }

    // ɾ��β���ڵ㲢����
    private Node<K, V> removeTail() {
        Node<K, V> last = tail.prev;
        removeNode(last);
        return last;
    }

    // ����ʾ��
    public static void main(String[] args) {
        SafeLRU<Integer, String> cache = new SafeLRU<>(2);

        // �߳�1������put
        Thread t1 = new Thread(() -> {
            cache.put(1, "A");
            cache.put(2, "B");
            System.out.println("t1: put 1,2");
        });

        // �߳�2������get
        Thread t2 = new Thread(() -> {
            try {
                Thread.sleep(100); // �ȴ�t1��ִ��
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("t2: get 1 -> " + cache.get(1)); // Ԥ��A
        });

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // ����LRU��̭
        cache.put(3, "C"); // ��̭2
        System.out.println("get 2 -> " + cache.get(2)); // Ԥ��null
    }
}

