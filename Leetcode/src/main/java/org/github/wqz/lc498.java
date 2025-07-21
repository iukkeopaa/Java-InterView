package org.github.wqz;

public class lc498 {
    class Solution {
        public int[] findDiagonalOrder(int[][] mat) {
            if (mat == null || mat.length == 0) return new int[0];

            int m = mat.length;
            int n = mat[0].length;
            int[] result = new int[m * n];
            int idx = 0;

            // 遍历每条对角线
            for (int d = 0; d < m + n - 1; d++) {
                if (d % 2 == 0) {  // 偶数对角线：从下向上
                    int i = Math.min(d, m - 1);
                    int j = Math.max(0, d - m + 1);

                    while (i >= 0 && j < n) {
                        result[idx++] = mat[i][j];
                        i--;
                        j++;
                    }
                } else {  // 奇数对角线：从上向下
                    int j = Math.min(d, n - 1);
                    int i = Math.max(0, d - n + 1);

                    while (j >= 0 && i < m) {
                        result[idx++] = mat[i][j];
                        i++;
                        j--;
                    }
                }
            }

            return result;
        }
    }
    public static void main(String[] args) {

    }
}
