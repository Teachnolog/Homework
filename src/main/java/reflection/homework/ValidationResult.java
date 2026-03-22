package reflection.homework;

import java.util.ArrayList;
import java.util.List;

public class ValidationResult {
  private boolean valid;
  private List<String> errors;

  public ValidationResult() {
    this.valid = true;
    this.errors = new ArrayList<>();
  }

  public boolean isValid() {
    return valid;
  }

  public List<String> getErrors() {
    return new ArrayList<>(errors);
  }

  public void addError(String error) {
    this.valid = false;
    this.errors.add(error);
  }

  public void merge(ValidationResult other) {
    if (!other.isValid()) {
      this.valid = false;
      this.errors.addAll(other.getErrors());
    }
  }

  @Override
  public String toString() {
    if (valid) {
      return "ValidationResult{valid=true}";
    } else {
      return "ValidationResult{valid=false, errors=" + errors + "}";
    }
  }
}