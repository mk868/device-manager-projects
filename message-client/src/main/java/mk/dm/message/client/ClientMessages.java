package mk.dm.message.client;

import java.util.ArrayList;
import java.util.List;

public class ClientMessages {

  private ClientMessages() {
  }

  /**
   * Get all supported message types
   *
   * @return list of record classes
   */
  public static List<Class<? extends Record>> getAll() {
    var result = new ArrayList<Class<? extends Record>>();
    result.add(EnableLogger.class);
    result.add(ErrorLog.class);
    result.add(InfoLog.class);
    result.add(UpdateBlinkParams.class);

    return result;
  }
}
