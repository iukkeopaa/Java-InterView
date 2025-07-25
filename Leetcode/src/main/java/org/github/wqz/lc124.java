package org.github.wqz;

public class lc124 {

    private static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
        TreeNode(int x) { val = x; }
    }

    private class Solution {
        private int maxSum = Integer.MIN_VALUE;



        public int maxPathSum(TreeNode root) {
            maxGain(root);
            return maxSum;
        }

        private int maxGain(TreeNode node) {
            if (node == null) {
                return 0;
            }

            // 递归计算左右子树的最大贡献值
            // 只有贡献值大于0时才选择该子树
            int leftGain = Math.max(maxGain(node.left), 0);
            int rightGain = Math.max(maxGain(node.right), 0);

            // 当前节点的最大路径和
            int priceNewpath = node.val + leftGain + rightGain;

            // 更新全局最大路径和
            maxSum = Math.max(maxSum, priceNewpath);

            // 返回当前节点的最大贡献值（只能选择左或右子树）
            return node.val + Math.max(leftGain, rightGain);
        }

        public void main(String[] args) {
            // 示例测试树: [-10,9,20,null,null,15,7]
            TreeNode root = new TreeNode(-10);
            root.left = new TreeNode(9);
            root.right = new TreeNode(20);
            root.right.left = new TreeNode(15);
            root.right.right = new TreeNode(7);

            Solution solution = new Solution();
            System.out.println(solution.maxPathSum(root)); // 输出 42 (路径: 15 -> 20 -> 7)
        }
    }
}
