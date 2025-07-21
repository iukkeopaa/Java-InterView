package org.github.wqz;

public class lc43 {

        public String multiply(String num1, String num2) {
            if (num1.equals("0") || num2.equals("0")) {
                return "0";
            }

            int m = num1.length();
            int n = num2.length();
            int[] result = new int[m + n];  // 结果数组，长度最大为 m+n

            // 从右向左遍历 num1 和 num2 的每一位
            for (int i = m - 1; i >= 0; i--) {
                for (int j = n - 1; j >= 0; j--) {
                    int digit1 = num1.charAt(i) - '0';
                    int digit2 = num2.charAt(j) - '0';

                    // 计算乘积
                    int product = digit1 * digit2;

                    // 确定乘积在结果数组中的位置
                    int p1 = i + j;
                    int p2 = i + j + 1;

                    // 累加乘积到结果数组
                    int sum = product + result[p2];

                    // 更新结果数组
                    result[p2] = sum % 10;
                    result[p1] += sum / 10;  // 进位
                }
            }

            // 转换结果数组为字符串
            StringBuilder sb = new StringBuilder();
            for (int num : result) {
                if (!(sb.length() == 0 && num == 0)) {  // 跳过前导零
                    sb.append(num);
                }
            }

            return sb.toString();
        }
    }

