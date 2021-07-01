package mk.dm.core.message.fieldserializer.string;

import static org.testng.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import mk.dm.core.message.annotation.AnnotationFactory;
import mk.dm.core.message.fieldserializer.FieldContext;
import org.testng.annotations.Test;

public class VarAsciiStringFieldSerializerTest {

  VarAsciiStringFieldSerializer parser = new VarAsciiStringFieldSerializer();

  @Test
  void shouldEmptyStringSerializeToFourBytes() throws IOException {
    var annotation = AnnotationFactory.varAsciiString();
    var context = new FieldContext("test");
    context.getAnnotations().add(annotation);

    var os = new ByteArrayOutputStream();
    parser.write(os, "", context);
    assertEquals(os.toByteArray(), new byte[]{0, 0, 0, 0});
  }

  @Test
  void shouldSerializeAndDeserialize() throws IOException {
    var annotation = AnnotationFactory.varAsciiString();
    var context = new FieldContext("test");
    context.getAnnotations().add(annotation);

    var input = "some text";
    var os = new ByteArrayOutputStream();
    parser.write(os, input, context);
    var is = new ByteArrayInputStream(os.toByteArray());
    var output = parser.read(is, context);

    assertEquals(output, input);
  }

  @Test
  void binaryShouldStartWithSize() throws IOException {
    var annotation = AnnotationFactory.varAsciiString();
    var context = new FieldContext("test");
    context.getAnnotations().add(annotation);

    var input = "abcdefghij";
    var os = new ByteArrayOutputStream();
    parser.write(os, input, context);
    var bin = os.toByteArray();
    assertEquals(bin[0], 0);
    assertEquals(bin[1], 0);
    assertEquals(bin[2], 0);
    assertEquals(bin[3], 10);
  }
}
