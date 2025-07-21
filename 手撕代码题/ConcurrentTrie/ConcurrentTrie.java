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

    // �����
    public void insert(String key) {
        if (key == null) throw new IllegalArgumentException("Key cannot be null");
        Node current = root;
        for (char c : key.toCharArray()) {
            // ԭ���Եػ�ȡ�򴴽��ӽڵ�
            current = current.getOrCreateChild(c);
        }
        current.setEndOfWord(true);
    }

    // ��ѯ���Ƿ����
    public boolean contains(String key) {
        if (key == null) return false;
        Node node = getNode(key);
        return node != null && node.isEndOfWord();
    }

    // ��ѯ�Ƿ������prefix��ͷ�ļ�
    public boolean startsWith(String prefix) {
        if (prefix == null) return false;
        return getNode(prefix) != null;
    }

    // ��ȡ����Ӧ�Ľڵ㣨˽�з�����
    private static Node getNode(String key) {
        Node current = root;
        for (char c : key.toCharArray()) {
            current = current.getChild(c);
            if (current == null) return null;
        }
        return current;
    }

    // ���ɱ�ڵ���
    private static class Node {
        private final boolean isEndOfWord;
        private final Map<Character, Node> children;

        public Node(boolean isEndOfWord) {
            this.isEndOfWord = isEndOfWord;
            this.children = new ConcurrentHashMap<>();
        }

        // ��ȡ�ӽڵ�
        public Node getChild(char c) {
            return children.get(c);
        }

        // ԭ���Եػ�ȡ�򴴽��ӽڵ�
        public Node getOrCreateChild(char c) {
            // �ȳ���ֱ�ӻ�ȡ
            Node child = children.get(c);
            if (child != null) return child;

            // �������򴴽��½ڵ�
            Node newNode = new Node(false);
            // ʹ��putIfAbsentԭ���Եز���
            Node existing = children.putIfAbsent(c, newNode);
            return existing != null ? existing : newNode;
        }

        // ����Ϊ���ʽ�β��ʹ��CAS˼�룩
        public void setEndOfWord(boolean isEndOfWord) {
            if (this.isEndOfWord == isEndOfWord) return;

            // ����һ���½ڵ㣬���Ƶ�ǰ�ڵ���ӽڵ�
            Node newNode = new Node(true);
            newNode.children.putAll(this.children);

            // ������Ҫ���ڵ�����ò��ܸ��£�ʵ��ʵ������Ҫ����׷��
            // �򻯰棺��ʵ��Ӧ���У��˷���Ӧ�ɸ��ڵ���ò���������
        }

        public boolean isEndOfWord() {
            return isEndOfWord;
        }

        public Map<Character, Node> getChildren() {
            return Collections.unmodifiableMap(children);
        }

        // ��չ��ɾ������֧�ֲ�����
        public boolean delete(String key) {
            if (key == null) return false;
            // ʹ�õݹ�����ɾ��·���ϵĽڵ�
            // �򻯰棺ʵ���账���ڵ�ĸ��º�·��ѹ��
            return deleteHelper(root, key, 0);
        }

        private boolean deleteHelper(Node current, String key, int index) {
            if (index == key.length()) {
                if (!current.isEndOfWord()) return false;
                // ����һ���½ڵ㣬���Ϊ�ǵ��ʽ�β
                Node newNode = new Node(false);
                newNode.children.putAll(current.children);
                // ����¸��ڵ�����ã��˴��򻯣�
                return true;
            }
            char c = key.charAt(index);
            Node child = current.getChild(c);
            if (child == null) return false;

            boolean canDeleteCurrentNode = deleteHelper(child, key, index + 1)
                    && !child.isEndOfWord()
                    && child.getChildren().isEmpty();

            if (canDeleteCurrentNode) {
                // ԭ���Ե��Ƴ��ӽڵ�
                current.children.remove(c);
                return true;
            }
            return false;
        }

        // ��չ����ȡ������prefix��ͷ�ļ�
        public List<String> getAllWithPrefix(String prefix) {
            List<String> result = new ArrayList<>();
            Node node = getNode(prefix);
            if (node == null) return result;

            // �ݹ��ռ����к�׺
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



