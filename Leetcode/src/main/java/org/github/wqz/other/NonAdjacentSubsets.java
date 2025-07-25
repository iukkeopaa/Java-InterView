package org.github.wqz.other;

import java.util.ArrayList;
import java.util.List;

public class NonAdjacentSubsets {
    public static List<List<Integer>> findSubsets(int[] nums, int n) {
        List<List<Integer>> result = new ArrayList<>();
        if (nums == null || nums.length == 0 || n <= 0 || n > (nums.length + 1) / 2) {
            return result;
        }
        backtrack(nums, n, 0, new ArrayList<>(), result);
        return result;
    }

    private static void backtrack(int[] nums, int targetSize, int start, List<Integer> current, List<List<Integer>> result) {
        if (current.size() == targetSize) {
            result.add(new ArrayList<>(current));
            return;
        }

        for (int i = start; i < nums.length; i++) {
            current.add(nums[i]);
            // 跳过下一个元素以避免相邻
            backtrack(nums, targetSize, i + 2, current, result);
            current.remove(current.size() - 1);
        }
    }

    public static void main(String[] args) {
        int[] nums = {1, 2, 3, 4, 5};
        int n = 2;
        List<List<Integer>> subsets = findSubsets(nums, n);

        // 输出所有符合条件的子集
        for (List<Integer> subset : subsets) {
            System.out.println(subset);
        }
    }
}