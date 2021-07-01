package mk.dm.core.message.fieldserializer;

import static org.testng.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.testng.annotations.Test;

public class IntegerMapperTest {

  IntegerFieldSerializer parser = new IntegerFieldSerializer();

  @Test
  void shouldSerialize() throws IOException {
    var context = new FieldContext("test");
    int i = 138928;

    var os = new ByteArrayOutputStream();
    parser.write(os, i, context);
    assertEquals(os.toByteArray(), new byte[]{0, 0x02, 0x1E, (byte) 0xB0});
  }

  @Test
  void shouldDeserialize() throws IOException {
    var context = new FieldContext("test");
    byte[] bin = new byte[]{0x03, 0x7D, 0x21, (byte) 0xA2};

    var is = new ByteArrayInputStream(bin);
    var out = parser.read(is, context);

    assertEquals(out.intValue(), 58532258);
  }

}
