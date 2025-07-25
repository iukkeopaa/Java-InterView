package org.github.wqz;

import java.util.Stack;

public class lc224 {


    public class Solution {
        public int calculate(String s) {
            Stack<Integer> stack = new Stack<>();
            int result = 0;
            int sign = 1;
            int num = 0;

            for (int i = 0; i < s.length(); i++) {
                char c = s.charAt(i);

                if (Character.isDigit(c)) {
                    num = num * 10 + (c - '0');
                } else if (c == '+') {
                    result += sign * num;
                    num = 0;
                    sign = 1;
                } else if (c == '-') {
                    result += sign * num;
                    num = 0;
                    sign = -1;
                } else if (c == '(') {
                    stack.push(result);
                    stack.push(sign);
                    result = 0;
                    sign = 1;
                } else if (c == ')') {
                    result += sign * num;
                    num = 0;
                    result *= stack.pop(); // 取出符号
                    result += stack.pop(); // 取出结果
                }
            }

            if (num != 0) {
                result += sign * num;
            }

            return result;
        }
    }
}
