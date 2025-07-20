package org.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class lc54 {
    private static class Solution {
        public List<Integer> spiralOrder(int[][] matrix) {
            List<Integer> result = new ArrayList<>();
            if (matrix == null || matrix.length == 0) {
                return result;
            }

            int m = matrix.length;    // 行数
            int n = matrix[0].length; // 列数
            int top = 0, bottom = m - 1;
            int left = 0, right = n - 1;

            while (top <= bottom && left <= right) {
                // 向右遍历
                for (int j = left; j <= right; j++) {
                    result.add(matrix[top][j]);
                }
                top++;

                // 向下遍历
                for (int i = top; i <= bottom; i++) {
                    result.add(matrix[i][right]);
                }
                right--;

                // 向左遍历（需检查是否还有行）
                if (top <= bottom) {
                    for (int j = right; j >= left; j--) {
                        result.add(matrix[bottom][j]);
                    }
                    bottom--;
                }

                // 向上遍历（需检查是否还有列）
                if (left <= right) {
                    for (int i = bottom; i >= top; i--) {
                        result.add(matrix[i][left]);
                    }
                    left++;
                }
            }

            return result;
        }
    }

    private static class Solution1 {
        public List<Integer> spiralOrder(int[][] matrix) {
            List<Integer> result = new ArrayList<>();
            if (matrix == null || matrix.length == 0) {
                return result;
            }

            int m = matrix.length;
            int n = matrix[0].length;
            boolean[][] visited = new boolean[m][n];
            int[][] dirs = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}}; // 右、下、左、上
            int i = 0, j = 0, dir = 0; // 初始位置和方向

            for (int k = 0; k < m * n; k++) {
                result.add(matrix[i][j]);
                visited[i][j] = true;

                // 尝试按当前方向前进
                int nextI = i + dirs[dir][0];
                int nextJ = j + dirs[dir][1];

                // 如果下一步越界或已访问，切换方向
                if (nextI < 0 || nextI >= m || nextJ < 0 || nextJ >= n || visited[nextI][nextJ]) {
                    dir = (dir + 1) % 4; // 切换到下一个方向
                    nextI = i + dirs[dir][0];
                    nextJ = j + dirs[dir][1];
                }

                i = nextI;
                j = nextJ;
            }

            return result;
        }
    }

    private static class Solution2 {
        public List<Integer> spiralOrder(int[][] matrix) {
            if (matrix.length == 0)
                return new ArrayList<Integer>();
            int l = 0, r = matrix[0].length - 1, t = 0, b = matrix.length - 1, x = 0;
            Integer[] res = new Integer[(r + 1) * (b + 1)];
            while (true) {
                for (int i = l; i <= r; i++) res[x++] = matrix[t][i]; // left to right
                if (++t > b) break;
                for (int i = t; i <= b; i++) res[x++] = matrix[i][r]; // top to bottom
                if (l > --r) break;
                for (int i = r; i >= l; i--) res[x++] = matrix[b][i]; // right to left
                if (t > --b) break;
                for (int i = b; i >= t; i--) res[x++] = matrix[i][l]; // bottom to top
                if (++l > r) break;
            }
            return Arrays.asList(res);
        }
    }


    public static void main(String[] args) {

    }
}
