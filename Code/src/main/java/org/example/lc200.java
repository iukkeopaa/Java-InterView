package org.example;

import java.util.LinkedList;
import java.util.Queue;

public class lc200 {
    private static class Solution {
        public int numIslands(char[][] grid) {
            if (grid == null || grid.length == 0) {
                return 0;
            }

            int count = 0;
            int rows = grid.length;
            int cols = grid[0].length;

            // 遍历网格中的每个单元格
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    // 遇到未访问的陆地，启动DFS并计数
                    if (grid[i][j] == '1') {
                        count++;
                        dfs(grid, i, j);
                    }
                }
            }
            return count;
        }

        // DFS：标记所有相连的陆地为已访问（改为'0'）
        private void dfs(char[][] grid, int i, int j) {
            // 边界检查：超出网格范围或当前单元格是水（'0'），直接返回
            if (i < 0 || i >= grid.length || j < 0 || j >= grid[0].length || grid[i][j] == '0') {
                return;
            }

            // 将当前陆地标记为已访问（改为'0'，避免重复遍历）
            grid[i][j] = '0';

            // 递归访问上下左右四个方向的相邻单元格
            dfs(grid, i - 1, j); // 上
            dfs(grid, i + 1, j); // 下
            dfs(grid, i, j - 1); // 左
            dfs(grid, i, j + 1); // 右
        }
    }

    private static class Solution1 {
        public int numIslands(char[][] grid) {
            if (grid == null || grid.length == 0) {
                return 0;
            }

            int count = 0;
            int rows = grid.length;
            int cols = grid[0].length;
            // 四个方向的偏移量（上、下、左、右）
            int[][] dirs = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

            // 遍历网格中的每个单元格
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    // 遇到未访问的陆地，启动BFS并计数
                    if (grid[i][j] == '1') {
                        count++;
                        // 用队列存储待访问的陆地单元格
                        Queue<int[]> queue = new LinkedList<>();
                        queue.add(new int[]{i, j});
                        grid[i][j] = '0'; // 标记为已访问

                        // BFS遍历所有相连的陆地
                        while (!queue.isEmpty()) {
                            int[] curr = queue.poll();
                            // 检查四个方向的相邻单元格
                            for (int[] dir : dirs) {
                                int x = curr[0] + dir[0];
                                int y = curr[1] + dir[1];
                                // 若相邻单元格是陆地且未访问，加入队列并标记
                                if (x >= 0 && x < rows && y >= 0 && y < cols && grid[x][y] == '1') {
                                    queue.add(new int[]{x, y});
                                    grid[x][y] = '0'; // 标记为已访问
                                }
                            }
                        }
                    }
                }
            }
            return count;
        }
    }
    public static void main(String[] args) {
        char[][] grid = {
                {'1', '1', '0', '0', '0'},
                {'1', '1', '0', '0', '0'},
                {'0', '0', '1', '0', '0'},
                {'0', '0', '0', '1', '1'}
        };
        Solution1 solution = new Solution1();

        int numIslands = solution.numIslands(grid);
        System.out.println("岛屿数量: " + numIslands);
    }
}
