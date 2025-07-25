public class longestString {
    public class Solution {
        public int longestString(int a, int b, int p) {
            // 当a和b相差不大时，可以尽可能多地使用AB对
            if (Math.abs(a - b) <= p) {
                return a + b + p * 2;
            }
            // 当a比b大很多时，尽可能使用AB对后，剩余的A单独使用
            else if (a > b) {
                return 2 * b + 2 * p + 1;
            }
            // 当b比a大很多时，尽可能使用AB对后，剩余的B单独使用
            else {
                return 2 * a + 2 * p + 1;
            }
        }

        public void main(String[] args) {
            Solution solution = new Solution();
            System.out.println(solution.longestString(2, 3, 1)); // 输出7
        }
    }
}
