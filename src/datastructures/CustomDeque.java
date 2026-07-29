package datastructures;

import java.util.NoSuchElementException;

public class CustomDeque<T> {
    private static class Node<T> {
        T data;
        Node<T> prev, next;
        Node(T data) { this.data = data; }
    }

    private Node<T> head;
    private Node<T> tail;
    private int size;

    public void addFirst(T item) {
        if (item == null) throw new IllegalArgumentException("Cannot insert null");
        Node<T> newNode = new Node<>(item);
        if (isEmpty()) {
            head = tail = newNode;
        } else {
            newNode.next = head;
            head.prev = newNode;
            head = newNode;
        }
        size++;
    }

    public void addLast(T item) {
        if (item == null) throw new IllegalArgumentException("Cannot insert null");
        Node<T> newNode = new Node<>(item);
        if (isEmpty()) {
            head = tail = newNode;
        } else {
            tail.next = newNode;
            newNode.prev = tail;
            tail = newNode;
        }
        size++;
    }

    public T removeFirst() {
        if (isEmpty()) throw new NoSuchElementException("Deque is empty");
        T data = head.data;
        head = head.next;
        if (head == null) tail = null;
        else head.prev = null;
        size--;
        return data;
    }

    public T removeLast() {
        if (isEmpty()) throw new NoSuchElementException("Deque is empty");
        T data = tail.data;
        tail = tail.prev;
        if (tail == null) head = null;
        else tail.next = null;
        size--;
        return data;
    }

    public boolean isEmpty() { return size == 0; }
    public int size() { return size; }
    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("  Ghana Smart Service Operations Optimizer");
        System.out.println("  Testing CustomDeque (Core Developer 2)");
        System.out.println("==================================================\n");

        CustomDeque<String> deque = new CustomDeque<>();

        System.out.println("--- Testing Queue Insertions ---");
        deque.addLast("Standard Operation #1");
        deque.addLast("Standard Operation #2");
        deque.addFirst("URGENT: Power Outage Task"); // Adds to front

        System.out.println("Deque size: " + deque.size());

        System.out.println("\n--- Testing Queue Removals ---");
        System.out.println("Removed from Front: " + deque.removeFirst());
        System.out.println("Removed from Back:  " + deque.removeLast());
        System.out.println("Remaining Size:    " + deque.size());

        System.out.println("\nExecution successful!");
    }
}