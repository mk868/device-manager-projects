package mk.dm.app.client.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TopController implements Initializable {

  private static final Logger log = LoggerFactory.getLogger(TopController.class);

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    log.warn("initialize");
  }
}
