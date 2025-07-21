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
    private final Node<K, V> head;  // 哨兵头节点
    private final Node<K, V> tail;  // 哨兵尾节点
    private final ReentrantLock lock = new ReentrantLock();

    // 双向链表节点
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

    // 获取缓存项
    public V get(K key) {
        lock.lock();
        try {
            Node<K, V> node = cache.get(key);
            if (node == null) {
                return null;
            }
            // 访问节点后，将其移至链表头部（最近使用）
            moveToHead(node);
            return node.value;
        } finally {
            lock.unlock();
        }
    }

    // 插入/更新缓存项
    public void put(K key, V value) {
        lock.lock();
        try {
            Node<K, V> node = cache.get(key);
            if (node != null) {
                // 键已存在，更新值并移至头部
                node.value = value;
                moveToHead(node);
            } else {
                // 键不存在，创建新节点
                Node<K, V> newNode = new Node<>(key, value);
                cache.put(key, newNode);
                addToHead(newNode);

                // 检查容量，超出则删除尾部节点（最久未使用）
                if (cache.size() > capacity) {
                    Node<K, V> removed = removeTail();
                    cache.remove(removed.key);
                }
            }
        } finally {
            lock.unlock();
        }
    }

    // 删除缓存项
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

    // 将节点移至链表头部
    private void moveToHead(Node<K, V> node) {
        removeNode(node);
        addToHead(node);
    }

    // 添加节点到链表头部
    private void addToHead(Node<K, V> node) {
        node.prev = head;
        node.next = head.next;
        head.next.prev = node;
        head.next = node;
    }

    // 从链表中删除节点
    private void removeNode(Node<K, V> node) {
        node.prev.next = node.next;
        node.next.prev = node.prev;
    }

    // 删除尾部节点并返回
    private Node<K, V> removeTail() {
        Node<K, V> last = tail.prev;
        removeNode(last);
        return last;
    }

    // 测试示例
    public static void main(String[] args) {
        SafeLRU<Integer, String> cache = new SafeLRU<>(2);

        // 线程1：连续put
        Thread t1 = new Thread(() -> {
            cache.put(1, "A");
            cache.put(2, "B");
            System.out.println("t1: put 1,2");
        });

        // 线程2：尝试get
        Thread t2 = new Thread(() -> {
            try {
                Thread.sleep(100); // 等待t1先执行
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("t2: get 1 -> " + cache.get(1)); // 预期A
        });

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 测试LRU淘汰
        cache.put(3, "C"); // 淘汰2
        System.out.println("get 2 -> " + cache.get(2)); // 预期null
    }
}

