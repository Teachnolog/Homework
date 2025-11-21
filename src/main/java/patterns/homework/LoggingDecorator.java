package patterns.homework;

import java.util.Optional;

public class LoggingDecorator extends AbstractDataServiceDecorator {

  public LoggingDecorator(DataService wrappedService) {
    super(wrappedService);
  }

  @Override
  public Optional<String> findDataByKey(String key) {
    System.out.println("[LOG] Поиск данных по ключу: '" + key + "'");
    Optional<String> result = super.findDataByKey(key);
    if (result.isPresent()) {
      System.out.println("[LOG] Данные найдены для ключа: '" + key + "'");
    } else {
      System.out.println("[LOG] Данные не найдены для ключа: '" + key + "'");
    }
    return result;
  }

  @Override
  public void saveData(String key, String data) {
    System.out.println("[LOG] Сохранение данных. Ключ: '" + key + "', Данные: '" + data + "'");
    super.saveData(key, data);
    System.out.println("[LOG] Данные успешно сохранены для ключа: '" + key + "'");
  }

  @Override
  public boolean deleteData(String key) {
    System.out.println("[LOG] Удаление данных по ключу: '" + key + "'");
    boolean result = super.deleteData(key);
    if (result) {
      System.out.println("[LOG] Данные успешно удалены для ключа: '" + key + "'");
    } else {
      System.out.println("[LOG] Данные не найдены для удаления по ключу: '" + key + "'");
    }
    return result;
  }
}