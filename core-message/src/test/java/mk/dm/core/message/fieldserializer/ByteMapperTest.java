package mk.dm.core.message.fieldserializer;

import static org.testng.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.testng.annotations.Test;

public class ByteMapperTest {

  ByteFieldSerializer parser = new ByteFieldSerializer();

  @Test
  void shouldSerialize() throws IOException {
    var context = new FieldContext("test");
    byte b = (byte) 0b1110_0000;

    var os = new ByteArrayOutputStream();
    parser.write(os, b, context);
    assertEquals(os.toByteArray(), new byte[]{b});
  }

  @Test
  void shouldDeserialize() throws IOException {
    var context = new FieldContext("test");
    byte inputByte = (byte) 0b0111_0001;

    var is = new ByteArrayInputStream(new byte[]{inputByte});
    var outputByte = parser.read(is, context);

    assertEquals(outputByte.byteValue(), inputByte);
  }

}
