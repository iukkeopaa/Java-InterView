package LRU;

import java.util.HashMap;

/**
 * @Description:
 * @Author: wjh
 * @Date: 2025/7/19 14:21
 */
public class WriteLRUCache {

    private static class DlinkedNode {
        int key;
        int value;
        DlinkedNode prev;
        DlinkedNode next;
        public DlinkedNode() {
        }
        public DlinkedNode(int key, int value) {
            this.key = key;
            this.value = value;
        }
    }

    private final int capacity;
    private int size;
    private DlinkedNode head, tail;
    private HashMap<Integer, DlinkedNode> cache = new HashMap<>();
    public WriteLRUCache(int capacity) {
        this.capacity = capacity;
        this.size = 0;
        this.head = new DlinkedNode();
        this.tail = new DlinkedNode();
        this.head.next = this.tail;
        this.tail.prev = this.head;
    }

    private void addNodeToHead(DlinkedNode node){
        node.prev = head;
        node.next = head.next;
        head.next.prev = node;
        head.next = node;
    }

    private void removeNode(DlinkedNode node){
        node.prev.next = node.next;
        node.next.prev = node.prev;
    }
    //�ƶ���ͷ��
    private void moveToHead(DlinkedNode node){
        removeNode(node);
        addNodeToHead(node);
    }

    //ɾ��β���ڵ�
    private DlinkedNode removeTail(){
        DlinkedNode removed = tail.prev;
        removeNode(removed);
        return removed;
    }

    // ��ȡ������
    public int get(int key) {
        DlinkedNode node = cache.get(key);
        if (node == null) {
            return -1;
        }
        moveToHead(node);
        return node.value;
    }

    // ��ӻ�����
    public void put(int key, int value) {
        DlinkedNode node = cache.get(key);
        if (node == null) {
            DlinkedNode newNode = new DlinkedNode(key, value);
            cache.put(key, newNode);
            moveToHead(newNode);
            size++;
            if (size > capacity) {
                DlinkedNode removed = removeTail();
                cache.remove(removed.key);
                size--;
            }

        }
        else {
            node.value = value;
            moveToHead(node);
        }

    }
    //��������
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
