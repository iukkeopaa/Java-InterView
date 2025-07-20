package org.github.wqz;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * @Description:
 * @Author: wjh
 * @Date: 2025/7/20 16:23
 */
public class lc56 {


    class Solution {
        public int[][] merge(int[][] intervals) {
            if (intervals == null || intervals.length == 0) {
                return new int[0][];
            }

            // 按区间的起始点排序
            Arrays.sort(intervals, Comparator.comparingInt(a -> a[0]));

            List<int[]> merged = new ArrayList<>();
            merged.add(intervals[0]); // 初始化结果列表，加入第一个区间

            for (int i = 1; i < intervals.length; i++) {
                int[] current = intervals[i];
                int[] last = merged.get(merged.size() - 1);

                if (current[0] <= last[1]) { // 如果当前区间与最后一个区间重叠
                    // 更新最后一个区间的结束点为两者的最大值
                    last[1] = Math.max(last[1], current[1]);
                } else {
                    // 不重叠，直接添加当前区间
                    merged.add(current);
                }
            }

            // 将List转换为二维数组
            return merged.toArray(new int[merged.size()][]);
        }
    }
}
