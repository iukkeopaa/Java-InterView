package org.example;

public class lc215 {
    public int findKthLargest(int[] nums, int k) {
        int targetIndex = nums.length - k; // 第k大元素在排序后的索引
        return quickSelect(nums, 0, nums.length - 1, targetIndex);
    }

    private int quickSelect(int[] nums, int left, int right, int targetIndex) {
        if (left == right) {
            return nums[left];
        }

        // 选择最右侧元素作为基准
        int pivotIndex = right;
        pivotIndex = partition(nums, left, right, pivotIndex);

        if (targetIndex == pivotIndex) {
            return nums[targetIndex];
        } else if (targetIndex < pivotIndex) {
            return quickSelect(nums, left, pivotIndex - 1, targetIndex);
        } else {
            return quickSelect(nums, pivotIndex + 1, right, targetIndex);
        }
    }

    private int partition(int[] nums, int left, int right, int pivotIndex) {
        int pivotValue = nums[pivotIndex];
        // 将基准值移到最右侧
        swap(nums, pivotIndex, right);
        int storeIndex = left;

        // 将所有小于基准值的元素移到左侧
        for (int i = left; i < right; i++) {
            if (nums[i] < pivotValue) {
                swap(nums, storeIndex, i);
                storeIndex++;
            }
        }

        // 将基准值移到正确的位置
        swap(nums, storeIndex, right);
        return storeIndex;
    }

    private void swap(int[] nums, int i, int j) {
        int temp = nums[i];
        nums[i] = nums[j];
        nums[j] = temp;
    }

    public static void main(String[] args) {
        lc215 solution = new lc215();
        int[] nums = {3, 2, 1, 5, 6, 4};
        int k = 2;
        System.out.println("第 " + k + " 大的元素是: " + solution.findKthLargest(nums, k)); // 输出5
    }

}


//public class KthLargestElement {
//    public int findKthLargest(int[] nums, int k) {
//        int targetIndex = nums.length - k; // 第k大元素在排序后的索引
//        return quickSelect(nums, 0, nums.length - 1, targetIndex);
//    }
//
//    private int quickSelect(int[] nums, int left, int right, int targetIndex) {
//        if (left == right) {
//            return nums[left];
//        }
//
//        // 选择最左侧元素作为基准
//        int pivotIndex = left;
//        pivotIndex = partition(nums, left, right, pivotIndex);
//
//        if (targetIndex == pivotIndex) {
//            return nums[targetIndex];
//        } else if (targetIndex < pivotIndex) {
//            return quickSelect(nums, left, pivotIndex - 1, targetIndex);
//        } else {
//            return quickSelect(nums, pivotIndex + 1, right, targetIndex);
//        }
//    }
//
//    private int partition(int[] nums, int left, int right, int pivotIndex) {
//        int pivotValue = nums[pivotIndex];
//        int storeIndex = left + 1;
//
//        // 将基准值与最左侧元素交换（如果选择最左侧作为基准，这一步可省略）
//        swap(nums, pivotIndex, left);
//
//        // 将所有小于基准值的元素移到左侧
//        for (int i = left + 1; i <= right; i++) {
//            if (nums[i] < pivotValue) {
//                swap(nums, storeIndex, i);
//                storeIndex++;
//            }
//        }
//
//        // 将基准值移到正确的位置（storeIndex-1）
//        swap(nums, left, storeIndex - 1);
//        return storeIndex - 1;
//    }
//
//    private void swap(int[] nums, int i, int j) {
//        int temp = nums[i];
//        nums[i] = nums[j];
//        nums[j] = temp;
//    }
//
//    public static void main(String[] args) {
//        KthLargestElement solution = new KthLargestElement();
//        int[] nums = {3, 2, 1, 5, 6, 4};
//        int k = 2;
//        System.out.println("第 " + k + " 大的元素是: " + solution.findKthLargest(nums, k)); // 输出5
//    }
//}

//import java.util.PriorityQueue;
//
//public class Solution {
//    public int findKthLargest(int[] nums, int k) {
//        // 创建一个小根堆，容量为k
//        PriorityQueue<Integer> minHeap = new PriorityQueue<>(k);
//
//        // 遍历数组
//        for (int num : nums) {
//            // 如果堆的大小小于k，直接添加
//            if (minHeap.size() < k) {
//                minHeap.offer(num);
//            } else if (num > minHeap.peek()) {
//                // 堆已满且当前元素大于堆顶元素时，替换堆顶
//                minHeap.poll();
//                minHeap.offer(num);
//            }
//        }
//
//        // 堆顶元素即为第k大元素
//        return minHeap.peek();
//    }
//}

//import java.util.Collections;
//import java.util.PriorityQueue;
//
//public class Solution {
//    public int findKthLargest(int[] nums, int k) {
//        // 创建一个大根堆
//        PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Collections.reverseOrder());
//
//        // 将所有元素加入大根堆
//        for (int num : nums) {
//            maxHeap.offer(num);
//        }
//
//        // 弹出前k-1个最大元素
//        for (int i = 0; i < k - 1; i++) {
//            maxHeap.poll();
//        }
//
//        // 返回第k个最大元素
//        return maxHeap.poll();
//    }
//}