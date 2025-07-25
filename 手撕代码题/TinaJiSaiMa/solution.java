package TinaJiSaiMa;


import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

//�㷨˼·
//���򣺽���ɺ���������ֱ��ٶ����򣨴�С�����Ӵ�С���ɣ�������ô�С���󣩡�
//˫���бȽϣ�
//�Ƚ�˫�������������ɵ������������Ŀ죬��ֱ�ӶԿ���
//�����ɵ�������������������������������������������
//��������������ٶ���ͬ����Ƚ�˫����������
//���������������죬�������Կ�������������
//�������������������������������
public class solution {



    public class TianJiHorseRacing {
        public static int[] solve(int[] tianji, int[] qiwang) {
            // �������鲢����
            int[] tianjiSorted = Arrays.copyOf(tianji, tianji.length);
            int[] qiwangSorted = Arrays.copyOf(qiwang, qiwang.length);
            Arrays.sort(tianjiSorted);
            Arrays.sort(qiwangSorted);

            // ��ʼ������
            Queue<Integer> tianjiQueue = new LinkedList<>();
            Queue<Integer> qiwangQueue = new LinkedList<>();
            for (int horse : tianjiSorted) {
                tianjiQueue.offer(horse);
            }
            for (int horse : qiwangSorted) {
                qiwangQueue.offer(horse);
            }

            // �洢����˳��
            Queue<Integer> raceOrder = new LinkedList<>();

            while (!qiwangQueue.isEmpty()) {
                // ȡ������������
                int qiwangFastest = qiwangQueue.poll();

                // �Ƚ����������������������
                if (((LinkedList<Integer>) tianjiQueue).peekLast() > qiwangFastest) {
                    // �����������죬��������
                    raceOrder.offer(((LinkedList<Integer>) tianjiQueue).pollLast());
                } else {
                    // �������������������������������
                    raceOrder.offer(tianjiQueue.poll());
                }
            }

            // ת��Ϊ���鷵��
            int[] result = new int[raceOrder.size()];
            for (int i = 0; i < result.length; i++) {
                result[i] = raceOrder.poll();
            }
            return result;
        }

        public static void main(String[] args) {
            int[] tianji = {3, 1, 2};
            int[] qiwang = {2, 1, 3};
            int[] result = solve(tianji, qiwang);
            System.out.println("��ɵı���˳��" + Arrays.toString(result));
            // �������Ϊ [1, 3, 2]����Ӧ������ [3, 2, 1]�����2ʤ1��
        }
    }
}
