package mk.dm.core.message.util;

import java.util.Objects;
import mk.dm.core.message.annotation.Message;

public class FieldSerializerUtil {

  private FieldSerializerUtil() {
  }

  /**
   * Each record which wants to represent a message should be annotated with {@link Message}. {@link
   * Message} provides information about message id.
   *
   * @param messageClazz record class
   * @return message id described by {@link Message}
   */
  public static short getMessageId(Class<? extends Record> messageClazz) {
    var messageAnnotation = messageClazz.getAnnotation(Message.class);
    Objects.requireNonNull(messageAnnotation,
        "@" + Message.class.getSimpleName() + " record annotation required!");
    return messageAnnotation.messageId();
  }
}
