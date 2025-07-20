package org.github.wqz;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedList;

/**
 * @Description:
 * @Author: wjh
 * @Date: 2025/7/20 17:19
 */
public class lc239 {


    class Solution {
        public int[] maxSlidingWindow(int[] nums, int k) {
            int n = nums.length;
            if (n == 0 || k == 0) return new int[0];

            int[] result = new int[n - k + 1];
            Deque<Integer> deque = new LinkedList<>();

            for (int i = 0; i < n; i++) {
                // 移除过期元素（索引不在当前窗口内）
                while (!deque.isEmpty() && deque.peekFirst() <= i - k) {
                    deque.pollFirst();
                }

                // 维护队列递减性：移除比当前元素小的队尾元素
                while (!deque.isEmpty() && nums[deque.peekLast()] < nums[i]) {
                    deque.pollLast();
                }

                // 添加当前元素索引到队尾
                deque.offerLast(i);

                // 窗口形成后，记录队首元素对应的值（即当前窗口最大值）
                if (i >= k - 1) {
                    result[i - k + 1] = nums[deque.peekFirst()];
                }
            }

            return result;
        }
    }

    class Solution2 {
        public int[] maxSlidingWindow(int[] nums, int k) {
            int n = nums.length;
            int[] ans = new int[n - k + 1]; // 窗口个数
            Deque<Integer> q = new ArrayDeque<>(); // 更快的写法见【Java 数组】

            for (int i = 0; i < n; i++) {
                // 1. 右边入
                while (!q.isEmpty() && nums[q.getLast()] <= nums[i]) {
                    q.removeLast(); // 维护 q 的单调性
                }
                q.addLast(i);

                // 2. 左边出
                int left = i - k + 1; // 窗口左端点
                if (q.getFirst() < left) { // 队首已经离开窗口了
                    q.removeFirst();
                }

                // 3. 在窗口左端点处记录答案
                if (left >= 0) {
                    // 由于队首到队尾单调递减，所以窗口最大值就在队首
                    ans[left] = nums[q.getFirst()];
                }
            }

            return ans;
        }
    }

}
