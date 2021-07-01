package mk.dm.core.message.fieldserializer;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Provides context for field reading/writing
 */
public class FieldContext {

  private final String fieldName;
  private final List<Annotation> annotations = new ArrayList<>();

  public FieldContext(String fieldName) {
    this.fieldName = fieldName;
  }

  public String getFieldName() {
    return fieldName;
  }

  public List<Annotation> getAnnotations() {
    return annotations;
  }

  public <T extends Annotation> Optional<T> getAnnotation(Class<T> clazz) {
    return annotations.stream()
        .filter(a -> a.annotationType() == clazz)
        .map(clazz::cast)
        .findFirst();
  }
}
