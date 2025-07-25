package org.github.wqz;

/**
 * @Description:
 * @Author: wjh
 * @Date: 2025/7/21 15:51
 */
public class lc33 {
    public class Solution {
        public int search(int[] nums, int target) {
            int left = 0;
            int right = nums.length - 1;

            while (left <= right) {
                int mid = left + (right - left) / 2;

                // 找到目标值，直接返回
                if (nums[mid] == target) {
                    return mid;
                }

                // 判断左半部分是否有序
                if (nums[left] <= nums[mid]) {
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

            return -1; // 未找到目标值
        }
    }

    class Solution2 {
        public int search(int[] nums, int target) {
            int n = nums.length;
            if (n == 0) return -1;
            if (n == 1) return nums[0] == target ? 0 : -1;

            // 第一次「二分」：从中间开始找，找到满足 >=nums[0] 的分割点（旋转点）
            int l = 0, r = n - 1;
            while (l < r) {
                int mid = l + r + 1 >> 1;
                if (nums[mid] >= nums[0]) {
                    l = mid;
                } else {
                    r = mid - 1;
                }
            }

            // 第二次「二分」：通过和 nums[0] 进行比较，得知 target 是在旋转点的左边还是右边
            if (target >= nums[0]) {
                l = 0;
            } else {
                l = l + 1;
                r = n - 1;
            }
            while (l < r) {
                int mid = l + r >> 1;
                if (nums[mid] >= target) {
                    r = mid;
                } else {
                    l = mid + 1;
                }
            }

            return nums[r] == target ? r : -1;
        }
    }



}
