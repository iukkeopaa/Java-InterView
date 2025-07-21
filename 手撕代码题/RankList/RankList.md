### ���� Redis ZSet �����а���Ʒ���

Redis �����򼯺ϣ�ZSet����ʵ�����а������ѡ����ͨ��������score���Զ�����֧�ָ�Ч�Ĳ��롢ɾ���ͷ�Χ��ѯ������������һ�����������а�ϵͳ��ƣ�

### **���Ĺ������**

#### **1. ���ݽṹѡ��**

- **ZSet �洢��������**��
    - **Key**�����а����ƣ��� `leaderboard:global`����
    - **Member**���û� ID ��ʵ���ʶ��
    - **Score**���û�����������֡��ȼ�����Ծ�ȵȣ���
- **Hash �洢�û���ϸ��Ϣ**����ѡ����
    - **Key**���û���Ϣǰ׺ + �û� ID���� `user:info:{userId}`����
    - **Field-Value**���洢�û��ǳơ�ͷ��������ʱ��ȡ�

#### **2. ���Ľӿ����**

java



����









```java
public interface LeaderboardService {
    // ���/�����û�����
    void addScore(String leaderboardKey, String userId, double score);
    
    // ��ȡ�û������������� 0 ��ʼ������Խ������Խǰ��
    Long getRank(String leaderboardKey, String userId);
    
    // ��ȡ�û�����
    Double getScore(String leaderboardKey, String userId);
    
    // ��ȡ���а�ǰ N ��
    List<LeaderboardEntry> getTopN(String leaderboardKey, int n);
    
    // ��ȡָ����Χ����������� 10-20 ����
    List<LeaderboardEntry> getRange(String leaderboardKey, int start, int end);
    
    // ��ȡ�û�����ҳ�����а��統ǰ�û��ڵ� 100 ������ȡ�� 91-100 ����
    List<LeaderboardEntry> getPageAroundUser(String leaderboardKey, String userId, int pageSize);
    
    // ɾ�����а�
    void deleteLeaderboard(String leaderboardKey);
}
```

#### **3. ����ģ��**

java



����









```java
@Data // Lombok ע��
public class LeaderboardEntry {
    private String userId;
    private double score;
    private long rank; // �������� 1 ��ʼ��
    private Map<String, String> userInfo; // ��ѡ���û���ϸ��Ϣ
}
```

### **ʵ��ʾ�������� Jedis��**

java



����









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
        // ZREVRANK ���ط����Ӹߵ��͵�������0 ��ʾ��һ����
        return jedis.zrevrank(leaderboardKey, userId);
    }

    @Override
    public Double getScore(String leaderboardKey, String userId) {
        return jedis.zscore(leaderboardKey, userId);
    }

    @Override
    public List<LeaderboardEntry> getTopN(String leaderboardKey, int n) {
        // ZREVRANGEBYSCORE ��ȡ�����Ӹߵ��͵�ǰ N ��
        Set<Tuple> topN = jedis.zrevrangeByScoreWithScores(leaderboardKey, "+inf", "-inf", 0, n);
        return buildEntries(leaderboardKey, topN);
    }

    @Override
    public List<LeaderboardEntry> getRange(String leaderboardKey, int start, int end) {
        // ZREVRANGE ��ȡ������ [start, end] ֮����û����� 0 ��ʼ��
        Set<Tuple> range = jedis.zrevrangeWithScores(leaderboardKey, start, end);
        return buildEntries(leaderboardKey, range);
    }

    @Override
    public List<LeaderboardEntry> getPageAroundUser(String leaderboardKey, String userId, int pageSize) {
        Long rank = getRank(leaderboardKey, userId);
        if (rank == null) return Collections.emptyList();
        
        // ���㵱ǰҳ����ʼλ�ã�ȷ����Խ�磩
        int start = Math.max(0, rank - pageSize / 2);
        int end = start + pageSize - 1;
        
        return getRange(leaderboardKey, start, end);
    }

    @Override
    public void deleteLeaderboard(String leaderboardKey) {
        jedis.del(leaderboardKey);
    }

    // �������а���Ŀ������������Ϣ��
    private List<LeaderboardEntry> buildEntries(String leaderboardKey, Set<Tuple> tuples) {
        List<LeaderboardEntry> entries = new ArrayList<>();
        long rank = getRank(leaderboardKey, tuples.iterator().next().getElement()); // ��ȡ��һ��������
        
        for (Tuple tuple : tuples) {
            LeaderboardEntry entry = new LeaderboardEntry();
            entry.setUserId(tuple.getElement());
            entry.setScore(tuple.getScore());
            entry.setRank(++rank); // ������ 1 ��ʼ
            entries.add(entry);
        }
        
        return entries;
    }

    // ��ѡ��������ȡ�û���ϸ��Ϣ
    public void fetchUserInfoBatch(List<LeaderboardEntry> entries) {
        for (LeaderboardEntry entry : entries) {
            String userKey = USER_INFO_PREFIX + entry.getUserId();
            Map<String, String> userInfo = jedis.hgetAll(userKey);
            entry.setUserInfo(userInfo);
        }
    }
}
```

### **�߼�����ʵ��**

#### **1. ʱЧ�����а񣨰��� / �� / ��ͳ�ƣ�**

java



����









```java
// ���ɰ���ͳ�Ƶ����а� Key
public String getDailyLeaderboardKey(String baseKey, Date date) {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    return baseKey + ":daily:" + sdf.format(date);
}

