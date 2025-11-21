package patterns.homework;

import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

class MetricableDecoratorTest {

  @Test
  void testMetricsAreSent() {
    SimpleDataService simpleService = new SimpleDataService();
    MetricableDecorator metricService = new MetricableDecorator(simpleService);

    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    PrintStream originalOut = System.out;
    System.setOut(new PrintStream(outputStream));

    try {
      metricService.saveData("key1", "value1");
      metricService.findDataByKey("key1");
      metricService.deleteData("key1");

      String output = outputStream.toString();

      assertTrue(output.contains("[METRIC] Метод 'saveData' выполнялся:"));
      assertTrue(output.contains("[METRIC] Метод 'findDataByKey' выполнялся:"));
      assertTrue(output.contains("[METRIC] Метод 'deleteData' выполнялся:"));

    } finally {
      System.setOut(originalOut);
    }
  }

  @Test
  void testMetricsWithValidData() {
    SimpleDataService simpleService = new SimpleDataService();
    MetricableDecorator metricService = new MetricableDecorator(simpleService);

    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    PrintStream originalOut = System.out;
    System.setOut(new PrintStream(outputStream));

    try {
      metricService.saveData("test", "data");
      Optional<String> result = metricService.findDataByKey("test");

      String output = outputStream.toString();

      assertEquals("data", result.get());
      assertTrue(output.contains("saveData"));
      assertTrue(output.contains("findDataByKey"));

    } finally {
      System.setOut(originalOut);
    }
  }
}