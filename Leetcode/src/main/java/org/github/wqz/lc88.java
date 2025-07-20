package org.github.wqz;

/**
 * @Description:
 *
 *
 * 当 m > 0 并且 n > 0 时，从后向前比较 num1[m−1] 和 nums2[n−1] ：
 * 如果是 nums1[m−1] 大，则把 nums1[m−1]放到 num1 的第 m+n−1 位置，并让 m−=1。
 * 如果是 nums1[n−1] 大，则把 nums2[n−1] 放到 num1 的第 m+n−1 位置，并让 n−=1。
 * 当上面的遍历条件结束的时候，此时 m 和 n 至少有一个为 0。
 * 当 m == 0 时，说明 num1 的数字恰好用完了，此时 nums2 可能还剩元素，需要复制到 nums1 的头部；
 * 当 n == 0 时，说明 num2 的数字恰好用完了，此时 nums1 可能还剩元素，由于剩余的这些元素一定是 nums1 和 nums2 中最小的元素，所以不用动，直接留在原地就行。
 *

 * @Author: wjh
 * @Date: 2025/7/20 15:29
 */
public class lc88 {

    class Solution {
        public void merge(int[] nums1, int m, int[] nums2, int n) {
            int tail = nums1.length - 1;
            int i1 = m - 1;
            int i2 = n - 1;

            // 个人经常用 while true，然后在循环内部列出所有情况
            while (true) {
                // 都左移到头，退出
                if (i1 == -1 && i2 == -1) break;
                // 一方左移到头，选取另一方赋值，然后左移
                if (i1 == -1) {
                    nums1[tail] = nums2[i2--];
                } else if (i2 == -1) {
                    nums1[tail] = nums1[i1--]; // 也可以直接 i1--;
                }
                // 选取大的元素赋值，然后左移
                else if (nums1[i1] > nums2[i2]) {
                    nums1[tail] = nums1[i1--];
                } else {
                    nums1[tail] = nums2[i2--];
                }
                tail--;
            }
        }
    }


    class Solution123{
        public void merge(int[] nums1, int m, int[] nums2, int n) {
            int len1 = m - 1;
            int len2 = n - 1;
            int len = m + n - 1;
            while (len1 >= 0 && len2 >= 0) {
                if (nums1[len1] >= nums2[len2]) {
                    nums1[len] = nums1[len1];
                    len1--;
                } else {
                    nums1[len] = nums2[len2];
                    len2--;
                }
                len--;
            }
            while (len2 >= 0) {
                nums1[len] = nums2[len2];
                len2--;
                len--;
            }
        }
    }

    class Solution321 {
        public void merge(int[] nums1, int m, int[] nums2, int n) {
            int p1 = m - 1;
            int p2 = n - 1;
            int p = m + n - 1;
            while (p2 >= 0) { // nums2 还有要合并的元素
                // 如果 p1 < 0，那么走 else 分支，把 nums2 合并到 nums1 中
                if (p1 >= 0 && nums1[p1] > nums2[p2]) {
                    nums1[p--] = nums1[p1--]; // 填入 nums1[p1]
                } else {
                    nums1[p--] = nums2[p2--]; // 填入 nums2[p1]
                }
            }
        }
    }


    public static void main(String[] args) {

    }
}