// ��ʱ�������а����ݺϲ��������а�
public void mergeDailyToGlobal(String dailyKey, String globalKey) {
    // ʹ�� ZUNIONSTORE �ϲ�������򼯺�
    jedis.zunionstore(globalKey, dailyKey);
}
```

#### **2. Ȩ�ؼ��㣨��ά�ȷ�����**

java



����









```java
// �����ۺϵ÷֣��磺80% ��Ծ�� + 20% ʤ�ʣ�
public double calculateCompositeScore(double activity, double winRate) {
    return activity * 0.8 + winRate * 0.2;
}

// ���¶�ά�ȷ����������ۺϵ÷�
public void updateMultiDimensionalScore(String userId, double activity, double winRate) {
    double compositeScore = calculateCompositeScore(activity, winRate);
    addScore("leaderboard:composite", userId, compositeScore);
    
    // ��ѡ�������洢��ά�ȷ������������ݷ���
    jedis.hset("user:scores:" + userId, "activity", String.valueOf(activity));
    jedis.hset("user:scores:" + userId, "winRate", String.valueOf(winRate));
}
```

### **�����Ż�����**

1. **��������**��

    - ʹ�� `Pipeline` �� `Transaction` �������������󣬼������翪����

   java



����









   ```java
   Pipeline pipeline = jedis.pipelined();
   pipeline.zadd("leaderboard", score1, userId1);
   pipeline.zadd("leaderboard", score2, userId2);
   pipeline.sync();
   ```

2. **�����ȵ�����**��

    - ����Ƶ�����ʵ� Top N ���ݣ�ʹ�ñ��ػ��棨�� Guava Cache������ Redis ѹ����

3. **��Ƭ����**��

    - ���ڳ����ģ���а񣬰�ҵ��ά�ȷ�Ƭ���簴�������ȼ����飩�����͵��� ZSet ����������

### **ʹ��ʾ��**

java



����









```java
public static void main(String[] args) {
    Jedis jedis = new Jedis("localhost", 6379);
    LeaderboardService service = new RedisLeaderboardService(jedis);
    
    // ����û�����
    service.addScore("leaderboard:game", "user1", 1000);
    service.addScore("leaderboard:game", "user2", 1200);
    service.addScore("leaderboard:game", "user3", 900);
    
    // ��ȡ����
    System.out.println("User2 rank: " + service.getRank("leaderboard:game", "user2")); // ���: 0 (��һ��)
    
    // ��ȡ Top 2
    List<LeaderboardEntry> top2 = service.getTopN("leaderboard:game", 2);
    System.out.println(top2); // ���: [user2(1200), user1(1000)]
}
```






### **�ܽ�**

Redis ZSet ��ʵ�����а�����ѡ�����������ڣ�



- **��Ч����**������ / ɾ�� / ��ѯ����ʱ�临�ӶȾ�Ϊ O (log n)��
- **ԭ���Բ���**���������֤ԭ���ԣ���������ͨ�� Lua �ű�ʵ��ԭ���ԡ�
- **�ḻ����������**��֧������ / ������������Χ��ѯ�ȡ�


## 

### Redis ZSet �ײ�ʵ�ֽ���

Redis �� ZSet�����򼯺ϣ���һ�ָ����ܵ��������ݽṹ��������� **��ϣ��** �� **����Skip List��** �����ƣ�ͬʱ֧�� **O (1) ʱ��� member �� score ��ӳ��** �� **O (log n) ʱ��ķ�Χ��ѯ**�������������ʵ��ԭ��

### **���ݽṹ���**

ZSet �ײ�ʹ���������ݽṹ��



1. **��ϣ��Hash Table��**��
    - **��ֵ��**��`member �� score`
    - **����**�����ٲ���ĳ�� member �� score��O (1) ʱ�临�Ӷȣ���
2. **����Skip List��**��
    - **�ڵ�ṹ**��ÿ���ڵ���� `member`��`score`��**���ָ��** �� **��ȣ�span��**��
    - **����**��֧�ְ� score ���򡢿��ٷ�Χ��ѯ��O (log n) ʱ�临�Ӷȣ���

### **����Ĺؼ����**

������ ZSet ʵ������ͷ�Χ��ѯ�ĺ��ģ���ؼ���ư�����



1. **��������ṹ**��
    - ÿ���ڵ�Ĳ㼶��level����������ɵģ�ͨ��Ϊ 1~32 �㣩��
    - �߲㼶�Ľڵ������������Ͳ㼶�ڵ㣬���ٲ��ҡ�
2. **��ȣ�Span��**��
    - ÿ���ڵ��ָ�����ָ����һ���ڵ㣬��ά��һ�� **���ֵ**����ʾ����һ���ڵ�ľ��루���м������Ľڵ�������
    - **����**�����ټ���������rank����
3. **˫��ָ��**��
    - ÿ���ڵ����ǰ��ָ�루backward����֧�����������

### **���Ĳ���ʵ��**

#### **1. ���� member ��ѯ rank**

java



����









```java
// α���룺��ȡ member ���������� 1 ��ʼ��
long getRank(String member) {
    // 1. ͨ����ϣ����ٲ��� score
    double score = hashTable.get(member);
    
    // 2. ������ͷ����ʼ��������²���
    Node current = header;
    long rank = 0;
    
    for (int i = maxLevel - 1; i >= 0; i--) {
        while (current.forward[i] != null && 
               (current.forward[i].score < score || 
                (current.forward[i].score == score && 
                 current.forward[i].member.compareTo(member) < 0))) {
            // �ۼӿ�ȣ���������
            rank += current.span[i];
            current = current.forward[i];
        }
    }
    
    // �ҵ�Ŀ��ڵ��rank + 1 ��Ϊ��������
    return rank + 1;
}
```



**���Ӷ�**��O (log n)����Ҫ��������Ĳ��ҡ�

#### **2. ���� rank ��ѯ member**

java



����









```java
// α���룺��ȡָ�� rank �� member��rank �� 1 ��ʼ��
String getMemberByRank(long rank) {
    Node current = header;
    long traversed = 0;  // �ѱ����Ľڵ���
    
    for (int i = maxLevel - 1; i >= 0; i--) {
        while (current.forward[i] != null && traversed + current.span[i] <= rank) {
            traversed += current.span[i];
            current = current.forward[i];
        }
        
        if (traversed == rank) {
            return current.member;
        }
    }
    
    return null;  // ����������
}
```



**���Ӷ�**��O (log n)��ͨ������Ŀ�ȿ��ٶ�λ��

#### **3. ���� score ��ѯ rank ��Χ**

java



����









```java
// α���룺��ȡ score �� [min, max] ��Χ�ڵ����� member �� rank
List<Long> getRanksByScore(double min, double max) {
    List<Long> ranks = new ArrayList<>();
    Node current = header;
    long rank = 0;
    
    // 1. �ҵ���һ�� score >= min �Ľڵ�
    for (int i = maxLevel - 1; i >= 0; i--) {
        while (current.forward[i] != null && current.forward[i].score < min) {
            rank += current.span[i];
            current = current.forward[i];
        }
    }
    
    // 2. �Ӹýڵ㿪ʼ���ռ����� score <= max �Ľڵ�� rank
    current = current.forward[0];  // ������һ���ڵ㣨�����ǵ�һ�� >= min �Ľڵ㣩
    
    while (current != null && current.score <= max) {
        ranks.add(rank + 1);  // rank �� 1 ��ʼ
        rank++;
        current = current.forward[0];
    }
    
    return ranks;
}
```



**���Ӷ�**��O (log n + m)������ m �ǽ�����Ĵ�С��

### **����ڵ�ṹʾ��**

java



����









```java
class Node {
    String member;  // ��Ա
    double score;   // ����
    
