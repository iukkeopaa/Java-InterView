package org.example;


/**
 * 通过跟踪 “历史最低价”，确保买入价格尽可能低；
 * 每天计算 “当前价格 - 历史最低价” 的利润，保证卖出在买入之后；
 * 若所有情况均亏损（如 prices = [7,6,4,3,1]），maxProfit 始终为 0，符合题意。
 */
public class lc121 {

    private static class Solution {
        public int maxProfit(int[] prices) {
            if (prices == null || prices.length < 2) {
                return 0; // 不足两天，无法交易
            }

            int minPrice = prices[0]; // 历史最低买入价
            int maxProfit = 0;       // 最大利润

            for (int i = 1; i < prices.length; i++) {
                // 计算当前卖出的利润，更新最大利润
                int currentProfit = prices[i] - minPrice;
                if (currentProfit > maxProfit) {
                    maxProfit = currentProfit;
                }
                // 更新历史最低买入价（确保买入在卖出之前）
                if (prices[i] < minPrice) {
                    minPrice = prices[i];
                }
            }

            return maxProfit;
        }
    }

    private static  class Solution1 {
        public int maxProfit(int[] prices) {
            if (prices == null || prices.length == 0) {
                return 0;
            }

            int max = 0;
            int minPrice = prices[0];

            for (int i = 1; i < prices.length; i++) {
                minPrice = Math.min(prices[i], minPrice);
                max = Math.max(max, prices[i] - minPrice);
            }

            return max;
        }
    }
    public static void main(String[] args) {

    }
}
