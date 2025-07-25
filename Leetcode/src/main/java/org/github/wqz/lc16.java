package org.github.wqz;

import java.util.Arrays;

public class lc16 {


    public class ThreeSumClosest {
        public int threeSumClosest(int[] nums, int target) {
            Arrays.sort(nums);
            int closestSum = nums[0] + nums[1] + nums[2];
            int minDiff = Math.abs(closestSum - target);

            for (int i = 0; i < nums.length - 2; i++) {
                int left = i + 1;
                int right = nums.length - 1;

                while (left < right) {
                    int currentSum = nums[i] + nums[left] + nums[right];
                    int currentDiff = Math.abs(currentSum - target);

                    if (currentDiff < minDiff) {
                        minDiff = currentDiff;
                        closestSum = currentSum;
                    }

                    if (currentSum < target) {
                        left++;
                    } else if (currentSum > target) {
                        right--;
                    } else {
                        return currentSum;
                    }
                }
            }

            return closestSum;
        }

        public void main(String[] args) {
            ThreeSumClosest solution = new ThreeSumClosest();
            int[] nums = {-1, 2, 1, -4};
            int target = 1;
            System.out.println(solution.threeSumClosest(nums, target));
        }


    }

    class Solution {
        public int threeSumClosest(int[] nums, int target) {
            Arrays.sort(nums);
            int ans = nums[0] + nums[1] + nums[2];
            for(int i=0;i<nums.length;i++) {
                int start = i+1, end = nums.length - 1;
                while(start < end) {
                    int sum = nums[start] + nums[end] + nums[i];
                    if(Math.abs(target - sum) < Math.abs(target - ans))
                        ans = sum;
                    if(sum > target)
                        end--;
                    else if(sum < target)
                        start++;
                    else
                        return ans;
                }
            }
            return ans;
        }
    }


}