    // ���ָ�����飬ÿ��Ԫ�ذ�����
    // - forward��ָ����һ���ڵ�
    // - span������һ���ڵ�Ŀ��
    Level[] levels;
    
    // ǰ��ָ�루�������������
    Node backward;
}

class Level {
    Node forward;  // ָ����һ���ڵ�
    long span;     // ����һ���ڵ�Ŀ�ȣ������Ľڵ�����
}
```





![img](data:image/png;base64,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)

### **�ܽ�**

Redis ZSet ͨ�� **��ϣ�� + ����** �����ʵ���˸�Ч������Ͳ�ѯ��



1. **��ϣ��** ��֤�� O (1) �� member �� score ��ӳ�䡣

2. ����



ͨ����������Ϳ���Ż���ʵ���ˣ�

- O (log n) �İ� score ����ͷ�Χ��ѯ��
- O (log n) �İ� rank ��ѯ member��
- O (log n) �İ� member ��ѯ rank��



��������ڱ�֤��ѯЧ�ʵ�ͬʱ������ʵ�ָ��Ӷȣ���Ⱥ�����Ƚṹ������ Redis �����ܵĹؼ�֮һ��



### **ʾ����������Ϸ������а�**

����������һ����Ϸ���а񣬼�¼����ҵķ��������������� 5 ��������ݣ�



| ��� ID (member) | ���� (score) |
| ---------------- | ------------ |
| player1          | 100          |
| player2          | 200          |
| player3          | 150          |
| player4          | 300          |
| player5          | 150          |

### **ZSet �ײ����ݽṹ��ʾ**

#### **1. ��ϣ����**

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

#### **2. ������**

��������Ľڵ�㼶����������£���ʾ����ʵ�ʿ��ܸ����ӣ���



plaintext











```plaintext
Header
  |
  v
