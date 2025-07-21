package org.github.wqz;

import java.util.HashMap;
import java.util.Map;

public class lc138 {

    class Node {
        int val;
        Node next;
        Node random;
        public Node(int val) {
            this.val = val;
            this.next = null;
            this.random = null;
        }
    }

    public class Solution {
        public Node copyRandomList(Node head) {
            if (head == null) return null;

            // 哈希表：原节点 → 新节点
            Map<Node, Node> map = new HashMap<>();

            // 第一次遍历：创建新节点并建立映射
            Node curr = head;
            while (curr != null) {
                map.put(curr, new Node(curr.val));
                curr = curr.next;
            }

            // 第二次遍历：设置新节点的 next 和 random 指针
            curr = head;
            while (curr != null) {
                Node newNode = map.get(curr);
                newNode.next = map.get(curr.next);      // 新节点的 next 指向原节点 next 对应的新节点
                newNode.random = map.get(curr.random);  // 新节点的 random 指向原节点 random 对应的新节点
                curr = curr.next;
            }

            return map.get(head);  // 返回新链表的头节点
        }
    }

    public class Solution2 {
        public Node copyRandomList(Node head) {
            if (head == null) return null;

            // 第一次遍历：插入复制节点
            Node curr = head;
            while (curr != null) {
                Node newNode = new Node(curr.val);
                newNode.next = curr.next;
                curr.next = newNode;
                curr = newNode.next;
            }

            // 第二次遍历：设置复制节点的 random 指针
            curr = head;
            while (curr != null) {
                if (curr.random != null) {
                    curr.next.random = curr.random.next;  // 新节点的 random 指向原节点 random 的下一个节点（即复制节点）
                }
                curr = curr.next.next;
            }

            // 第三次遍历：拆分链表
            Node newHead = head.next;
            Node newCurr = newHead;
            curr = head;

            while (curr != null) {
                curr.next = newCurr.next;  // 恢复原链表
                curr = curr.next;

                if (curr != null) {
                    newCurr.next = curr.next;  // 构建新链表
                    newCurr = newCurr.next;
                }
            }

            return newHead;
        }
    }
}
