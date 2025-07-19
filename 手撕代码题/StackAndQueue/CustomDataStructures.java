package StackAndQueue;

/**
 * @Description:
 * @Author: wjh
 * @Date: 2025/7/19 16:04
 */
import java.util.EmptyStackException;

// 自定义栈实现（基于数组）
class Stack {
    private int[] arr;
    private int top;
    private static final int DEFAULT_CAPACITY = 10;

    public Stack() {
        arr = new int[DEFAULT_CAPACITY];
        top = -1;
    }

    public Stack(int initialCapacity) {
        arr = new int[initialCapacity];
        top = -1;
    }

    // 入栈操作
    public void push(int value) {
        if (top == arr.length - 1) {
            resize(2 * arr.length);
        }
        arr[++top] = value;
    }

    // 出栈操作
    public int pop() {
        if (isEmpty()) {
            throw new EmptyStackException();
        }
        int value = arr[top];
        top--;
        // 缩容优化
        if (top > 0 && top == arr.length / 4) {
            resize(arr.length / 2);
        }
        return value;
    }

    // 查看栈顶元素
    public int peek() {
        if (isEmpty()) {
            throw new EmptyStackException();
        }
        return arr[top];
    }

    // 判断栈是否为空
    public boolean isEmpty() {
        return top == -1;
    }

    // 获取栈的大小
    public int size() {
        return top + 1;
    }

    // 扩容/缩容方法
    private void resize(int newCapacity) {
        int[] newArr = new int[newCapacity];
        for (int i = 0; i <= top; i++) {
            newArr[i] = arr[i];
        }
        arr = newArr;
    }
}

// 自定义队列实现（基于链表）
class Queue {
    private static class Node {
        int value;
        Node next;

        Node(int value) {
            this.value = value;
        }
    }

    private Node head;
    private Node tail;
    private int size;

    public Queue() {
        head = null;
        tail = null;
        size = 0;
    }

    // 入队操作
    public void enqueue(int value) {
        Node newNode = new Node(value);
        if (tail == null) {
            head = tail = newNode;
        } else {
            tail.next = newNode;
            tail = newNode;
        }
        size++;
    }

    // 出队操作
    public int dequeue() {
        if (isEmpty()) {
            throw new IllegalStateException("Queue is empty");
        }
        int value = head.value;
        head = head.next;
        if (head == null) {
            tail = null;
        }
        size--;
        return value;
    }

    // 查看队首元素
    public int peek() {
        if (isEmpty()) {
            throw new IllegalStateException("Queue is empty");
        }
        return head.value;
    }

    // 判断队列是否为空
    public boolean isEmpty() {
        return size == 0;
    }

    // 获取队列的大小
    public int size() {
        return size;
    }
}

// 测试类
public class CustomDataStructures {
    public static void main(String[] args) {
        // 测试栈
        Stack stack = new Stack();
        stack.push(1);
        stack.push(2);
        stack.push(3);
        System.out.println("Stack pop: " + stack.pop()); // 输出3
        System.out.println("Stack peek: " + stack.peek()); // 输出2

        // 测试队列
        Queue queue = new Queue();
        queue.enqueue(10);
        queue.enqueue(20);
        queue.enqueue(30);
        System.out.println("Queue dequeue: " + queue.dequeue()); // 输出10
        System.out.println("Queue peek: " + queue.peek()); // 输出20
    }
}