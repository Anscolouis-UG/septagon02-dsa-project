package datastructures;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class DispatchDeque {

    public static class RoadSegment {
        private String source;
        private String target;
        private double distanceKm;
        private double distanceM;

        public RoadSegment(String source, String target, double distanceKm, double distanceM) {
            this.source = source;
            this.target = target;
            this.distanceKm = distanceKm;
            this.distanceM = distanceM;
        }

        @Override
        public String toString() {
            return String.format("[Road (%s -> %s) | Distance: %.2fm]",
                    source, target, distanceM);
        }
    }

    private static class Node {
        RoadSegment data;
        Node prev, next;
        Node(RoadSegment data) { this.data = data; }
    }

    private Node head;
    private Node tail;
    private int size;

    public void addFirst(RoadSegment item) {
        if (item == null) throw new IllegalArgumentException("Cannot insert null");
        Node newNode = new Node(item);
        if (isEmpty()) {
            head = tail = newNode;
        } else {
            newNode.next = head;
            head.prev = newNode;
            head = newNode;
        }
        size++;
    }

    public void addLast(RoadSegment item) {
        if (item == null) throw new IllegalArgumentException("Cannot insert null");
        Node newNode = new Node(item);
        if (isEmpty()) {
            head = tail = newNode;
        } else {
            tail.next = newNode;
            newNode.prev = tail;
            tail = newNode;
        }
        size++;
    }

    public RoadSegment removeFirst() {
        if (isEmpty()) throw new java.util.NoSuchElementException("java.util.Deque is empty");
        RoadSegment data = head.data;
        head = head.next;
        if (head == null) tail = null;
        else head.prev = null;
        size--;
        return data;
    }

    public RoadSegment removeLast() {
        if (isEmpty()) throw new java.util.NoSuchElementException("java.util.Deque is empty");
        RoadSegment data = tail.data;
        tail = tail.prev;
        if (tail == null) head = null;
        else tail.next = null;
        size--;
        return data;
    }

    public boolean isEmpty() { return size == 0; }
    public int size() { return size; }

    public static void main(String[] args) throws IOException {
        DispatchDeque deque = new DispatchDeque();

        java.util.List<String> rows = Files.readAllLines(Path.of("data_base", "edges_knn_sparse.csv"));
        for (int index = 1; index < rows.size(); index++) {
            String[] columns = rows.get(index).split(",", -1);
            if (columns.length < 4) continue;
            deque.addLast(new RoadSegment(
                    columns[0].trim(), columns[1].trim(), 
                    Double.parseDouble(columns[2].trim()), Double.parseDouble(columns[3].trim())));
        }

        System.out.println("=== Operations Dispatch Sequence (CustomDeque) ===");
        System.out.println("Processing First Enroute Segment: " + deque.removeFirst());
        System.out.println("Processing Last Backup Segment:   " + deque.removeLast());
        System.out.println("Remaining Active Segments: " + deque.size());
    }
}