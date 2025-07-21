package org.github.wqz;

import java.util.PriorityQueue;

/**
 * @Description:
 * @Author: wjh
 * @Date: 2025/7/21 16:06
 */
public class lc23 {


    public class Solution {
        public ListNode mergeKLists(ListNode[] lists) {
            if (lists == null || lists.length == 0) return null;
            return merge(lists, 0, lists.length - 1);
        }

        private ListNode merge(ListNode[] lists, int left, int right) {
            if (left == right) return lists[left];
            int mid = left + (right - left) / 2;
            ListNode l1 = merge(lists, left, mid);
            ListNode l2 = merge(lists, mid + 1, right);
            return mergeTwoLists(l1, l2);
        }

        private ListNode mergeTwoLists(ListNode l1, ListNode l2) {
            ListNode dummy = new ListNode(0);
            ListNode tail = dummy;

            while (l1 != null && l2 != null) {
                if (l1.val < l2.val) {
                    tail.next = l1;
                    l1 = l1.next;
                } else {
                    tail.next = l2;
                    l2 = l2.next;
                }
                tail = tail.next;
            }

            tail.next = (l1 != null) ? l1 : l2;
            return dummy.next;
        }
    }



    public class Solution2 {
        public ListNode mergeKLists(ListNode[] lists) {
            if (lists == null || lists.length == 0) return null;

            PriorityQueue<ListNode> minHeap = new PriorityQueue<>((a, b) -> a.val - b.val);

            // 将每个链表的头节点加入堆
            for (ListNode list : lists) {
                if (list != null) {
                    minHeap.offer(list);
                }
            }

            ListNode dummy = new ListNode(0);
            ListNode tail = dummy;

            while (!minHeap.isEmpty()) {
                ListNode minNode = minHeap.poll();
                tail.next = minNode;
                tail = tail.next;

                // 将取出节点的后继节点加入堆
                if (minNode.next != null) {
                    minHeap.offer(minNode.next);
                }
            }

            return dummy.next;
        }
    }
}
