public class longestString {
    public class Solution {
        public int longestString(int a, int b, int p) {
            // ��a��b����ʱ�����Ծ����ܶ��ʹ��AB��
            if (Math.abs(a - b) <= p) {
                return a + b + p * 2;
            }
            // ��a��b��ܶ�ʱ��������ʹ��AB�Ժ�ʣ���A����ʹ��
            else if (a > b) {
                return 2 * b + 2 * p + 1;
            }
            // ��b��a��ܶ�ʱ��������ʹ��AB�Ժ�ʣ���B����ʹ��
            else {
                return 2 * a + 2 * p + 1;
            }
        }

        public void main(String[] args) {
            Solution solution = new Solution();
            System.out.println(solution.longestString(2, 3, 1)); // ���7
        }
    }
}
