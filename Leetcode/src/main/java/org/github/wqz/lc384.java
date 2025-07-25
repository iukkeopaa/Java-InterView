package org.github.wqz;

import java.util.Random;

public class lc384 {


    public class Solution {
        private final int[] original;
        private int[] current;
        private final Random random;

        public Solution(int[] nums) {
            original = nums.clone();
            current = nums.clone();
            random = new Random();
        }

        public int[] reset() {
            current = original.clone();
            return current;
        }

        public int[] shuffle() {
            for (int i = current.length - 1; i > 0; i--) {
                int j = random.nextInt(i + 1); // 生成 0 到 i（包含）之间的随机索引
                swap(current, i, j);
            }
            return current;
        }

        private void swap(int[] arr, int i, int j) {
            int temp = arr[i];
            arr[i] = arr[j];
            arr[j] = temp;
        }
    }
}
