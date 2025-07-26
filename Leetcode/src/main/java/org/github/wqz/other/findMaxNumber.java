package org.github.wqz.other;

import java.util.Arrays;

public class findMaxNumber {


    public class Main {
        public static void main(String[] args) {
            int n = 231;
            int[] nums = {2, 4, 9};
            System.out.println(findMaxNumber(n, nums)); // 输出：229
        }

        public static long findMaxNumber(int n, int[] nums) {
            char[] nDigits = String.valueOf(n).toCharArray();
            int len = nDigits.length;
            Arrays.sort(nums);

            // 情况1：位数少于n的最大数
            long maxWithLessDigits = getMaxWithLessDigits(len - 1, nums);

            // 情况2：位数等于n的最大数
            long maxWithSameDigits = getMaxWithSameDigits(nDigits, nums);

            return Math.max(maxWithLessDigits, maxWithSameDigits);
        }

        private static long getMaxWithLessDigits(int length, int[] nums) {
            if (length == 0) return 0;
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < length; i++) {
                sb.append(nums[nums.length - 1]);
            }
            return sb.length() > 0 ? Long.parseLong(sb.toString()) : 0;
        }

        private static long getMaxWithSameDigits(char[] nDigits, int[] nums) {
            long max = 0;
            int len = nDigits.length;

            for (int i = 0; i < len; i++) {
                int currentDigit = nDigits[i] - '0';
                boolean foundSmaller = false;

                // 寻找当前位置可以放置的最大数字（小于当前位）
                for (int j = nums.length - 1; j >= 0; j--) {
                    if (nums[j] < currentDigit) {
                        StringBuilder sb = new StringBuilder();
                        // 添加高位
                        for (int k = 0; k < i; k++) {
                            sb.append(nDigits[k]);
                        }
                        // 添加当前位
                        sb.append(nums[j]);
                        // 添加后续位的最大可能数字
                        for (int k = i + 1; k < len; k++) {
                            sb.append(nums[nums.length - 1]);
                        }
                        long candidate = Long.parseLong(sb.toString());
                        if (candidate < Long.parseLong(new String(nDigits))) {
                            max = Math.max(max, candidate);
                        }
                        foundSmaller = true;
                        break;
                    }
                }

                // 如果找不到更小的数字，则尝试相同数字
                if (!foundSmaller) {
                    boolean hasSame = false;
                    for (int num : nums) {
                        if (num == currentDigit) {
                            hasSame = true;
                            break;
                        }
                    }
                    if (!hasSame) {
                        break;
                    }
                }
            }

            return max;
        }
    }
}
