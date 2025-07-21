package ConcurrentTrie;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @Description:
 * @Author: wjh
 * @Date: 2025/7/21 17:16
 */


public class ConcurrentTrie {
    private static final Node root = new Node(false);

    // 插入键
    public void insert(String key) {
        if (key == null) throw new IllegalArgumentException("Key cannot be null");
        Node current = root;
        for (char c : key.toCharArray()) {
            // 原子性地获取或创建子节点
            current = current.getOrCreateChild(c);
        }
        current.setEndOfWord(true);
    }

    // 查询键是否存在
    public boolean contains(String key) {
        if (key == null) return false;
        Node node = getNode(key);
        return node != null && node.isEndOfWord();
    }

    // 查询是否存在以prefix开头的键
    public boolean startsWith(String prefix) {
        if (prefix == null) return false;
        return getNode(prefix) != null;
    }

    // 获取键对应的节点（私有方法）
    private static Node getNode(String key) {
        Node current = root;
        for (char c : key.toCharArray()) {
            current = current.getChild(c);
            if (current == null) return null;
        }
        return current;
    }

    // 不可变节点类
    private static class Node {
        private final boolean isEndOfWord;
        private final Map<Character, Node> children;

        public Node(boolean isEndOfWord) {
            this.isEndOfWord = isEndOfWord;
            this.children = new ConcurrentHashMap<>();
        }

        // 获取子节点
        public Node getChild(char c) {
            return children.get(c);
        }

        // 原子性地获取或创建子节点
        public Node getOrCreateChild(char c) {
            // 先尝试直接获取
            Node child = children.get(c);
            if (child != null) return child;

            // 不存在则创建新节点
            Node newNode = new Node(false);
            // 使用putIfAbsent原子性地插入
            Node existing = children.putIfAbsent(c, newNode);
            return existing != null ? existing : newNode;
        }

        // 设置为单词结尾（使用CAS思想）
        public void setEndOfWord(boolean isEndOfWord) {
            if (this.isEndOfWord == isEndOfWord) return;

            // 创建一个新节点，复制当前节点的子节点
            Node newNode = new Node(true);
            newNode.children.putAll(this.children);

            // 这里需要父节点的引用才能更新，实际实现中需要向上追溯
            // 简化版：在实际应用中，此方法应由父节点调用并更新引用
        }

        public boolean isEndOfWord() {
            return isEndOfWord;
        }

        public Map<Character, Node> getChildren() {
            return Collections.unmodifiableMap(children);
        }

        // 扩展：删除键（支持并发）
        public boolean delete(String key) {
            if (key == null) return false;
            // 使用递归或迭代删除路径上的节点
            // 简化版：实际需处理父节点的更新和路径压缩
            return deleteHelper(root, key, 0);
        }

        private boolean deleteHelper(Node current, String key, int index) {
            if (index == key.length()) {
                if (!current.isEndOfWord()) return false;
                // 创建一个新节点，标记为非单词结尾
                Node newNode = new Node(false);
                newNode.children.putAll(current.children);
                // 需更新父节点的引用（此处简化）
                return true;
            }
            char c = key.charAt(index);
            Node child = current.getChild(c);
            if (child == null) return false;

            boolean canDeleteCurrentNode = deleteHelper(child, key, index + 1)
                    && !child.isEndOfWord()
                    && child.getChildren().isEmpty();

            if (canDeleteCurrentNode) {
                // 原子性地移除子节点
                current.children.remove(c);
                return true;
            }
            return false;
        }

        // 扩展：获取所有以prefix开头的键
        public List<String> getAllWithPrefix(String prefix) {
            List<String> result = new ArrayList<>();
            Node node = getNode(prefix);
            if (node == null) return result;

            // 递归收集所有后缀
            collectWords(node, new StringBuilder(prefix), result);
            return result;
        }

        private void collectWords(Node node, StringBuilder currentPrefix, List<String> result) {
            if (node.isEndOfWord()) {
                result.add(currentPrefix.toString());
            }

            for (Map.Entry<Character, Node> entry : node.getChildren().entrySet()) {
                collectWords(entry.getValue(),
                        currentPrefix.append(entry.getKey()),
                        result);
                currentPrefix.deleteCharAt(currentPrefix.length() - 1);
            }
        }
    }
}



