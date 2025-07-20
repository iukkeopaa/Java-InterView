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
}
