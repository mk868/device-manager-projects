package mk.dm.app.client.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javax.inject.Inject;
import mk.dm.app.client.model.AppConfig;

public class BottomController implements Initializable {

  private final AppConfig appConfig;

  @FXML
  private CheckBox debugUI;

  @Inject
  public BottomController(AppConfig appConfig) {
    this.appConfig = appConfig;
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    debugUI.setVisible(appConfig.isDebugUI());
    debugUI.selectedProperty().bindBidirectional(appConfig.debugUIProperty());
  }
}
