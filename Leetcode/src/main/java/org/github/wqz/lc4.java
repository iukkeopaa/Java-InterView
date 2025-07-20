package org.github.wqz;

/**
 * @Description:
 * @Author: wjh
 * @Date: 2025/7/20 17:04
 */
public class lc4 {
    class Solution {
        public double findMedianSortedArrays(int[] nums1, int[] nums2) {
            // 确保nums1是较短数组，减少二分次数
            if (nums1.length > nums2.length) {
                int[] temp = nums1;
                nums1 = nums2;
                nums2 = temp;
            }

            int m = nums1.length;
            int n = nums2.length;
            int left = 0, right = m; // 二分nums1的范围

            while (left <= right) {
                // 计算nums1的分割点i
                int i = (left + right) / 2;
                // 计算nums2的分割点j（确保左右元素数量平衡）
                int j = (m + n + 1) / 2 - i;

                // 处理边界：获取左侧最大值和右侧最小值（越界时用正负无穷表示）
                int nums1LeftMax = (i == 0) ? Integer.MIN_VALUE : nums1[i - 1];
                int nums1RightMin = (i == m) ? Integer.MAX_VALUE : nums1[i];
                int nums2LeftMax = (j == 0) ? Integer.MIN_VALUE : nums2[j - 1];
                int nums2RightMin = (j == n) ? Integer.MAX_VALUE : nums2[j];

                // 检查分割点是否满足条件
                if (nums1LeftMax <= nums2RightMin && nums2LeftMax <= nums1RightMin) {
                    // 找到正确分割点，计算中位数
                    if ((m + n) % 2 == 1) {
                        // 总长度为奇数，取左侧最大值
                        return Math.max(nums1LeftMax, nums2LeftMax);
                    } else {
                        // 总长度为偶数，取左侧最大值和右侧最小值的平均值
                        return (Math.max(nums1LeftMax, nums2LeftMax) + Math.min(nums1RightMin, nums2RightMin)) / 2.0;
                    }
                } else if (nums1LeftMax > nums2RightMin) {
                    // nums1左侧元素过大，i需减小
                    right = i - 1;
                } else {
                    // nums2左侧元素过大，i需增大
                    left = i + 1;
                }
            }
            // 理论上不会走到这里（输入均为有效数组）
            throw new IllegalArgumentException("Input arrays are not sorted.");
        }
    }
}
