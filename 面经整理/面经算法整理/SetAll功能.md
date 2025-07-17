# 要实现一个支持 setAll 操作且时间复杂度为 O (1) 的 HashMap 封装，需要在常规哈希表的基础上增加一个全局标记机制。以下是具体实现方案：

### **核心思路**

1. **全局版本号**：维护一个全局时间戳 `globalVersion`，记录最近一次 `setAll` 操作的时间。

2. **键值对版本号**：每个键值对记录创建时的版本号 `entryVersion`。

3. 读取逻辑

   ：当读取一个键时，比较其



   ```
   entryVersion
   ```



与



   ```
   globalVersion
   ```

：

- 若 `entryVersion < globalVersion`，说明该键在 `setAll` 之后未被更新，返回全局值。
- 否则返回键的实际值。

### **代码实现**

java



运行









```java
import java.util.HashMap;
import java.util.Map;

public class EnhancedHashMap<K, V> {
    private final HashMap<K, Entry<V>> map = new HashMap<>();
    private V globalValue;
    private long globalVersion = 0;
    private long currentVersion = 0;

    private static class Entry<V> {
        V value;
        long version;

        Entry(V value, long version) {
            this.value = value;
            this.version = version;
        }
    }

    // O(1) 时间复杂度：更新所有键值为指定值
    public synchronized void setAll(V value) {
        globalValue = value;
        globalVersion = ++currentVersion;
    }

    // O(1) 时间复杂度：获取指定键的值
    public synchronized V get(K key) {
        Entry<V> entry = map.get(key);
        if (entry == null) {
            return null;
        }
        // 若 entry 的版本早于全局版本，说明该键在 setAll 后未被更新
        return entry.version < globalVersion ? globalValue : entry.value;
    }

    // O(1) 时间复杂度：设置单个键值
    public synchronized void put(K key, V value) {
        map.put(key, new Entry<>(value, ++currentVersion));
    }

    // O(1) 时间复杂度：检查键是否存在
    public synchronized boolean containsKey(K key) {
        return map.containsKey(key);
    }

    // O(1) 时间复杂度：删除键
    public synchronized void remove(K key) {
        map.remove(key);
    }

    // O(n) 时间复杂度：返回当前状态下的所有键值对
    public synchronized Map<K, V> getAll() {
        Map<K, V> result = new HashMap<>();
        for (Map.Entry<K, Entry<V>> entry : map.entrySet()) {
            K key = entry.getKey();
            Entry<V> valueEntry = entry.getValue();
            result.put(key, valueEntry.version < globalVersion ? globalValue : valueEntry.value);
        }
        return result;
    }

    // O(1) 时间复杂度：获取当前键值对数量
    public synchronized int size() {
        return map.size();
    }

    // 示例用法
    public static void main(String[] args) {
        EnhancedHashMap<String, Integer> map = new EnhancedHashMap<>();
        map.put("A", 1);
        map.put("B", 2);
        map.setAll(100);
        System.out.println(map.get("A")); // 输出 100
        map.put("B", 200);
        System.out.println(map.get("B")); // 输出 200
    }
}
```