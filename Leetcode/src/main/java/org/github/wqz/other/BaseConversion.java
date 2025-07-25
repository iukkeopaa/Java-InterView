package org.github.wqz.other;

import java.util.*;

public class BaseConversion {
    public static String convertBase4ToBase3(String num) {
        // 四进制转十进制
        long decimal = 0;
        for (char c : num.toCharArray()) {
            decimal = decimal * 4 + (c - '0');
        }

        // 十进制转三进制
        if (decimal == 0) return "0";
        StringBuilder result = new StringBuilder();
        while (decimal > 0) {
            result.append(decimal % 3);
            decimal /= 3;
        }
        return result.reverse().toString();
    }

    public static void main(String[] args) {
        String base4Num = "10"; // 四进制的10，对应十进制的4
        String base3Num = convertBase4ToBase3(base4Num);
        System.out.println(base3Num); // 输出 "11"（三进制）
    }
}