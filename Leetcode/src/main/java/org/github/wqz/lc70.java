package org.github.wqz;

public class lc70 {
    public class Solution {
        public int climbStairs(int n) {
            if (n <= 2) {
                return n;
            }

            int prev1 = 1;  // 到达第n-2阶的方法数
            int prev2 = 2;  // 到达第n-1阶的方法数

            for (int i = 3; i <= n; i++) {
                int current = prev1 + prev2;  // 当前阶的方法数
                prev1 = prev2;
                prev2 = current;
            }

            return prev2;
        }

        public void main(String[] args) {
            Solution solution = new Solution();
            System.out.println(solution.climbStairs(2)); // 输出 2 (1+1, 2)
            System.out.println(solution.climbStairs(3)); // 输出 3 (1+1+1, 1+2, 2+1)
            System.out.println(solution.climbStairs(4)); // 输出 5 (1+1+1+1, 1+1+2, 1+2+1, 2+1+1, 2+2)
        }
    }

    class Solutio2 {
        public int climbStairs(int n) {
            int[] dp = new int[n + 1];
            dp[0] = 1;
            dp[1] = 1;
            for(int i = 2; i <= n; i++) {
                dp[i] = dp[i - 1] + dp[i - 2];
            }
            return dp[n];
        }
    }


}
