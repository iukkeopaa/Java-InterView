package org.example;

public class lc53 {

    private static  class Solution {
        public int maxSubArray(int[] nums) {
            if (nums == null || nums.length == 0) {
                return 0; // 题目要求子数组至少含1个元素，此处处理空数组边界
            }

            int currentMax = nums[0]; // 以当前元素结尾的最大子数组和
            int globalMax = nums[0];  // 全局最大子数组和

            for (int i = 1; i < nums.length; i++) {
                // 决策：加入之前的子数组 或 开始新的子数组
                currentMax = Math.max(nums[i], currentMax + nums[i]);
                // 更新全局最大值
                globalMax = Math.max(globalMax, currentMax);
            }

            return globalMax;
        }
    }

    private static class Solution2 {
        public int maxSubArray(int[] nums) {
            int res = nums[0];
            for(int i = 1; i < nums.length; i++) {
                nums[i] += Math.max(nums[i - 1], 0);
                res = Math.max(res, nums[i]);
            }
            return res;
        }
    }


    private static  class Solution3 {

        public int maxSubArray(int[] nums) {
            int pre = 0;
            int res = nums[0];
            for (int num : nums) {
                pre = Math.max(pre + num, num);
                res = Math.max(res, pre);
            }
            return res;
        }
    }

    private static  class Solution6 {

        public int maxSubArray(int[] nums) {
            int len = nums.length;
            // dp[i] 表示：以 nums[i] 结尾的连续子数组的最大和
            int[] dp = new int[len];
            dp[0] = nums[0];

            for (int i = 1; i < len; i++) {
                if (dp[i - 1] > 0) {
                    dp[i] = dp[i - 1] + nums[i];
                } else {
                    dp[i] = nums[i];
                }
            }

            // 也可以在上面遍历的同时求出 res 的最大值，这里我们为了语义清晰分开写，大家可以自行选择
            int res = dp[0];
            for (int i = 1; i < len; i++) {
                res = Math.max(res, dp[i]);
            }
            return res;
        }
    }

    public static void main(String[] args) {

    }
}

/**
 * 核心思路
 * 状态定义：
 * 设 currentMax 表示 “以当前元素 nums[i] 结尾的最大子数组和”。
 * 这意味着子数组必须包含 nums[i]，且是连续的。
 * 状态转移方程：
 * 对于每个元素 nums[i]，有两种选择：
 * 将 nums[i] 加入之前的子数组（即 currentMax + nums[i]）；
 * 从 nums[i] 开始新的子数组（即 nums[i]）。
 * 取两者中的较大值作为新的 currentMax：
 * currentMax = max(nums[i], currentMax + nums[i])。
 * 全局最大值：
 * 用 globalMax 记录遍历过程中所有 currentMax 的最大值，即最终结果。
 * 示例说明
 * 以 nums = [-2, 1, -3, 4, -1, 2, 1, -5, 4] 为例，逐步拆解过程：
 *
 * 步骤（i）	当前元素	currentMax（以当前元素结尾的最大和）	globalMax（全局最大和）
 * 初始	-	-2（初始化为 nums [0]）	-2
 * i=1	1	max(1, -2+1)=1	max(-2, 1)=1
 * i=2	-3	max(-3, 1+(-3)=-2)=-2	max(1, -2)=1
 * i=3	4	max(4, -2+4=2)=4	max(1, 4)=4
 * i=4	-1	max(-1, 4+(-1)=3)=3	max(4, 3)=4
 * i=5	2	max(2, 3+2=5)=5	max(4, 5)=5
 * i=6	1	max(1, 5+1=6)=6	max(5, 6)=6
 * i=7	-5	max(-5, 6+(-5)=1)=1	max(6, 1)=6
 * i=8	4	max(4, 1+4=5)=5	max(6, 5)=6
 *
 * 最终 globalMax = 6，对应最大子数组 [4, -1, 2, 1]。
 */
