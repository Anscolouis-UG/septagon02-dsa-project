

import org.junit.jupiter.api.Test;
import datastructures.CustomStack;

import static org.junit.jupiter.api.Assertions.*;

public class CustomStackTest {

    @Test
    void testPush() {
        CustomStack<Integer> stack = new CustomStack<>();

        stack.push(10);
        stack.push(20);

        assertEquals(2, stack.size());
        assertEquals(20, stack.peek());
    }

    @Test
    void testPop() {
        CustomStack<Integer> stack = new CustomStack<>();

        stack.push(10);
        stack.push(20);

        assertEquals(20, stack.pop());
        assertEquals(10, stack.peek());
    }

    @Test
    void testPeek() {
        CustomStack<String> stack = new CustomStack<>();

        stack.push("Java");

        assertEquals("Java", stack.peek());
    }

    @Test
    void testIsEmpty() {
        CustomStack<Integer> stack = new CustomStack<>();

        assertTrue(stack.isEmpty());

        stack.push(100);

        assertFalse(stack.isEmpty());
    }

    @Test
    void testPopEmptyStack() {
        CustomStack<Integer> stack = new CustomStack<>();

        assertThrows(IllegalStateException.class,
                stack::pop);
    }

    @Test
    void testPeekEmptyStack() {
        CustomStack<Integer> stack = new CustomStack<>();

        assertThrows(IllegalStateException.class, stack::peek);
    }

    @Test
    void testDuplicateValuesPopInLifoOrder() {
        CustomStack<Integer> stack = new CustomStack<>();
        stack.push(10);
        stack.push(20);
        stack.push(10);

        assertEquals(10, stack.pop());
        assertEquals(20, stack.pop());
        assertEquals(10, stack.pop());
        assertTrue(stack.isEmpty());
    }
}
