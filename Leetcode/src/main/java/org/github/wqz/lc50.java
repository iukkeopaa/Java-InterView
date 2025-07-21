package org.github.wqz;/**
* @Description: 
* @Author: wjh
* @Date: 2025/7/21 15:49
*/
public class lc50 {

    public class Solution {
        public double myPow(double x, int n) {
            if (n == 0) return 1; // 任何数的0次幂为1

            // 处理负数指数（注意：直接取反可能导致溢出，因此使用long类型）
            long N = n;
            if (N < 0) {
                x = 1 / x;
                N = -N;
            }

            return fastPow(x, N);
        }

        private double fastPow(double x, long n) {
            if (n == 0) return 1;

            // 递归计算x^(n/2)
            double half = fastPow(x, n / 2);

            // 根据n的奇偶性合并结果
            if (n % 2 == 0) {
                return half * half; // n为偶数
            } else {
                return half * half * x; // n为奇数
            }
        }
    }

    public class Solution123 {
        public double myPow(double x, int n) {
            if (n == 0) return 1;

            long N = n;
            if (N < 0) {
                x = 1 / x;
                N = -N;
            }

            double result = 1;
            double currentProduct = x;

            for (long i = N; i > 0; i /= 2) {
                if (i % 2 == 1) {
                    result *= currentProduct;
                }
                currentProduct *= currentProduct; // 平方，处理更高次幂
            }

            return result;
        }
    }
}
