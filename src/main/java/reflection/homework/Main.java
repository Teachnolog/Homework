package reflection.homework;

public class Main {
  public static void main(String[] args) {
    System.out.println("=== Тест 1: Невалидный пользователь ===");
    User invalidUser = new User();
    invalidUser.setName("A");
    invalidUser.setEmail("invalid-email");
    invalidUser.setAge(200);
    invalidUser.setPassword("123");

    ValidationResult result1 = Validator.validate(invalidUser);

    if (!result1.isValid()) {
      System.out.println("Ошибки валидации:");
      result1.getErrors().forEach(System.out::println);
    }

    System.out.println();

    System.out.println("=== Тест 2: Валидный пользователь ===");
    User validUser = new User();
    validUser.setName("Иван Иванов");
    validUser.setEmail("ivan@example.com");
    validUser.setAge(25);
    validUser.setPassword("securepassword123");

    ValidationResult result2 = Validator.validate(validUser);

    if (result2.isValid()) {
      System.out.println("✓ Валидация пройдена успешно!");
    } else {
      System.out.println("Ошибки валидации:");
      result2.getErrors().forEach(System.out::println);
    }

    System.out.println();

    System.out.println("=== Тест 3: Null объект ===");
    ValidationResult result3 = Validator.validate(null);
    System.out.println("Результат: " + result3);
  }
}