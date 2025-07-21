package LRU;

import java.util.HashMap;
import java.util.Map;

public class LRUwithTime {

    private class LRUCache {
        // ˫������ڵ���
        private static class DLinkedNode {
            int key;
            int value;
            long expireTime; // ����ʱ���(����)
            DLinkedNode prev;
            DLinkedNode next;

            public DLinkedNode() {}

            public DLinkedNode(int key, int value, long expireTime) {
                this.key = key;
                this.value = value;
                this.expireTime = expireTime;
            }

            // �жϽڵ��Ƿ����
            public boolean isExpired() {
                return expireTime > 0 && System.currentTimeMillis() > expireTime;
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

            // ����Ƿ����
            if (node.isExpired()) {
                removeNode(node);
                cache.remove(key);
                size--;
                return -1;
            }

            // �ƶ���˫������ͷ��
            moveToHead(node);
            return node.value;
        }

        // ��ӻ�����(������ʱ�䣬��λ��)
        public void put(int key, int value, int expireSeconds) {
            long expireTime = expireSeconds > 0 ?
                    System.currentTimeMillis() + expireSeconds * 1000 : 0;

            DLinkedNode node = cache.get(key);
            if (node == null) {
                // �����ڵ�
                DLinkedNode newNode = new DLinkedNode(key, value, expireTime);
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
                // ���½ڵ�ֵ�͹���ʱ�䲢�Ƶ�ͷ��
                node.value = value;
                node.expireTime = expireTime;
                moveToHead(node);
            }
        }

        // ��ӻ�����(�޹���ʱ��)
        public void put(int key, int value) {
            put(key, value, 0); // 0��ʾ��������
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
        public void main(String[] args) throws InterruptedException {
            LRUCache cache = new LRUCache(2);

            // ������ͨLRU����
            cache.put(1, 1);
            cache.put(2, 2);
            System.out.println(cache.get(1)); // ���� 1

            // ���Թ��ڹ���
            cache.put(3, 3, 1); // ����1�����
            System.out.println(cache.get(3)); // ���� 3

            Thread.sleep(1100); // �ȴ�1.1��
            System.out.println(cache.get(3)); // ���� -1 (�ѹ���)

            // ���Թ��ں��Ƿ�Ӱ��LRU˳��
            cache.put(4, 4); // ����Ϊ2����ʱ����ӦΪ {1=1, 4=4}
            System.out.println(cache.get(1)); // ���� 1
            System.out.println(cache.get(2)); // ���� -1 (�ѱ��Ƴ�)
        }
    }
}
