package mk.dm.core.message.fieldserializer.string;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import mk.dm.core.message.annotation.FixedAsciiString;
import mk.dm.core.message.fieldserializer.FieldContext;
import mk.dm.core.message.fieldserializer.FieldSerializer;

/**
 * Serializer for ASCII strings with fixed length, when input string shorter output data will be
 * completed with bytes 0x00.
 *
 * @see FixedAsciiString - required field annotation
 */
public class FixedAsciiStringFieldSerializer implements FieldSerializer<String> {

  @Override
  public void write(OutputStream os, String value, FieldContext context) throws IOException {
    var len = context.getAnnotation(FixedAsciiString.class).orElseThrow().length();
    var bytes = value.getBytes(StandardCharsets.US_ASCII);
    var output = new byte[len];
    System.arraycopy(bytes, 0, output, 0, Math.min(bytes.length, len));
    os.write(output);
  }

  @Override
  public String read(InputStream is, FieldContext context) throws IOException {
    var len = context.getAnnotation(FixedAsciiString.class).orElseThrow().length();
    var bytes = is.readNBytes(len);
    var strLen = 0;
    for (; strLen < len; strLen++) {
      if (bytes[strLen] == 0) {
        break;
      }
    }
    return new String(bytes, 0, strLen, StandardCharsets.US_ASCII);
  }

  @Override
  public boolean canHandle(FieldContext context) {
    return context.getAnnotation(FixedAsciiString.class).isPresent();
  }
}
