package reflection.homework;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ValidatorTest {

  @Test
  void testValidUser() {
    User user = new User("John Doe", "john@example.com", 30, "password123");
    ValidationResult result = Validator.validate(user);

    assertTrue(result.isValid());
    assertEquals(0, result.getErrors().size());
  }

  @Test
  void testNotNullValidation() {
    User user = new User();
    user.setEmail("test@example.com");
    user.setAge(25);
    user.setPassword("password");

    ValidationResult result = Validator.validate(user);

    assertFalse(result.isValid());
    assertTrue(result.getErrors().contains("Имя не может быть null"));
  }

  @Test
  void testSizeValidation() {
    User user = new User();
    user.setName("A");
    user.setEmail("test@example.com");
    user.setAge(25);
    user.setPassword("123");

    ValidationResult result = Validator.validate(user);

    assertFalse(result.isValid());
    assertTrue(result.getErrors().stream()
        .anyMatch(error -> error.contains("Имя должно быть от 2 до 50 символов")));
    assertTrue(result.getErrors().stream()
        .anyMatch(error -> error.contains("Пароль должен быть от 6 до 20 символов")));
  }

  @Test
  void testRangeValidation() {
    User user = new User();
    user.setName("John");
    user.setEmail("test@example.com");
    user.setAge(-5);
    user.setPassword("password");

    ValidationResult result = Validator.validate(user);

    assertFalse(result.isValid());
    assertTrue(result.getErrors().stream()
        .anyMatch(error -> error.contains("Возраст должен быть от 0 до 150")));
  }

  @Test
  void testEmailValidation() {
    User user = new User();
    user.setName("John");
    user.setEmail("invalid-email");
    user.setAge(25);
    user.setPassword("password");

    ValidationResult result = Validator.validate(user);

    assertFalse(result.isValid());
    assertTrue(result.getErrors().stream()
        .anyMatch(error -> error.contains("Некорректный формат email")));
  }

  @Test
  void testMultipleValidationsOnSameField() {
    User user = new User();
    user.setName(null);
    user.setEmail("valid@example.com");
    user.setAge(25);
    user.setPassword("password");

    ValidationResult result = Validator.validate(user);

    assertFalse(result.isValid());

    assertTrue(result.getErrors().stream()
        .anyMatch(error -> error.contains("Имя не может быть null")));
  }

  @Test
  void testNullObject() {
    ValidationResult result = Validator.validate(null);

    assertFalse(result.isValid());
    assertTrue(result.getErrors().contains("Validated object cannot be null"));
  }

  @Test
  void testEdgeCases() {
    User user1 = new User("Ab", "test@example.com", 25, "123456");
    ValidationResult result1 = Validator.validate(user1);
    assertTrue(result1.isValid());

    User user2 = new User("John", "test@example.com", 0, "password");
    User user3 = new User("John", "test@example.com", 150, "password");

    assertTrue(Validator.validate(user2).isValid());
    assertTrue(Validator.validate(user3).isValid());
  }
}