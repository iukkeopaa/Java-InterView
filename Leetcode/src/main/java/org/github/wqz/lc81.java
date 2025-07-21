package org.github.wqz;

/**
 * @Description:
 * @Author: wjh
 * @Date: 2025/7/21 15:53
 */
public class lc81 {
    public class Solution {
        public boolean search(int[] nums, int target) {
            int left = 0;
            int right = nums.length - 1;

            while (left <= right) {
                int mid = left + (right - left) / 2;

                // 找到目标值，直接返回
                if (nums[mid] == target) {
                    return true;
                }

                // 处理重复元素：无法确定有序部分，收缩左右边界
                if (nums[left] == nums[mid] && nums[mid] == nums[right]) {
                    left++;
                    right--;
                }
                // 判断左半部分是否有序
                else if (nums[left] <= nums[mid]) {
                    // 目标值在左半部分的有序区间内
                    if (target >= nums[left] && target < nums[mid]) {
                        right = mid - 1;
                    } else {
                        left = mid + 1;
                    }
                }
                // 右半部分有序
                else {
                    // 目标值在右半部分的有序区间内
                    if (target > nums[mid] && target <= nums[right]) {
                        left = mid + 1;
                    } else {
                        right = mid - 1;
                    }
                }
            }

            return false; // 未找到目标值
        }
    }
}
