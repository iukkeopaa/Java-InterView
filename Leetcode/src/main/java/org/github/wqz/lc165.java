package org.github.wqz;

/**
 * @Description:
 * @Author: wjh
 * @Date: 2025/7/20 16:37
 */
public class lc165 {
    public class Solution {
        public int compareVersion(String version1, String version2) {
            // 将版本号按点分割成数组
            String[] v1 = version1.split("\\.");
            String[] v2 = version2.split("\\.");

            // 获取最大长度，确保所有修订号都被比较
            int maxLength = Math.max(v1.length, v2.length);

            for (int i = 0; i < maxLength; i++) {
                // 获取当前位置的修订号，若已超出数组长度则视为0
                int num1 = i < v1.length ? Integer.parseInt(v1[i]) : 0;
                int num2 = i < v2.length ? Integer.parseInt(v2[i]) : 0;

                // 比较修订号大小
                if (num1 < num2) {
                    return -1;
                } else if (num1 > num2) {
                    return 1;
                }
                // 若相等则继续比较下一组
            }

            // 所有修订号都相等
            return 0;
        }
    }

    class Solution123{
        public int compareVersion(String v1, String v2) {
            String[] ss1 = v1.split("\\."), ss2 = v2.split("\\.");
            int n = ss1.length, m = ss2.length;
            int i = 0, j = 0;
            while (i < n || j < m) {
                int a = 0, b = 0;
                if (i < n) a = Integer.parseInt(ss1[i++]);
                if (j < m) b = Integer.parseInt(ss2[j++]);
                if (a != b) return a > b ? 1 : -1;
            }
            return 0;
        }
    }

    public class Solutio212n {
        public int compareVersion(String version1, String version2) {
            int i = 0, j = 0;
            int n1 = version1.length(), n2 = version2.length();

            while (i < n1 || j < n2) {
                int num1 = 0, num2 = 0;

                // 提取version1的下一个修订号
                while (i < n1 && version1.charAt(i) != '.') {
                    num1 = num1 * 10 + (version1.charAt(i) - '0');
                    i++;
                }
                i++; // 跳过点号

                // 提取version2的下一个修订号
                while (j < n2 && version2.charAt(j) != '.') {
                    num2 = num2 * 10 + (version2.charAt(j) - '0');
                    j++;
                }
                j++; // 跳过点号

                // 比较两个修订号
                if (num1 < num2) return -1;
                if (num1 > num2) return 1;
                // 相等则继续比较下一组
            }

            return 0; // 所有修订号都相等
        }
    }


}
