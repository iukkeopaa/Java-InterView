package TinaJiSaiMa;


import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

//算法思路
//排序：将田忌和齐王的马分别按速度排序（从小到大或从大到小均可，这里采用从小到大）。
//双队列比较：
//比较双方最快的马：如果田忌的最快马比齐王的快，则直接对抗。
//如果田忌的最快马不如齐王，则用田忌最慢的马消耗齐王最快的马。
//如果两者最快的马速度相同，则比较双方最慢的马：
//若田忌最慢的马更快，则用它对抗齐王最慢的马。
//否则，用田忌最慢的马消耗齐王最快的马。
public class solution {



    public class TianJiHorseRacing {
        public static int[] solve(int[] tianji, int[] qiwang) {
            // 复制数组并排序
            int[] tianjiSorted = Arrays.copyOf(tianji, tianji.length);
            int[] qiwangSorted = Arrays.copyOf(qiwang, qiwang.length);
            Arrays.sort(tianjiSorted);
            Arrays.sort(qiwangSorted);

            // 初始化队列
            Queue<Integer> tianjiQueue = new LinkedList<>();
            Queue<Integer> qiwangQueue = new LinkedList<>();
            for (int horse : tianjiSorted) {
                tianjiQueue.offer(horse);
            }
            for (int horse : qiwangSorted) {
                qiwangQueue.offer(horse);
            }

            // 存储比赛顺序
            Queue<Integer> raceOrder = new LinkedList<>();

            while (!qiwangQueue.isEmpty()) {
                // 取出齐王最快的马
                int qiwangFastest = qiwangQueue.poll();

                // 比较田忌最快的马与齐王最快的马
                if (((LinkedList<Integer>) tianjiQueue).peekLast() > qiwangFastest) {
                    // 田忌最快的马更快，用它比赛
                    raceOrder.offer(((LinkedList<Integer>) tianjiQueue).pollLast());
                } else {
                    // 田忌最快的马不如齐王，用最慢的马消耗
                    raceOrder.offer(tianjiQueue.poll());
                }
            }

            // 转换为数组返回
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
            System.out.println("田忌的比赛顺序：" + Arrays.toString(result));
            // 输出可能为 [1, 3, 2]，对应齐王的 [3, 2, 1]，田忌2胜1负
        }
    }
}
