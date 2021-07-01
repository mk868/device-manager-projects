package mk.dm.message.client;

import java.util.HashSet;
import mk.dm.core.message.util.FieldSerializerUtil;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class ClientMessagesTest {

  @Test
  void shouldHaveUniqueFrameIds() {
    SoftAssert softAssertion = new SoftAssert();

    var frames = ClientMessages.getAll();
    var ids = frames.stream()
        .map(FieldSerializerUtil::getMessageId)
        .toList();

    var uniqueIds = new HashSet<>(ids);
    for (var uid : uniqueIds) {
      var count = ids.stream()
          .filter(uid::equals)
          .count();
      softAssertion.assertEquals(count, 1,
          "frameId 0x" + Integer.toHexString(uid) + "(" + uid + ") used " + count + " times");
    }

    softAssertion.assertAll();
  }

}
