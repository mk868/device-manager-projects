package mk.dm.core.message.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Message {

  /**
   * message id
   *
   * @return id of the message
   */
  short messageId();

  /**
   * Who could send a message - information mainly for documentation purposes.
   *
   * @return sender
   */
  Sender sender() default Sender.BOTH;

  enum Sender {
    BOTH,
    HOST,
    DEVICE
  }
}
