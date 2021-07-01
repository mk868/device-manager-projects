package mk.dm.core.message.fieldserializer;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.testng.annotations.Test;

public class BooleanMapperTest {

  static final byte BYTE_TRUE = 0b0000_0001;
  static final byte BYTE_FALSE = 0b0000_0000;

  BooleanFieldSerializer parser = new BooleanFieldSerializer();

  @Test
  void shouldSerializeTrue() throws IOException {
    var context = new FieldContext("test");

    var os = new ByteArrayOutputStream();
    parser.write(os, true, context);
    assertEquals(os.toByteArray(), new byte[]{BYTE_TRUE});
  }

  @Test
  void shouldSerializeFalse() throws IOException {
    var context = new FieldContext("test");

    var os = new ByteArrayOutputStream();
    parser.write(os, false, context);
    assertEquals(os.toByteArray(), new byte[]{BYTE_FALSE});
  }

  @Test
  void anyNonZeroByteShouldBeTrue() throws IOException {
    var context = new FieldContext("test");

    for (int i = Byte.MIN_VALUE; i <= Byte.MAX_VALUE; i++) {
      byte b = (byte) i;

      if (b == BYTE_FALSE) {
        continue;
      }

      var is = new ByteArrayInputStream(new byte[]{b});
      assertTrue(parser.read(is, context));
    }
  }

  @Test
  void zeroByteShouldBeFalse() throws IOException {
    var context = new FieldContext("test");

    var is = new ByteArrayInputStream(new byte[]{BYTE_FALSE});
    assertFalse(parser.read(is, context));
  }

}
