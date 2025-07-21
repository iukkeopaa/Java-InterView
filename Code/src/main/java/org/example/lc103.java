package org.example;

import javax.swing.tree.TreeNode;
import java.util.*;

public class lc103 {

    private static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        public TreeNode(int x) {
            val = x;
        }
    }

    private static List<List<Integer>> zigzagLevelorder(TreeNode root){
        Queue<TreeNode> queue = new LinkedList<>();
        List<List<Integer>> res = new LinkedList<>();
        if(root == null){
            return res;
        }
        queue.offer(root);
        boolean isOrderLeft = true;
        while(!queue.isEmpty()){
            int size = queue.size();
            LinkedList<Integer> level = new LinkedList<>();
            for(int i = 0;i<size;i++){
                TreeNode node = queue.poll();
                if(isOrderLeft){
                    level.offerLast(node.val);
                }else{
                    level.offerFirst(node.val);
                }
                if(node.left != null){
                    queue.offer(node.left);
                }
                if(node.right != null){
                    queue.offer(node.right);
                }
            }
            res.add(level);
            isOrderLeft = !isOrderLeft;
        }
        return res;
    }

    private class Solution {
        public List<List<Integer>> zigzagLevelOrder(lc103.TreeNode root) {
            Queue<lc103.TreeNode> queue = new LinkedList<>();
            List<List<Integer>> res = new ArrayList<>();
            if (root != null) queue.add(root);
            while (!queue.isEmpty()) {
                LinkedList<Integer> tmp = new LinkedList<>();
                for(int i = queue.size(); i > 0; i--) {
                    lc103.TreeNode node = queue.poll();
                    if (res.size() % 2 == 0) tmp.addLast(node.val);
                    else tmp.addFirst(node.val);
                    if (node.left != null) queue.add(node.left);
                    if (node.right != null) queue.add(node.right);
                }
                res.add(tmp);
            }
            return res;
        }
    }

    private class Solution1 {
        public List<List<Integer>> zigzagLevelOrder(lc103.TreeNode root) {
            Deque<lc103.TreeNode> deque = new LinkedList<>();
            List<List<Integer>> res = new ArrayList<>();
            if (root != null) deque.add(root);
            while (!deque.isEmpty()) {
                // 打印奇数层
                List<Integer> tmp = new ArrayList<>();
                for(int i = deque.size(); i > 0; i--) {
                    // 从左向右打印
                    lc103.TreeNode node = deque.removeFirst();
                    tmp.add(node.val);
                    // 先左后右加入下层节点
                    if (node.left != null) deque.addLast(node.left);
                    if (node.right != null) deque.addLast(node.right);
                }
                res.add(tmp);
                if (deque.isEmpty()) break; // 若为空则提前跳出
                // 打印偶数层
                tmp = new ArrayList<>();
                for(int i = deque.size(); i > 0; i--) {
                    // 从右向左打印
                    lc103.TreeNode node = deque.removeLast();
                    tmp.add(node.val);
                    // 先右后左加入下层节点
                    if (node.right != null) deque.addFirst(node.right);
                    if (node.left != null) deque.addFirst(node.left);
                }
                res.add(tmp);
            }
            return res;
        }
    }





    public static void main(String[] args) {
        lc103.TreeNode root = new lc103.TreeNode(3);
        root.left = new lc103.TreeNode(9);
        root.right = new lc103.TreeNode(20);
        root.right.left = new lc103.TreeNode(15);
        root.right.right = new lc103.TreeNode(7);
        System.out.println(zigzagLevelorder(root));

    }
}
