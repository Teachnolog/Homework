package collections.homework;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CustomArrayListTest {

    @Test
    void testAddAndGet() {
        CustomList<String> list = new CustomArrayList<>();
        list.add("Test1");
        list.add("Test2");

        assertEquals("Test1", list.get(0));
        assertEquals("Test2", list.get(1));
        assertEquals(2, list.size());
    }

    @Test
    void testAddNullThrowsException() {
        CustomList<String> list = new CustomArrayList<>();
        assertThrows(IllegalArgumentException.class, () -> list.add(null));
    }

    @Test
    void testRemove() {
        CustomList<Integer> list = new CustomArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);

        Integer removed = list.remove(1);
        assertEquals(2, removed);
        assertEquals(2, list.size());
        assertEquals(1, list.get(0));
        assertEquals(3, list.get(1));
    }

    @Test
    void testRemoveInvalidIndexThrowsException() {
        CustomList<String> list = new CustomArrayList<>();
        list.add("Test");

        assertThrows(IndexOutOfBoundsException.class, () -> list.remove(1));
        assertThrows(IndexOutOfBoundsException.class, () -> list.remove(-1));
    }

    @Test
    void testGetInvalidIndexThrowsException() {
        CustomList<String> list = new CustomArrayList<>();
        list.add("Test");

        assertThrows(IndexOutOfBoundsException.class, () -> list.get(1));
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(-1));
    }

    @Test
    void testIsEmpty() {
        CustomList<String> list = new CustomArrayList<>();
        assertTrue(list.isEmpty());

        list.add("Test");
        assertFalse(list.isEmpty());

        list.remove(0);
        assertTrue(list.isEmpty());
    }

    @Test
    void testDynamicExpansion() {
        CustomList<Integer> list = new CustomArrayList<>(2);

        for (int i = 0; i < 10; i++) {
            list.add(i);
        }

        assertEquals(10, list.size());
        for (int i = 0; i < 10; i++) {
            assertEquals(i, list.get(i));
        }
    }

    @Test
    void testIterator() {
        CustomArrayList<Integer> list = new CustomArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);

        int sum = 0;
        for (Integer num : list) {
            sum += num;
        }

        assertEquals(6, sum);
    }
}