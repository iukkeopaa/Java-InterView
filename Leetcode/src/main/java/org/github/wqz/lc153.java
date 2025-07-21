package org.github.wqz;

/**
 * @Description:
 * @Author: wjh
 * @Date: 2025/7/21 15:54
 */
public class lc153 {
    public class Solution {
        public int findMin(int[] nums) {
            int left = 0;
            int right = nums.length - 1;

            while (left < right) {
                int mid = left + (right - left) / 2; // 计算中间索引，避免溢出

                if (nums[mid] < nums[right]) {
                    // 右半部分有序，最小元素在左半部分（包括mid）
                    right = mid;
                } else {
                    // 左半部分有序，最小元素在右半部分（不包括mid）
                    left = mid + 1;
                }
            }

            // 循环结束时，left == right，即为最小元素索引
            return nums[left];
        }
    }
}
