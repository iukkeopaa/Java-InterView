## ʹ��Java��DelayQueue�Ļ�����һ����崻������ݲ��Ͷ�ʧ����

### **1. �������� + �����ָ�**

���ӳ����������ǰ�־û������̣������ݿ⡢�ļ�������������ʱ���¼��ص����У�



java



����









```java
import java.util.concurrent.DelayQueue;

public class PersistentDelayQueueManager {
    private final DelayQueue<DelayedTask> queue = new DelayQueue<>();
    private final TaskRepository repository; // ���ݿ���ʲ�

    // ����ʱ�ָ�����
    public void init() {
        repository.findAllPendingTasks().forEach(task -> {
            if (task.getDelay(TimeUnit.MILLISECONDS) > 0) {
                queue.put(task);
            } else {
                // �����ѹ�������
                executeTask(task);
            }
        });
    }

    // ��������ȳ־û�������ӣ�
    public void addTask(DelayedTask task) {
        repository.save(task);
        queue.put(task);
    }

    // ��������ִ�к���Ϊ��ɣ�
    public void processTasks() {
        while (true) {
            try {
                DelayedTask task = queue.take();
                executeTask(task);
                repository.markAsCompleted(task.getId());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    private void executeTask(DelayedTask task) {
        // ִ�о��������߼�
    }
}
```

### **2. ʹ�ó־û���Ϣ����**

�� `DelayQueue` �滻Ϊ֧���ӳٹ��ܵķֲ�ʽ��Ϣ���У��磺



- **RabbitMQ**��ͨ�� TTL����Ϣ���ڣ�+ ���Ŷ��У�Dead Letter Exchange��ʵ���ӳ����ѡ�
- **RocketMQ/Kafka**���Դ��ӳ���Ϣ���ܣ���Ϣ��־û������̣���Ⱥ������֤�߿��á�

### **3. ��ʱ���� + ������־**

- **���ڿ���**���������е�����״̬���浽���̣���ÿ�� 5 ���ӣ���
- **������־**����¼������� / ���Ӳ���������ָ�ʱͨ���ط���־�ؽ����С�

### **4. ��� Redis**

���� Redis �� `Sorted Set` ʵ���ӳٶ��У������Զ��־û���



java



����









```java
import redis.clients.jedis.Jedis;

public class RedisDelayQueue {
    private static final String DELAY_QUEUE_KEY = "delay_queue";
    private final Jedis jedis;

    // ������񣨷���Ϊִ��ʱ�����
    public void addTask(String taskId, long executeTime) {
        jedis.zadd(DELAY_QUEUE_KEY, executeTime, taskId);
    }

    // ��ȡ��ִ������
    public Set<Tuple> getReadyTasks(long currentTime) {
        return jedis.zrangeByScoreWithScores(DELAY_QUEUE_KEY, 0, currentTime);
    }

    // �Ƴ�����
    public void removeTask(String taskId) {
        jedis.zrem(DELAY_QUEUE_KEY, taskId);
    }
}
```





![img](data:image/png;base64,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)

### **5. �ֲ�ʽ�� + �ݵ����**

���ʹ�ö�ʵ��������ȷ�����񲻱��ظ�ִ�У�



- ͨ�� Redis �� ZooKeeper ��ȡ������֤ͬһ����ֻ��һ��ʵ������
- �������߼����Ϊ**�ݵ�**�����ִ�н����ͬ����

### **�ܽ�**

`DelayQueue` �ʺ϶��ڡ�����ʧ���ӳ����񡣶��ڹؼ�ҵ�񣬽��飺



1. **����ʹ�÷ֲ�ʽ��Ϣ����**���� RocketMQ/Kafka������Ȼ֧�ֳ־û��͸߿��á�
2. **����־û�����**�����������ڴ�ṹ��
3. **��Ʋ�������**���綨ʱɨ��δ�������񣩣��������ݶ�ʧ���ա�

## Redis�����ʵ���ӳٶ���

Redis ʵ���ӳٶ�����Ҫ���� **Sorted Set�����򼯺ϣ�** �� **Lua �ű�**��������������ܡ��־û��ͷֲ�ʽ���������������Ǻ���ʵ��˼·��

### **1. ���ݽṹ���**

- **Key**��ʹ��һ�� Sorted Set �洢�����ӳ��������� `delay_queue:order_expire`��
- **Member**�������Ψһ��ʶ���綩�� ID����Ϣ ID����
- **Score**�������ִ��ʱ��������뼶����

### **2. ���Ĳ���**

#### **������������ߣ�**

java



����









```java
import redis.clients.jedis.Jedis;

public class RedisDelayQueue {
    private static final String DELAY_QUEUE_KEY = "delay_queue:order_expire";
    private final Jedis jedis;

    // ����ӳ�����score Ϊִ��ʱ�����
    public void addTask(String taskId, long executeTime) {
        jedis.zadd(DELAY_QUEUE_KEY, executeTime, taskId);
    }
}
```





![img](data:image/png;base64,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)

#### **���������������ߣ�**

ʹ�� **Lua �ű�ԭ�ӻ���������ȡ��ɾ��**������������߾������µ��ظ�����



lua











