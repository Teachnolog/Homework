package collections.homework;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class CustomArrayList<A> implements CustomList<A>, Iterable<A> {
    private static final int DEFAULT_CAPACITY = 10;
    private Object[] elements;
    private int size;

    public CustomArrayList() {
        this.elements = new Object[DEFAULT_CAPACITY];
        this.size = 0;
    }

        public CustomArrayList(int initialCapacity) {
        if (initialCapacity < 0) {
            throw new IllegalArgumentException("Initial capacity cannot be negative: " + initialCapacity);
        }
        this.elements = new Object[initialCapacity];
        this.size = 0;
    }

    @Override
    public boolean add(A element) {
        if (element == null) {
            throw new IllegalArgumentException("Element cannot be null");
        }

        ensureCapacity();
        elements[size] = element;
        size++;
        return true;
    }

    @Override
    @SuppressWarnings("unchecked")
    public A get(int index) {
        checkIndex(index);
        return (A) elements[index];
    }

    @Override
    @SuppressWarnings("unchecked")
    public A remove(int index) {
        checkIndex(index);

        A removedElement = (A) elements[index];

        for (int i = index; i < size - 1; i++) {
            elements[i] = elements[i + 1];
        }

        elements[size - 1] = null;
        size--;
        return removedElement;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    private void ensureCapacity() {
        if (size == elements.length) {
            int newCapacity = (int) (elements.length * 1.5);
            Object[] newElements = new Object[newCapacity];
            System.arraycopy(elements, 0, newElements, 0, size);
            elements = newElements;
        }
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
    }

    @Override
    public Iterator<A> iterator() {
        return new CustomArrayListIterator();
    }

    private class CustomArrayListIterator implements Iterator<A> {
        private int currentIndex = 0;

        @Override
        public boolean hasNext() {
            return currentIndex < size;
        }

        @Override
        @SuppressWarnings("unchecked")
        public A next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            return (A) elements[currentIndex++];
        }
    }

    @Override
    public String toString() {
        if (isEmpty()) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(elements[i]);
            if (i < size - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    public static void main(String[] args) {
        CustomList<String> list = new CustomArrayList<>();

        list.add("Apple");
        list.add("Banana");
        list.add("Orange");
        System.out.println("After adding: " + list);

        System.out.println("Element at index 1: " + list.get(1));

        String removed = list.remove(1);
        System.out.println("Removed: " + removed);
        System.out.println("After removal: " + list);

        System.out.println("Using iterator:");
        for (String fruit : (CustomArrayList<String>) list) {
            System.out.println(fruit);
        }
    }
}