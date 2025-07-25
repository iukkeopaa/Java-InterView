package org.github.wqz;

import java.util.ArrayList;
import java.util.List;

public class lc113 {


    static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
        TreeNode(int x) { val = x; }
    }

    public static class Solution {
        public List<List<Integer>> pathSum(TreeNode root, int targetSum) {
            List<List<Integer>> result = new ArrayList<>();
            dfs(root, targetSum, new ArrayList<>(), result);
            return result;
        }

        private void dfs(TreeNode node, int remainingSum, List<Integer> currentPath, List<List<Integer>> result) {
            if (node == null) {
                return;
            }

            // 将当前节点加入路径
            currentPath.add(node.val);
            remainingSum -= node.val;

            // 如果是叶节点且路径和等于目标值
            if (node.left == null && node.right == null && remainingSum == 0) {
                result.add(new ArrayList<>(currentPath));
            } else {
                // 继续遍历左右子树
                dfs(node.left, remainingSum, currentPath, result);
                dfs(node.right, remainingSum, currentPath, result);
            }

            // 回溯：移除当前节点，以便尝试其他路径
            currentPath.remove(currentPath.size() - 1);
        }

        public static void main(String[] args) {
            // 示例树: [5,4,8,11,null,13,4,7,2,null,null,5,1]
            TreeNode root = new TreeNode(5);
            root.left = new TreeNode(4);
            root.right = new TreeNode(8);
            root.left.left = new TreeNode(11);
            root.left.left.left = new TreeNode(7);
            root.left.left.right = new TreeNode(2);
            root.right.left = new TreeNode(13);
            root.right.right = new TreeNode(4);
            root.right.right.left = new TreeNode(5);
            root.right.right.right = new TreeNode(1);

            Solution solution = new Solution();
            List<List<Integer>> paths = solution.pathSum(root, 22);

            // 输出结果: [[5, 4, 11, 2], [5, 8, 4, 5]]
            for (List<Integer> path : paths) {
                System.out.println(path);
            }
        }
    }
}
