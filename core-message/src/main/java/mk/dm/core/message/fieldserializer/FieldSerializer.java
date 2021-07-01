package mk.dm.core.message.fieldserializer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Interface for writing and reading the smallest unit of information - "field". Big endian byte
 * coding preferred.
 *
 * @param <T> field data type
 */
public interface FieldSerializer<T> {

  /**
   * Write field value to stream
   *
   * @param os    output stream
   * @param value field value
   * @throws IOException in case of IO error
   */
  void write(OutputStream os, T value, FieldContext context) throws IOException;

  /**
   * Read field value from stream
   *
   * @param is input stream, mark/reset not supported
   * @return field value
   * @throws IOException in case of IO error
   */
  T read(InputStream is, FieldContext context) throws IOException;

  /**
   * Check if serializer is able to read / write following field
   *
   * @param context field info
   * @return true when field supportable
   */
  default boolean canHandle(FieldContext context) {
    return true;
  }
}
