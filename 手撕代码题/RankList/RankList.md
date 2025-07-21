### 基于 Redis ZSet 的排行榜设计方案

Redis 的有序集合（ZSet）是实现排行榜的理想选择，它通过分数（score）自动排序，支持高效的插入、删除和范围查询操作。以下是一个完整的排行榜系统设计：

### **核心功能设计**

#### **1. 数据结构选择**

- **ZSet 存储主体数据**：
    - **Key**：排行榜名称（如 `leaderboard:global`）。
    - **Member**：用户 ID 或实体标识。
    - **Score**：用户分数（如积分、等级、活跃度等）。
- **Hash 存储用户详细信息**（可选）：
    - **Key**：用户信息前缀 + 用户 ID（如 `user:info:{userId}`）。
    - **Field-Value**：存储用户昵称、头像、最后更新时间等。

#### **2. 核心接口设计**

java



运行









```java
public interface LeaderboardService {
    // 添加/更新用户分数
    void addScore(String leaderboardKey, String userId, double score);
    
    // 获取用户排名（排名从 0 开始，分数越高排名越前）
    Long getRank(String leaderboardKey, String userId);
    
    // 获取用户分数
    Double getScore(String leaderboardKey, String userId);
    
    // 获取排行榜前 N 名
    List<LeaderboardEntry> getTopN(String leaderboardKey, int n);
    
    // 获取指定范围的排名（如第 10-20 名）
    List<LeaderboardEntry> getRange(String leaderboardKey, int start, int end);
    
    // 获取用户所在页的排行榜（如当前用户在第 100 名，获取第 91-100 名）
    List<LeaderboardEntry> getPageAroundUser(String leaderboardKey, String userId, int pageSize);
    
    // 删除排行榜
    void deleteLeaderboard(String leaderboardKey);
}
```

#### **3. 数据模型**

java



运行









```java
@Data // Lombok 注解
public class LeaderboardEntry {
    private String userId;
    private double score;
    private long rank; // 排名（从 1 开始）
    private Map<String, String> userInfo; // 可选：用户详细信息
}
```

### **实现示例（基于 Jedis）**

java



运行









```java
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;
import java.util.*;

public class RedisLeaderboardService implements LeaderboardService {
    private final Jedis jedis;
    private static final String USER_INFO_PREFIX = "user:info:";

    public RedisLeaderboardService(Jedis jedis) {
        this.jedis = jedis;
    }

    @Override
    public void addScore(String leaderboardKey, String userId, double score) {
        jedis.zadd(leaderboardKey, score, userId);
    }

    @Override
    public Long getRank(String leaderboardKey, String userId) {
        // ZREVRANK 返回分数从高到低的排名（0 表示第一名）
        return jedis.zrevrank(leaderboardKey, userId);
    }

    @Override
    public Double getScore(String leaderboardKey, String userId) {
        return jedis.zscore(leaderboardKey, userId);
    }

    @Override
    public List<LeaderboardEntry> getTopN(String leaderboardKey, int n) {
        // ZREVRANGEBYSCORE 获取分数从高到低的前 N 名
        Set<Tuple> topN = jedis.zrevrangeByScoreWithScores(leaderboardKey, "+inf", "-inf", 0, n);
        return buildEntries(leaderboardKey, topN);
    }

    @Override
    public List<LeaderboardEntry> getRange(String leaderboardKey, int start, int end) {
        // ZREVRANGE 获取排名在 [start, end] 之间的用户（从 0 开始）
        Set<Tuple> range = jedis.zrevrangeWithScores(leaderboardKey, start, end);
        return buildEntries(leaderboardKey, range);
    }

    @Override
    public List<LeaderboardEntry> getPageAroundUser(String leaderboardKey, String userId, int pageSize) {
        Long rank = getRank(leaderboardKey, userId);
        if (rank == null) return Collections.emptyList();
        
        // 计算当前页的起始位置（确保不越界）
        int start = Math.max(0, rank - pageSize / 2);
        int end = start + pageSize - 1;
        
        return getRange(leaderboardKey, start, end);
    }

    @Override
    public void deleteLeaderboard(String leaderboardKey) {
        jedis.del(leaderboardKey);
    }

    // 构建排行榜条目（包含排名信息）
    private List<LeaderboardEntry> buildEntries(String leaderboardKey, Set<Tuple> tuples) {
        List<LeaderboardEntry> entries = new ArrayList<>();
        long rank = getRank(leaderboardKey, tuples.iterator().next().getElement()); // 获取第一名的排名
        
        for (Tuple tuple : tuples) {
            LeaderboardEntry entry = new LeaderboardEntry();
            entry.setUserId(tuple.getElement());
            entry.setScore(tuple.getScore());
            entry.setRank(++rank); // 排名从 1 开始
            entries.add(entry);
        }
        
        return entries;
    }

    // 可选：批量获取用户详细信息
    public void fetchUserInfoBatch(List<LeaderboardEntry> entries) {
        for (LeaderboardEntry entry : entries) {
            String userKey = USER_INFO_PREFIX + entry.getUserId();
            Map<String, String> userInfo = jedis.hgetAll(userKey);
            entry.setUserInfo(userInfo);
        }
    }
}
```

