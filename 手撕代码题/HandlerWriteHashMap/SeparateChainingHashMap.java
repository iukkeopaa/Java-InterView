package HandlerWriteHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.LinkedList;
import java.util.List;

/**
 * @Description:
 * @Author: wjh
 * @Date: 2025/7/19 13:57
 */

public class SeparateChainingHashMap<K, V> implements HashTable<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private List<Entry<K, V>>[] table;
    private int size;

    @SuppressWarnings("unchecked")
    public SeparateChainingHashMap() {
        table = new LinkedList[DEFAULT_CAPACITY];
        for (int i = 0; i < DEFAULT_CAPACITY; i++) {
            table[i] = new LinkedList<>();
        }
    }

    @Override
    public void put(K key, V value) {
        int index = hash(key);
        List<Entry<K, V>> bucket = table[index];

        for (Entry<K, V> entry : bucket) {
            if (entry.key.equals(key)) {
                entry.value = value;
                return;
            }
        }

        bucket.add(new Entry<>(key, value));
        size++;
    }

    @Override
    public V get(K key) {
        int index = hash(key);
        List<Entry<K, V>> bucket = table[index];

        for (Entry<K, V> entry : bucket) {
            if (entry.key.equals(key)) {
                return entry.value;
            }
        }

        return null;
    }

    @Override
    public int size() {
        return size;
    }

    private int hash(K key) {
        return (key.hashCode() & 0x7FFFFFFF) % table.length;
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