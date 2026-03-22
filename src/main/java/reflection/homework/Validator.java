package reflection.homework;

import java.lang.reflect.Field;
import java.util.regex.Pattern;

public class Validator {

  private static final String EMAIL_PATTERN =
      "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
  private static final Pattern emailPattern = Pattern.compile(EMAIL_PATTERN);

  public static ValidationResult validate(Object object) {
    ValidationResult result = new ValidationResult();

    if (object == null) {
      result.addError("Validated object cannot be null");
      return result;
    }

    Class<?> clazz = object.getClass();
    Field[] fields = clazz.getDeclaredFields();

    for (Field field : fields) {
      field.setAccessible(true);

      try {
        Object fieldValue = field.get(object);
        validateField(field, fieldValue, result);
      } catch (IllegalAccessException e) {
        result.addError("Cannot access field: " + field.getName());
      }
    }

    return result;
  }

  private static void validateField(Field field, Object fieldValue, ValidationResult result) {
    if (field.isAnnotationPresent(NotNull.class)) {
      NotNull annotation = field.getAnnotation(NotNull.class);
      if (fieldValue == null) {
        result.addError(annotation.message());
      }
    }

    if (field.isAnnotationPresent(Size.class) && fieldValue instanceof String) {
      Size annotation = field.getAnnotation(Size.class);
      String stringValue = (String) fieldValue;
      int length = stringValue.length();

      if (length < annotation.min() || length > annotation.max()) {
        String message = annotation.message()
            .replace("{min}", String.valueOf(annotation.min()))
            .replace("{max}", String.valueOf(annotation.max()));
        result.addError(message);
      }
    }

    if (field.isAnnotationPresent(Range.class) && fieldValue != null) {
      Range annotation = field.getAnnotation(Range.class);

      if (fieldValue instanceof Number) {
        Number numberValue = (Number) fieldValue;
        long value = numberValue.longValue();

        if (value < annotation.min() || value > annotation.max()) {
          String message = annotation.message()
              .replace("{min}", String.valueOf(annotation.min()))
              .replace("{max}", String.valueOf(annotation.max()));
          result.addError(message);
        }
      }
    }

    if (field.isAnnotationPresent(Email.class) && fieldValue instanceof String) {
      Email annotation = field.getAnnotation(Email.class);
      String email = (String) fieldValue;

      if (email != null && !email.isEmpty() && !isValidEmail(email)) {
        result.addError(annotation.message());
      }
    }
  }

  private static boolean isValidEmail(String email) {
    return emailPattern.matcher(email).matches();
  }
}