package org.github.wqz;

public class lc1325 {
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
    public TreeNode removeLeafNodes(TreeNode root, int target) {
        // 递归终止条件
        if (root == null){
            return null;
        }
        // 每次递归的任务
        root.left = removeLeafNodes(root.left,target);
        root.right = removeLeafNodes(root.right,target);
        // 递归返回值
        if (root.left == null && root.right == null && root.val == target){
            return null;
        }
        return root;
    }


}
