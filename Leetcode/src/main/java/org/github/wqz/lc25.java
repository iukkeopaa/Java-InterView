package org.github.wqz;

import java.util.List;

/**
 * @Description: k个一组反转  参考官方解
 * @Author: wjh
 * @Date: 2025/7/19 17:31
 */
public class lc25 {

    //链表节点类定义
    private static class ListNode {
        int val;
        ListNode next;
        ListNode() {}
        ListNode(int val, ListNode next){
            this.val = val;
            this.next = next;
        }

        public ListNode(int i) {
            this.val = i;
        }
    }

    //反转链表
    private static ListNode reverse(ListNode head){
        ListNode pre = null;
        ListNode cur = head;

        while(cur != null){
            ListNode next = cur.next;
            cur.next = pre;
            pre = cur;
            cur = next;
        }
        return pre;
    }

    /**
     * Definition for singly-linked list.
     * public class ListNode {
     *     int val;
     *     ListNode next;
     *     ListNode(int x) { val = x; }
     * }
     */
    private static class Solution {
        public ListNode reverseKGroup(ListNode head, int k) {
            if (head == null || head.next == null){
                return head;
            }
            //定义一个假的节点。
            ListNode dummy=new ListNode(0);
            //假节点的next指向head。
            // dummy->1->2->3->4->5
            dummy.next=head;
            //初始化pre和end都指向dummy。pre指每次要翻转的链表的头结点的上一个节点。end指每次要翻转的链表的尾节点
            ListNode pre=dummy;
            ListNode end=dummy;

            while(end.next!=null){
                //循环k次，找到需要翻转的链表的结尾,这里每次循环要判断end是否等于空,因为如果为空，end.next会报空指针异常。
                //dummy->1->2->3->4->5 若k为2，循环2次，end指向2
                for(int i=0;i<k&&end != null;i++){
                    end=end.next;
                }
                //如果end==null，即需要翻转的链表的节点数小于k，不执行翻转。
                if(end==null){
                    break;
                }
                //先记录下end.next,方便后面链接链表
                ListNode next=end.next;
                //然后断开链表
                end.next=null;
                //记录下要翻转链表的头节点
                ListNode start=pre.next;
                //翻转链表,pre.next指向翻转后的链表。1->2 变成2->1。 dummy->2->1
                pre.next=reverse(start);
                //翻转后头节点变到最后。通过.next把断开的链表重新链接。
                start.next=next;
                //将pre换成下次要翻转的链表的头结点的上一个节点。即start
                pre=start;
                //翻转结束，将end置为下次要翻转的链表的头结点的上一个节点。即start
                end=start;
            }
            return dummy.next;


        }
        //链表翻转
        // 例子：   head： 1->2->3->4
        public ListNode reverse(ListNode head) {
            //单链表为空或只有一个节点，直接返回原单链表
            if (head == null || head.next == null){
                return head;
            }
            //前一个节点指针
            ListNode preNode = null;
            //当前节点指针
            ListNode curNode = head;
            //下一个节点指针
            ListNode nextNode = null;
            while (curNode != null){
                nextNode = curNode.next;//nextNode 指向下一个节点,保存当前节点后面的链表。
                curNode.next=preNode;//将当前节点next域指向前一个节点   null<-1<-2<-3<-4
                preNode = curNode;//preNode 指针向后移动。preNode指向当前节点。
                curNode = nextNode;//curNode指针向后移动。下一个节点变成当前节点
            }
            return preNode;

        }


    }
    public static void main(String[] args) {
        System.out.println("Hello World!");
    }
}