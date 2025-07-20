package org.github.wqz;

import java.util.*;

/**
 * @Description: 无重复字符的最大子串的长度
 * @Question: 给定一个字符串 s ，请你找出其中不含有重复字符的 最长 子串 的长度。
 *
 * 输入: s = "abcabcbb"
 * 输出: 3
 * 解释: 因为无重复字符的最长子串是 "abc"，所以其长度为 3。
 *
 * @Author: wjh
 * @Date: 2025/7/19 17:05
 */
public class lc3 {

    public static int lengthOfLongestSubstring(String s) {

        char[] charArray = s.toCharArray();

        int n = charArray.length;

        int ans = 0;

        int left = 0;

        int[] freq = new int[256];
        for(int right = 0; right < n; right++){

            char c = charArray[right];
            freq[c]++;
            while(freq[c] > 1){
                charArray[left] = c;
                // cnt[s[left]]--; // 移除窗口左端点字母
                freq[c]--;
                left++;

            }
            ans = Math.max(ans, right - left + 1);
        }
        return ans;
    }
    private static class Solution1 {
        public int lengthOfLongestSubstring(String s) {
            Map<Character, Integer> dic = new HashMap<>();
            int i = -1, res = 0, len = s.length();
            for(int j = 0; j < len; j++) {
                if (dic.containsKey(s.charAt(j)))
                    i = Math.max(i, dic.get(s.charAt(j))); // 更新左指针 i
                dic.put(s.charAt(j), j); // 哈希表记录
                res = Math.max(res, j - i); // 更新结果
            }
            return res;
        }
    }

    private static class Solution2 {
        public int lengthOfLongestSubstring(String s) {
            //滑动窗口
            char[] ss = s.toCharArray();
            Set<Character> set = new HashSet<>();//去重
            int res = 0;//结果
            for(int left = 0, right = 0; right < s.length(); right++) {//每一轮右端点都扩一个。
                char ch = ss[right];//right指向的元素，也是当前要考虑的元素
                while(set.contains(ch)) {//set中有ch，则缩短左边界，同时从set集合出元素
                    set.remove(ss[left]);
                    left++;
                }
                set.add(ss[right]);//别忘。将当前元素加入。
                res = Math.max(res, right - left + 1);//计算当前不重复子串的长度。
            }
            return res;
        }
    }
    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);
        String s = input.next();
        System.out.println(new Solution2().lengthOfLongestSubstring(s));
//        System.out.println(lengthOfLongestSubstring(s));
    }
}
