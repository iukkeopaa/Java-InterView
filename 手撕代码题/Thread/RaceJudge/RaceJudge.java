package Thread.RaceJudge;

/**
 * @Description: �� 5 ��?���ܣ��������?�����̵߳Ĳ��г�������������ܵĽ��˳��5 ��?���ٶ�����������ǽ����̳߳غ� CountDownLatch ��ʵ����?���󼴿ɡ�
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
    // �洢���������������˳���¼ѡ��ID��1-5��
    private static final List<Integer> result = Collections.synchronizedList(new ArrayList<>());
    // ѡ������
    private static final int RUNNER_COUNT = 5;

    public static void main(String[] args) throws InterruptedException {
        // ���� latch��1�������źţ��ͷź�����ѡ�ֿ�ʼ
        CountDownLatch startLatch = new CountDownLatch(1);
        // ���� latch���ȴ�5��ѡ�ֶ�����
        CountDownLatch endLatch = new CountDownLatch(RUNNER_COUNT);

        // �����̳߳أ�5���߳�ģ��5��ѡ�֣�
        ExecutorService executor = Executors.newFixedThreadPool(RUNNER_COUNT);

        // ��ʼ��5��ѡ��
        for (int i = 1; i <= RUNNER_COUNT; i++) {
            int runnerId = i;
            executor.submit(() -> {
                try {
                    // �ȴ����з���
                    System.out.println("ѡ��" + runnerId + "׼���������ȴ�����...");
                    startLatch.await();

                    // ģ������ܲ�ʱ�䣨1-5�룩
                    int runTime = new Random().nextInt(5) + 1;
                    Thread.sleep(runTime * 1000);

                    // �����յ㣬��¼˳��
                    synchronized (result) {
                        result.add(runnerId);
                        System.out.println("ѡ��" + runnerId + "�����յ㣡��ǰ������" + result.size() + "��");
                    }

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    // ֪ͨ���и�ѡ�������
                    endLatch.countDown();
                }
            });
        }

        // ���з���
        System.out.println("\n���У����͸�λ��Ԥ�������ܣ�");
        startLatch.countDown(); // �ͷ�����ѡ��

        // �ȴ�����ѡ����ɱ���
        endLatch.await();
        System.out.println("\n===== ������������������ =====");
        for (int i = 0; i < result.size(); i++) {
            System.out.println("��" + (i + 1) + "����ѡ��" + result.get(i));
        }

        // �ر��̳߳�
        executor.shutdown();
    }
}