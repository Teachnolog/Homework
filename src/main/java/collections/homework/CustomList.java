package collections.homework;

/**
 * Интерфейс для кастомного списка
 * @param <A> тип элементов в списке
 */
public interface CustomList<A> {
    /**
     * Добавляет элемент в конец списка
     * @param element элемент для добавления
     * @return true если элемент успешно добавлен
     * @throws IllegalArgumentException если element равен null
     */
    boolean add(A element);

    /**
     * Получает элемент по индексу
     * @param index индекс элемента
     * @return элемент по указанному индексу
     * @throws IndexOutOfBoundsException если индекс выходит за границы списка
     */
    A get(int index);

    /**
     * Удаляет элемент по индексу
     * @param index индекс элемента для удаления
     * @return удаленный элемент
     * @throws IndexOutOfBoundsException если индекс выходит за границы списка
     */
    A remove(int index);

    /**
     * Возвращает количество элементов в списке
     * @return размер списка
     */
    int size();

    /**
     * Проверяет, пуст ли список
     * @return true если список пуст
     */
    boolean isEmpty();
}