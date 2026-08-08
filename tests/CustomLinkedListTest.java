

import org.junit.jupiter.api.Test;
import datastructures.CustomLinkedList;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

public class CustomLinkedListTest {

    @Test
    void testAddFirst() {
        CustomLinkedList<Integer> list = new CustomLinkedList<>();

        list.addFirst(20);
        list.addFirst(10);

        assertEquals(10, list.get(0));
        assertEquals(20, list.get(1));
    }

    @Test
    void testAddLast() {
        CustomLinkedList<Integer> list = new CustomLinkedList<>();

        list.addLast(10);
        list.addLast(20);

        assertEquals(2, list.size());
        assertEquals(20, list.get(1));
    }

    @Test
    void testInsertAfter() {
        CustomLinkedList<Integer> list = new CustomLinkedList<>();

        list.addLast(10);
        list.addLast(30);

        list.insertAfter(10, 20);

        assertEquals(20, list.get(1));
    }

    @Test
    void testRemove() {
        CustomLinkedList<Integer> list = new CustomLinkedList<>();

        list.addLast(10);
        list.addLast(20);

        assertEquals(10, list.remove(10));
        assertEquals(20, list.get(0));
    }

    @Test
    void testSize() {
        CustomLinkedList<String> list = new CustomLinkedList<>();

        list.addLast("A");
        list.addLast("B");

        assertEquals(2, list.size());
    }

    @Test
    void testIsEmpty() {
        CustomLinkedList<Integer> list = new CustomLinkedList<>();

        assertTrue(list.isEmpty());

        list.addFirst(1);

        assertFalse(list.isEmpty());
    }

    @Test
    void testIteratorVisitsElementsInOrder() {
        CustomLinkedList<String> list = new CustomLinkedList<>();
        list.addLast("A");
        list.addLast("B");

        Iterator<String> iterator = list.iterator();

        assertTrue(iterator.hasNext());
        assertEquals("A", iterator.next());
        assertEquals("B", iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    void testGetInvalidIndex() {
        CustomLinkedList<Integer> list = new CustomLinkedList<>();
        list.addLast(10);

        assertThrows(IndexOutOfBoundsException.class, () -> list.get(1));
    }
}
