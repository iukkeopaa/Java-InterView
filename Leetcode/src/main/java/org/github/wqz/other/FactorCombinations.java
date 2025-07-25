package org.github.wqz.other;

import java.util.ArrayList;
import java.util.List;

public class FactorCombinations {
    public static List<List<Integer>> getFactors(int n) {
        List<List<Integer>> result = new ArrayList<>();
        if (n <= 1) return result;
        backtrack(n, 2, new ArrayList<>(), result);
        return result;
    }

    private static void backtrack(int remaining, int start, List<Integer> current, List<List<Integer>> result) {
        if (remaining == 1) {
            if (current.size() > 1) { // 排除n本身的情况
                result.add(new ArrayList<>(current));
            }
            return;
        }

        for (int i = start; i <= remaining; i++) {
            if (remaining % i == 0) {
                current.add(i);
                backtrack(remaining / i, i, current, result);
                current.remove(current.size() - 1);
            }
        }
    }

    public static void main(String[] args) {
        int n = 8;
        List<List<Integer>> combinations = getFactors(n);

        // 输出所有组合
        for (List<Integer> combo : combinations) {
            System.out.println(combo);
        }
    }
}