package patterns.homework;

import org.junit.jupiter.api.Test;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

class ValidationDecoratorTest {

  @Test
  void testValidOperations() {
    SimpleDataService simpleService = new SimpleDataService();
    ValidationDecorator validationService = new ValidationDecorator(simpleService);

    assertDoesNotThrow(() -> {
      validationService.saveData("valid_key", "valid_data");
      Optional<String> result = validationService.findDataByKey("valid_key");
      boolean deleted = validationService.deleteData("valid_key");

      assertEquals("valid_data", result.get());
      assertTrue(deleted);
    });
  }

  @Test
  void testInvalidKeyNull() {
    SimpleDataService simpleService = new SimpleDataService();
    ValidationDecorator validationService = new ValidationDecorator(simpleService);

    assertThrows(IllegalArgumentException.class, () -> {
      validationService.saveData(null, "data");
    });

    assertThrows(IllegalArgumentException.class, () -> {
      validationService.findDataByKey(null);
    });

    assertThrows(IllegalArgumentException.class, () -> {
      validationService.deleteData(null);
    });
  }

  @Test
  void testInvalidKeyEmpty() {
    SimpleDataService simpleService = new SimpleDataService();
    ValidationDecorator validationService = new ValidationDecorator(simpleService);

    assertThrows(IllegalArgumentException.class, () -> {
      validationService.saveData("", "data");
    });

    assertThrows(IllegalArgumentException.class, () -> {
      validationService.saveData("   ", "data");
    });
  }

  @Test
  void testInvalidKeyTooLong() {
    SimpleDataService simpleService = new SimpleDataService();
    ValidationDecorator validationService = new ValidationDecorator(simpleService);

    String longKey = "a".repeat(101); // 101 символ

    assertThrows(IllegalArgumentException.class, () -> {
      validationService.saveData(longKey, "data");
    });
  }

  @Test
  void testInvalidKeyCharacters() {
    SimpleDataService simpleService = new SimpleDataService();
    ValidationDecorator validationService = new ValidationDecorator(simpleService);

    assertThrows(IllegalArgumentException.class, () -> {
      validationService.saveData("invalid@key", "data");
    });

    assertThrows(IllegalArgumentException.class, () -> {
      validationService.saveData("invalid key", "data");
    });

    assertThrows(IllegalArgumentException.class, () -> {
      validationService.saveData("invalid.key", "data");
    });
  }

  @Test
  void testInvalidDataNull() {
    SimpleDataService simpleService = new SimpleDataService();
    ValidationDecorator validationService = new ValidationDecorator(simpleService);

    assertThrows(IllegalArgumentException.class, () -> {
      validationService.saveData("valid_key", null);
    });
  }

  @Test
  void testInvalidDataTooLong() {
    SimpleDataService simpleService = new SimpleDataService();
    ValidationDecorator validationService = new ValidationDecorator(simpleService);

    String longData = "a".repeat(10001); // 10001 символ

    assertThrows(IllegalArgumentException.class, () -> {
      validationService.saveData("valid_key", longData);
    });
  }

  @Test
  void testValidKeyCharacters() {
    SimpleDataService simpleService = new SimpleDataService();
    ValidationDecorator validationService = new ValidationDecorator(simpleService);

    assertDoesNotThrow(() -> {
      validationService.saveData("valid-key", "data");
      validationService.saveData("valid_key", "data");
      validationService.saveData("valid123", "data");
      validationService.saveData("VALID", "data");
    });
  }
}