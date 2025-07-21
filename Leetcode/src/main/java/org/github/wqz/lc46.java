package org.github.wqz;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class lc46 {

    private static class Solution {
        public List<List<Integer>> permute(int[] nums) {
            List<List<Integer>> ans = new ArrayList<>();
            List<Integer> path = Arrays.asList(new Integer[nums.length]); // 所有排列的长度都是一样的 n
            boolean[] onPath = new boolean[nums.length];
            dfs(0, nums, ans, path, onPath);
            return ans;
        }

        private void dfs(int i, int[] nums, List<List<Integer>> ans, List<Integer> path, boolean[] onPath) {
            if (i == nums.length) {
                ans.add(new ArrayList<>(path));
                return;
            }
            for (int j = 0; j < nums.length; j++) {
                if (!onPath[j]) {
                    path.set(i, nums[j]); // 从没有选的数字中选一个
                    onPath[j] = true; // 已选上
                    dfs(i + 1, nums, ans, path, onPath);
                    onPath[j] = false; // 恢复现场
                    // 注意 path 无需恢复现场，因为排列长度固定，直接覆盖就行
                }
            }
        }
    }
    private static class Solution3 {
        public List<List<Integer>> permute(int[] nums) {
            List<List<Integer>> result = new ArrayList<>();
            if (nums == null || nums.length == 0) {
                return result;
            }

            boolean[] used = new boolean[nums.length];
            backtrack(nums, used, new ArrayList<>(), result);
            return result;
        }

        private void backtrack(int[] nums, boolean[] used, List<Integer> path, List<List<Integer>> result) {
            // 当路径长度等于数组长度时，说明找到了一个完整的排列
            if (path.size() == nums.length) {
                result.add(new ArrayList<>(path)); // 注意：必须复制path，避免后续修改
                return;
            }

            // 尝试所有可能的元素
            for (int i = 0; i < nums.length; i++) {
                // 如果元素已被使用，跳过
                if (used[i]) {
                    continue;
                }

                // 选择当前元素
                used[i] = true;
                path.add(nums[i]);

                // 递归处理下一个位置
                backtrack(nums, used, path, result);

                // 回溯：撤销选择
                used[i] = false;
                path.remove(path.size() - 1);
            }
        }
    }
    public static void main(String[] args) {

    }
}
