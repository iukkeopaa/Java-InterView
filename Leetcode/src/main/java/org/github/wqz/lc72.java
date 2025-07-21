package org.github.wqz;

public class lc72 {

    private class Solution {
        public int minDistance(String word1, String word2) {
            int m = word1.length();
            int n = word2.length();

            // 创建二维数组 dp，dp[i][j] 表示 word1 前 i 个字符转换为 word2 前 j 个字符的最少操作次数
            int[][] dp = new int[m + 1][n + 1];

            // 初始化边界条件
            for (int i = 0; i <= m; i++) {
                dp[i][0] = i; // word2 为空，需要删除 word1 的所有字符
            }
            for (int j = 0; j <= n; j++) {
                dp[0][j] = j; // word1 为空，需要插入 word2 的所有字符
            }

            // 填充 dp 数组
            for (int i = 1; i <= m; i++) {
                for (int j = 1; j <= n; j++) {
                    if (word1.charAt(i - 1) == word2.charAt(j - 1)) {
                        dp[i][j] = dp[i - 1][j - 1]; // 字符相同，无需操作
                    } else {
                        // 插入、删除、替换中的最小操作次数 + 1
                        dp[i][j] = Math.min(Math.min(dp[i][j - 1],    // 插入操作
                                        dp[i - 1][j]),    // 删除操作
                                dp[i - 1][j - 1]) + 1;  // 替换操作
                    }
                }
            }

            return dp[m][n];
        }

        public void main(String[] args) {
            Solution solution = new Solution();
            System.out.println(solution.minDistance("horse", "ros"));  // 输出 3
            System.out.println(solution.minDistance("intention", "execution"));  // 输出 5
        }
    }
}
