package org.github.wqz;

public class lc172 {

    public class Solution {
        public int trailingZeroes(int n) {
            int count = 0;
            while (n > 0) {
                n /= 5;
                count += n;
            }
            return count;
        }
    }
}
