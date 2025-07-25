package org.github.wqz;

public class lc902 {



    public class Solution {
        public int atMostNGivenDigitSet(String[] digits, int n) {
            String s = String.valueOf(n);
            int m = s.length();
            int[] dp = new int[m + 1];
            dp[m] = 1; // 边界条件：空字符串视为一种有效情况

            // 计算位数等于n的情况
            for (int i = m - 1; i >= 0; i--) {
                int si = s.charAt(i) - '0';
                for (String d : digits) {
                    int digit = Integer.parseInt(d);
                    if (digit < si) {
                        dp[i] += Math.pow(digits.length, m - i - 1);
                    } else if (digit == si) {
                        dp[i] += dp[i + 1];
                    }
                }
            }

            // 计算位数少于n的情况
            int ans = dp[0];
            for (int i = 1; i < m; i++) {
                ans += Math.pow(digits.length, i);
            }

            return ans;
        }

        public void main(String[] args) {
            Solution solution = new Solution();
            String[] digits = {"1", "3", "5", "7"};
            System.out.println(solution.atMostNGivenDigitSet(digits, 100)); // 输出 20
        }
    }
}
