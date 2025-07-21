package org.github.wqz.other;

public class _1 {
    private class Solution {
        private static final String[] UNITS = {"", "十", "百", "千"};
        private static final String[] GROUP_UNITS = {"", "万", "亿", "兆"};
        private static final String[] CHINESE_DIGITS = {"零", "一", "二", "三", "四", "五", "六", "七", "八", "九"};

        public String numberToChinese(long num) {
            if (num == 0) {
                return CHINESE_DIGITS[0];
            }

            StringBuilder result = new StringBuilder();
            if (num < 0) {
                result.append("负");
                num = -num;
            }

            // 分组处理，每组四位
            int groupIndex = 0;
            while (num > 0) {
                long group = num % 10000;
                if (group != 0) {
                    // 转换当前组并添加单位
                    String groupChinese = convertGroup((int) group);
                    result.insert(0, groupChinese + GROUP_UNITS[groupIndex]);
                } else if (result.length() > 0 && result.charAt(0) != '零') {
                    // 处理跨组零（如10001读作"一万零一"）
                    result.insert(0, "零");
                }

                num /= 10000;
                groupIndex++;
            }
//
//            // 处理特殊情况：一十X → 十X
//            if (result.startsWith("一十")) {
//                result.deleteCharAt(0);
//            }

            return result.toString();
        }

        private String convertGroup(int num) {
            StringBuilder sb = new StringBuilder();
            boolean zero = false;
            int position = 0;

            while (num > 0) {
                int digit = num % 10;
                if (digit == 0) {
                    // 处理零
                    if (!zero && sb.length() > 0) {
                        sb.insert(0, CHINESE_DIGITS[digit]);
                        zero = true;
                    }
                } else {
                    // 处理非零数字
                    sb.insert(0, CHINESE_DIGITS[digit] + UNITS[position]);
                    zero = false;
                }
                num /= 10;
                position++;
            }

            return sb.toString();
        }
    }
    public static void main(String[] args) {

    }
}
