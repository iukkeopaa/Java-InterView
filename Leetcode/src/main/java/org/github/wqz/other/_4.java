//package org.github.wqz.other;
//
//public class _4 {
//    public class QuickSort {
//        public void sort(int[] arr) {
//            if (arr == null || arr.length <= 1) return;
//            quickSort(arr, 0, arr.length - 1);
//        }
//
//        private void quickSort(int[] arr, int low, int high) {
//            if (low < high) {
//                int pivotIndex = partition(arr, low, high);
//                quickSort(arr, low, pivotIndex - 1);      // 递归排序左半部分
//                quickSort(arr, pivotIndex + 1, high);    // 递归排序右半部分
//            }
//        }
//
//        private int partition(int[] arr, int low, int high) {
//            int pivot = arr[high];  // 选择最后一个元素作为基准值
//            int i = low - 1;        // 小于等于基准值的元素的右边界
//
//            for (int j = low; j < high; j++) {
//                if (arr[j] <= pivot) {  // 使用 ≤ 确保相等元素均匀分布
//                    i++;
//                    swap(arr, i, j);
//                }
//            }
//
//            swap(arr, i + 1, high);  // 将基准值放到正确位置
//            return i + 1;            // 返回基准值的索引
//        }
//
//        private void swap(int[] arr, int i, int j) {
//            int temp = arr[i];
//            arr[i] = arr[j];
//            arr[j] = temp;
//        }
//    }
//    public class QuickSortHoare {
//        public void sort(int[] arr) {
//            if (arr == null || arr.length <= 1) return;
//            quickSort(arr, 0, arr.length - 1);
//        }
//
//        private void quickSort(int[] arr, int low, int high) {
//            if (low < high) {
//                int pivotIndex = partition(arr, low, high);
//                quickSort(arr, low, pivotIndex);          // 递归排序左半部分
//                quickSort(arr, pivotIndex + 1, high);    // 递归排序右半部分
//            }
//        }
//
//        private int partition(int[] arr, int low, int high) {
//            int pivot = arr[low];  // 选择第一个元素作为基准值
//            int i = low - 1;       // 左指针初始化为 low-1
//            int j = high + 1;      // 右指针初始化为 high+1
//
//            while (true) {
//                // 找到左侧第一个大于等于基准值的元素
//                do {
//                    i++;
//                } while (arr[i] < pivot);
//
//                // 找到右侧第一个小于等于基准值的元素
//                do {
//                    j--;
//                } while (arr[j] > pivot);
//
//                if (i >= j) return j;  // 指针相遇，返回分割点
//
//                swap(arr, i, j);       // 交换不符合条件的元素
//            }
//        }
//
//        private void swap(int[] arr, int i, int j) {
//            int temp = arr[i];
//            arr[i] = arr[j];
//            arr[j] = temp;
//        }
//    }
//
//    public class QuickSortThreeWay {
//        public void sort(int[] arr) {
//            if (arr == null || arr.length <= 1) return;
//            quickSort(arr, 0, arr.length - 1);
//        }
//
//        private void quickSort(int[] arr, int low, int high) {
//            if (low >= high) return;
//
//            int pivot = arr[high];
//            int lt = low;      // 小于区域的右边界
//            int gt = high;     // 大于区域的左边界
//            int i = low;       // 当前扫描位置
//
//            while (i <= gt) {
//                if (arr[i] < pivot) {
//                    swap(arr, lt, i);
//                    lt++;
//                    i++;
//                } else if (arr[i] > pivot) {
//                    swap(arr, i, gt);
//                    gt--;
//                } else {  // arr[i] == pivot
//                    i++;  // 相等元素保持原位
//                }
//            }
//
//            quickSort(arr, low, lt - 1);     // 递归处理小于部分
//            quickSort(arr, gt + 1, high);    // 递归处理大于部分
//        }
//
//        private void swap(int[] arr, int i, int j) {
//            int temp = arr[i];
//            arr[i] = arr[j];
//            arr[j] = temp;
//        }
//    }
//
//    import java.util.Stack;
//
//    public class QuickSortWhile {
//        public void sort(int[] arr) {
//            if (arr == null || arr.length <= 1) return;
//
//            Stack<Integer> stack = new Stack<>();
//            stack.push(0);
//            stack.push(arr.length - 1);
//
//            while (!stack.isEmpty()) {
//                int high = stack.pop();
//                int low = stack.pop();
//
//                if (low >= high) continue;
//
//                int pivotIndex = partition(arr, low, high);
//
//                // 先处理较小的子数组，减少栈深度
//                if (pivotIndex - low < high - pivotIndex) {
//                    stack.push(pivotIndex + 1);
//                    stack.push(high);
//                    stack.push(low);
//                    stack.push(pivotIndex);
//                } else {
//                    stack.push(low);
//                    stack.push(pivotIndex);
//                    stack.push(pivotIndex + 1);
//                    stack.push(high);
//                }
//            }
//        }
//
//        private int partition(int[] arr, int low, int high) {
//            int pivot = arr[low];  // 选择第一个元素作为基准值
//            int left = low;
//            int right = high + 1;
//
//            while (true) {
//                // 从左向右找第一个大于等于基准值的元素
//                while (left < high && arr[++left] < pivot);
//
//                // 从右向左找第一个小于等于基准值的元素
//                while (right > low && arr[--right] > pivot);
//
//                if (left >= right) break;  // 指针相遇，分区完成
//
//                swap(arr, left, right);    // 交换不符合条件的元素
//            }
//
//            swap(arr, low, right);  // 将基准值放到正确位置
//            return right;           // 返回基准值的索引
//        }
//
//        private void swap(int[] arr, int i, int j) {
//            int temp = arr[i];
//            arr[i] = arr[j];
//            arr[j] = temp;
//        }
//    }
//
//    import java.util.Random;
//
//    public class Solution {
//
//        public static final Random random = new Random(System.currentTimeMillis());
//
//        public int[] sortArray(int[] nums) {
//            int n = nums.length;
//            quickSort(nums, 0, n - 1);
//            return nums;
//        }
//
//        private void quickSort(int[] nums, int left, int right) {
//            if (left >= right) {
//                return;
//            }
//
//            int pivotIndex = partition(nums, left, right);
//            quickSort(nums, left, pivotIndex - 1);
//            quickSort(nums, pivotIndex + 1, right);
//        }
//
//        private int partition(int[] nums, int left, int right) {
//            int randomIndex = left + random.nextInt(right - left + 1);
//            swap(nums, left, randomIndex);
//
//            // nums[left + 1..le) <= pivot，nums(ge..right] >= pivot
//            int pivot = nums[left];
//
//            int le = left + 1;
//            int ge = right;
//            while (true) {
//                while (le <= ge && nums[le] < pivot) {
//                    le++;
//                }
//                while (le <= ge && nums[ge] > pivot) {
//                    ge--;
//                }
//
//                // le 来到了第一个大于等于 pivot 的位置
//                // ge 来到了第一个小于等于 pivot 的位置
//                if (le >= ge) {
//                    // le 与 ge 重合的时候就可以退出循环了，因为此时重合位置的值就等于 pivot
//                    break;
//                }
//
//                swap(nums, le, ge);
//                le++;
//                ge--;
//            }
//
//            swap(nums, left, ge);
//            return ge;
//        }
//
//        private void swap(int[] nums, int index1, int index2) {
//            int temp = nums[index1];
//            nums[index1] = nums[index2];
//            nums[index2] = temp;
//        }
//
//    }
//
//    class Solution {
//        public int[] sortArray(int[] nums) {
//
//            quickSort(nums,0,nums.length-1);
//            return nums;
//
//        }
//        public void quickSort (int[] nums, int low, int high) {
//
//            if (low < high) {
//                int index = partition(nums,low,high);
//                quickSort(nums,low,index-1);
//                quickSort(nums,index+1,high);
//            }
//
//        }
//        public int partition (int[] nums, int low, int high) {
//
//            int pivot = nums[low];
//            while (low < high) {
//                //移动high指针
//                while (low < high && nums[high] >= pivot) {
//                    hight--;
//                }
//                //填坑
//                if (low < high) nums[low] = nums[high];
//                while (low < high && nums[low] <= pivot) {
//                    low++;
//                }
//                //填坑
//                if (low < high) nums[high] = nums[low];
//            }
//            //基准数放到合适的位置
//            nums[low] = pivot;
//            return low;
//        }
//    }
//
//
//
//}
