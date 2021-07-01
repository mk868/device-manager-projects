package mk.dm.core.message.util;

import static org.testng.Assert.assertEquals;

import mk.dm.core.message.example.Message1;
import mk.dm.core.message.example.Message300;
import org.testng.annotations.Test;

public class FieldSerializerUtilTest {

  @Test
  void testMessageId() {
    assertEquals(FieldSerializerUtil.getMessageId(Message1.class), 1);
    assertEquals(FieldSerializerUtil.getMessageId(Message300.class), 300);
  }
}
