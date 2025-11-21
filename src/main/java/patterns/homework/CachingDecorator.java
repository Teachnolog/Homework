package patterns.homework;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class CachingDecorator extends AbstractDataServiceDecorator {
  private final ConcurrentMap<String, String> cache = new ConcurrentHashMap<>();

  public CachingDecorator(DataService wrappedService) {
    super(wrappedService);
  }

  @Override
  public Optional<String> findDataByKey(String key) {
    String cachedData = cache.get(key);
    if (cachedData != null) {
      return Optional.of(cachedData);
    }

    Optional<String> data = super.findDataByKey(key);

    data.ifPresent(value -> cache.put(key, value));

    return data;
  }

  @Override
  public void saveData(String key, String data) {
    super.saveData(key, data);
    cache.put(key, data);
  }

  @Override
  public boolean deleteData(String key) {
    boolean result = super.deleteData(key);
    cache.remove(key);
    return result;
  }

  public int getCacheSize() {
    return cache.size();
  }

  public void clearCache() {
    cache.clear();
  }
}