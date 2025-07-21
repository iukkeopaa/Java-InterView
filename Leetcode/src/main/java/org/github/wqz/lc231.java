package org.github.wqz;

/**
 * @Description:
 * @Author: wjh
 * @Date: 2025/7/21 16:01
 */
public class lc231 {
    public class Solution {
        public boolean isPowerOfTwo(int n) {
            return n > 0 && (n & (n - 1)) == 0;
        }
    }
}
