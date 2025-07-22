package org.github.wqz;

public class lc144 {
    class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
        TreeNode(int x) { val = x; }
    }

    public class Solution {
        public TreeNode invertTree(TreeNode root) {
            // 递归终止条件：节点为空时返回null
            if (root == null) {
                return null;
            }

            // 交换当前节点的左右子树
            TreeNode temp = root.left;
            root.left = root.right;
            root.right = temp;

            // 递归翻转左子树和右子树
            invertTree(root.left);
            invertTree(root.right);

            return root; // 返回根节点
        }
    }

    class Solution2 {
        public TreeNode mirrorTree(TreeNode root) {
            if(root == null) return null;
            TreeNode tmp = root.left;
            root.left = mirrorTree(root.right);
            root.right = mirrorTree(tmp);
            return root;
        }
    }

//
//        // 辅助方法：用于打印二叉树（层序遍历）
//        public static void printTree(TreeNode root) {
//            if (root == null) return;
//            java.util.Queue<TreeNode> queue = new java.util.LinkedList<>();
//            queue.add(root);
//            StringBuilder sb = new StringBuilder();
//            while (!queue.isEmpty()) {
//                TreeNode node = queue.poll();
//                sb.append(node.val).append(" ");
//                if (node.left != null) queue.add(node.left);
//                if (node.right != null) queue.add(node.right);
//            }
//            System.out.println(sb.toString());
//        }
//
//        public static void main(String[] args) {
//            // 构建二叉树 [4,2,7,1,3,6,9]
//            TreeNode root = new TreeNode(4);
//            root.left = new TreeNode(2);
//            root.right = new TreeNode(7);
//            root.left.left = new TreeNode(1);
//            root.left.right = new TreeNode(3);
//            root.right.left = new TreeNode(6);
//            root.right.right = new TreeNode(9);
//
//            Solution solution = new Solution();
//            System.out.println("原树:");
//            printTree(root); // 输出: 4 2 7 1 3 6 9
//
//            TreeNode invertedRoot = solution.invertTree(root);
//            System.out.println("翻转后的树:");
//            printTree(invertedRoot); // 输出: 4 7 2 9 6 3 1
//        }
//    }
}
