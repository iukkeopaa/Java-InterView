package org.github.wqz;

public class lc5 {

    public String longestPalindrome(String s) {
        if (s == null || s.length() < 1) {
            return "";
        }

        int start = 0;
        int end = 0;

        for (int i = 0; i < s.length(); i++) {
            // 以单个字符为中心的回文子串
            int len1 = expandAroundCenter(s, i, i);
            // 以两个字符为中心的回文子串
            int len2 = expandAroundCenter(s, i, i + 1);

            int len = Math.max(len1, len2);

            if (len > end - start + 1) {
                // 计算新的起始和结束位置
                start = i - (len - 1) / 2;
                end = i + len / 2;
            }
        }

        return s.substring(start, end + 1);
    }

    private int expandAroundCenter(String s, int left, int right) {
        while (left >= 0 && right < s.length() && s.charAt(left) == s.charAt(right)) {
            left--;
            right++;
        }
        // 返回回文子串的长度
        return right - left - 1;
    }


}
