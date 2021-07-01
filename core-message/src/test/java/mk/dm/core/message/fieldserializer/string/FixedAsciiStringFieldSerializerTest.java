package mk.dm.core.message.fieldserializer.string;

import static org.testng.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import mk.dm.core.message.annotation.AnnotationFactory;
import mk.dm.core.message.fieldserializer.FieldContext;
import org.testng.annotations.Test;

public class FixedAsciiStringFieldSerializerTest {

  FixedAsciiStringFieldSerializer parser = new FixedAsciiStringFieldSerializer();

  @Test
  void shouldAddPaddingBytesToEmptyString() throws IOException {
    var annotation = AnnotationFactory.fixedAsciiString(4);
    var context = new FieldContext("test");
    context.getAnnotations().add(annotation);

    var os = new ByteArrayOutputStream();
    parser.write(os, "", context);
    assertEquals(os.toByteArray(), new byte[]{0, 0, 0, 0});
  }

  @Test
  void shouldAddPaddingBytes() throws IOException {
    var annotation = AnnotationFactory.fixedAsciiString(10);
    var context = new FieldContext("test");
    context.getAnnotations().add(annotation);

    var os = new ByteArrayOutputStream();
    parser.write(os, "test", context);
    assertEquals(os.toByteArray(), new byte[]{116, 101, 115, 116, 0, 0, 0, 0, 0, 0});
  }

  @Test
  void shouldSerializeAndDeserialize() throws IOException {
    var annotation = AnnotationFactory.fixedAsciiString(20);
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
  void shouldCutLongText() throws IOException {
    var annotation = AnnotationFactory.fixedAsciiString(5);
    var context = new FieldContext("test");
    context.getAnnotations().add(annotation);

    var os = new ByteArrayOutputStream();
    parser.write(os, "0123456789", context);
    assertEquals(os.toByteArray(), new byte[]{'0', '1', '2', '3', '4'});
  }
}
