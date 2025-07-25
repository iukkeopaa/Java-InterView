package org.github.wqz.other;

class ListNode {
    int val;
    ListNode next;
    ListNode(int x) { val = x; }
}

public class QuickSortLinkedList {
    public ListNode sortList(ListNode head) {
        if (head == null || head.next == null) return head;

        // 找到尾节点
        ListNode tail = head;
        while (tail.next != null) tail = tail.next;

        // 调用快速排序
        return quickSort(head, tail);
    }

    private ListNode quickSort(ListNode head, ListNode tail) {
        if (head == null || tail == null || head == tail) return head;

        // 分区并获取分区点
        ListNode pivotNode = partition(head, tail);

        // 递归排序左半部分（head到pivotNode前一个节点）
        if (head != pivotNode) {
            ListNode prevPivot = getTail(head, pivotNode);
            prevPivot.next = null; // 断开连接
            ListNode newHead = quickSort(head, prevPivot);
            pivotNode.next = quickSort(pivotNode.next, tail);
            return newHead;
        } else {
            // pivotNode是头节点，直接递归右半部分
            pivotNode.next = quickSort(pivotNode.next, tail);
            return pivotNode;
        }
    }

    private ListNode partition(ListNode head, ListNode tail) {
        int pivot = tail.val;
        ListNode i = new ListNode(0); // 虚拟头节点
        i.next = head;
        ListNode j = head;
        ListNode prev = i;

        while (j != tail) {
            if (j.val <= pivot) {
                prev = prev.next;
                swapValue(prev, j);
            }
            j = j.next;
        }

        // 将pivot放到正确位置
        prev = prev.next;
        swapValue(prev, tail);
        return prev;
    }

    private void swapValue(ListNode a, ListNode b) {
        int temp = a.val;
        a.val = b.val;
        b.val = temp;
    }

    private ListNode getTail(ListNode head, ListNode end) {
        ListNode curr = head;
        while (curr != null && curr.next != end) {
            curr = curr.next;
        }
        return curr;
    }

    // 测试代码
    public static void main(String[] args) {
        ListNode head = new ListNode(3);
        head.next = new ListNode(5);
        head.next.next = new ListNode(2);
        head.next.next.next = new ListNode(1);
        head.next.next.next.next = new ListNode(4);

        QuickSortLinkedList sorter = new QuickSortLinkedList();
        ListNode sorted = sorter.sortList(head);

        // 打印排序后的链表
        ListNode curr = sorted;
        while (curr != null) {
            System.out.print(curr.val + " ");
            curr = curr.next;
        }
    }
}