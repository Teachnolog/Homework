package patterns.homework;

import java.util.Optional;

public abstract class AbstractDataServiceDecorator implements DataService {
  protected final DataService wrappedService;

  public AbstractDataServiceDecorator(DataService wrappedService) {
    this.wrappedService = wrappedService;
  }

  @Override
  public Optional<String> findDataByKey(String key) {
    return wrappedService.findDataByKey(key);
  }

  @Override
  public void saveData(String key, String data) {
    wrappedService.saveData(key, data);
  }

  @Override
  public boolean deleteData(String key) {
    return wrappedService.deleteData(key);
  }
}