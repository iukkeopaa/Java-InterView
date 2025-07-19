package RoundRobin.WeightedRoundRobin;

/**
 * @Description:
 * @Author: wjh
 * @Date: 2025/7/19 16:06
 */
import java.util.List;



// 优化版加权轮询负载均衡器
public class WeightedRoundRobin {
    private List<ServerNode> servers;
    private int currentIndex = -1;
    private int currentWeight = 0;
    private int gcd; // 最大公约数
    private int maxWeight; // 最大权重

    public WeightedRoundRobin(List<ServerNode> servers) {
        this.servers = servers;
        this.gcd = calculateGCD();
        this.maxWeight = findMaxWeight();
    }

    /**
     * 计算所有服务器权重的最大公约数
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
     * 计算两个数的最大公约数
     */
    private int greatestCommonDivisor(int a, int b) {
        return b == 0 ? a : greatestCommonDivisor(b, a % b);
    }

    /**
     * 查找最大权重
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
     * 选择下一个服务器
     */
    public synchronized ServerNode select() {
        while (true) {
            currentIndex = (currentIndex + 1) % servers.size();
            if (currentIndex == 0) {
                currentWeight = currentWeight - gcd;
                if (currentWeight <= 0) {
                    currentWeight = maxWeight;
                    if (currentWeight == 0) {
                        return null; // 所有服务器权重为0
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

        // 模拟10次请求
        for (int i = 1; i <= 10; i++) {
            System.out.println("请求 " + i + " => " + balancer.select());
        }
    }
}