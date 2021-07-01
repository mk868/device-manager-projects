package mk.dm.core.message.annotation;

import java.lang.annotation.Annotation;

public class AnnotationFactory {

  public static Message message(short messageId) {
    return new Message() {
      @Override
      public short messageId() {
        return messageId;
      }

      @Override
      public Sender sender() {
        return Sender.BOTH;
      }

      @Override
      public Class<? extends Annotation> annotationType() {
        return Message.class;
      }
    };
  }

  public static FixedAsciiString fixedAsciiString(int length) {
    return new FixedAsciiString() {
      @Override
      public int length() {
        return length;
      }

      @Override
      public Class<? extends Annotation> annotationType() {
        return FixedAsciiString.class;
      }
    };
  }

  public static VarAsciiString varAsciiString() {
    return new VarAsciiString() {
      @Override
      public Class<? extends Annotation> annotationType() {
        return VarAsciiString.class;
      }
    };
  }


}
