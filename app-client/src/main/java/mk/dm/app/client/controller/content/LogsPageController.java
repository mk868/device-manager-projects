package mk.dm.app.client.controller.content;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javax.inject.Inject;
import mk.dm.app.client.model.LogEntry;
import mk.dm.app.client.service.DeviceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogsPageController implements Initializable {

  private static final Logger log = LoggerFactory.getLogger(LogsPageController.class);

  private final DeviceService deviceService;

  @FXML
  private CheckBox loggerEnabledCheckbox;
  @FXML
  private TableView<LogEntry> logTableView;
  @FXML
  private TableColumn<TableView<LogEntry>, String> dateColumn;
  @FXML
  private TableColumn<TableView<LogEntry>, String> typeColumn;
  @FXML
  private TableColumn<TableView<LogEntry>, String> messageColumn;

  @Inject
  public LogsPageController(DeviceService deviceService) {
    this.deviceService = deviceService;
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    loggerEnabledCheckbox.setSelected(deviceService.loggerEnabledProperty().getValue());
    loggerEnabledCheckbox.disableProperty().bind(deviceService.getConnectedProperty().not());

    logTableView.setItems(deviceService.getLogsProperty());
    logTableView.disableProperty().bind(deviceService.getConnectedProperty().not());

    messageColumn.prefWidthProperty().bind(
        logTableView.widthProperty()
            .subtract(dateColumn.widthProperty())
            .subtract(typeColumn.widthProperty())
            .subtract(20)
    );
  }

  @FXML
  protected void loggerEnabledAction(ActionEvent actionEvent) {
    deviceService.enableLogger(loggerEnabledCheckbox.isSelected());
  }
}
