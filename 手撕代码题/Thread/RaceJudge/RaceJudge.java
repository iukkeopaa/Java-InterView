package Thread.RaceJudge;

/**
 * @Description: 有 5 个?赛跑，请你设计?个多线程的裁判程序给出他们赛跑的结果顺序，5 个?的速度随机处理。我们借助线程池和 CountDownLatch 来实现这?需求即可。
 * @Author: wjh
 * @Date: 2025/7/19 15:29
 */
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RaceJudge {
    // 存储比赛结果：按到达顺序记录选手ID（1-5）
    private static final List<Integer> result = Collections.synchronizedList(new ArrayList<>());
    // 选手数量
    private static final int RUNNER_COUNT = 5;

    public static void main(String[] args) throws InterruptedException {
        // 发令 latch：1个发令信号，释放后所有选手开始
        CountDownLatch startLatch = new CountDownLatch(1);
        // 结束 latch：等待5个选手都到达
        CountDownLatch endLatch = new CountDownLatch(RUNNER_COUNT);

        // 创建线程池（5个线程模拟5个选手）
        ExecutorService executor = Executors.newFixedThreadPool(RUNNER_COUNT);

        // 初始化5个选手
        for (int i = 1; i <= RUNNER_COUNT; i++) {
            int runnerId = i;
            executor.submit(() -> {
                try {
                    // 等待裁判发令
                    System.out.println("选手" + runnerId + "准备就绪，等待发令...");
                    startLatch.await();

                    // 模拟随机跑步时间（1-5秒）
                    int runTime = new Random().nextInt(5) + 1;
                    Thread.sleep(runTime * 1000);

                    // 到达终点，记录顺序
                    synchronized (result) {
                        result.add(runnerId);
                        System.out.println("选手" + runnerId + "到达终点！当前排名第" + result.size() + "名");
                    }

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    // 通知裁判该选手已完成
                    endLatch.countDown();
                }
            });
        }

        // 裁判发令
        System.out.println("\n裁判：各就各位，预备——跑！");
        startLatch.countDown(); // 释放所有选手

        // 等待所有选手完成比赛
        endLatch.await();
        System.out.println("\n===== 比赛结束，最终排名 =====");
        for (int i = 0; i < result.size(); i++) {
            System.out.println("第" + (i + 1) + "名：选手" + result.get(i));
        }

        // 关闭线程池
        executor.shutdown();
    }
}