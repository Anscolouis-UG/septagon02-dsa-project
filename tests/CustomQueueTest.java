package tests;

import org.junit.jupiter.api.Test;
import datastructures.CustomQueue;

import static org.junit.jupiter.api.Assertions.*;

public class CustomQueueTest {

    @Test
    void testEnqueue() {
        CustomQueue<Integer> queue = new CustomQueue<>();

        queue.enqueue(10);
        queue.enqueue(20);

        assertEquals(2, queue.size());
        assertEquals(10, queue.peek());
    }

    @Test
    void testDequeue() {
        CustomQueue<Integer> queue = new CustomQueue<>();

        queue.enqueue(10);
        queue.enqueue(20);

        assertEquals(10, queue.dequeue());
        assertEquals(20, queue.peek());
    }

    @Test
    void testPeek() {
        CustomQueue<String> queue = new CustomQueue<>();

        queue.enqueue("Java");

        assertEquals("Java", queue.peek());
    }

    @Test
    void testIsEmpty() {
        CustomQueue<Integer> queue = new CustomQueue<>();

        assertTrue(queue.isEmpty());

        queue.enqueue(5);

        assertFalse(queue.isEmpty());
    }

    @Test
    void testDequeueEmptyQueue() {
        CustomQueue<Integer> queue = new CustomQueue<>();

        assertThrows(IllegalStateException.class,
                queue::dequeue);
    }

    @Test
    void testPeekEmptyQueue() {
        CustomQueue<Integer> queue = new CustomQueue<>();

        assertThrows(IllegalStateException.class, queue::peek);
    }

    @Test
    void testWrapAroundPreservesFifoOrder() {
        CustomQueue<Integer> queue = new CustomQueue<>();

        for (int i = 0; i < 10; i++) {
            queue.enqueue(i);
        }
        for (int i = 0; i < 5; i++) {
            assertEquals(i, queue.dequeue());
        }
        for (int i = 10; i < 15; i++) {
            queue.enqueue(i);
        }

        for (int i = 5; i < 15; i++) {
            assertEquals(i, queue.dequeue());
        }
        assertTrue(queue.isEmpty());
    }
}