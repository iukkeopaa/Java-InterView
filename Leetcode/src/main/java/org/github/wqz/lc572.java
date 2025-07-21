package org.github.wqz;

public class lc572 {


    class Solution {
        public boolean isSubtree(TreeNode root, TreeNode subRoot) {
            if (root == null) {
                return false;
            }
            return isSameTree(root, subRoot) ||
                    isSubtree(root.left, subRoot) ||
                    isSubtree(root.right, subRoot);
        }

        // 100. 相同的树
        private boolean isSameTree(TreeNode p, TreeNode q) {
            if (p == null || q == null) {
                return p == q; // 必须都是 null
            }
            return p.val == q.val &&
                    isSameTree(p.left, q.left) &&
                    isSameTree(p.right, q.right);
        }
    }

    class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
        TreeNode(int x) { val = x; }
    }

    class Solution2 {
        public boolean isSubtree(TreeNode root, TreeNode subRoot) {
            if (root == null) {
                return subRoot == null;
            }
            // 检查当前节点的子树是否匹配，或左/右子树中是否存在匹配
            return isSameTree(root, subRoot) ||
                    isSubtree(root.left, subRoot) ||
                    isSubtree(root.right, subRoot);
        }

        private boolean isSameTree(TreeNode p, TreeNode q) {
            if (p == null && q == null) {
                return true;
            }
            if (p == null || q == null) {
                return false;
            }
            // 当前节点值相同，且左右子树均相同
            return p.val == q.val &&
                    isSameTree(p.left, q.left) &&
                    isSameTree(p.right, q.right);
        }
    }
}
