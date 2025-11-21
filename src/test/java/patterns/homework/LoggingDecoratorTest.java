package patterns.homework;

import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import static org.junit.jupiter.api.Assertions.*;

class LoggingDecoratorTest {

  @Test
  void testFindDataLogging() {
    SimpleDataService simpleService = new SimpleDataService();
    LoggingDecorator loggingService = new LoggingDecorator(simpleService);

    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    PrintStream originalOut = System.out;
    System.setOut(new PrintStream(outputStream));

    try {
      loggingService.saveData("test_key", "test_value");
      loggingService.findDataByKey("test_key");
      loggingService.findDataByKey("nonexistent");

      String output = outputStream.toString();

      assertTrue(output.contains("[LOG] Поиск данных по ключу: 'test_key'"));
      assertTrue(output.contains("[LOG] Данные найдены для ключа: 'test_key'"));
      assertTrue(output.contains("[LOG] Данные не найдены для ключа: 'nonexistent'"));

    } finally {
      System.setOut(originalOut);
    }
  }

  @Test
  void testSaveDataLogging() {
    SimpleDataService simpleService = new SimpleDataService();
    LoggingDecorator loggingService = new LoggingDecorator(simpleService);

    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    PrintStream originalOut = System.out;
    System.setOut(new PrintStream(outputStream));

    try {
      loggingService.saveData("key1", "value1");

      String output = outputStream.toString();

      assertTrue(output.contains("[LOG] Сохранение данных. Ключ: 'key1', Данные: 'value1'"));
      assertTrue(output.contains("[LOG] Данные успешно сохранены для ключа: 'key1'"));

    } finally {
      System.setOut(originalOut);
    }
  }

  @Test
  void testDeleteDataLogging() {
    SimpleDataService simpleService = new SimpleDataService();
    LoggingDecorator loggingService = new LoggingDecorator(simpleService);

    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    PrintStream originalOut = System.out;
    System.setOut(new PrintStream(outputStream));

    try {
      loggingService.saveData("key1", "value1");
      loggingService.deleteData("key1");
      loggingService.deleteData("nonexistent");

      String output = outputStream.toString();

      assertTrue(output.contains("[LOG] Удаление данных по ключу: 'key1'"));
      assertTrue(output.contains("[LOG] Данные успешно удалены для ключа: 'key1'"));
      assertTrue(output.contains("[LOG] Данные не найдены для удаления по ключу: 'nonexistent'"));

    } finally {
      System.setOut(originalOut);
    }
  }
}