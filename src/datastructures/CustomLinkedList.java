package datastructures;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class CustomLinkedList<T> implements Iterable<T> {

    private class Node {
        T data;
        Node next;

        Node(T data) {
            this.data = data;
            this.next = null;
        }
    }

    private Node head;
    private Node tail;
    private int size;

    public CustomLinkedList() {
        head = null;
        tail = null;
        size = 0;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void addFirst(T data) {
        Node newNode = new Node(data);

        if (isEmpty()) {
            head = tail = newNode;
        } else {
            newNode.next = head;
            head = newNode;
        }

        size++;
    }

    public void addLast(T data) {
        Node newNode = new Node(data);

        if (isEmpty()) {
            head = tail = newNode;
        } else {
            tail.next = newNode;
            tail = newNode;
        }

        size++;
    }

    public void insertAfter(T target, T data) {
        Node current = head;

        while (current != null) {
            if (current.data.equals(target)) {
                Node newNode = new Node(data);
                newNode.next = current.next;
                current.next = newNode;

                if (current == tail) {
                    tail = newNode;
                }

                size++;
                return;
            }

            current = current.next;
        }

        throw new IllegalArgumentException("Target not found");
    }

    public T remove(T data) {
        if (isEmpty()) {
            return null;
        }

        if (head.data.equals(data)) {
            T removed = head.data;
            head = head.next;

            if (head == null) {
                tail = null;
            }

            size--;
            return removed;
        }

        Node current = head;

        while (current.next != null) {
            if (current.next.data.equals(data)) {
                T removed = current.next.data;

                current.next = current.next.next;

                if (current.next == null) {
                    tail = current;
                }

                size--;
                return removed;
            }

            current = current.next;
        }

        return null;
    }

    public T get(int index) {
        checkIndex(index);

        Node current = head;

        for (int i = 0; i < index; i++) {
            current = current.next;
        }

        return current.data;
    }

   private void checkIndex(int index) {
    if (index < 0 || index >= size) {
        throw new IndexOutOfBoundsException("Invalid index");
    }
}

@Override
public Iterator<T> iterator() {

    return new Iterator<T>() {

        private Node current = head;

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public T next() {

            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            T data = current.data;
            current = current.next;

            return data;
        }
    };
}
}
