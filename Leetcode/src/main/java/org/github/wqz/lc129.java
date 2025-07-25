package org.github.wqz;

public class lc129 {
    private static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
        TreeNode(int x) { val = x; }
    }
    public int sumNumbers(TreeNode root) {
        return helper(root, 0);
    }

    public int helper(TreeNode root, int i){
        if (root == null) return 0;
        int temp = i * 10 + root.val;
        if (root.left == null && root.right == null)
            return temp;
        return helper(root.left, temp) + helper(root.right, temp);
    }



    public static class Solution {
        private int sum = 0;

        public int sumNumbers(TreeNode root) {
            if (root == null) {
                return 0;
            }
            dfs(root, 0);
            return sum;
        }

        private void dfs(TreeNode node, int currentSum) {
            if (node == null) {
                return;
            }

            // 计算当前路径的数字
            currentSum = currentSum * 10 + node.val;

            // 如果是叶节点，累加到总和
            if (node.left == null && node.right == null) {
                sum += currentSum;
            } else {
                // 继续遍历左右子树
                dfs(node.left, currentSum);
                dfs(node.right, currentSum);
            }
        }

        public static void main(String[] args) {
            // 示例树: [1,2,3]
            TreeNode root = new TreeNode(1);
            root.left = new TreeNode(2);
            root.right = new TreeNode(3);

            Solution solution = new Solution();
            System.out.println(solution.sumNumbers(root)); // 输出 25 (12 + 13)
        }
    }


}
