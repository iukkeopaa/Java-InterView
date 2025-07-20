package org.github.wqz;

/**
 * @Description:
 * @Author: wjh
 * @Date: 2025/7/20 15:51
 */
public class lc92 {
    public static class ListNode {
        int val;
        ListNode next;
        ListNode() {}
        ListNode(int val) { this.val = val; }

    }

    class Solution {
        public ListNode reverseBetween(ListNode head, int m, int n) {
            // 定义一个dummyHead, 方便处理
            ListNode dummyHead = new ListNode(0);
            dummyHead.next = head;

            // 初始化指针
            ListNode g = dummyHead;
            ListNode p = dummyHead.next;

            // 将指针移到相应的位置
            for(int step = 0; step < m - 1; step++) {
                g = g.next; p = p.next;
            }

            // 头插法插入节点
            for (int i = 0; i < n - m; i++) {
                ListNode removed = p.next;
                p.next = p.next.next;

                removed.next = g.next;
                g.next = removed;
            }

            return dummyHead.next;
        }
    }

    class Solution123 {
        public ListNode reverseBetween(ListNode head, int l, int r) {
            ListNode dummy = new ListNode(0);
            dummy.next = head;

            r -= l;
            ListNode hh = dummy;
            while (l-- > 1) hh = hh.next;

            ListNode a = hh.next, b = a.next;
            while (r-- > 0) {
                ListNode tmp = b.next;
                b.next = a;
                a = b;
                b = tmp;
            }

            hh.next.next = b;
            hh.next = a;
            return dummy.next;
        }
    }

    public ListNode reverseBetween(ListNode head, int m, int n) {
        ListNode res = new ListNode(0);
        res.next = head;
        ListNode node = res;
        //找到需要反转的那一段的上一个节点。
        for (int i = 1; i < m; i++) {
            node = node.next;
        }
        //node.next就是需要反转的这段的起点。
        ListNode nextHead = node.next;
        ListNode next = null;
        ListNode pre = null;
        //反转m到n这一段
        for (int i = m; i <= n; i++) {
            next = nextHead.next;
            nextHead.next = pre;
            pre = nextHead;
            nextHead = next;
        }
        //将反转的起点的next指向next。
        node.next.next = next;
        //需要反转的那一段的上一个节点的next节点指向反转后链表的头结点
        node.next = pre;
        return res.next;
    }




    public class Solution3211 {
        public ListNode reverseBetween(ListNode head, int left, int right) {
            // 创建虚拟头节点，简化边界处理
            ListNode dummy = new ListNode(0);
            dummy.next = head;
            ListNode pre = dummy;

            // 移动pre到left-1位置
            for (int i = 0; i < left - 1; i++) {
                pre = pre.next;
            }

            // 当前节点cur为pre的下一个节点（即left位置）
            ListNode cur = pre.next;

            // 反转right-left次
            for (int i = 0; i < right - left; i++) {
                ListNode nextNode = cur.next;  // 保存下一个节点
                cur.next = nextNode.next;      // cur连接到nextNode的下一个节点
                nextNode.next = pre.next;      // nextNode插入到pre之后
                pre.next = nextNode;           // pre连接到nextNode
            }

            return dummy.next;
        }
    }



}
