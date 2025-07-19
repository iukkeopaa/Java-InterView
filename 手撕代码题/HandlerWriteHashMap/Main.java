package HandlerWriteHashMap;

/**
 * @Description:
 * @Author: wjh
 * @Date: 2025/7/19 13:57
 */


public class Main {
    public static void main(String[] args) {
        // 测试单链表法
        HashTable<String, Integer> chainMap = new SeparateChainingHashMap<>();
        testHashTable(chainMap);

        // 测试线性探测法
        HashTable<String, Integer> linearMap = new LinearProbingHashMap<>();
        testHashTable(linearMap);

        // 测试再散列法
        HashTable<String, Integer> doubleHashMap = new DoubleHashingHashMap<>();
        testHashTable(doubleHashMap);
    }

    private static void testHashTable(HashTable<String, Integer> map) {
        map.put("one", 1);
        map.put("two", 2);
        map.put("three", 3);
        map.put("four", 4);
        map.put("one", 100); // 更新值

        System.out.println("Size: " + map.size());
        System.out.println("Get 'one': " + map.get("one"));
        System.out.println("Get 'three': " + map.get("three"));
        System.out.println("Get 'five': " + map.get("five"));
        System.out.println("-------------------");
    }
}