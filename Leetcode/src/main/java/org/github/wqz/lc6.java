package org.github.wqz;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description:
 * @Author: wjh
 * @Date: 2025/7/21 16:09
 */
public class lc6 {

    class Solution {
        public String convert(String s, int numRows) {
            if(numRows < 2) return s;
            List<StringBuilder> rows = new ArrayList<StringBuilder>();
            for(int i = 0; i < numRows; i++) rows.add(new StringBuilder());
            int i = 0, flag = -1;
            for(char c : s.toCharArray()) {
                rows.get(i).append(c);
                if(i == 0 || i == numRows -1) flag = - flag;
                i += flag;
            }
            StringBuilder res = new StringBuilder();
            for(StringBuilder row : rows) res.append(row);
            return res.toString();
        }
    }

    public class Solution1 {
        public String convert(String s, int numRows) {
            // 特殊情况：行数为1，直接返回原字符串
            if (numRows == 1) {
                return s;
            }

            int cycle = 2 * numRows - 2; // 周期长度
            int n = s.length();
            // 用StringBuilder数组存储每行的字符
            StringBuilder[] rowSb = new StringBuilder[numRows];
            for (int i = 0; i < numRows; i++) {
                rowSb[i] = new StringBuilder();
            }

            for (int i = 0; i < n; i++) {
                char c = s.charAt(i);
                int pos = i % cycle; // 计算在周期内的位置
                // 确定当前字符的行索引
                int row = pos < numRows ? pos : cycle - pos;
                rowSb[row].append(c);
            }

            // 拼接所有行的字符
            StringBuilder result = new StringBuilder();
            for (StringBuilder sb : rowSb) {
                result.append(sb);
            }

            return result.toString();
        }
    }



}
