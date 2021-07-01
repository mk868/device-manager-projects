package mk.dm.core.message.fieldserializer.string;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import mk.dm.core.message.annotation.VarAsciiString;
import mk.dm.core.message.fieldserializer.FieldContext;
import mk.dm.core.message.fieldserializer.FieldSerializer;

/**
 * Serializer for ASCII strings. The first 4 bytes of data stores string length for
 * deserialization.
 *
 * @see VarAsciiString - required field annotation
 */
public class VarAsciiStringFieldSerializer implements FieldSerializer<String> {

  private static final int LENGTH_FIELD_SIZE = 4;

  @Override
  public void write(OutputStream os, String value, FieldContext context) throws IOException {
    var len = value.length();
    os.write(ByteBuffer.allocate(LENGTH_FIELD_SIZE).putInt(len).array());
    os.write(value.getBytes(StandardCharsets.US_ASCII));
  }

  @Override
  public String read(InputStream is, FieldContext context) throws IOException {
    var len = ByteBuffer.wrap(is.readNBytes(LENGTH_FIELD_SIZE)).getInt();
    return new String(is.readNBytes(len), 0, len, StandardCharsets.US_ASCII);
  }

  @Override
  public boolean canHandle(FieldContext context) {
    return context.getAnnotation(VarAsciiString.class).isPresent();
  }
}