Level 3: [ ] �� [player4:300] �� NULL
Level 2: [ ] �� [player2:200] �� [player4:300] �� NULL
Level 1: [ ] �� [player1:100] �� [player3:150] �� [player5:150] �� [player2:200] �� [player4:300] �� NULL
```



ÿ���ڵ�Ŀ�ȣ�span����ע���£�Ϊ�򻯣�����ʾ Level 1 �Ŀ�ȣ���



plaintext











```plaintext
Header �� [player1:100, span=1] �� [player3:150, span=1] �� [player5:150, span=1] �� [player2:200, span=1] �� [player4:300, span=1] �� NULL
```

### **���Ĳ���ʾ��**

#### **1. ���� member ��ѯ rank**

**����**����ѯ `player3` ��������
**����**��



1. **��ϣ�����**��ͨ����ϣ��ֱ�ӻ�ȡ `player3` �� score Ϊ 150��

2. ������� rank

   ��

    - ������ͷ����ʼ��������²��ҵ�һ�� score �� 150 �Ľڵ㣺

      plaintext











     ```plaintext
     Header �� player1(100) �� player3(150)  // �ۼƿ�� = 1 + 1 = 2
     ```

- ���� rank = 2�������� 1 ��ʼ����
  **���**��`player3` ������Ϊ **2**��

#### **2. ���� rank ��ѯ member**

**����**����ѯ������ 3 ����ҡ�
**����**��



1. �������

   ��������ͷ����ʼ������ۼӿ�ȣ�ֱ���ҵ��� 3 ���ڵ㣺

   plaintext











   ```plaintext
   Header �� player1(100, span=1) �� player3(150, span=1) �� player5(150, span=1)  // �ۼƿ�� = 1 + 1 + 1 = 3
   ```

- �� 3 ���ڵ�Ϊ `player5`��
  **���**�������� 3 ������� `player5`��

#### **3. ���� score ��ѯ rank ��Χ**

**����**����ѯ������ [150, 200] ��Χ�ڵ�������Ҽ���������
**����**��



1. ����λ��ʼ�ڵ�

   ���ҵ���һ�� score �� 150 �Ľڵ㣺

   plaintext











   ```plaintext
   Header �� player1(100) �� player3(150)  // ��ʼ�ڵ�Ϊ player3
   ```

2. �����ռ����

   ����



   ```
   player3
   ```



��ʼ���ռ����� score �� 200 �Ľڵ㼰��������

plaintext











   ```plaintext
   player3(150, rank=2) �� player5(150, rank=3) �� player2(200, rank=4)
   ```



**���**��



- `player3`������ 2��
- `player5`������ 3��
- `player2`������ 4��

### **ʱ�临�Ӷ���֤**

1. **���� member ��ѯ rank**��
    - ��ϣ����� O (1) + ������� O (log n) �� **O(log n)**
2. **���� rank ��ѯ member**��
    - ����ͨ����ȿ��ٶ�λ �� **O(log n)**
3. **���� score ��ѯ rank ��Χ**��
    - ����λ��ʼ�� O (log n) + ������� O (m) �� **O(log n + m)**

### **�ؼ������ܽ�**

1. **��Ч������**������ͨ����������Ϳ�ȣ�����������Ż��� O (log n)��
2. **���Ĳ�ѯ**��֧�ְ� member��rank��score ����ά�Ȳ�ѯ���Ҹ��ӶȾ�Ϊ O (log n) ����
3. **�ռ���Ч�ʵ�ƽ��**�����������㼶����ڱ�֤Ч�ʵ�ͬʱ�������˺�����Ƚṹ�ĸ���ά����



ͨ��������ӣ������ֱ����� Redis ZSet ���ͨ����ϣ����������ϣ�ʵ�ָ����ܵ����а��ܡ