package org.github.wqz.other;

import java.util.HashMap;
import java.util.Map;

//给定一个合理的数字中文，将其转换成对应的阿拉伯数字。
public class ChineseNumberConverter {
    private static final Map<Character, Long> UNIT_MAP = new HashMap<>();
    private static final Map<Character, Long> DIGIT_MAP = new HashMap<>();

    static {
        UNIT_MAP.put('十', 10L);
        UNIT_MAP.put('百', 100L);
        UNIT_MAP.put('千', 1000L);
        UNIT_MAP.put('万', 10000L);
        UNIT_MAP.put('亿', 100000000L);

        DIGIT_MAP.put('零', 0L);
        DIGIT_MAP.put('一', 1L);
        DIGIT_MAP.put('二', 2L);
        DIGIT_MAP.put('三', 3L);
        DIGIT_MAP.put('四', 4L);
        DIGIT_MAP.put('五', 5L);
        DIGIT_MAP.put('六', 6L);
        DIGIT_MAP.put('七', 7L);
        DIGIT_MAP.put('八', 8L);
        DIGIT_MAP.put('九', 9L);
    }

    public static long chineseToNumber(String chineseNumber) {
        if (chineseNumber == null || chineseNumber.isEmpty()) {
            return 0;
        }

        // 处理特殊情况
        if (chineseNumber.equals("十")) {
            return 10;
        }
        if (chineseNumber.startsWith("十")) {
            chineseNumber = "一" + chineseNumber;
        }

        long result = 0;
        long sectionValue = 0;
        long currentDigit = 0;

        for (char c : chineseNumber.toCharArray()) {
            if (DIGIT_MAP.containsKey(c)) {
                currentDigit = DIGIT_MAP.get(c);
            } else if (UNIT_MAP.containsKey(c)) {
                long unit = UNIT_MAP.get(c);
                if (unit >= 10000) {
                    sectionValue = (sectionValue + currentDigit) * unit;
                    result += sectionValue;
                    sectionValue = 0;
                    currentDigit = 0;
                } else {
                    if (currentDigit == 0) {
                        sectionValue += unit;
                    } else {
                        sectionValue += currentDigit * unit;
                    }
                    currentDigit = 0;
                }
            }
        }

        result += sectionValue + currentDigit;
        return result;
    }

    public static void main(String[] args) {
        System.out.println(chineseToNumber("一百二十三"));      // 123
        System.out.println(chineseToNumber("一千二百三十四万五千六百七十八"));  // 12345678
        System.out.println(chineseToNumber("一亿零一万零一"));  // 100010001
        System.out.println(chineseToNumber("十"));              // 10
        System.out.println(chineseToNumber("十三"));             // 13
        System.out.println(chineseToNumber("二十一"));           // 21
    }
}