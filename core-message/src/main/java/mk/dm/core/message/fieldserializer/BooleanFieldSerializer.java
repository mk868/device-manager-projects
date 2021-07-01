package mk.dm.core.message.fieldserializer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Serializer for booleans
 */
public class BooleanFieldSerializer implements FieldSerializer<Boolean> {

  @Override
  public void write(OutputStream os, Boolean value, FieldContext context) throws IOException {
    os.write(((boolean) value) ? 1 : 0);
  }

  @Override
  public Boolean read(InputStream is, FieldContext context) throws IOException {
    return is.readNBytes(1)[0] != 0;
  }
}
