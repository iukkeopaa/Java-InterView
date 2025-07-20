package org.github.wqz;

import java.util.*;

/**
 * @Description:
 * @Author: wjh
 * @Date: 2025/7/20 15:16
 */
public class lc20 {

    public boolean isValid(String s) {
        Stack<Character> stack = new Stack<>();
        char[] charArray = s.toCharArray();

        for (char ch : charArray) {
            //如果是左括号则直接入栈
            if (ch == '(' || ch == '{' || ch == '[') {
                stack.push(ch);
            } else {
                //如果是右括号，并且此时栈不为空
                if (!stack.isEmpty()) {
                    if (ch == ')') {
                        if (stack.pop() != '(')
                            return false;
                    } else if (ch == '}') {
                        if (stack.pop() != '{')
                            return false;
                    } else {
                        if (stack.pop() != '[')
                            return false;
                    }
                }
                else{ //此时栈为空，但却来了个右括号，也直接返回false
                    return false;
                }
            }
        }
        return stack.isEmpty();
    }


    class Solution2{
        public boolean isValid(String s) {
            int n = s.length();
            if (n % 2 == 1) {
                return false;
            }

            Map<Character, Character> pairs = new HashMap<Character, Character>() {{
                put(')', '(');
                put(']', '[');
                put('}', '{');
            }};
            Deque<Character> stack = new LinkedList<Character>();
            for (int i = 0; i < n; i++) {
                char ch = s.charAt(i);
                if (pairs.containsKey(ch)) {
                    if (stack.isEmpty() || stack.peek() != pairs.get(ch)) {
                        return false;
                    }
                    stack.pop();
                } else {
                    stack.push(ch);
                }
            }
            return stack.isEmpty();
        }
    }

    class Solution123 {
        public boolean isValid(String s) {
            List<Character> chars = new ArrayList<>(); // 亦可用 LinkedList, Stack 内部设计线程锁，不建议用
            // 符号对
            Map<Character, Character> pairs = new HashMap<>();
            pairs.put(')', '(');
            pairs.put(']', '[');
            pairs.put('}', '{');

            for (char c : s.toCharArray()) {
                if (c == '(' || c == '{' || c == '[') {
                    chars.add(c);
                } else {
                    if (chars.isEmpty() || chars.get(chars.size() - 1) != pairs.get(c)) {
                        return false;
                    }
                    chars.remove(chars.size() - 1);
                }
            }

            return chars.isEmpty();
        }
    }


    public static void main(String[] args) {

    }
}
