package org.github.wqz;

/**
 * @Description:
 * @Author: wjh
 * @Date: 2025/7/21 15:56
 */
public class lc154 {
    public class Solution {
        public int findMin(int[] nums) {
            int left = 0;
            int right = nums.length - 1;

            while (left < right) {
                int mid = left + (right - left) / 2;

                if (nums[mid] < nums[right]) {
                    // 右半部分有序，最小元素在左半部分（包括mid）
                    right = mid;
                } else if (nums[mid] > nums[right]) {
                    // 左半部分有序，最小元素在右半部分（不包括mid）
                    left = mid + 1;
                } else {
                    // nums[mid] == nums[right]，无法确定，右指针左移
                    right--;
                }
            }

            return nums[left];
        }
    }
}
