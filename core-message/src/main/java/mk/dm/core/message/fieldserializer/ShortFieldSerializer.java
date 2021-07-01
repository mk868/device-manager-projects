package mk.dm.core.message.fieldserializer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;

/**
 * Serializer for shorts
 */
public class ShortFieldSerializer implements FieldSerializer<Short> {

  private static final int SIZE = Short.BYTES;

  @Override
  public void write(OutputStream os, Short value, FieldContext context) throws IOException {
    os.write(ByteBuffer.allocate(SIZE)
        .putShort(value)
        .array());
  }

  @Override
  public Short read(InputStream is, FieldContext context) throws IOException {
    return ByteBuffer.wrap(is.readNBytes(SIZE)).getShort();
  }
}
