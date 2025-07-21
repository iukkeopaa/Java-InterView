//package org.github.wqz.other;
//
//public class _2 {
//    private import java.util.HashMap;
//import java.util.Map;
//
//    class Solution {
//        private static final Map<Character, Integer> DIGIT_MAP = new HashMap<>();
//        private static final Map<Character, Long> UNIT_MAP = new HashMap<>();
//
//        static {
//            // 初始化数字映射
//            DIGIT_MAP.put('零', 0);
//            DIGIT_MAP.put('一', 1);
//            DIGIT_MAP.put('二', 2);
//            DIGIT_MAP.put('三', 3);
//            DIGIT_MAP.put('四', 4);
//            DIGIT_MAP.put('五', 5);
//            DIGIT_MAP.put('六', 6);
//            DIGIT_MAP.put('七', 7);
//            DIGIT_MAP.put('八', 8);
//            DIGIT_MAP.put('九', 9);
//
//            // 初始化单位映射
//            UNIT_MAP.put('十', 10L);
//            UNIT_MAP.put('百', 100L);
//            UNIT_MAP.put('千', 1000L);
//            UNIT_MAP.put('万', 10000L);
//            UNIT_MAP.put('亿', 100000000L);
//        }
//
//        public long chineseToNumber(String chinese) {
//            if (chinese == null || chinese.isEmpty()) {
//                return 0;
//            }
//
//            // 处理符号
//            boolean negative = false;
//            if (chinese.startsWith("负")) {
//                negative = true;
//                chinese = chinese.substring(1);
//            }
//
//            long result = 0;
//            long current = 0;  // 当前组的数值（如"一千"中的1000）
//            long lastUnit = 1; // 上一个单位（处理"十"的特殊情况）
//
//            for (int i = 0; i < chinese.length(); i++) {
//                char c = chinese.charAt(i);
//
//                if (DIGIT_MAP.containsKey(c)) {
//                    // 当前字符是数字
//                    current = DIGIT_MAP.get(c);
//
//                    // 处理下一个字符是否为单位
//                    if (i + 1 < chinese.length()) {
//                        char nextChar = chinese.charAt(i + 1);
//                        if (UNIT_MAP.containsKey(nextChar)) {
//                            // 数字后接单位，如"一"后接"十"
//                            long unit = UNIT_MAP.get(nextChar);
//                            current *= unit;
//                            i++; // 跳过单位字符
//
//                            // 累加当前组到结果
//                            result += current;
//                            current = 0;
//                            lastUnit = unit;
//                        }
//                    }
//                } else if (UNIT_MAP.containsKey(c)) {
//                    // 当前字符是单位
//                    long unit = UNIT_MAP.get(c);
//
//                    if (unit >= 10000) {
//                        // 大单位（万、亿）：将当前结果乘以单位，并重置当前组
//                        result = (result + current) * unit;
//                        current = 0;
//                    } else {
//                        // 小单位（十、百、千）
//                        if (current == 0) {
//                            // 特殊情况：单位前没有数字（如"十"表示10）
//                            if (unit == 10) {
//                                current = unit;
//                            } else {
//                                // 其他单位（如"百"、"千"）必须前接数字
//                                throw new IllegalArgumentException("Invalid Chinese number: " + chinese);
//                            }
//                        } else {
//                            // 正常情况：单位前有数字（如"一百"）
//                            current *= unit;
//                        }
//                        result += current;
//                        current = 0;
//                    }
//                    lastUnit = unit;
//                } else {
//                    // 非法字符
//                    throw new IllegalArgumentException("Invalid character: " + c);
//                }
//            }
//
//            // 处理最后可能未累加的当前组
//            result += current;
//
//            return negative ? -result : result;
//        }
//    }
//    public static void main(String[] args) {
//
//    }
//}
