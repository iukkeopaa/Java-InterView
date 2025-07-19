package HandlerWriteHashMap;

/**
 * @Description:
 * @Author: wjh
 * @Date: 2025/7/19 13:56
 */

public class DoubleHashingHashMap<K, V> implements HashTable<K, V> {
    private static final int DEFAULT_CAPACITY = 17; // 使用质数减少二次聚集
    private static final double LOAD_FACTOR_THRESHOLD = 0.5;
    private Entry<K, V>[] table;
    private int size;

    @SuppressWarnings("unchecked")
    public DoubleHashingHashMap() {
        table = new Entry[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size >= table.length * LOAD_FACTOR_THRESHOLD) {
            resize();
        }

        int index = findSlot(key);
        if (table[index] == null) {
            table[index] = new Entry<>(key, value);
            size++;
        } else {
            table[index].value = value;
        }
    }

    @Override
    public V get(K key) {
        int index = findSlot(key);
        if (table[index] != null) {
            return table[index].value;
        }
        return null;
    }

    @Override
    public int size() {
        return size;
    }

    private int findSlot(K key) {
        int hash1 = hash1(key);
        int hash2 = hash2(key);
        int index = hash1;

        for (int i = 0; table[index] != null; i++) {
            if (table[index].key.equals(key)) {
                return index;
            }
            index = (hash1 + i * hash2) % table.length;
        }

        return index;
    }

    private int hash1(K key) {
        return (key.hashCode() & 0x7FFFFFFF) % table.length;
    }

    private int hash2(K key) {
        int hash = key.hashCode() & 0x7FFFFFFF;
        int secondaryHash = 5 - (hash % 5); // 确保步长与表大小互质
        return secondaryHash == 0 ? 1 : secondaryHash;
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        int newCapacity = nextPrime(table.length * 2);
        Entry<K, V>[] oldTable = table;
        table = new Entry[newCapacity];
        size = 0;

        for (Entry<K, V> entry : oldTable) {
            if (entry != null) {
                put(entry.key, entry.value);
            }
        }
    }

    private int nextPrime(int n) {
        while (!isPrime(n)) {
            n++;
        }
        return n;
    }

    private boolean isPrime(int n) {
        if (n <= 1) return false;
        if (n == 2) return true;
        if (n % 2 == 0) return false;
        for (int i = 3; i * i <= n; i += 2) {
            if (n % i == 0) return false;
        }
        return true;
    }

    private static class Entry<K, V> {
        K key;
        V value;

        Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}