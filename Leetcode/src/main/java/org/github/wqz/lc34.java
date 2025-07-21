package org.github.wqz;

/**
 * @Description:
 * @Author: wjh
 * @Date: 2025/7/21 15:48
 */
public class lc34 {

    public class Solution {
        public int[] searchRange(int[] nums, int target) {
            int[] result = {-1, -1};

            // 查找起始位置
            int left = 0;
            int right = nums.length;
            while (left < right) {
                int mid = left + (right - left) / 2;
                if (nums[mid] >= target) {
                    right = mid;
                } else {
                    left = mid + 1;
                }
            }
            // 检查起始位置是否有效
            if (left < nums.length && nums[left] == target) {
                result[0] = left;
            } else {
                return result; // 不存在目标值，直接返回
            }

            // 查找结束位置（第一个大于target的位置减1）
            left = 0;
            right = nums.length;
            while (left < right) {
                int mid = left + (right - left) / 2;
                if (nums[mid] > target) {
                    right = mid;
                } else {
                    left = mid + 1;
                }
            }
            result[1] = left - 1; // 第一个大于target的位置减1

            return result;
        }
    }

    public int[] searchRange(int[] nums, int target) {
        int len = nums.length;
        if (len == 0) {
            return new int[]{-1, -1};
        }
        int firstPosition = findFirstPosition(nums, target);
        if (firstPosition == -1) {
            return new int[]{-1, -1};
        }
        int lastPosition = findLastPosition(nums, target);
        return new int[]{firstPosition, lastPosition};
    }

    private int findFirstPosition(int[] nums, int target) {
        int left = 0;
        int right = nums.length - 1;
        while (left < right) {
            int mid = left + (right - left) / 2;
            if (nums[mid] == target) {
                // 下一轮搜索区间是 [left, mid]
                right = mid;
            }else if (nums[mid] > target) {
                // 大于target一定不是解，下一轮搜索区间是 [left, mid - 1]
                right = mid - 1;
            } else {
                // 小于target一定不是解，下一轮搜索区间是 [mid + 1, right]
                left = mid + 1;
            }
        }
        //需要判断最终left处是不是target
        if (nums[left] == target) {
            return left;
        }
        return -1;
    }

    private int findLastPosition(int[] nums, int target) {
        int left = 0;
        int right = nums.length - 1;
        while (left < right) {
            int mid = left + (right - left + 1) / 2;
            if (nums[mid] == target) {
                // 下一轮搜索区间是 [mid, right]
                left = mid;
            }else if (nums[mid] > target) {
                // 大于target一定不是解，下一轮搜索区间是 [left, mid - 1]
                right = mid - 1;
            } else {
                // 小于target一定不是解，下一轮搜索区间是 [mid + 1, right]
                left = mid + 1;
            }
        }
        //经过findFirstPosition，一定有相等的，所以直接返回即可。
        return left;
    }
}
