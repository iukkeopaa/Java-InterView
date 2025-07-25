package org.github.wqz.other;

import java.util.*;

public class ReversePermutation {
    public static List<Integer> reconstruct(int[] counts) {
        int n = counts.length;
        List<Integer> result = new ArrayList<>(n);
        List<Integer> available = new ArrayList<>(n);

        // 初始化可用编号列表
        for (int i = 1; i <= n; i++) {
            available.add(i);
        }

        // 从后向前遍历报数序列
        for (int i = n - 1; i >= 0; i--) {
            int k = counts[i];
            // 移除第k+1小的可用编号
            int num = available.remove(k);
            result.add(0, num);
        }

        return result;
    }

    public static void main(String[] args) {
        int[] counts = {0, 1, 0, 0, 3};
        List<Integer> permutation = reconstruct(counts);
        System.out.println(permutation); // 输出 [3, 5, 2, 1, 4]
    }
}