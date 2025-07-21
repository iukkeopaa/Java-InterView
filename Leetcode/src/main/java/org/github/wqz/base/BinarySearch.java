package org.github.wqz.base;

/**
 * @Description:
 * @Author: wjh
 * 使用等号（<=）	不使用等号（<）
 * 循环处理单个元素（left == right）	循环结束时区间剩一个元素（left == right）
 * 找到目标立即返回	需额外检查剩余元素
 * 常用于精确查找	常用于范围查找（如第一个≥target 的位置）
 * @Date: 2025/7/21 15:21
 */
public class BinarySearch {

    public static int binarySearchWithEquals(int[] arr, int target) {
        int left = 0;
        int right = arr.length - 1;

        while (left <= right) { // 使用等号，允许left和right相等
            int mid = left + (right - left) / 2;

            if (arr[mid] == target) {
                return mid; // 找到目标值，直接返回
            } else if (arr[mid] < target) {
                left = mid + 1; // 目标在右半部分
            } else {
                right = mid - 1; // 目标在左半部分
            }
        }

        return -1; // 未找到目标值
    }

    public static int binarySearchWithoutEquals(int[] arr, int target) {
        if (arr.length == 0) return -1;

        int left = 0;
        int right = arr.length - 1;

        while (left < right) { // 不使用等号，区间长度至少为2
            int mid = left + (right - left) / 2;

            if (arr[mid] < target) {
                left = mid + 1; // 目标在右半部分
            } else {
                right = mid; // 目标在左半部分或就是mid
            }
        }

        // 最终检查剩余元素是否等于目标值
        return arr[left] == target ? left : -1;
    }

    public class BinarySearchRecursive {
        // 递归二分查找
        public static int binarySearch(int[] arr, int target) {
            // 初始调用：左边界0，右边界arr.length - 1
            return search(arr, target, 0, arr.length - 1);
        }

        // 递归辅助函数：参数为数组、目标值、当前左边界、当前右边界
        private static int search(int[] arr, int target, int left, int right) {
            // 终止条件：区间无效（目标不存在）
            if (left > right) {
                return -1;
            }

            // 计算中间索引（避免溢出）
            int mid = left + (right - left) / 2;

            // 找到目标，返回索引
            if (arr[mid] == target) {
                return mid;
            }
            // 目标在右半部分，递归搜索右区间
            else if (arr[mid] < target) {
                return search(arr, target, mid + 1, right);
            }
            // 目标在左半部分，递归搜索左区间
            else {
                return search(arr, target, left, mid - 1);
            }
        }

        // 测试
        public static void main(String[] args) {
            int[] arr = {1, 3, 5, 7, 9, 11};
            System.out.println(binarySearch(arr, 5));  // 输出：2（5在索引2）
            System.out.println(binarySearch(arr, 2));  // 输出：-1（2不存在）
            System.out.println(binarySearch(arr, 11)); // 输出：5（11在索引5）
        }
    }

    //查找元素起始位置
    public class FirstPosition {
        public static int findFirstPosition(int[] arr, int target) {
            int left = 0;
            int right = arr.length - 1;
            int result = -1; // 初始化为-1，若未找到目标值则返回-1

            while (left <= right) {
                int mid = left + (right - left) / 2; // 防止整数溢出

                if (arr[mid] == target) {
                    result = mid; // 记录当前可能的起始位置
                    right = mid - 1; // 继续向左搜索，尝试找到更左侧的目标值
                } else if (arr[mid] < target) {
                    left = mid + 1; // 目标在右半部分
                } else {
                    right = mid - 1; // 目标在左半部分
                }
            }

            return result;
        }

        public static void main(String[] args) {
            int[] arr = {1, 2, 4, 4, 4, 5, 6};
            System.out.println(findFirstPosition(arr, 4)); // 输出：2（第一个4的索引是2）
            System.out.println(findFirstPosition(arr, 7)); // 输出：-1（未找到）
        }
    }
    //查找元素终止位置
    public class LastPosition {
        public static int findLastPosition(int[] arr, int target) {
            int left = 0;
            int right = arr.length - 1;
            int result = -1; // 初始化为-1，若未找到目标值则返回-1

            while (left <= right) {
                int mid = left + (right - left) / 2; // 防止整数溢出

                if (arr[mid] == target) {
                    result = mid; // 记录当前可能的终止位置
                    left = mid + 1; // 继续向右搜索，尝试找到更右侧的目标值
                } else if (arr[mid] < target) {
                    left = mid + 1; // 目标在右半部分
                } else {
                    right = mid - 1; // 目标在左半部分
                }
            }

            return result;
        }

        public static void main(String[] args) {
            int[] arr = {1, 2, 4, 4, 4, 5, 6};
            System.out.println(findLastPosition(arr, 4)); // 输出：4（最后一个4的索引是4）
            System.out.println(findLastPosition(arr, 7)); // 输出：-1（未找到）
        }
    }

    //查找第一个大于等于目标值的位置
    public class FirstGreaterOrEqual {
        public static int findFirstGreaterOrEqual(int[] arr, int target) {
            int left = 0;
            int right = arr.length; // 注意：右边界初始化为arr.length，处理所有元素都小于target的情况

            while (left < right) { // 注意：循环条件为left < right
                int mid = left + (right - left) / 2;

                if (arr[mid] < target) {
                    left = mid + 1; // 目标在右半部分
                } else {
                    right = mid; // 目标在左半部分或就是mid
                }
            }

            // 此时left == right，检查该位置是否满足条件
            if (left < arr.length && arr[left] >= target) {
                return left;
            } else {
                return arr.length; // 所有元素均小于target，返回数组长度
            }
        }

        public static void main(String[] args) {
            int[] arr = {1, 3, 5, 7};
            System.out.println(findFirstGreaterOrEqual(arr, 4)); // 输出：2（第一个≥4的元素是5，索引为2）
            System.out.println(findFirstGreaterOrEqual(arr, 7)); // 输出：3（第一个≥7的元素是7，索引为3）
            System.out.println(findFirstGreaterOrEqual(arr, 8)); // 输出：4（所有元素均<8，返回数组长度4）
        }
    }

    //查找第一个小于等于目标值的位置
    public class FirstLessOrEqual {
        public static int findFirstLessOrEqual(int[] arr, int target) {
            int left = 0;
            int right = arr.length - 1;
            int result = -1; // 初始化为-1，若未找到目标值则返回-1

            while (left <= right) {
                int mid = left + (right - left) / 2;

                if (arr[mid] <= target) {
                    result = mid; // 记录当前可能的位置
                    left = mid + 1; // 继续向右搜索，尝试找到更右侧的目标值
                } else {
                    right = mid - 1; // 目标在左半部分
                }
            }

            return result;
        }

        public static void main(String[] args) {
            int[] arr = {1, 3, 5, 7};
            System.out.println(findFirstLessOrEqual(arr, 4)); // 输出：1（第一个≤4的元素是3，索引为1）
            System.out.println(findFirstLessOrEqual(arr, 7)); // 输出：3（第一个≤7的元素是7，索引为3）
            System.out.println(findFirstLessOrEqual(arr, 0)); // 输出：-1（所有元素均>0）
        }
    }
}
