package LRU;/**
* @Description: 
* @Author: wjh
* @Date: 2025/7/19 14:21
*/
import java.util.HashMap;
import java.util.Map;

public class LRUCache {
    // ˫������ڵ���
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

    // ��ȡ������
    public int get(int key) {
        DLinkedNode node = cache.get(key);
        if (node == null) {
            return -1;
        }
        // �ƶ���˫������ͷ��
        moveToHead(node);
        return node.value;
    }

    // ��ӻ�����
    public void put(int key, int value) {
        DLinkedNode node = cache.get(key);
        if (node == null) {
            // �����ڵ�
            DLinkedNode newNode = new DLinkedNode(key, value);
            cache.put(key, newNode);
            addToHead(newNode);
            size++;
            if (size > capacity) {
                // �����������Ƴ�β���ڵ�
                DLinkedNode removed = removeTail();
                cache.remove(removed.key);
                size--;
            }
        } else {
            // ���½ڵ�ֵ���Ƶ�ͷ��
            node.value = value;
            moveToHead(node);
        }
    }

    // ��ӽڵ㵽ͷ��
    private void addToHead(DLinkedNode node) {
        node.prev = head;
        node.next = head.next;
        head.next.prev = node;
        head.next = node;
    }

    // �Ƴ��ڵ�
    private void removeNode(DLinkedNode node) {
        node.prev.next = node.next;
        node.next.prev = node.prev;
    }

    // �ƶ��ڵ㵽ͷ��
    private void moveToHead(DLinkedNode node) {
        removeNode(node);
        addToHead(node);
    }

    // �Ƴ�β���ڵ�
    private DLinkedNode removeTail() {
        DLinkedNode removed = tail.prev;
        removeNode(removed);
        return removed;
    }

    // ����ʾ��
    public static void main(String[] args) {
        LRUCache cache = new LRUCache(2);
        cache.put(1, 1);
        cache.put(2, 2);
        System.out.println(cache.get(1)); // ���� 1
        cache.put(3, 3); // �ò�����ʹ�ùؼ��� 2 ����
        System.out.println(cache.get(2)); // ���� -1 (δ�ҵ�)
        cache.put(4, 4); // �ò�����ʹ�ùؼ��� 1 ����
        System.out.println(cache.get(1)); // ���� -1 (δ�ҵ�)
        System.out.println(cache.get(3)); // ���� 3
        System.out.println(cache.get(4)); // ���� 4
    }
}
