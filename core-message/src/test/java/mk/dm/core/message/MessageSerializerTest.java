package mk.dm.core.message;

import static org.testng.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import mk.dm.core.message.fieldserializer.FieldSerializers;
import mk.dm.core.message.example.Message1;
import mk.dm.core.message.example.Message300;
import org.testng.annotations.Test;

public class MessageSerializerTest {

  @Test
  void shouldSerializeAndDeserialize() throws IOException {
    var fs = new FieldSerializers();
    fs.initDefaults();

    var mapper = new MessageSerializer(fs);
    mapper.addKnownMessage(Message1.class);
    mapper.addKnownMessage(Message300.class);

    var os = new ByteArrayOutputStream();
    var input1 = new Message1(123, 456, "Some text", true);
    mapper.write(os, input1);
    var input300 = new Message300("Some message");
    mapper.write(os, input300);

    var is = new ByteArrayInputStream(os.toByteArray());
    var output1 = mapper.read(is);
    var output300 = mapper.read(is);

    assertEquals(output1.getClass(), Message1.class);
    assertEquals(output1, input1);
    assertEquals(output300.getClass(), Message300.class);
    assertEquals(output300, input300);
  }
}
