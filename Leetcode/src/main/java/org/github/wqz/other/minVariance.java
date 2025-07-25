package org.github.wqz.other;

import java.util.*;

class Solution {
    public double minVariance(List<List<Integer>> arrays) {
        if (arrays == null || arrays.isEmpty()) {
            return 0.0;
        }
        int k = arrays.size();
        PriorityQueue<Element> minHeap = new PriorityQueue<>(Comparator.comparingInt(e -> e.value));
        int currentMax = Integer.MIN_VALUE;
        long sum = 0;
        long sumSquare = 0;

        // 初始化堆和当前窗口
        for (int i = 0; i < k; i++) {
            List<Integer> array = arrays.get(i);
            if (array.isEmpty()) {
                return 0.0;
            }
            int value = array.get(0);
            minHeap.offer(new Element(value, 0, i));
            currentMax = Math.max(currentMax, value);
            sum += value;
            sumSquare += (long) value * value;
        }

        double minVar = calculateVariance(sum, sumSquare, k);

        while (true) {
            Element minElement = minHeap.poll();
            int arrayIndex = minElement.arrayIndex;
            int currentPos = minElement.pos;
            List<Integer> currentArray = arrays.get(arrayIndex);

            if (currentPos + 1 >= currentArray.size()) {
                break;
            }

            // 移除当前最小元素的贡献
            sum -= minElement.value;
            sumSquare -= (long) minElement.value * minElement.value;

            // 添加下一个元素
            int nextValue = currentArray.get(currentPos + 1);
            Element nextElement = new Element(nextValue, currentPos + 1, arrayIndex);
            minHeap.offer(nextElement);
            sum += nextValue;
            sumSquare += (long) nextValue * nextValue;

            // 更新当前最大值
            currentMax = updateCurrentMax(arrays, minHeap, currentMax);

            // 计算新的方差
            double currentVar = calculateVariance(sum, sumSquare, k);
            if (currentVar < minVar) {
                minVar = currentVar;
            }
        }

        return minVar;
    }

    private int updateCurrentMax(List<List<Integer>> arrays, PriorityQueue<Element> minHeap, int previousMax) {
        int currentMax = previousMax;
        for (Element e : minHeap) {
            currentMax = Math.max(currentMax, e.value);
        }
        return currentMax;
    }

    private double calculateVariance(long sum, long sumSquare, int k) {
        double mean = (double) sum / k;
        return (double) sumSquare / k - mean * mean;
    }

    static class Element {
        int value;
        int pos;
        int arrayIndex;

        public Element(int value, int pos, int arrayIndex) {
            this.value = value;
            this.pos = pos;
            this.arrayIndex = arrayIndex;
        }
    }

    public static void main(String[] args) {
        Solution solution = new Solution();
        List<List<Integer>> arrays = Arrays.asList(
                Arrays.asList(1, 3, 4, 6, 7, 100),
                Arrays.asList(28, 50, 70, 102),
                Arrays.asList(14, 76, 98)
        );
        double minVar = solution.minVariance(arrays);
        System.out.printf("%.2f%n", minVar); // 输出2.67
    }
}