### **高级特性实现**

#### **1. 时效性排行榜（按日 / 周 / 月统计）**

java



运行









```java
// 生成按日统计的排行榜 Key
public String getDailyLeaderboardKey(String baseKey, Date date) {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    return baseKey + ":daily:" + sdf.format(date);
}

// 定时将日排行榜数据合并到总排行榜
public void mergeDailyToGlobal(String dailyKey, String globalKey) {
    // 使用 ZUNIONSTORE 合并多个有序集合
    jedis.zunionstore(globalKey, dailyKey);
}
```

#### **2. 权重计算（多维度分数）**

java



运行









```java
// 计算综合得分（如：80% 活跃度 + 20% 胜率）
public double calculateCompositeScore(double activity, double winRate) {
    return activity * 0.8 + winRate * 0.2;
}

// 更新多维度分数并计算综合得分
public void updateMultiDimensionalScore(String userId, double activity, double winRate) {
    double compositeScore = calculateCompositeScore(activity, winRate);
    addScore("leaderboard:composite", userId, compositeScore);
    
    // 可选：单独存储各维度分数，用于数据分析
    jedis.hset("user:scores:" + userId, "activity", String.valueOf(activity));
    jedis.hset("user:scores:" + userId, "winRate", String.valueOf(winRate));
}
```

### **性能优化建议**

1. **批量操作**：

    - 使用 `Pipeline` 或 `Transaction` 批量处理多个请求，减少网络开销。

   java



运行









   ```java
   Pipeline pipeline = jedis.pipelined();
   pipeline.zadd("leaderboard", score1, userId1);
   pipeline.zadd("leaderboard", score2, userId2);
   pipeline.sync();
   ```

2. **缓存热点数据**：

    - 对于频繁访问的 Top N 数据，使用本地缓存（如 Guava Cache）减少 Redis 压力。

3. **分片处理**：

    - 对于超大规模排行榜，按业务维度分片（如按地区、等级分组），降低单个 ZSet 的数据量。

### **使用示例**

java



运行









```java
public static void main(String[] args) {
    Jedis jedis = new Jedis("localhost", 6379);
    LeaderboardService service = new RedisLeaderboardService(jedis);
    
    // 添加用户分数
    service.addScore("leaderboard:game", "user1", 1000);
    service.addScore("leaderboard:game", "user2", 1200);
    service.addScore("leaderboard:game", "user3", 900);
    
    // 获取排名
    System.out.println("User2 rank: " + service.getRank("leaderboard:game", "user2")); // 输出: 0 (第一名)
    
    // 获取 Top 2
    List<LeaderboardEntry> top2 = service.getTopN("leaderboard:game", 2);
    System.out.println(top2); // 输出: [user2(1200), user1(1000)]
}
```






### **总结**

Redis ZSet 是实现排行榜的最佳选择，其优势在于：



- **高效排序**：插入 / 删除 / 查询操作时间复杂度均为 O (log n)。
- **原子性操作**：单个命令保证原子性，多个命令可通过 Lua 脚本实现原子性。
- **丰富的排名操作**：支持正序 / 逆序排名、范围查询等。


## 

### Redis ZSet 底层实现解析

Redis 的 ZSet（有序集合）是一种高性能的排序数据结构，它结合了 **哈希表** 和 **跳表（Skip List）** 的优势，同时支持 **O (1) 时间的 member 到 score 的映射** 和 **O (log n) 时间的范围查询**。以下是其核心实现原理：

### **数据结构组合**

