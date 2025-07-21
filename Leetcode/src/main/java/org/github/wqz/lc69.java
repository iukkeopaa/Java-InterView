package org.github.wqz;

/**
 * @Description:
 * @Author: wjh
 * @Date: 2025/7/20 16:57
 */
public class lc69 {
    public class Solution {
        public int mySqrt(int x) {
            int left = 0, right = x, ans = -1;
            while (left <= right) {
                int mid = left + (right - left) / 2;
                // 避免使用 mid*mid 防止溢出，改用 x/mid 比较
                if ((long) mid * mid <= x) {
                    ans = mid;
                    left = mid + 1;
                } else {
                    right = mid - 1;
                }
            }
            return ans;
        }
    }

    public class Solution1 {
        public int mySqrt(int x) {
            if (x == 0) return 0;
            long t = x;
            while (t > x / t) {
                t = (t + x / t) / 2;
            }
            return (int) t;
        }
    }

    public class Sqrt {
        public static int mySqrt(int x) {
            if (x < 2) return x; // 处理特殊情况：0和1的平方根为其本身

            int left = 1;
            int right = x;
            int result = 0;

            while (left <= right) {
                int mid = left + (right - left) / 2; // 防止整数溢出

                // 使用除法代替乘法，避免溢出
                int div = x / mid;

                if (mid == div) {
                    return mid; // 找到精确平方根
                } else if (mid < div) {
                    result = mid; // 记录可能的结果
                    left = mid + 1; // 向右搜索更大的值
                } else {
                    right = mid - 1; // 向左搜索更小的值
                }
            }

            return result; // 返回最大的整数平方根
        }

        public static void main(String[] args) {
            System.out.println(mySqrt(4));  // 输出：2
            System.out.println(mySqrt(8));  // 输出：2（因为sqrt(8)≈2.828，整数部分为2）
            System.out.println(mySqrt(2147395599)); // 处理大数溢出
        }
    }

    public int mySqrt(int x) {
        if (x <= 1) {
            return x;
        }
        int left = 1;
        int right = x;
        while (left < right) {
            int mid = left + (right - left + 1) / 2;
            if (mid > x / mid) {
                //target在[left,right-1]内
                right = mid - 1;
            } else {
                //target在[mid,right]内，可能是mid
                left = mid;
            }
        }
        return left;
    }
}