```lua
-- ��ȡ���Ƴ��ѵ��ڵ�����
local key = KEYS[1]
local currentTime = ARGV[1]
local tasks = redis.call('ZRANGEBYSCORE', key, 0, currentTime, 'LIMIT', 0, 100)
if #tasks > 0 then
    redis.call('ZREM', key, unpack(tasks))
end
return tasks
```



Java ������� Lua �ű���



java



����









```java
// ִ�� Lua �ű���ȡ���Ƴ��ѵ�������
public List<String> getReadyTasks(long currentTime) {
    String script = 
        "local key = KEYS[1] " +
        "local currentTime = ARGV[1] " +
        "local tasks = redis.call('ZRANGEBYSCORE', key, 0, currentTime, 'LIMIT', 0, 100) " +
        "if #tasks > 0 then " +
        "    redis.call('ZREM', key, unpack(tasks)) " +
        "end " +
        "return tasks";
    
    return (List<String>) jedis.eval(script, 
                                     Collections.singletonList(DELAY_QUEUE_KEY), 
                                     Collections.singletonList(String.valueOf(currentTime)));
}
```

### **3. ������ѭ������**

java



����









```java
// �������̳߳�����ѯ
public void startConsumer() {
    Executors.newSingleThreadExecutor().execute(() -> {
        while (true) {
            try {
                long currentTime = System.currentTimeMillis();
                List<String> readyTasks = getReadyTasks(currentTime);
                
                for (String taskId : readyTasks) {
                    // �����������綩����ʱ�رա���Ϣ���ͣ�
                    processTask(taskId);
                }
                
                // �������߱���Ƶ������
                Thread.sleep(100);
            } catch (Exception e) {
                log.error("�����ӳ�����ʧ��", e);
                Thread.sleep(1000); // ��������Լ��
            }
        }
    });
}
```

### **4. �־û���߿���**

- **RDB/AOF �־û�**��Redis ��Ȼ֧���������̣��������Զ��ָ���
- **���Ӹ��� / �ڱ���Ⱥ**�����ⵥ����ϣ���֤�����ԡ�

### **5. �Ż�����**

1. **��������**��Lua �ű���ʹ�� `LIMIT` �������Ƶ��λ�ȡ�������������⴦���ʱ������
2. **��������**������ʧ�ܵ���������¼�����У����� score Ϊδ��ʱ�䣩��
3. **�ֲ�ʽ��**����ʵ������ʱ��ʹ�� Redis ����֤���񲻻ᱻ�ظ����ѡ�
4. **��ظ澯**����ض��г��Ⱥʹ����ʱ����������ѻ���

### **����ʾ������**

java



����









```java
import redis.clients.jedis.Jedis;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class RedisDelayQueue {
    private static final String DELAY_QUEUE_KEY = "delay_queue:order_expire";
    private final Jedis jedis;

    public RedisDelayQueue(String host, int port) {
        this.jedis = new Jedis(host, port);
    }

    // ����ӳ�����
    public void addTask(String taskId, long delaySeconds) {
        long executeTime = System.currentTimeMillis() + (delaySeconds * 1000);
        jedis.zadd(DELAY_QUEUE_KEY, executeTime, taskId);
    }

    // ��ȡ���Ƴ��ѵ�������
    public List<String> getReadyTasks() {
        String script = 
            "local key = KEYS[1] " +
            "local currentTime = ARGV[1] " +
            "local tasks = redis.call('ZRANGEBYSCORE', key, 0, currentTime, 'LIMIT', 0, 100) " +
            "if #tasks > 0 then " +
            "    redis.call('ZREM', key, unpack(tasks)) " +
            "end " +
            "return tasks";
        
        return (List<String>) jedis.eval(script, 
                                         Collections.singletonList(DELAY_QUEUE_KEY), 
                                         Collections.singletonList(String.valueOf(System.currentTimeMillis())));
    }

    // ����������
    public void startConsumer() {
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            try {
                List<String> readyTasks = getReadyTasks();
                for (String taskId : readyTasks) {
                    processTask(taskId);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 0, 100, TimeUnit.MILLISECONDS);
    }

    // ����������ҵ��ʵ�֣�
    private void processTask(String taskId) {
        System.out.println("��������: " + taskId);
        // ҵ���߼����綩����ʱ�رա���Ϣ���ͣ�
    }

    public static void main(String[] args) {
        RedisDelayQueue queue = new RedisDelayQueue("localhost", 6379);
        
        // ����ӳ�����10���ִ�У�
        queue.addTask("order_123", 10);
        
        // ����������
        queue.startConsumer();
    }
}
```

### **���ó���**

- ������ʱδ֧���Զ��ر�
- ��Ϣ�ӳ�����
- �û����� 30 ��δ��¼����
- ����Ԥ�ȣ���ʱ�����ȵ����ݣ�

### **�Ա���������**

| ����              | �ŵ�                   | ȱ��                 |
| ----------------- | ---------------------- | -------------------- |
| Redis Sorted Set  | �����ܡ��־û����ֲ�ʽ | ��Ҫ����ʵ����ѯ�߼� |
| RabbitMQ ���Ŷ��� | ���췽������֤˳��     | ���ø��ӡ�����һ��   |
| RocketMQ �ӳ���Ϣ | ԭ��֧�֡���������     | ����������м��     |



����ҵ���ģ�͸��Ӷ�ѡ����ʵķ�����Redis �����ʺϿ���ʵ���Ҷ�������Ҫ��ϸߵĳ�����