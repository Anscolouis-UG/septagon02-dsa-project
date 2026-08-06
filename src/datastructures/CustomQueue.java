package datastructures;

public class CustomQueue<T> {

    private Object[] elements;
    private int front;
    private int rear;
    private int size;
    private int capacity;

    public CustomQueue() {
        capacity = 10;
        elements = new Object[capacity];
        front = 0;
        rear = -1;
        size = 0;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void enqueue(T item) {

        if (size == capacity) {
            resize();
        }

        rear = (rear + 1) % capacity;
        elements[rear] = item;
        size++;
    }

    @SuppressWarnings("unchecked")
    public T dequeue() {

        if (isEmpty()) {
            throw new IllegalStateException("Queue is empty");
        }

        T item = (T) elements[front];
        elements[front] = null;

        front = (front + 1) % capacity;
        size--;

        return item;
    }

    @SuppressWarnings("unchecked")
    public T peek() {

        if (isEmpty()) {
            throw new IllegalStateException("Queue is empty");
        }

        return (T) elements[front];
    }

    private void resize() {

        Object[] newArray = new Object[capacity * 2];

        for (int i = 0; i < size; i++) {
            newArray[i] = elements[(front + i) % capacity];
        }

        elements = newArray;
        front = 0;
        rear = size - 1;
        capacity *= 2;
    }
}