ZSet 底层使用两种数据结构：



1. **哈希表（Hash Table）**：
    - **键值对**：`member → score`
    - **作用**：快速查找某个 member 的 score（O (1) 时间复杂度）。
2. **跳表（Skip List）**：
    - **节点结构**：每个节点包含 `member`、`score`、**多层指针** 和 **跨度（span）**。
    - **作用**：支持按 score 排序、快速范围查询（O (log n) 时间复杂度）。

### **跳表的关键设计**

跳表是 ZSet 实现排序和范围查询的核心，其关键设计包括：



1. **多层索引结构**：
    - 每个节点的层级（level）是随机生成的（通常为 1~32 层）。
    - 高层级的节点可以跳过多个低层级节点，加速查找。
2. **跨度（Span）**：
    - 每个节点的指针除了指向下一个节点，还维护一个 **跨度值**，表示到下一个节点的距离（即中间跳过的节点数）。
    - **作用**：快速计算排名（rank）。
3. **双向指针**：
    - 每个节点包含前驱指针（backward），支持逆序遍历。

### **核心操作实现**

#### **1. 根据 member 查询 rank**

java



运行









```java
// 伪代码：获取 member 的排名（从 1 开始）
long getRank(String member) {
    // 1. 通过哈希表快速查找 score
    double score = hashTable.get(member);
    
    // 2. 从跳表头部开始，逐层向下查找
    Node current = header;
    long rank = 0;
    
    for (int i = maxLevel - 1; i >= 0; i--) {
        while (current.forward[i] != null && 
               (current.forward[i].score < score || 
                (current.forward[i].score == score && 
                 current.forward[i].member.compareTo(member) < 0))) {
            // 累加跨度，更新排名
            rank += current.span[i];
            current = current.forward[i];
        }
    }
    
    // 找到目标节点后，rank + 1 即为最终排名
    return rank + 1;
}
```



**复杂度**：O (log n)，主要来自跳表的查找。

#### **2. 根据 rank 查询 member**

java



运行









```java
// 伪代码：获取指定 rank 的 member（rank 从 1 开始）
String getMemberByRank(long rank) {
    Node current = header;
    long traversed = 0;  // 已遍历的节点数
    
    for (int i = maxLevel - 1; i >= 0; i--) {
        while (current.forward[i] != null && traversed + current.span[i] <= rank) {
            traversed += current.span[i];
            current = current.forward[i];
        }
        
        if (traversed == rank) {
            return current.member;
        }
    }
    
    return null;  // 排名不存在
}
```



**复杂度**：O (log n)，通过跳表的跨度快速定位。

#### **3. 根据 score 查询 rank 范围**

java



运行









```java
// 伪代码：获取 score 在 [min, max] 范围内的所有 member 的 rank
List<Long> getRanksByScore(double min, double max) {
    List<Long> ranks = new ArrayList<>();
    Node current = header;
    long rank = 0;
    
    // 1. 找到第一个 score >= min 的节点
    for (int i = maxLevel - 1; i >= 0; i--) {
        while (current.forward[i] != null && current.forward[i].score < min) {
            rank += current.span[i];
            current = current.forward[i];
        }
    }
    
    // 2. 从该节点开始，收集所有 score <= max 的节点的 rank
    current = current.forward[0];  // 跳到下一个节点（可能是第一个 >= min 的节点）
    
    while (current != null && current.score <= max) {
        ranks.add(rank + 1);  // rank 从 1 开始
        rank++;
        current = current.forward[0];
    }
    
    return ranks;
}
```



**复杂度**：O (log n + m)，其中 m 是结果集的大小。

### **跳表节点结构示例**

java



运行









```java
class Node {
    String member;  // 成员
    double score;   // 分数
    
    // 多层指针数组，每个元素包含：
    // - forward：指向下一个节点
    // - span：到下一个节点的跨度
    Level[] levels;
    
    // 前驱指针（用于逆序遍历）
    Node backward;
}

class Level {
    Node forward;  // 指向下一个节点
    long span;     // 到下一个节点的跨度（跳过的节点数）
}
```





![img](data:image/png;base64,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)

### **总结**

Redis ZSet 通过 **哈希表 + 跳表** 的组合实现了高效的排序和查询：



1. **哈希表** 保证了 O (1) 的 member 到 score 的映射。

2. 跳表



通过多层索引和跨度优化，实现了：

