package patterns.homework;

import java.util.Optional;

public class Main {
  public static void main(String[] args) {
    final DataService service = new ValidationDecorator(
        new MetricableDecorator(
            new LoggingDecorator(
                new CachingDecorator(
                    new SimpleDataService()
                )
            )
        )
    );

    System.out.println("=== Тестирование сервиса данных ===");

    service.saveData("user_1", "John Doe");
    service.saveData("user_2", "Jane Smith");

    final Optional<String> data = service.findDataByKey("user_1");
    System.out.println("Найденные данные: " + data.orElse("не найдено"));

    service.deleteData("user_1");

    final Optional<String> noData = service.findDataByKey("user_1");
    System.out.println("После удаления: " + noData.orElse("не найдено"));

    try {
      service.saveData("", "invalid key");
    } catch (IllegalArgumentException e) {
      System.out.println("Валидация сработала: " + e.getMessage());
    }

    try {
      service.saveData("valid_key", null);
    } catch (IllegalArgumentException e) {
      System.out.println("Валидация сработала: " + e.getMessage());
    }

    System.out.println("=== Завершено ===");
  }
}