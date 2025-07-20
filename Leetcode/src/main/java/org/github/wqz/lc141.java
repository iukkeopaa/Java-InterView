package org.github.wqz;

/**
 * @Description:
 * @Author: wjh
 * @Date: 2025/7/20 15:43
 */
public class lc141 {
    public static class ListNode {
        int val;
        ListNode next;
        ListNode(int x) {
            val = x;
            next = null;
        }
    }
    class Solution {
        public boolean hasCycle(ListNode head) {
            ListNode slow = head, fast = head; // 乌龟和兔子同时从起点出发
            while (fast != null && fast.next != null) {
                slow = slow.next; // 乌龟走一步
                fast = fast.next.next; // 兔子走两步
                if (fast == slow) { // 兔子追上乌龟（套圈），说明有环
                    return true;
                }
            }
            return false; // 访问到了链表末尾，无环
        }
    }


}
