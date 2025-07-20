package org.github.wqz;

import java.util.LinkedList;
import java.util.Queue;

/**
 * @Description:
 * @Author: wjh
 * @Date: 2025/7/20 16:47
 */
public class lc101 {
    class Solution {
        public boolean isSymmetric(TreeNode root) {
            if(root==null) {
                return true;
            }
            //调用递归函数，比较左节点，右节点
            return dfs(root.left,root.right);
        }

        boolean dfs(TreeNode left, TreeNode right) {
            //递归的终止条件是两个节点都为空
            //或者两个节点中有一个为空
            //或者两个节点的值不相等
            if(left==null && right==null) {
                return true;
            }
            if(left==null || right==null) {
                return false;
            }
            if(left.val!=right.val) {
                return false;
            }
            //再递归的比较 左节点的左孩子 和 右节点的右孩子
            //以及比较  左节点的右孩子 和 右节点的左孩子
            return dfs(left.left,right.right) && dfs(left.right,right.left);
        }
    }
    class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
        TreeNode(int x) { val = x; }
    }

    public class Solution23 {
        public boolean isSymmetric(TreeNode root) {
            return check(root, root);
        }

        private boolean check(TreeNode p, TreeNode q) {
            if (p == null && q == null) {
                return true;
            }
            if (p == null || q == null) {
                return false;
            }
            return p.val == q.val && check(p.left, q.right) && check(p.right, q.left);
        }
    }




    public class Solution1321{
        public boolean isSymmetric(TreeNode root) {
            Queue<TreeNode> queue = new LinkedList<>();
            queue.offer(root);
            queue.offer(root);

            while (!queue.isEmpty()) {
                TreeNode p = queue.poll();
                TreeNode q = queue.poll();

                if (p == null && q == null) {
                    continue;
                }
                if (p == null || q == null) {
                    return false;
                }
                if (p.val != q.val) {
                    return false;
                }

                queue.offer(p.left);
                queue.offer(q.right);
                queue.offer(p.right);
                queue.offer(q.left);
            }

            return true;
        }
    }
}
