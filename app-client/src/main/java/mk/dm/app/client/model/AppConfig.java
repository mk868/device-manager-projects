package mk.dm.app.client.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class AppConfig {

  private final BooleanProperty debugUI = new SimpleBooleanProperty(false);

  public boolean isDebugUI() {
    return debugUI.get();
  }

  public BooleanProperty debugUIProperty() {
    return debugUI;
  }

  public void setDebugUI(boolean debugUI) {
    this.debugUI.set(debugUI);
  }
}
