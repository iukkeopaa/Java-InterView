package HandlerWriteHashMap;

/**
 * @Description:
 * @Author: wjh
 * @Date: 2025/7/19 13:57
 */

public class LinearProbingHashMap<K, V> implements HashTable<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR_THRESHOLD = 0.75;
    private Entry<K, V>[] table;
    private int size;
    private int deletedCount;

    @SuppressWarnings("unchecked")
    public LinearProbingHashMap() {
        table = new Entry[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if ((size + deletedCount) >= table.length * LOAD_FACTOR_THRESHOLD) {
            resize();
        }

        int index = findSlot(key);
        if (table[index] == null || table[index].isDeleted) {
            table[index] = new Entry<>(key, value);
            size++;
            deletedCount = 0;
        } else {
            table[index].value = value;
        }
    }

    @Override
    public V get(K key) {
        int index = findSlot(key);
        if (table[index] != null && !table[index].isDeleted) {
            return table[index].value;
        }
        return null;
    }

    @Override
    public int size() {
        return size;
    }

    private int findSlot(K key) {
        int hash = hash(key);
        int index = hash;
        int step = 1;

        while (table[index] != null) {
            if (!table[index].isDeleted && table[index].key.equals(key)) {
                return index;
            }
            index = (index + step) % table.length;
        }

        return index;
    }

    private int hash(K key) {
        return (key.hashCode() & 0x7FFFFFFF) % table.length;
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        Entry<K, V>[] oldTable = table;
        table = new Entry[oldTable.length * 2];
        size = 0;
        deletedCount = 0;

        for (Entry<K, V> entry : oldTable) {
            if (entry != null && !entry.isDeleted) {
                put(entry.key, entry.value);
            }
        }
    }

    private static class Entry<K, V> {
        K key;
        V value;
        boolean isDeleted;

        Entry(K key, V value) {
            this.key = key;
            this.value = value;
            this.isDeleted = false;
        }
    }
}