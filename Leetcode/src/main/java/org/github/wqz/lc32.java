package org.github.wqz;

import java.util.Stack;

/**
 * @Description:
 * @Author: wjh
 * @Date: 2025/7/22 14:34
 */
public class lc32 {


    public class Main {
        public static void main(String[] args) {
            String s = "(()))";
            System.out.println(longestValidParentheses(s)); // 输出4
        }

        public static int longestValidParentheses(String s) {
            int maxLen = 0;
            Stack<Integer> stack = new Stack<>();
            stack.push(-1); // 初始边界

            for (int i = 0; i < s.length(); i++) {
                if (s.charAt(i) == '(') {
                    stack.push(i);
                } else {
                    stack.pop();
                    if (stack.isEmpty()) {
                        stack.push(i); // 记录新的边界
                    } else {
                        maxLen = Math.max(maxLen, i - stack.peek());
                    }
                }
            }
            return maxLen;
        }
    }

    class Solution {
        public int longestValidParentheses(String s) {
            Stack<Integer> st = new Stack<Integer>();
            int ans = 0;
            for(int i = 0 ,start = 0;i < s.length();i ++)
            {
                if( s.charAt(i) == '(') st.add(i);
                else
                {
                    if(!st.isEmpty())
                    {
                        st.pop();
                        if(st.isEmpty()) ans = Math.max(ans,i - start + 1);
                        else ans = Math.max(ans,i - st.peek());
                    }
                    else start = i + 1;
                }
            }
            return ans;
        }
    }


}
