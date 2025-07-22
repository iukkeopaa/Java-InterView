package LRU;

import java.util.HashMap;
import java.util.Map;

public class LRUwithTime {

    private class LRUCache {
        // 双向链表节点类
        private static class DLinkedNode {
            int key;
            int value;
            long expireTime; // 过期时间戳(毫秒)
            DLinkedNode prev;
            DLinkedNode next;

            public DLinkedNode() {}

            public DLinkedNode(int key, int value, long expireTime) {
                this.key = key;
                this.value = value;
                this.expireTime = expireTime;
            }

            // 判断节点是否过期
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

        // 获取缓存项
        public int get(int key) {
            DLinkedNode node = cache.get(key);
            if (node == null) {
                return -1;
            }

            // 检查是否过期
            if (node.isExpired()) {
                removeNode(node);
                cache.remove(key);
                size--;
                return -1;
            }

            // 移动到双向链表头部
            moveToHead(node);
            return node.value;
        }

        // 添加缓存项(带过期时间，单位秒)
        public void put(int key, int value, int expireSeconds) {
            long expireTime = expireSeconds > 0 ?
                    System.currentTimeMillis() + expireSeconds * 1000 : 0;

            DLinkedNode node = cache.get(key);
            if (node == null) {
                // 新增节点
                DLinkedNode newNode = new DLinkedNode(key, value, expireTime);
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
                // 更新节点值和过期时间并移到头部
                node.value = value;
                node.expireTime = expireTime;
                moveToHead(node);
            }
        }

        // 添加缓存项(无过期时间)
        public void put(int key, int value) {
            put(key, value, 0); // 0表示永不过期
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
        public void main(String[] args) throws InterruptedException {
            LRUCache cache = new LRUCache(2);

            // 测试普通LRU功能
            cache.put(1, 1);
            cache.put(2, 2);
            System.out.println(cache.get(1)); // 返回 1

            // 测试过期功能
            cache.put(3, 3, 1); // 设置1秒过期
            System.out.println(cache.get(3)); // 返回 3

            Thread.sleep(1100); // 等待1.1秒
            System.out.println(cache.get(3)); // 返回 -1 (已过期)

            // 测试过期后是否影响LRU顺序
            cache.put(4, 4); // 容量为2，此时缓存应为 {1=1, 4=4}
            System.out.println(cache.get(1)); // 返回 1
            System.out.println(cache.get(2)); // 返回 -1 (已被移除)
        }
    }
}
