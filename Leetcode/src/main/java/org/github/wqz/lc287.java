package org.github.wqz;

/**
 * @Description:
 * @Author: wjh
 * @Date: 2025/7/21 15:58
 */
public class lc287 {

    public class Solution {
        public int findDuplicate(int[] nums) {
            // 步骤1：快慢指针相遇
            int slow = nums[0];
            int fast = nums[nums[0]];

            while (slow != fast) {
                slow = nums[slow];        // 慢指针移动一步
                fast = nums[nums[fast]];  // 快指针移动两步
            }

            // 步骤2：寻找入环点
            slow = 0;  // 慢指针重置为起点
            while (slow != fast) {
                slow = nums[slow];
                fast = nums[fast];
            }

            return slow;  // 返回入环点，即重复的数
        }
    }
}