- O (log n) 的按 score 排序和范围查询。
- O (log n) 的按 rank 查询 member。
- O (log n) 的按 member 查询 rank。



这种设计在保证查询效率的同时，简化了实现复杂度（相比红黑树等结构），是 Redis 高性能的关键之一。



### **示例场景：游戏玩家排行榜**

假设我们有一个游戏排行榜，记录了玩家的分数和排名。现有 5 个玩家数据：



| 玩家 ID (member) | 分数 (score) |
| ---------------- | ------------ |
| player1          | 100          |
| player2          | 200          |
| player3          | 150          |
| player4          | 300          |
| player5          | 150          |

### **ZSet 底层数据结构表示**

#### **1. 哈希表部分**

plaintext











```plaintext
{
  "player1": 100,
  "player2": 200,
  "player3": 150,
  "player4": 300,
  "player5": 150
}
```

#### **2. 跳表部分**

假设跳表的节点层级随机生成如下（简化示例，实际可能更复杂）：



plaintext











```plaintext
Header
  |
  v
Level 3: [ ] → [player4:300] → NULL
Level 2: [ ] → [player2:200] → [player4:300] → NULL
Level 1: [ ] → [player1:100] → [player3:150] → [player5:150] → [player2:200] → [player4:300] → NULL
```



每个节点的跨度（span）标注如下（为简化，仅显示 Level 1 的跨度）：



plaintext











```plaintext
Header → [player1:100, span=1] → [player3:150, span=1] → [player5:150, span=1] → [player2:200, span=1] → [player4:300, span=1] → NULL
```

### **核心操作示例**

#### **1. 根据 member 查询 rank**

**需求**：查询 `player3` 的排名。
**步骤**：



1. **哈希表查找**：通过哈希表直接获取 `player3` 的 score 为 150。

2. 跳表计算 rank

   ：

    - 从跳表头部开始，逐层向下查找第一个 score ≥ 150 的节点：

      plaintext











     ```plaintext
     Header → player1(100) → player3(150)  // 累计跨度 = 1 + 1 = 2
     ```

- 最终 rank = 2（排名从 1 开始）。
  **结果**：`player3` 的排名为 **2**。

#### **2. 根据 rank 查询 member**

**需求**：查询排名第 3 的玩家。
**步骤**：



1. 跳表遍历

   ：从跳表头部开始，逐层累加跨度，直到找到第 3 个节点：

   plaintext











   ```plaintext
   Header → player1(100, span=1) → player3(150, span=1) → player5(150, span=1)  // 累计跨度 = 1 + 1 + 1 = 3
   ```

- 第 3 个节点为 `player5`。
  **结果**：排名第 3 的玩家是 `player5`。

#### **3. 根据 score 查询 rank 范围**

**需求**：查询分数在 [150, 200] 范围内的所有玩家及其排名。
**步骤**：



1. 跳表定位起始节点

   ：找到第一个 score ≥ 150 的节点：

   plaintext











   ```plaintext
   Header → player1(100) → player3(150)  // 起始节点为 player3
   ```

2. 遍历收集结果

   ：从



   ```
   player3
   ```



开始，收集所有 score ≤ 200 的节点及其排名：

plaintext











   ```plaintext
   player3(150, rank=2) → player5(150, rank=3) → player2(200, rank=4)
   ```



**结果**：



- `player3`（排名 2）
- `player5`（排名 3）
- `player2`（排名 4）

### **时间复杂度验证**

1. **根据 member 查询 rank**：
    - 哈希表查找 O (1) + 跳表查找 O (log n) → **O(log n)**
2. **根据 rank 查询 member**：
    - 跳表通过跨度快速定位 → **O(log n)**
3. **根据 score 查询 rank 范围**：
    - 跳表定位起始点 O (log n) + 遍历结果 O (m) → **O(log n + m)**

### **关键优势总结**

1. **高效的排序**：跳表通过多层索引和跨度，将排序操作优化到 O (log n)。
2. **灵活的查询**：支持按 member、rank、score 多种维度查询，且复杂度均为 O (log n) 级别。
3. **空间与效率的平衡**：跳表的随机层级设计在保证效率的同时，避免了红黑树等结构的复杂维护。



通过这个例子，你可以直观理解 Redis ZSet 如何通过哈希表和跳表的组合，实现高性能的排行榜功能。