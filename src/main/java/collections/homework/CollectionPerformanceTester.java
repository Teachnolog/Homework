package collections.homework;

import java.util.*;

/**
 * Тестер производительности коллекций ArrayList vs LinkedList
 */
public class CollectionPerformanceTester {

    private static final int ELEMENT_COUNT = 10000;

    public static void main(String[] args) {
        System.out.println("Сравнение производительности ArrayList и LinkedList");
        System.out.println("Количество элементов: " + ELEMENT_COUNT);
        System.out.println("==================================================");

        // Тестируем операции
        testAddToEnd();
        testAddToBeginning();
        testInsertInMiddle();
        testRandomAccess();
        testRemoveFromBeginning();
        testRemoveFromEnd();
    }

    private static void testAddToEnd() {
        List<Integer> arrayList = new ArrayList<>();
        List<Integer> linkedList = new LinkedList<>();

        long arrayListTime = measureTime(() -> {
            for (int i = 0; i < ELEMENT_COUNT; i++) {
                arrayList.add(i);
            }
        });

        long linkedListTime = measureTime(() -> {
            for (int i = 0; i < ELEMENT_COUNT; i++) {
                linkedList.add(i);
            }
        });

        printResult("Добавление в конец", arrayListTime, linkedListTime);
    }

    private static void testAddToBeginning() {
        List<Integer> arrayList = new ArrayList<>();
        List<Integer> linkedList = new LinkedList<>();

        long arrayListTime = measureTime(() -> {
            for (int i = 0; i < ELEMENT_COUNT; i++) {
                arrayList.add(0, i);
            }
        });

        long linkedListTime = measureTime(() -> {
            for (int i = 0; i < ELEMENT_COUNT; i++) {
                linkedList.add(0, i);
            }
        });

        printResult("Добавление в начало", arrayListTime, linkedListTime);
    }

    private static void testInsertInMiddle() {
        List<Integer> arrayList = new ArrayList<>();
        List<Integer> linkedList = new LinkedList<>();

        // Предварительно заполняем списки
        for (int i = 0; i < ELEMENT_COUNT; i++) {
            arrayList.add(i);
            linkedList.add(i);
        }

        long arrayListTime = measureTime(() -> {
            for (int i = 0; i < 1000; i++) {
                arrayList.add(ELEMENT_COUNT / 2, i);
            }
        });

        long linkedListTime = measureTime(() -> {
            for (int i = 0; i < 1000; i++) {
                linkedList.add(ELEMENT_COUNT / 2, i);
            }
        });

        printResult("Вставка в середину (1000 операций)", arrayListTime, linkedListTime);
    }

    private static void testRandomAccess() {
        List<Integer> arrayList = new ArrayList<>();
        List<Integer> linkedList = new LinkedList<>();

        // Предварительно заполняем списки
        for (int i = 0; i < ELEMENT_COUNT; i++) {
            arrayList.add(i);
            linkedList.add(i);
        }

        long arrayListTime = measureTime(() -> {
            for (int i = 0; i < 10000; i++) {
                arrayList.get(i % ELEMENT_COUNT);
            }
        });

        long linkedListTime = measureTime(() -> {
            for (int i = 0; i < 10000; i++) {
                linkedList.get(i % ELEMENT_COUNT);
            }
        });

        printResult("Доступ по индексу (10000 операций)", arrayListTime, linkedListTime);
    }

    private static void testRemoveFromBeginning() {
        List<Integer> arrayList = createFilledList(new ArrayList<>());
        List<Integer> linkedList = createFilledList(new LinkedList<>());

        long arrayListTime = measureTime(() -> {
            while (!arrayList.isEmpty()) {
                arrayList.remove(0);
            }
        });

        long linkedListTime = measureTime(() -> {
            while (!linkedList.isEmpty()) {
                linkedList.remove(0);
            }
        });

        printResult("Удаление из начала", arrayListTime, linkedListTime);
    }

    private static void testRemoveFromEnd() {
        List<Integer> arrayList = createFilledList(new ArrayList<>());
        List<Integer> linkedList = createFilledList(new LinkedList<>());

        long arrayListTime = measureTime(() -> {
            while (!arrayList.isEmpty()) {
                arrayList.remove(arrayList.size() - 1);
            }
        });

        long linkedListTime = measureTime(() -> {
            while (!linkedList.isEmpty()) {
                linkedList.remove(linkedList.size() - 1);
            }
        });

        printResult("Удаление из конца", arrayListTime, linkedListTime);
    }

    private static List<Integer> createFilledList(List<Integer> list) {
        for (int i = 0; i < ELEMENT_COUNT; i++) {
            list.add(i);
        }
        return list;
    }

    private static long measureTime(Runnable operation) {
        long startTime = System.nanoTime();
        operation.run();
        long endTime = System.nanoTime();
        return (endTime - startTime) / 1_000_000; // Конвертируем в миллисекунды
    }

    private static void printResult(String operation, long arrayListTime, long linkedListTime) {
        System.out.printf("%-40s | ArrayList: %6d ms | LinkedList: %6d ms%n",
                operation, arrayListTime, linkedListTime);
    }
}