package org.github.wqz;

public class lc8 {


    private class Solution {
        public int myAtoi(String s) {
            int n = s.length();
            int i = 0;

            // 1. 跳过前导空格
            while (i < n && s.charAt(i) == ' ') {
                i++;
            }

            // 2. 处理符号位
            int sign = 1;
            if (i < n && (s.charAt(i) == '+' || s.charAt(i) == '-')) {
                sign = s.charAt(i) == '-' ? -1 : 1;
                i++;
            }

            // 3. 跳过前导零
            while (i < n && s.charAt(i) == '0') {
                i++;
            }

            // 4. 转换数字并处理溢出
            int result = 0;
            while (i < n && Character.isDigit(s.charAt(i))) {
                int digit = s.charAt(i) - '0';

                // 检查溢出
                if (result > (Integer.MAX_VALUE - digit) / 10) {
                    return sign == 1 ? Integer.MAX_VALUE : Integer.MIN_VALUE;
                }

                result = result * 10 + digit;
                i++;
            }

            return sign * result;
        }
    }

    private class Solution2 {
        public int myAtoi(String s) {
            char[] c = s.trim().toCharArray();
            if (c.length == 0) return 0;
            int res = 0, bndry = Integer.MAX_VALUE / 10;
            int i = 1, sign = 1;
            if (c[0] == '-') sign = -1;
            else if (c[0] != '+') i = 0;
            for (int j = i; j < c.length; j++) {
                if (c[j] < '0' || c[j] > '9') break;
                if (res > bndry || res == bndry && c[j] > '7') return sign == 1 ? Integer.MAX_VALUE : Integer.MIN_VALUE;
                res = res * 10 + (c[j] - '0');
            }
            return sign * res;
        }
    }

    public static void main(String[] args) {

    }
}
