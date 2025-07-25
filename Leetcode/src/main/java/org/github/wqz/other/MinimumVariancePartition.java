package org.github.wqz.other;

public class MinimumVariancePartition {
    public static int findPartition(int[] nums) {
        if (nums == null || nums.length <= 1) {
            return -1; // 无法分割
        }

        int n = nums.length;
        long sum = 0;
        long sumSq = 0;

        // 计算整个数组的总和和平方和
        for (int num : nums) {
            sum += num;
            sumSq += (long) num * num;
        }

        long leftSum = 0;
        long leftSumSq = 0;
        int bestPartition = -1;
        double minVariance = Double.MAX_VALUE;

        // 遍历每个可能的分割点 i（0 ≤ i < n-1）
        for (int i = 0; i < n - 1; i++) {
            int num = nums[i];
            leftSum += num;
            leftSumSq += (long) num * num;
            int leftCount = i + 1;
            int rightCount = n - leftCount;

            if (leftCount == 0 || rightCount == 0) {
                continue; // 避免除以零
            }

            // 计算左子数组的方差
            double leftVar = (double) leftSumSq / leftCount - Math.pow((double) leftSum / leftCount, 2);

            // 计算右子数组的统计量
            long rightSum = sum - leftSum;
            long rightSumSq = sumSq - leftSumSq;
            double rightVar = (double) rightSumSq / rightCount - Math.pow((double) rightSum / rightCount, 2);

            // 计算方差之和
            double totalVar = leftVar + rightVar;

            // 更新最小方差和分割点
            if (totalVar < minVariance) {
                minVariance = totalVar;
                bestPartition = i;
            }
        }

        return bestPartition;
    }

    public static void main(String[] args) {
        int[] nums = {1, 2, 3, 4, 5};
        int partition = findPartition(nums);
        System.out.println("最优分割点索引: " + partition); // 输出2，表示分割为[1,2,3]和[4,5]
    }
}