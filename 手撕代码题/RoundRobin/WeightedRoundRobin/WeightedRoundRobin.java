package RoundRobin.WeightedRoundRobin;

/**
 * @Description:
 * @Author: wjh
 * @Date: 2025/7/19 16:06
 */
import java.util.List;



// �Ż����Ȩ��ѯ���ؾ�����
public class WeightedRoundRobin {
    private List<ServerNode> servers;
    private int currentIndex = -1;
    private int currentWeight = 0;
    private int gcd; // ���Լ��
    private int maxWeight; // ���Ȩ��

    public WeightedRoundRobin(List<ServerNode> servers) {
        this.servers = servers;
        this.gcd = calculateGCD();
        this.maxWeight = findMaxWeight();
    }

    /**
     * �������з�����Ȩ�ص����Լ��
     */
    private int calculateGCD() {
        if (servers == null || servers.isEmpty()) return 1;
        int gcd = servers.get(0).getWeight();
        for (int i = 1; i < servers.size(); i++) {
            gcd = greatestCommonDivisor(gcd, servers.get(i).getWeight());
        }
        return gcd;
    }

    /**
     * ���������������Լ��
     */
    private int greatestCommonDivisor(int a, int b) {
        return b == 0 ? a : greatestCommonDivisor(b, a % b);
    }

    /**
     * �������Ȩ��
     */
    private int findMaxWeight() {
        int max = 0;
        for (ServerNode server : servers) {
            if (server.getWeight() > max) {
                max = server.getWeight();
            }
        }
        return max;
    }

    /**
     * ѡ����һ��������
     */
    public synchronized ServerNode select() {
        while (true) {
            currentIndex = (currentIndex + 1) % servers.size();
            if (currentIndex == 0) {
                currentWeight = currentWeight - gcd;
                if (currentWeight <= 0) {
                    currentWeight = maxWeight;
                    if (currentWeight == 0) {
                        return null; // ���з�����Ȩ��Ϊ0
                    }
                }
            }

            ServerNode server = servers.get(currentIndex);
            if (server.getWeight() >= currentWeight) {
                return server;
            }
        }
    }

    public static void main(String[] args) {
        List<ServerNode> servers = List.of(
                new ServerNode("192.168.1.1", 5),
                new ServerNode("192.168.1.2", 3),
                new ServerNode("192.168.1.3", 1)
        );

        WeightedRoundRobin balancer = new WeightedRoundRobin(servers);

        // ģ��10������
        for (int i = 1; i <= 10; i++) {
            System.out.println("���� " + i + " => " + balancer.select());
        }
    }
}