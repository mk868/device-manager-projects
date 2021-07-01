package mk.dm.app.client.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;
import javax.inject.Inject;
import mk.dm.app.client.service.DeviceService;
import org.kordamp.ikonli.javafx.FontIcon;

public class DeviceStatusController implements Initializable {

  private final DeviceService deviceService;

  @FXML
  private FontIcon disconnectedDeviceIcon;
  @FXML
  private FontIcon connectedDeviceIcon;
  @FXML
  private FontIcon disconnectedDotIcon;
  @FXML
  private FontIcon connectedDotIcon;
  @FXML
  private Text disconnectedText;
  @FXML
  private Text connectedText;

  @Inject
  public DeviceStatusController(DeviceService deviceService) {
    this.deviceService = deviceService;
  }


  @Override
  public void initialize(URL location, ResourceBundle resources) {
    var connectedProperty = deviceService.getConnectedProperty();

    connectedDeviceIcon.visibleProperty().bind(connectedProperty);
    connectedDeviceIcon.managedProperty().bind(connectedProperty);
    disconnectedDeviceIcon.visibleProperty().bind(connectedProperty.not());
    disconnectedDeviceIcon.managedProperty().bind(connectedProperty.not());

    connectedDotIcon.visibleProperty().bind(connectedProperty);
    connectedDotIcon.managedProperty().bind(connectedProperty);
    disconnectedDotIcon.visibleProperty().bind(connectedProperty.not());
    disconnectedDotIcon.managedProperty().bind(connectedProperty.not());

    connectedText.visibleProperty().bind(connectedProperty);
    connectedText.managedProperty().bind(connectedProperty);
    disconnectedText.visibleProperty().bind(connectedProperty.not());
    disconnectedText.managedProperty().bind(connectedProperty.not());
  }
}
