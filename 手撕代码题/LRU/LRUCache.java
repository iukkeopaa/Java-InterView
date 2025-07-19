package LRU;/**
* @Description: 
* @Author: wjh
* @Date: 2025/7/19 14:21
*/
import java.util.HashMap;
import java.util.Map;

public class LRUCache {
    // 双向链表节点类
    private static class DLinkedNode {
        int key;
        int value;
        DLinkedNode prev;
        DLinkedNode next;
        public DLinkedNode() {}
        public DLinkedNode(int key, int value) {
            this.key = key;
            this.value = value;
        }
    }

    private final int capacity;
    private int size;
    private final Map<Integer, DLinkedNode> cache = new HashMap<>();
    private final DLinkedNode head;
    private final DLinkedNode tail;

    public LRUCache(int capacity) {
        this.capacity = capacity;
        this.size = 0;
        head = new DLinkedNode();
        tail = new DLinkedNode();
        head.next = tail;
        tail.prev = head;
    }

    // 获取缓存项
    public int get(int key) {
        DLinkedNode node = cache.get(key);
        if (node == null) {
            return -1;
        }
        // 移动到双向链表头部
        moveToHead(node);
        return node.value;
    }

    // 添加缓存项
    public void put(int key, int value) {
        DLinkedNode node = cache.get(key);
        if (node == null) {
            // 新增节点
            DLinkedNode newNode = new DLinkedNode(key, value);
            cache.put(key, newNode);
            addToHead(newNode);
            size++;
            if (size > capacity) {
                // 超出容量，移除尾部节点
                DLinkedNode removed = removeTail();
                cache.remove(removed.key);
                size--;
            }
        } else {
            // 更新节点值并移到头部
            node.value = value;
            moveToHead(node);
        }
    }

    // 添加节点到头部
    private void addToHead(DLinkedNode node) {
        node.prev = head;
        node.next = head.next;
        head.next.prev = node;
        head.next = node;
    }

    // 移除节点
    private void removeNode(DLinkedNode node) {
        node.prev.next = node.next;
        node.next.prev = node.prev;
    }

    // 移动节点到头部
    private void moveToHead(DLinkedNode node) {
        removeNode(node);
        addToHead(node);
    }

    // 移除尾部节点
    private DLinkedNode removeTail() {
        DLinkedNode removed = tail.prev;
        removeNode(removed);
        return removed;
    }

    // 测试示例
    public static void main(String[] args) {
        LRUCache cache = new LRUCache(2);
        cache.put(1, 1);
        cache.put(2, 2);
        System.out.println(cache.get(1)); // 返回 1
        cache.put(3, 3); // 该操作会使得关键字 2 作废
        System.out.println(cache.get(2)); // 返回 -1 (未找到)
        cache.put(4, 4); // 该操作会使得关键字 1 作废
        System.out.println(cache.get(1)); // 返回 -1 (未找到)
        System.out.println(cache.get(3)); // 返回 3
        System.out.println(cache.get(4)); // 返回 4
    }
}
