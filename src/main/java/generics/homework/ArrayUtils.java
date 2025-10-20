package generics.homework;

public class ArrayUtils {
    public static <T> int findFirst(T[] array, T element) {
        if (array == null) {
            return -1;
        }

        for (int i = 0; i < array.length; i++) {
            if (element == null) {
                if (array[i] == null) {
                    return i;
                }
            } else {
                if (element.equals(array[i])) {
                    return i;
                }
            }
        }
        return -1;
    }

    public static void main(String[] args) {
        final String[] names = {"Alice", "Bob", "Charlie"};
        final int index = ArrayUtils.findFirst(names, "Bob"); // Ожидаем: 1
        System.out.println("Index of 'Bob': " + index);

        final int nullArrayResult = ArrayUtils.findFirst(null, "test");
        System.out.println("Null array result: " + nullArrayResult);

        final Integer[] numbers = {1, 2, null, 4};
        final int nullElementIndex = ArrayUtils.findFirst(numbers, null);
        System.out.println("Null element index: " + nullElementIndex);

        final int notFound = ArrayUtils.findFirst(names, "David");
        System.out.println("Not found result: " + notFound);
    }
}