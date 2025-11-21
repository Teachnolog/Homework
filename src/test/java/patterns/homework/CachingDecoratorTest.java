package patterns.homework;

import org.junit.jupiter.api.Test;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

class CachingDecoratorTest {

  @Test
  void testFindDataCaching() {
    SimpleDataService simpleService = new SimpleDataService();
    CachingDecorator cachingService = new CachingDecorator(simpleService);

    cachingService.saveData("key1", "value1");

    Optional<String> result1 = cachingService.findDataByKey("key1");
    assertEquals("value1", result1.get());

    Optional<String> result2 = cachingService.findDataByKey("key1");
    assertEquals("value1", result2.get());

    assertTrue(cachingService.getCacheSize() > 0);
  }

  @Test
  void testSaveDataUpdatesCache() {
    SimpleDataService simpleService = new SimpleDataService();
    CachingDecorator cachingService = new CachingDecorator(simpleService);

    cachingService.saveData("key1", "value1");

    assertEquals(1, cachingService.getCacheSize());

    cachingService.saveData("key1", "value2");

    Optional<String> result = cachingService.findDataByKey("key1");
    assertEquals("value2", result.get());
  }

  @Test
  void testDeleteDataInvalidatesCache() {
    SimpleDataService simpleService = new SimpleDataService();
    CachingDecorator cachingService = new CachingDecorator(simpleService);

    cachingService.saveData("key1", "value1");
    cachingService.saveData("key2", "value2");

    assertEquals(2, cachingService.getCacheSize());

    cachingService.deleteData("key1");

    assertEquals(1, cachingService.getCacheSize());

    Optional<String> result = cachingService.findDataByKey("key1");
    assertFalse(result.isPresent());
  }

  @Test
  void testCacheMiss() {
    SimpleDataService simpleService = new SimpleDataService();
    CachingDecorator cachingService = new CachingDecorator(simpleService);

    Optional<String> result = cachingService.findDataByKey("nonexistent");

    assertFalse(result.isPresent());
    assertEquals(0, cachingService.getCacheSize());
  }
}