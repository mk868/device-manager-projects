package mk.dm.core.message.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Append to record components with the fixed length ASCII String type
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.RECORD_COMPONENT)
public @interface FixedAsciiString {

  /**
   * Fixed String length
   *
   * @return string length in bytes / ASCII chars
   */
  int length();
}
