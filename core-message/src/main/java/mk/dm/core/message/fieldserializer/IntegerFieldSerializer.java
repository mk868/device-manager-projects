package mk.dm.core.message.fieldserializer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;

/**
 * Serializer for ints
 */
public class IntegerFieldSerializer implements FieldSerializer<Integer> {

  private static final int SIZE = Integer.BYTES;

  @Override
  public void write(OutputStream os, Integer value, FieldContext context) throws IOException {
    os.write(ByteBuffer.allocate(SIZE)
        .putInt(value)
        .array());
  }

  @Override
  public Integer read(InputStream is, FieldContext context) throws IOException {
    return ByteBuffer.wrap(is.readNBytes(SIZE)).getInt();
  }
}
