package mk.dm.core.message.fieldserializer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;

/**
 * Serializer for longs
 */
public class LongFieldSerializer implements FieldSerializer<Long> {

  private static final int SIZE = Long.BYTES;

  @Override
  public void write(OutputStream os, Long value, FieldContext context) throws IOException {
    os.write(ByteBuffer.allocate(SIZE)
        .putLong(value)
        .array());
  }

  @Override
  public Long read(InputStream is, FieldContext context) throws IOException {
    return ByteBuffer.wrap(is.readNBytes(SIZE)).getLong();
  }
}
