package RoundRobin.WeightedRoundRobin;

/**
 * @Description:
 * @Author: wjh
 * @Date: 2025/7/19 16:07
 */
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

// �������ڵ���
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

// ���ؾ������ӿ�
interface LoadBalancer {
    ServerNode select(List<ServerNode> servers);
}

// ��ѯ�㷨
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

// ��Ȩ��ѯ�㷨
class WeightedRoundRobinLoadBalancer implements LoadBalancer {
    private AtomicInteger counter = new AtomicInteger(0);
    private List<ServerNode> expandedServers = new ArrayList<>();

    public WeightedRoundRobinLoadBalancer(List<ServerNode> servers) {
        // ��ʼ����չ�������б�
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

// ����㷨
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

// ��Ȩ����㷨
class WeightedRandomLoadBalancer implements LoadBalancer {
    private Random random = new Random();

    @Override
    public ServerNode select(List<ServerNode> servers) {
        if (servers == null || servers.isEmpty()) {
            return null;
        }

        // ������Ȩ��
        int totalWeight = 0;
        for (ServerNode server : servers) {
            totalWeight += server.getWeight();
        }

        // �������һ��[0, totalWeight)֮�����
        int randomValue = random.nextInt(totalWeight);

        // ȷ������������ĸ�����
        int currentSum = 0;
        for (ServerNode server : servers) {
            currentSum += server.getWeight();
            if (randomValue < currentSum) {
                return server;
            }
        }

        // �����ϲ���ִ�е�����
        return servers.get(0);
    }
}

// ��С�������㷨
class LeastConnectionsLoadBalancer implements LoadBalancer {
    // ģ�⵱ǰ��������ʵ��Ӧ������Ҫ��̬��ȡ��
    private Map<ServerNode, Integer> connectionMap = new HashMap<>();

    @Override
    public ServerNode select(List<ServerNode> servers) {
        if (servers == null || servers.isEmpty()) {
            return null;
        }

        // ��ʼ��������
        for (ServerNode server : servers) {
            connectionMap.putIfAbsent(server, 0);
        }

        // �ҳ����������ٵķ�����
        ServerNode selected = null;
        int minConnections = Integer.MAX_VALUE;

        for (ServerNode server : servers) {
            int connections = connectionMap.get(server);
            if (connections < minConnections) {
                minConnections = connections;
                selected = server;
            }
        }

        // ģ��ѡ�з�������������1
        if (selected != null) {
            connectionMap.put(selected, minConnections + 1);
        }

        return selected;
    }

    // ģ�����ӹرգ���������1
    public void closeConnection(ServerNode server) {
        if (connectionMap.containsKey(server)) {
            int connections = connectionMap.get(server);
            connectionMap.put(server, Math.max(0, connections - 1));
        }
    }
}

// ������
public class LoadBalancingAlgorithms {
    public static void main(String[] args) {
        List<ServerNode> servers = Arrays.asList(
                new ServerNode("192.168.1.1", 5),
                new ServerNode("192.168.1.2", 3),
                new ServerNode("192.168.1.3", 2)
        );

        // ������ѯ�㷨
        System.out.println("===== ��ѯ�㷨 =====");
        LoadBalancer roundRobin = new RoundRobinLoadBalancer();
        for (int i = 0; i < 10; i++) {
            System.out.println("���� " + (i + 1) + " -> " + roundRobin.select(servers));
        }

        // ���Լ�Ȩ��ѯ�㷨
        System.out.println("\n===== ��Ȩ��ѯ�㷨 =====");
        LoadBalancer weightedRoundRobin = new WeightedRoundRobinLoadBalancer(servers);
        for (int i = 0; i < 10; i++) {
            System.out.println("���� " + (i + 1) + " -> " + weightedRoundRobin.select(servers));
        }

        // ��������㷨
        System.out.println("\n===== ����㷨 =====");
        LoadBalancer random = new RandomLoadBalancer();
        for (int i = 0; i < 10; i++) {
            System.out.println("���� " + (i + 1) + " -> " + random.select(servers));
        }

        // ���Լ�Ȩ����㷨
        System.out.println("\n===== ��Ȩ����㷨 =====");
        LoadBalancer weightedRandom = new WeightedRandomLoadBalancer();
        for (int i = 0; i < 10; i++) {
            System.out.println("���� " + (i + 1) + " -> " + weightedRandom.select(servers));
        }

        // ������С�������㷨
        System.out.println("\n===== ��С�������㷨 =====");
        LeastConnectionsLoadBalancer leastConnections = new LeastConnectionsLoadBalancer();
        for (int i = 0; i < 5; i++) {
            ServerNode selected = leastConnections.select(servers);
            System.out.println("���� " + (i + 1) + " -> " + selected);
            // ģ��������ɣ��ر�����
            leastConnections.closeConnection(selected);
        }
    }
}