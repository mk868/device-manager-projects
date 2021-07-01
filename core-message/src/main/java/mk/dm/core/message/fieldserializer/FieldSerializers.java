package mk.dm.core.message.fieldserializer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import mk.dm.core.message.fieldserializer.string.FixedAsciiStringFieldSerializer;
import mk.dm.core.message.fieldserializer.string.VarAsciiStringFieldSerializer;

public class FieldSerializers {

  private final Map<Class<?>, List<FieldSerializer<?>>> mappings = new HashMap<>();

  public void initDefaults() {
    var fixedAsciiStringSer = new FixedAsciiStringFieldSerializer();
    var varAsciiStringSer = new VarAsciiStringFieldSerializer();
    var booleanSer = new BooleanFieldSerializer();
    var byteSer = new ByteFieldSerializer();
    var byteArraySer = new ByteArrayFieldSerializer();
    var integerSer = new IntegerFieldSerializer();
    var longSer = new LongFieldSerializer();

    mappings.clear();
    addSerializer(String.class, fixedAsciiStringSer);
    addSerializer(String.class, varAsciiStringSer);
    addSerializer(Boolean.TYPE, booleanSer);
    addSerializer(Boolean.class, booleanSer);
    addSerializer(Byte.TYPE, byteSer);
    addSerializer(Byte.class, byteSer);
    addSerializer(byte[].class, byteArraySer);
    addSerializer(Integer.TYPE, integerSer);
    addSerializer(Integer.class, integerSer);
    addSerializer(Long.TYPE, longSer);
    addSerializer(Long.class, longSer);
  }

  @SuppressWarnings("unchecked")
  public <T> List<FieldSerializer<T>> getSerializers(Class<T> clazz) {
    // the pairs placed in the map match, so this cast is safe
    return mappings.getOrDefault(clazz, new ArrayList<>()).stream()
        .map(s -> (FieldSerializer<T>) s)
        .collect(Collectors.toList());
  }

  public <T> void addSerializer(Class<T> clazz, FieldSerializer<T> serializer) {
    mappings.computeIfAbsent(clazz, k -> new ArrayList<>()).add(serializer);
  }
}
