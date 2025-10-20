package collections.homework;

import java.util.*;

/**
 * Утилиты для работы с Map студентов
 */
public class StudentMapUtils {

    /**
     * Находит студентов с оценкой в заданном диапазоне
     * @param map карта студентов
     * @param minGrade минимальная оценка (включительно)
     * @param maxGrade максимальная оценка (включительно)
     * @return список студентов с оценкой в диапазоне [minGrade, maxGrade]
     */
    public static List<Student> findStudentsByGradeRange(Map<Integer, Student> map,
                                                         double minGrade, double maxGrade) {
        List<Student> result = new ArrayList<>();

        for (Student student : map.values()) {
            if (student.getGrade() >= minGrade && student.getGrade() <= maxGrade) {
                result.add(student);
            }
        }

        return result;
    }

    /**
     * Возвращает N студентов с наибольшими id
     * @param map TreeMap студентов, отсортированный по убыванию id
     * @param n количество студентов для возврата
     * @return список N студентов с наибольшими id
     */
    public static List<Student> getTopNStudents(TreeMap<Integer, Student> map, int n) {
        List<Student> result = new ArrayList<>();

        // Используем нисходящий итератор для получения элементов в порядке убывания
        // Но TreeMap уже отсортирован по убыванию из-за Collections.reverseOrder()
        // Поэтому просто берем первые n элементов
        int count = 0;
        for (Map.Entry<Integer, Student> entry : map.entrySet()) {
            if (count >= n) break;
            result.add(entry.getValue());
            count++;
        }

        return result;
    }

    /**
     * Альтернативная реализация с использованием descendingMap
     */
    public static List<Student> getTopNStudentsAlternative(TreeMap<Integer, Student> map, int n) {
        List<Student> result = new ArrayList<>();

        // Для TreeMap с обычной сортировкой (по возрастанию) используем descendingMap
        // Но у нас уже reverseOrder, поэтому descendingMap даст возрастающий порядок
        // Просто итерируемся по entrySet
        Iterator<Map.Entry<Integer, Student>> iterator = map.entrySet().iterator();

        int count = 0;
        while (iterator.hasNext() && count < n) {
            result.add(iterator.next().getValue());
            count++;
        }

        return result;
    }

    public static void main(String[] args) {
        // Создаем HashMap
        Map<Integer, Student> hashMap = new HashMap<>();
        hashMap.put(1, new Student(1, "Alice", 4.5));
        hashMap.put(2, new Student(2, "Bob", 3.8));
        hashMap.put(3, new Student(3, "Charlie", 4.2));
        hashMap.put(4, new Student(4, "Diana", 3.5));
        hashMap.put(5, new Student(5, "Eve", 4.8));

        // Создаем TreeMap с сортировкой по убыванию id
        TreeMap<Integer, Student> treeMap = new TreeMap<>(Collections.reverseOrder());
        treeMap.putAll(hashMap);

        // Тестируем поиск по диапазону оценок
        System.out.println("Студенты с оценкой от 4.0 до 4.5:");
        List<Student> gradeRangeStudents = findStudentsByGradeRange(hashMap, 4.0, 4.5);
        gradeRangeStudents.forEach(System.out::println);

        System.out.println("\nТоп 3 студента по id (с наибольшими id):");
        List<Student> topStudents = getTopNStudents(treeMap, 3);
        topStudents.forEach(System.out::println);

        // Демонстрация сортировки TreeMap
        System.out.println("\nTreeMap (сортировка по убыванию id):");
        treeMap.forEach((id, student) -> System.out.println("ID: " + id + " -> " + student));

        // Покажем всех студентов для наглядности
        System.out.println("\nВсе студенты в HashMap:");
        hashMap.forEach((id, student) -> System.out.println("ID: " + id + " -> " + student));
    }
}