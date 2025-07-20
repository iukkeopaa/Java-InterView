package org.github.wqz.other;

import java.util.HashMap;
import java.util.Map;

public class _3 {


    class Solution {
        private static final String CHARSET = "0123456789abcdefghijklmnopqrstuvwxyz";
        private static final Map<Character, Integer> CHAR_TO_VAL = new HashMap<>();

        static {
            // 初始化字符到数值的映射
            for (int i = 0; i < CHARSET.length(); i++) {
                CHAR_TO_VAL.put(CHARSET.charAt(i), i);
            }
        }

        public String addBase36(String num1, String num2) {
            StringBuilder result = new StringBuilder();
            int carry = 0;  // 进位
            int i = num1.length() - 1;
            int j = num2.length() - 1;

            while (i >= 0 || j >= 0 || carry > 0) {
                int sum = carry;

                // 累加 num1 当前位
                if (i >= 0) {
                    sum += CHAR_TO_VAL.get(num1.charAt(i));
                    i--;
                }

                // 累加 num2 当前位
                if (j >= 0) {
                    sum += CHAR_TO_VAL.get(num2.charAt(j));
                    j--;
                }

                // 计算当前位结果和进位
                result.append(CHARSET.charAt(sum % 36));  // 当前位结果
                carry = sum / 36;                        // 进位
            }

            return result.reverse().toString();  // 反转结果（因低位在前）
        }
    }
}
