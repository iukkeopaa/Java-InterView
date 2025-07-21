package org.github.wqz;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description:
 * @Author: wjh
 * @Date: 2025/7/21 16:12
 */
public class lc114 {

    public class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
        TreeNode() {}
        TreeNode(int val) { this.val = val; }
        TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }
    }
    //思路1：递归
    public void flatten(TreeNode root) {
        if(root == null){
            return ;
        }
        //将根节点的左子树变成链表
        flatten(root.left);
        //将根节点的右子树变成链表
        flatten(root.right);
        TreeNode temp = root.right;
        //把树的右边换成左边的链表
        root.right = root.left;
        //记得要将左边置空
        root.left = null;
        //找到树的最右边的节点
        while(root.right != null) {
            root = root.right;
        }
        //把右边的链表接到刚才树的最右边的节点
        root.right = temp;
    }

    //思路2: 迭代
    public void flatten2(TreeNode root) {
        while (root != null) {
            //左子树为 null，直接考虑下一个节点
            if (root.left == null) {
                root = root.right;
            } else {
                // 找左子树最右边的节点
                TreeNode pre = root.left;
                while (pre.right != null) {
                    pre = pre.right;
                }
                //将原来的右子树接到左子树的最右边节点
                pre.right = root.right;
                // 将左子树插入到右子树的地方
                root.right = root.left;
                root.left = null;
                // 考虑下一个节点
                root = root.right;
            }
        }
    }


    class Solution {
        public void flatten(TreeNode root) {
            if (root == null) return;

            // 步骤1：前序遍历收集所有节点
            List<TreeNode> preorder = new ArrayList<>();
            traversal(root, preorder);

            // 步骤2：调整指针，构建单链表
            int n = preorder.size();
            for (int i = 1; i < n; i++) {
                TreeNode prev = preorder.get(i - 1);
                TreeNode curr = preorder.get(i);
                prev.left = null;    // 左指针置空
                prev.right = curr;   // 右指针指向下一节点
            }
        }

        // 前序遍历：根 → 左 → 右
        private void traversal(TreeNode node, List<TreeNode> list) {
            if (node == null) return;
            list.add(node);          // 先添加根节点
            traversal(node.left, list);  // 再遍历左子树
            traversal(node.right, list); // 最后遍历右子树
        }
    }

    class Solution2 {
        public void flatten(TreeNode root) {
            if (root == null) return;

            // 递归展开左、右子树
            flatten(root.left);
            flatten(root.right);

            // 保存原右子树
            TreeNode tempRight = root.right;

            // 左子树移到右子树位置，左指针置空
            root.right = root.left;
            root.left = null;

            // 找到当前右子树（原左子树）的最后一个节点
            TreeNode last = root;
            while (last.right != null) {
                last = last.right;
            }

            // 原右子树接到左子树的末尾
            last.right = tempRight;
        }
    }
}
