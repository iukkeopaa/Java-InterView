package org.github.wqz;

public class lc581 {
    public class Solution {
        public int findUnsortedSubarray(int[] nums) {
            int n = nums.length;
            int max = Integer.MIN_VALUE;
            int min = Integer.MAX_VALUE;
            int right = -1;
            int left = -1;

            // 确定右边界
            for (int i = 0; i < n; i++) {
                if (max > nums[i]) {
                    right = i;
                } else {
                    max = nums[i];
                }
            }

            // 确定左边界
            for (int i = n - 1; i >= 0; i--) {
                if (min < nums[i]) {
                    left = i;
                } else {
                    min = nums[i];
                }
            }

            return right == -1 ? 0 : right - left + 1;
        }
    }
}
