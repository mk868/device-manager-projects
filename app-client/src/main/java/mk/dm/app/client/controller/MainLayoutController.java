package mk.dm.app.client.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.GridPane;
import javax.inject.Inject;
import mk.dm.app.client.model.AppConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainLayoutController implements Initializable {

  private static final Logger log = LoggerFactory.getLogger(MainLayoutController.class);

  private final AppConfig appConfig;

  @FXML
  private GridPane rootGridPane;

  @Inject
  public MainLayoutController(AppConfig appConfig) {
    this.appConfig = appConfig;
  }

  @Override
  public void initialize(URL url, ResourceBundle rb) {
    log.warn("initialize");
    rootGridPane.gridLinesVisibleProperty().bind(appConfig.debugUIProperty());
  }
}
