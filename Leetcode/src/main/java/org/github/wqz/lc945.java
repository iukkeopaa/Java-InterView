package org.github.wqz;

import java.util.Arrays;

public class lc945 {



    public class Solution {
        public int minIncrementForUnique(int[] nums) {
            Arrays.sort(nums);
            int moves = 0;
            for (int i = 1; i < nums.length; i++) {
                if (nums[i] <= nums[i - 1]) {
                    int prev = nums[i];
                    nums[i] = nums[i - 1] + 1;
                    moves += nums[i] - prev;
                }
            }
            return moves;
        }
    }
}
