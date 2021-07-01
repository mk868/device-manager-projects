package mk.dm.app.client.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javax.inject.Inject;
import mk.dm.app.client.AppFragments;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MenuController implements Initializable {

  private static final Logger log = LoggerFactory.getLogger(MenuController.class);

  private final ContentController contentController;

  @Inject
  public MenuController(ContentController contentController) {
    this.contentController = contentController;
  }

  @FXML
  protected void handleButtonAction(ActionEvent event) {
    log.info("You clicked me!");
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    log.warn("initialize");
  }

  public void onMainPageAction(ActionEvent actionEvent) throws IOException {
    log.info("Go to main page");
    contentController.load(AppFragments.MAIN);
  }

  public void onSettingsAction(ActionEvent actionEvent) throws IOException {
    log.info("Go to settings page");
    contentController.load(AppFragments.SETTINGS);
  }

  public void onLogsAction(ActionEvent actionEvent) throws IOException {
    log.info("Go to logs page");
    contentController.load(AppFragments.LOGS);
  }
}
