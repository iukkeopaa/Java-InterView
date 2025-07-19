package RoundRobin.WeightedRoundRobin;

/**
 * @Description:
 * @Author: wjh
 * @Date: 2025/7/19 16:07
 */
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

// 服务器节点类
class ServerNode {
    private String ip;
    private int weight;

    public ServerNode(String ip, int weight) {
        this.ip = ip;
        this.weight = weight;
    }

    public String getIp() {
        return ip;
    }

    public int getWeight() {
        return weight;
    }

    @Override
    public String toString() {
        return "ServerNode{ip='" + ip + "', weight=" + weight + '}';
    }
}

// 负载均衡器接口
interface LoadBalancer {
    ServerNode select(List<ServerNode> servers);
}

// 轮询算法
class RoundRobinLoadBalancer implements LoadBalancer {
    private AtomicInteger counter = new AtomicInteger(0);

    @Override
    public ServerNode select(List<ServerNode> servers) {
        if (servers == null || servers.isEmpty()) {
            return null;
        }
        int index = counter.getAndIncrement() % servers.size();
        return servers.get(index);
    }
}

// 加权轮询算法
class WeightedRoundRobinLoadBalancer implements LoadBalancer {
    private AtomicInteger counter = new AtomicInteger(0);
    private List<ServerNode> expandedServers = new ArrayList<>();

    public WeightedRoundRobinLoadBalancer(List<ServerNode> servers) {
        // 初始化扩展服务器列表
        for (ServerNode server : servers) {
            for (int i = 0; i < server.getWeight(); i++) {
                expandedServers.add(server);
            }
        }
    }

    @Override
    public ServerNode select(List<ServerNode> servers) {
        if (expandedServers.isEmpty()) {
            return null;
        }
        int index = counter.getAndIncrement() % expandedServers.size();
        return expandedServers.get(index);
    }
}

// 随机算法
class RandomLoadBalancer implements LoadBalancer {
    private Random random = new Random();

    @Override
    public ServerNode select(List<ServerNode> servers) {
        if (servers == null || servers.isEmpty()) {
            return null;
        }
        return servers.get(random.nextInt(servers.size()));
    }
}

// 加权随机算法
class WeightedRandomLoadBalancer implements LoadBalancer {
    private Random random = new Random();

    @Override
    public ServerNode select(List<ServerNode> servers) {
        if (servers == null || servers.isEmpty()) {
            return null;
        }

        // 计算总权重
        int totalWeight = 0;
        for (ServerNode server : servers) {
            totalWeight += server.getWeight();
        }

        // 随机生成一个[0, totalWeight)之间的数
        int randomValue = random.nextInt(totalWeight);

        // 确定随机数落在哪个区间
        int currentSum = 0;
        for (ServerNode server : servers) {
            currentSum += server.getWeight();
            if (randomValue < currentSum) {
                return server;
            }
        }

        // 理论上不会执行到这里
        return servers.get(0);
    }
}

// 最小连接数算法
class LeastConnectionsLoadBalancer implements LoadBalancer {
    // 模拟当前连接数（实际应用中需要动态获取）
    private Map<ServerNode, Integer> connectionMap = new HashMap<>();

    @Override
    public ServerNode select(List<ServerNode> servers) {
        if (servers == null || servers.isEmpty()) {
            return null;
        }

        // 初始化连接数
        for (ServerNode server : servers) {
            connectionMap.putIfAbsent(server, 0);
        }

        // 找出连接数最少的服务器
        ServerNode selected = null;
        int minConnections = Integer.MAX_VALUE;

        for (ServerNode server : servers) {
            int connections = connectionMap.get(server);
            if (connections < minConnections) {
                minConnections = connections;
                selected = server;
            }
        }

        // 模拟选中服务器连接数加1
        if (selected != null) {
            connectionMap.put(selected, minConnections + 1);
        }

        return selected;
    }

    // 模拟连接关闭，连接数减1
    public void closeConnection(ServerNode server) {
        if (connectionMap.containsKey(server)) {
            int connections = connectionMap.get(server);
            connectionMap.put(server, Math.max(0, connections - 1));
        }
    }
}

// 测试类
public class LoadBalancingAlgorithms {
    public static void main(String[] args) {
        List<ServerNode> servers = Arrays.asList(
                new ServerNode("192.168.1.1", 5),
                new ServerNode("192.168.1.2", 3),
                new ServerNode("192.168.1.3", 2)
        );

        // 测试轮询算法
        System.out.println("===== 轮询算法 =====");
        LoadBalancer roundRobin = new RoundRobinLoadBalancer();
        for (int i = 0; i < 10; i++) {
            System.out.println("请求 " + (i + 1) + " -> " + roundRobin.select(servers));
        }

        // 测试加权轮询算法
        System.out.println("\n===== 加权轮询算法 =====");
        LoadBalancer weightedRoundRobin = new WeightedRoundRobinLoadBalancer(servers);
        for (int i = 0; i < 10; i++) {
            System.out.println("请求 " + (i + 1) + " -> " + weightedRoundRobin.select(servers));
        }

        // 测试随机算法
        System.out.println("\n===== 随机算法 =====");
        LoadBalancer random = new RandomLoadBalancer();
        for (int i = 0; i < 10; i++) {
            System.out.println("请求 " + (i + 1) + " -> " + random.select(servers));
        }

        // 测试加权随机算法
        System.out.println("\n===== 加权随机算法 =====");
        LoadBalancer weightedRandom = new WeightedRandomLoadBalancer();
        for (int i = 0; i < 10; i++) {
            System.out.println("请求 " + (i + 1) + " -> " + weightedRandom.select(servers));
        }

        // 测试最小连接数算法
        System.out.println("\n===== 最小连接数算法 =====");
        LeastConnectionsLoadBalancer leastConnections = new LeastConnectionsLoadBalancer();
        for (int i = 0; i < 5; i++) {
            ServerNode selected = leastConnections.select(servers);
            System.out.println("请求 " + (i + 1) + " -> " + selected);
            // 模拟请求完成，关闭连接
            leastConnections.closeConnection(selected);
        }
    }
}