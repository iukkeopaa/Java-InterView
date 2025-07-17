# Ҫʵ��һ��֧�� setAll ������ʱ�临�Ӷ�Ϊ O (1) �� HashMap ��װ����Ҫ�ڳ����ϣ��Ļ���������һ��ȫ�ֱ�ǻ��ơ������Ǿ���ʵ�ַ�����

### **����˼·**

1. **ȫ�ְ汾��**��ά��һ��ȫ��ʱ��� `globalVersion`����¼���һ�� `setAll` ������ʱ�䡣

2. **��ֵ�԰汾��**��ÿ����ֵ�Լ�¼����ʱ�İ汾�� `entryVersion`��

3. ��ȡ�߼�

   ������ȡһ����ʱ���Ƚ���



   ```
   entryVersion
   ```



��



   ```
   globalVersion
   ```

��

- �� `entryVersion < globalVersion`��˵���ü��� `setAll` ֮��δ�����£�����ȫ��ֵ��
- ���򷵻ؼ���ʵ��ֵ��

### **����ʵ��**

java



����









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

    // O(1) ʱ�临�Ӷȣ��������м�ֵΪָ��ֵ
    public synchronized void setAll(V value) {
        globalValue = value;
        globalVersion = ++currentVersion;
    }

    // O(1) ʱ�临�Ӷȣ���ȡָ������ֵ
    public synchronized V get(K key) {
        Entry<V> entry = map.get(key);
        if (entry == null) {
            return null;
        }
        // �� entry �İ汾����ȫ�ְ汾��˵���ü��� setAll ��δ������
        return entry.version < globalVersion ? globalValue : entry.value;
    }

    // O(1) ʱ�临�Ӷȣ����õ�����ֵ
    public synchronized void put(K key, V value) {
        map.put(key, new Entry<>(value, ++currentVersion));
    }

    // O(1) ʱ�临�Ӷȣ������Ƿ����
    public synchronized boolean containsKey(K key) {
        return map.containsKey(key);
    }

    // O(1) ʱ�临�Ӷȣ�ɾ����
    public synchronized void remove(K key) {
        map.remove(key);
    }

    // O(n) ʱ�临�Ӷȣ����ص�ǰ״̬�µ����м�ֵ��
    public synchronized Map<K, V> getAll() {
        Map<K, V> result = new HashMap<>();
        for (Map.Entry<K, Entry<V>> entry : map.entrySet()) {
            K key = entry.getKey();
            Entry<V> valueEntry = entry.getValue();
            result.put(key, valueEntry.version < globalVersion ? globalValue : valueEntry.value);
        }
        return result;
    }

    // O(1) ʱ�临�Ӷȣ���ȡ��ǰ��ֵ������
    public synchronized int size() {
        return map.size();
    }

    // ʾ���÷�
    public static void main(String[] args) {
        EnhancedHashMap<String, Integer> map = new EnhancedHashMap<>();
        map.put("A", 1);
        map.put("B", 2);
        map.setAll(100);
        System.out.println(map.get("A")); // ��� 100
        map.put("B", 200);
        System.out.println(map.get("B")); // ��� 200
    }
}
```