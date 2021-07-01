package mk.dm.core.message.fieldserializer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;

/**
 * Serializer for bytes. The first 4 bytes of data stores array size for deserialization.
 */
public class ByteArrayFieldSerializer implements FieldSerializer<byte[]> {

  private static final int LENGTH_FIELD_SIZE = Integer.BYTES;

  @Override
  public void write(OutputStream os, byte[] value, FieldContext context) throws IOException {
    var len = value.length;
    os.write(ByteBuffer.allocate(LENGTH_FIELD_SIZE).putInt(len).array());
    os.write(value);
  }

  @Override
  public byte[] read(InputStream is, FieldContext context) throws IOException {
    var len = ByteBuffer.wrap(is.readNBytes(LENGTH_FIELD_SIZE)).getInt();
    return is.readNBytes(len);
  }
}
