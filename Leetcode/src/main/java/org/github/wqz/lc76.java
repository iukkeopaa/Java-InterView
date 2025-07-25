package org.github.wqz;

import java.util.HashMap;
import java.util.Map;

public class lc76 {


    public class Solution {
        public String minWindow(String s, String t) {
            if (s == null || t == null || s.length() < t.length()) {
                return "";
            }

            // 统计t中各字符的频率
            Map<Character, Integer> targetMap = new HashMap<>();
            for (char c : t.toCharArray()) {
                targetMap.put(c, targetMap.getOrDefault(c, 0) + 1);
            }

            int required = targetMap.size(); // 需要匹配的不同字符数
            int left = 0, right = 0;
            int formed = 0; // 当前窗口中已匹配的不同字符数

            // 记录当前窗口的字符频率
            Map<Character, Integer> windowMap = new HashMap<>();

            // 记录最小子串的长度、起始位置和结束位置
            int minLen = Integer.MAX_VALUE;
            int minLeft = 0;

            while (right < s.length()) {
                char c = s.charAt(right);
                windowMap.put(c, windowMap.getOrDefault(c, 0) + 1);

                // 如果当前字符在t中，并且窗口中该字符的频率达到了t中的频率
                if (targetMap.containsKey(c) &&
                        windowMap.get(c).intValue() == targetMap.get(c).intValue()) {
                    formed++;
                }

                // 尝试缩小窗口
                while (left <= right && formed == required) {
                    c = s.charAt(left);

                    // 更新最小子串
                    if (right - left + 1 < minLen) {
                        minLen = right - left + 1;
                        minLeft = left;
                    }

                    // 移除左边界字符
                    windowMap.put(c, windowMap.get(c) - 1);
                    if (targetMap.containsKey(c) &&
                            windowMap.get(c).intValue() < targetMap.get(c).intValue()) {
                        formed--;
                    }

                    left++;
                }

                right++;
            }

            return minLen == Integer.MAX_VALUE ? "" : s.substring(minLeft, minLeft + minLen);
        }

        public void main(String[] args) {
            Solution solution = new Solution();
            System.out.println(solution.minWindow("ADOBECODEBANC", "ABC")); // 输出 "BANC"
            System.out.println(solution.minWindow("a", "a")); // 输出 "a"
            System.out.println(solution.minWindow("a", "aa")); // 输出 ""
        }
    }
}
