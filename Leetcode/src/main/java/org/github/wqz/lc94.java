package org.github.wqz;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class lc94 {

    class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int val) {
            this.val = val;
        }
    }



    class Solution {
        public List<Integer> inorderTraversal(TreeNode root) {
            List<Integer> result = new ArrayList<>();
            inorder(root, result);
            return result;
        }

        private void inorder(TreeNode node, List<Integer> result) {
            if (node == null) return;
            inorder(node.left, result);  // 递归遍历左子树
            result.add(node.val);        // 访问根节点
            inorder(node.right, result); // 递归遍历右子树
        }
    }


    class Solution2 {
        public List<Integer> inorderTraversal(TreeNode root) {
            List<Integer> result = new ArrayList<>();
            Stack<TreeNode> stack = new Stack<>();
            TreeNode curr = root;

            while (curr != null || !stack.isEmpty()) {
                // 遍历所有左节点并压入栈
                while (curr != null) {
                    stack.push(curr);
                    curr = curr.left;
                }

                // 弹出栈顶节点并访问
                curr = stack.pop();
                result.add(curr.val);

                // 转向右子树
                curr = curr.right;
            }

            return result;
        }
    }
}
