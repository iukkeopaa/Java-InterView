package org.github.wqz.other;

public class MaxTwoSubarrays {
    public static int maxTwoSubArrays(int[] nums) {
        int n = nums.length;
        if (n < 2) return 0;

        int currentLeftMax = nums[0]; // 当前左侧子数组的最大和
        int globalLeftMax = nums[0];  // 全局左侧子数组的最大和
        int currentRightMax = nums[n-1]; // 当前右侧子数组的最大和
        int globalRightMax = nums[n-1];  // 全局右侧子数组的最大和
        int maxSum = nums[0] + nums[n-1]; // 初始最大和

        // 双向遍历数组，i从左到右，j从右到左
        for (int i = 1, j = n - 2; i < n; i++, j--) {
            // 更新左侧最大子数组和
            currentLeftMax = Math.max(nums[i], currentLeftMax + nums[i]);
            globalLeftMax = Math.max(globalLeftMax, currentLeftMax);

            // 更新右侧最大子数组和
            currentRightMax = Math.max(nums[j], currentRightMax + nums[j]);
            globalRightMax = Math.max(globalRightMax, currentRightMax);

            // 更新最大和（i和j不重叠时）
            if (i < j) {
                maxSum = Math.max(maxSum, globalLeftMax + globalRightMax);
            }
        }

        return maxSum;
    }

    public static void main(String[] args) {
        int[] nums1 = {1, 2, -3, 4, 5};
        System.out.println(maxTwoSubArrays(nums1)); // 输出12

        int[] nums2 = {1, 1, -4, 4, 1, -2, 3, -9, 4, 5};
        System.out.println(maxTwoSubArrays(nums2)); // 输出11
    }
}