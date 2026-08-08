package tests;

import org.junit.jupiter.api.Test;
import datastructures.CustomArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class CustomArrayListTest {

    @Test
    void testAdd() {
        CustomArrayList<Integer> list = new CustomArrayList<>();

        list.add(10);
        list.add(20);
        list.add(30);

        assertEquals(3, list.size());
        assertEquals(10, list.get(0));
        assertEquals(20, list.get(1));
        assertEquals(30, list.get(2));
    }

    @Test
    void testInsert() {
        CustomArrayList<Integer> list = new CustomArrayList<>();

        list.add(10);
        list.add(30);
        list.insert(1, 20);

        assertEquals(3, list.size());
        assertEquals(20, list.get(1));
    }

    @Test
    void testSet() {
        CustomArrayList<String> list = new CustomArrayList<>();

        list.add("Java");
        list.set(0, "Python");

        assertEquals("Python", list.get(0));
    }

    @Test
    void testRemove() {
        CustomArrayList<Integer> list = new CustomArrayList<>();

        list.add(10);
        list.add(20);
        list.add(30);

        assertEquals(20, list.remove(1));
        assertEquals(2, list.size());
        assertEquals(30, list.get(1));
    }

    @Test
    void testClear() {
        CustomArrayList<Integer> list = new CustomArrayList<>();

        list.add(1);
        list.add(2);

        list.clear();

        assertTrue(list.isEmpty());
    }

    @Test
    void testInvalidIndex() {
        CustomArrayList<Integer> list = new CustomArrayList<>();

        assertThrows(IndexOutOfBoundsException.class,
                () -> list.get(0));
    }

    @Test
    void testResizeKeepsAllElements() {
        CustomArrayList<Integer> list = new CustomArrayList<>();

        for (int i = 0; i < 11; i++) {
            list.add(i);
        }

        assertEquals(11, list.size());
        assertEquals(0, list.get(0));
        assertEquals(10, list.get(10));
    }

    @Test
    void testInsertAtEnd() {
        CustomArrayList<Integer> list = new CustomArrayList<>();

        list.add(10);
        list.insert(list.size(), 20);

        assertEquals(2, list.size());
        assertEquals(20, list.get(1));
    }
}