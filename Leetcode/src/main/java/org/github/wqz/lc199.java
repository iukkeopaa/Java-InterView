package org.github.wqz;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * @Description:
 * @Author: wjh
 * @Date: 2025/7/20 14:31
 */
public class lc199 {

    private static class TreeNode{

        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(){}

        TreeNode(int x){
            this.val = x;
        }

        TreeNode(int x, TreeNode left, TreeNode right){
            this.val = x;
            this.left = left;
            this.right = right;
        }
    }


    private static List<Integer> rightSideView(TreeNode root){
        //先创建结果结合
        List<Integer> res = new ArrayList<>();

        //申请队列

        Queue<TreeNode> queue = new LinkedList<>();

        //将头节点入队列

        queue.add(root);

        while(!queue.isEmpty()){

            int size = queue.size();
            for(int i =0;i<size;i++){
                TreeNode node = queue.poll();
                if(i == size-1){
                    res.add(node.val);
                }
                if(node.left != null){
                    queue.add(node.left);
                }
                if(node.right != null){
                    queue.add(node.right);
                }
            }
        }
        return res;
    }

    //  1        ← 第0层
    //       / \
    //      2   3      ← 第1层
    //       \   \
    //        5   4    ← 第2层


    //1. dfs(1, 0, result)         → result = [1]
    //   └── dfs(3, 1, result)     → result = [1,3]
    //       └── dfs(4, 2, result) → result = [1,3,4]
    //           ├── dfs(null, 3)  → 返回
    //           └── dfs(null, 3)  → 返回
    //       └── dfs(null, 2)      → 返回
    //   └── dfs(2, 1, result)
    //       └── dfs(5, 2, result)
    //           ├── dfs(null, 3)  → 返回
    //           └── dfs(null, 3)  → 返回
    //       └── dfs(null, 2)      → 返回


    private static class Solution {
        public List<Integer> rightSideView(TreeNode root) {
            List<Integer> result = new ArrayList<>();
            dfs(root, 0, result); // 从根节点、深度0开始
            return result;
        }

        private void dfs(TreeNode node, int depth, List<Integer> result) {
            if (node == null) return; // 空节点直接返回

            // 若当前深度等于结果列表大小，说明是该层第一个被访问的节点（最右侧）
            if (depth == result.size()) {
                result.add(node.val);
            }

            // 先递归右子树（保证右节点先被访问），再递归左子树
            dfs(node.right, depth + 1, result);
            dfs(node.left, depth + 1, result);
        }
    }
    public static void main(String[] args){

    }
}
