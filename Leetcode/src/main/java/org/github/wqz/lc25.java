package org.example;

import java.util.Scanner;

public class lc25 {
    public static class ListNode {
        int val;
        ListNode next;

        ListNode() {
        }

        ListNode(int val) {
            this.val = val;
        }

        ListNode(int val, ListNode next) {
            this.val = val;
            this.next = next;
        }
    }

    private static class Solution {
        public ListNode reverseKGroup(ListNode head, int k) {
            ListNode hair = new ListNode(0);
            hair.next = head;
            ListNode pre = hair;

            while (head != null) {
                ListNode tail = pre;
                // 查看剩余部分长度是否大于等于 k
                for (int i = 0; i < k; ++i) {
                    tail = tail.next;
                    if (tail == null) {
                        return hair.next;
                    }
                }
                ListNode nex = tail.next;
                ListNode[] reverse = myReverse(head, tail);
                head = reverse[0];
                tail = reverse[1];
                // 把子链表重新接回原链表
                pre.next = head;
                tail.next = nex;
                pre = tail;
                head = tail.next;
            }

            return hair.next;
        }

        public ListNode[] myReverse(ListNode head, ListNode tail) {
            ListNode prev = tail.next;
            ListNode p = head;
            while (prev != tail) {
                ListNode nex = p.next;
                p.next = prev;
                prev = p;
                p = nex;
            }
            return new ListNode[]{tail, head};
        }
    }


//    public static void main(String[] args) {
//        Solution solution = new Solution();
//        ListNode head = new ListNode(1);
//        head.next = new ListNode(2);
//        head.next.next = new ListNode(3);
//        head.next.next.next = new ListNode(4);
//        head.next.next.next.next = new ListNode(5);
//        int k = 3;
//        ListNode listNode = solution.reverseKGroup(head, k);
//        while (listNode != null) {
//            System.out.println(listNode.val);
//            listNode = listNode.next;
//        }
//
//    }
public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);

    System.out.println("请输入链表元素，使用空格分隔：");
    String[] inputs = scanner.nextLine().split(" ");

    System.out.println("请输入k值：");
    int k = scanner.nextInt();
    scanner.nextLine(); // 消耗掉换行符

    // 构建链表
    ListNode dummy = new ListNode(0);
    ListNode current = dummy;
    for (String input : inputs) {
        current.next = new ListNode(Integer.parseInt(input));
        current = current.next;
    }
    ListNode head = dummy.next;

    Solution solution = new Solution();
    ListNode result = solution.reverseKGroup(head, k);

    // 输出结果
    System.out.println("反转后的链表：");
    while (result != null) {
        System.out.print(result.val + " ");
        result = result.next;
    }
    System.out.println();

    scanner.close();
}

}