package org.github.wqz.other;

import java.util.Arrays;

//给定一个数n，如23121，给定一组数字a，如｛2，4，9｝，求a中元素组成的小于n的最大数，如小于23121的最大数为22999。
//
// 思路：
// 1. 处理n为0的特殊情况。
// 2. 对可用数字进行排序，以便后续处理。
// 3. 分别处理两种情况：
//    a. 位数更少的最大数：直接用最大数字填充。
//    b. 位数相同的最大数：逐位尝试替换，确保每一位都尽可能大。
// 4. 返回两种情况中的较大值。
public class MaxNumberLessThanN {
    public static long getMaxNumber(long n, int[] digits) {
        // 处理n为0的特殊情况
        if (n == 0) return -1; // 不存在小于0的正整数

        // 对可用数字进行排序
        Arrays.sort(digits);

        String numStr = String.valueOf(n);
        int len = numStr.length();
        char[] numChars = numStr.toCharArray();

        // 情况1：位数更少的最大数
        long case1 = getMaxWithLessDigits(len - 1, digits);

        // 情况2：位数相同的最大数
        long case2 = getMaxWithSameDigits(numChars, digits);

        // 返回两种情况中的较大值
        return Math.max(case1, case2);
    }

    // 生成指定位数的最大数
    private static long getMaxWithLessDigits(int length, int[] digits) {
        if (length == 0) return -1; // 无法生成0位数
        StringBuilder sb = new StringBuilder();
        // 使用最大数字填充每一位
        for (int i = 0; i < length; i++) {
            sb.append(digits[digits.length - 1]);
        }
        return Long.parseLong(sb.toString());
    }

    // 生成相同位数的最大数
    private static long getMaxWithSameDigits(char[] numChars, int[] digits) {
        int len = numChars.length;
        long max = -1;

        // 尝试每一位的可能替换
        for (int i = 0; i < len; i++) {
            int currentDigit = numChars[i] - '0';
            // 找到小于当前位的最大可用数字
            int best = -1;
            for (int d : digits) {
                if (d < currentDigit && d > best) {
                    best = d;
                }
            }

            // 如果找到了合适的替换数字
            if (best != -1) {
                StringBuilder sb = new StringBuilder();
                // 复制前面的数字
                for (int j = 0; j < i; j++) {
                    sb.append(numChars[j]);
                }
                // 添加替换的数字
                sb.append(best);
                // 后面的位用最大数字填充
                for (int j = i + 1; j < len; j++) {
                    sb.append(digits[digits.length - 1]);
                }
                long candidate = Long.parseLong(sb.toString());
                if (candidate > max) {
                    max = candidate;
                }
            }

            // 检查当前位是否可以使用相同数字继续（需要后续位有更小的可能）
            boolean canContinue = false;
            for (int j = i + 1; j < len; j++) {
                int target = numChars[j] - '0';
                for (int d : digits) {
                    if (d < target) {
                        canContinue = true;
                        break;
                    }
                }
                if (canContinue) break;
            }

            if (!canContinue) break;
        }

        return max;
    }

    public static void main(String[] args) {
        long n = 23121;
        int[] digits = {2, 4, 9};
        System.out.println(getMaxNumber(n, digits)); // 输出22999
    }
}


//import java.util.Arrays;
//
//public class Main {
//    public static void main(String[] args) {
//        int[] nums = {2, 4, 8};
//        int n = 23121;
//        System.out.println(func(nums, n));
//    }
//
//    // 查找小于等于目标值的最大索引
//    public static int search(int[] nums, int target) {
//        int left = 0;
//        int right = nums.length;
//        while (left < right) {
//            int mid = left + (right - left) / 2;
//            if (nums[mid] == target) {
//                left = mid + 1;
//            } else if (nums[mid] > target) {
//                right = mid;
//            } else {
//                left = mid + 1;
//            }
//        }
//        return left - 1;
//    }
//
//    public static String func(int[] nums, int n) {
//        String strN = String.valueOf(n);
//        Arrays.sort(nums);
//        StringBuilder res = new StringBuilder();
//        boolean flag = false;
//
//        for (int i = 0; i < strN.length(); i++) {
//            if (flag) {
//                res.append(nums[nums.length - 1]);
//            } else {
//                int currentChar = strN.charAt(i) - '0';
//                int index = search(nums, currentChar);
//                if (index == -1) {
//                    // 处理特殊情况：生成少一位的最大数
//                    if (strN.length() - 1 == 0) return "0";
//                    return String.valueOf(nums[nums.length - 1]).repeat(strN.length() - 1);
//                } else {
//                    if (nums[index] < currentChar) {
//                        flag = true;
//                    }
//                    res.append(nums[index]);
//
//                    // 处理最后一位的情况
//                    if (i == strN.length() - 1 && !flag) {
//                        int curPos = i;
//                        int currentTarget = strN.charAt(curPos) - '0' - 1;
//                        index = search(nums, currentTarget);
//
//                        while (curPos >= 0 && index < 0) {
//                            curPos--;
//                            if (curPos >= 0) {
//                                currentTarget = strN.charAt(curPos) - '0' - 1;
//                                index = search(nums, currentTarget);
//                            }
//                        }
//
//                        if (curPos < 0) {
//                            // 处理无法找到更小数字的情况
//                            if (strN.length() - 1 == 0) return "0";
//                            return String.valueOf(nums[nums.length - 1]).repeat(strN.length() - 1);
//                        } else {
//                            // 修改前一位并将后续位设为最大值
//                            res.setCharAt(curPos, String.valueOf(nums[index]).charAt(0));
//                            for (int pos = curPos + 1; pos < strN.length(); pos++) {
//                                res.setCharAt(pos, String.valueOf(nums[nums.length - 1]).charAt(0));
//                            }
//                            flag = true;
//                        }
//                    }
//                }
//            }
//        }
//
//        String result = res.toString();
//        // 处理前导零的情况
//        if (result.startsWith("0") && result.length() > 1) {
//            return result.substring(1);
//        }
//        return result;
//    }
//}