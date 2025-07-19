package StackAndQueue;

/**
 * @Description:
 * @Author: wjh
 * @Date: 2025/7/19 16:04
 */
import java.util.EmptyStackException;

// �Զ���ջʵ�֣��������飩
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

    // ��ջ����
    public void push(int value) {
        if (top == arr.length - 1) {
            resize(2 * arr.length);
        }
        arr[++top] = value;
    }

    // ��ջ����
    public int pop() {
        if (isEmpty()) {
            throw new EmptyStackException();
        }
        int value = arr[top];
        top--;
        // �����Ż�
        if (top > 0 && top == arr.length / 4) {
            resize(arr.length / 2);
        }
        return value;
    }

    // �鿴ջ��Ԫ��
    public int peek() {
        if (isEmpty()) {
            throw new EmptyStackException();
        }
        return arr[top];
    }

    // �ж�ջ�Ƿ�Ϊ��
    public boolean isEmpty() {
        return top == -1;
    }

    // ��ȡջ�Ĵ�С
    public int size() {
        return top + 1;
    }

    // ����/���ݷ���
    private void resize(int newCapacity) {
        int[] newArr = new int[newCapacity];
        for (int i = 0; i <= top; i++) {
            newArr[i] = arr[i];
        }
        arr = newArr;
    }
}

// �Զ������ʵ�֣���������
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

    // ��Ӳ���
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

    // ���Ӳ���
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

    // �鿴����Ԫ��
    public int peek() {
        if (isEmpty()) {
            throw new IllegalStateException("Queue is empty");
        }
        return head.value;
    }

    // �ж϶����Ƿ�Ϊ��
    public boolean isEmpty() {
        return size == 0;
    }

    // ��ȡ���еĴ�С
    public int size() {
        return size;
    }
}

// ������
public class CustomDataStructures {
    public static void main(String[] args) {
        // ����ջ
        Stack stack = new Stack();
        stack.push(1);
        stack.push(2);
        stack.push(3);
        System.out.println("Stack pop: " + stack.pop()); // ���3
        System.out.println("Stack peek: " + stack.peek()); // ���2

        // ���Զ���
        Queue queue = new Queue();
        queue.enqueue(10);
        queue.enqueue(20);
        queue.enqueue(30);
        System.out.println("Queue dequeue: " + queue.dequeue()); // ���10
        System.out.println("Queue peek: " + queue.peek()); // ���20
    }
}