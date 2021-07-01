package mk.dm.core.message.fieldserializer;

import static org.testng.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.testng.annotations.Test;

public class ByteArrayFieldSerializerTest {

  ByteArrayFieldSerializer parser = new ByteArrayFieldSerializer();

  @Test
  void shouldEmptyStringSerializeToFourBytes() throws IOException {
    var context = new FieldContext("test");

    var os = new ByteArrayOutputStream();
    parser.write(os, new byte[0], context);
    assertEquals(os.toByteArray(), new byte[]{0, 0, 0, 0});
  }

  @Test
  void shouldSerializeAndDeserialize() throws IOException {
    var context = new FieldContext("test");

    var input = new byte[]{12, 13, 63, 62, 12};
    var os = new ByteArrayOutputStream();
    parser.write(os, input, context);
    var is = new ByteArrayInputStream(os.toByteArray());
    var output = parser.read(is, context);

    assertEquals(output, input);
  }

  @Test
  void binaryShouldStartWithSize() throws IOException {
    var context = new FieldContext("test");

    var input = new byte[10];
    var os = new ByteArrayOutputStream();
    parser.write(os, input, context);
    var bin = os.toByteArray();
    assertEquals(bin[0], 0);
    assertEquals(bin[1], 0);
    assertEquals(bin[2], 0);
    assertEquals(bin[3], 10);
  }
}
