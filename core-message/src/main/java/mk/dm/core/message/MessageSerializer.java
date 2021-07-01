package mk.dm.core.message;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.RecordComponent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import mk.dm.core.message.fieldserializer.FieldContext;
import mk.dm.core.message.fieldserializer.FieldSerializer;
import mk.dm.core.message.fieldserializer.FieldSerializers;
import mk.dm.core.message.fieldserializer.MessageIdFieldSerializer;
import mk.dm.core.message.util.FieldSerializerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageSerializer {

  private static final Logger log = LoggerFactory.getLogger(MessageSerializer.class);

  private final Map<Short, Class<? extends Record>> knownMessages = new HashMap<>();
  private final MessageIdFieldSerializer messageIdFieldSerializer = new MessageIdFieldSerializer();
  private final FieldSerializers fieldSerializers;

  public MessageSerializer(FieldSerializers fieldSerializers) {
    this.fieldSerializers = fieldSerializers;
  }

  public MessageSerializer() {
    this.fieldSerializers = new FieldSerializers();
    this.fieldSerializers.initDefaults();
  }

  /**
   * Add message type to serializer, when message is added then it's possible to deserialize it from
   * {@link InputStream}.
   *
   * @param messageClass message record class
   */
  public void addKnownMessage(Class<? extends Record> messageClass) {
    Objects.requireNonNull(messageClass, "messageClass");
    int constructorsNum = messageClass.getConstructors().length;
    if (constructorsNum != 1) {
      log.error("Record has {} constructors, but only one allowed", constructorsNum);
      throw new IllegalArgumentException("Only records with single constructor allowed");
    }
    knownMessages.put(FieldSerializerUtil.getMessageId(messageClass), messageClass);
  }

  public void addKnownMessages(Collection<Class<? extends Record>> messageClasses) {
    Objects.requireNonNull(messageClasses, "messageClasses");
    messageClasses.forEach(this::addKnownMessage);
  }

  @SuppressWarnings("unchecked")
  protected <F> void writeField(OutputStream os, F o, FieldContext context) throws IOException {
    // this cast is fine! o is instance of F, so the type is Class<F>
    Class<F> fieldClazz = (Class<F>) o.getClass();
    var serializer = getSerializer(fieldClazz, context);
    serializer.write(os, o, context);
  }

  protected <F> F readField(InputStream is, Class<F> fieldClazz, FieldContext context)
      throws IOException {
    var serializer = getSerializer(fieldClazz, context);
    return serializer.read(is, context);
  }

  protected <T> FieldSerializer<T> getSerializer(Class<T> clazz, FieldContext context) {
    var serializerOpt = fieldSerializers.getSerializers(clazz).stream()
        .filter(s -> s.canHandle(context))
        .findFirst();

    if (serializerOpt.isEmpty()) {
      throw new RuntimeException("Could not find serializer for data type: " + clazz.getName()
          + ", may missing component annotation?");
    }
    return serializerOpt.get();
  }

  protected FieldContext createFieldContext(RecordComponent recordComponent) {
    var context = new FieldContext(recordComponent.getName());
    context.getAnnotations().addAll(Arrays.asList(recordComponent.getAnnotations()));
    return context;
  }

  public void write(OutputStream os, Record value) throws IOException {
    var messageClazz = value.getClass();
    short messageId = FieldSerializerUtil.getMessageId(messageClazz);
    messageIdFieldSerializer.write(os, messageId, null);

    for (var rc : messageClazz.getRecordComponents()) {
      try {
        var context = createFieldContext(rc);
        Object fieldValue = rc.getAccessor().invoke(value);
        writeField(os, fieldValue, context);
      } catch (Exception e) {
        log.error("Could not serialize record", e);
        // TODO throw
      }
    }
  }

  public Record read(InputStream is) throws IOException {
    short messageId = messageIdFieldSerializer.read(is, null);
    Class<? extends Record> messageClazz = knownMessages.get(messageId);

    // TODO handle null messageClazz

    // wa always have one constructor
    Constructor<?> constructor = messageClazz.getConstructors()[0];
    List<Object> args = new ArrayList<>();
    for (var rc : messageClazz.getRecordComponents()) {
      var context = createFieldContext(rc);
      args.add(readField(is, rc.getType(), context));
    }
    try {
      return (Record) constructor.newInstance(args.toArray());
    } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
      // validation error?
      log.error("Could not create a new record object", e);
      // TODO throw
    }
    return null;
  }
}
