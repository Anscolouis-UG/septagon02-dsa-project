package structures;

public class CustomStack<T> {

    private CustomLinkedList<T> list;

    public CustomStack() {
        list = new CustomLinkedList<>();
    }

    public void push(T data) {
        list.addFirst(data);
    }

    public T pop() {
        if (isEmpty()) {
            throw new IllegalStateException("Stack is empty");
        }

        T data = peek();
        list.remove(data);
        return data;
    }

    public T peek() {
        if (isEmpty()) {
            throw new IllegalStateException("Stack is empty");
        }

        return list.get(0);
    }

    public boolean isEmpty() {
        return list.size() == 0;
    }

    public int size() {
        return list.size();
    }
}
