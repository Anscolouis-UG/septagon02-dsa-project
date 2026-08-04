package datastructures;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.NoSuchElementException;

public class DispatchDeque {

    public static class RoadSegment {
        private int roadId;
        private String roadName;
        private String roadType;
        private String startLocation;
        private String endLocation;
        private int distanceMeters;
        private int speedLimitKmh;

        public RoadSegment(int roadId, String roadName, String roadType, String startLocation,
                           String endLocation, int distanceMeters, int speedLimitKmh) {
            this.roadId = roadId;
            this.roadName = roadName;
            this.roadType = roadType;
            this.startLocation = startLocation;
            this.endLocation = endLocation;
            this.distanceMeters = distanceMeters;
            this.speedLimitKmh = speedLimitKmh;
        }

        @Override
        public String toString() {
            return String.format("[Road #%d: %s (%s -> %s) | Distance: %dm]",
                    roadId, roadName, startLocation, endLocation, distanceMeters);
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
        if (isEmpty()) throw new NoSuchElementException("Deque is empty");
        RoadSegment data = head.data;
        head = head.next;
        if (head == null) tail = null;
        else head.prev = null;
        size--;
        return data;
    }

    public RoadSegment removeLast() {
        if (isEmpty()) throw new NoSuchElementException("Deque is empty");
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

        List<String> rows = Files.readAllLines(Path.of("data_base", "roads.csv"));
        for (int index = 1; index < rows.size(); index++) {
            String[] columns = rows.get(index).split(",", -1);
            deque.addLast(new RoadSegment(
                    Integer.parseInt(columns[0]), columns[1], columns[2], columns[3], columns[4],
                    Integer.parseInt(columns[5]), Integer.parseInt(columns[6])));
        }

        System.out.println("=== Operations Dispatch Sequence (CustomDeque) ===");
        System.out.println("Processing First Enroute Segment: " + deque.removeFirst());
        System.out.println("Processing Last Backup Segment:   " + deque.removeLast());
        System.out.println("Remaining Active Segments: " + deque.size());
    }
}
