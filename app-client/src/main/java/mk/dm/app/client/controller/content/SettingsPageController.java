package mk.dm.app.client.controller.content;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory.IntegerSpinnerValueFactory;
import javax.inject.Inject;
import mk.dm.app.client.service.DeviceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SettingsPageController implements Initializable {

  private static final Logger log = LoggerFactory.getLogger(SettingsPageController.class);

  private final DeviceService deviceService;

  @FXML
  private Spinner<Integer> blinkTimeOnSpinner;
  @FXML
  private Spinner<Integer> blinkTimeOffSpinner;
  @FXML
  private Button saveButton;

  @Inject
  public SettingsPageController(DeviceService deviceService) {
    this.deviceService = deviceService;
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    saveButton.disableProperty().bind(deviceService.getConnectedProperty().not());

    blinkTimeOnSpinner.setValueFactory(new IntegerSpinnerValueFactory(0, 100_000, 0, 10));
    blinkTimeOffSpinner.setValueFactory(new IntegerSpinnerValueFactory(0, 100_000, 0, 10));

    blinkTimeOnSpinner.getValueFactory().setValue(deviceService.ledTimeOnProperty().get());
    blinkTimeOffSpinner.getValueFactory().setValue(deviceService.ledTimeOffProperty().get());
  }

  @FXML
  protected void saveAction(ActionEvent actionEvent) {
    deviceService.updateBlinkParams(blinkTimeOnSpinner.getValue(), blinkTimeOffSpinner.getValue());
  }
}
