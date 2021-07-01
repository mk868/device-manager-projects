package mk.dm.core.message.fieldserializer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Serializer for bytes
 */
public class ByteFieldSerializer implements FieldSerializer<Byte> {

  @Override
  public void write(OutputStream os, Byte value, FieldContext context) throws IOException {
    os.write(value);
  }

  @Override
  public Byte read(InputStream is, FieldContext context) throws IOException {
    return is.readNBytes(1)[0];
  }
}
