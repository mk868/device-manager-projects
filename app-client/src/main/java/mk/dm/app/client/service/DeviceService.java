package mk.dm.app.client.service;

import com.google.inject.Singleton;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import mk.dm.app.client.model.LogEntry;
import mk.dm.core.communication.serial.CommunicationMaster;
import mk.dm.core.communication.serial.model.DeviceStatus;
import mk.dm.core.communication.serial.model.ReceivedMessage;
import mk.dm.message.client.ClientMessages;
import mk.dm.message.client.EnableLogger;
import mk.dm.message.client.ErrorLog;
import mk.dm.message.client.InfoLog;
import mk.dm.message.client.UpdateBlinkParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class DeviceService {

  private static final Logger log = LoggerFactory.getLogger(DeviceService.class);
  private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter
      .ofPattern("HH:mm:ss").withZone(ZoneId.systemDefault());
  private static final int LOG_LIMIT = 50;

  private final CommunicationMaster communicationMaster;

  private final ObservableList<LogEntry> logsList = FXCollections.observableArrayList();
  private final ListProperty<LogEntry> logsProperty = new SimpleListProperty<>(logsList);
  private final BooleanProperty connectedProperty = new SimpleBooleanProperty();

  private final BooleanProperty loggerEnabledProperty = new SimpleBooleanProperty(true);
  private final IntegerProperty ledTimeOnProperty = new SimpleIntegerProperty(1000);
  private final IntegerProperty ledTimeOffProperty = new SimpleIntegerProperty(1000);

  public DeviceService() {
    communicationMaster = new CommunicationMaster();
    // register supported message types
    communicationMaster.getMessageSerializer().addKnownMessages(ClientMessages.getAll());
    communicationMaster.setOnDeviceStatusChanged(this::onDeviceStatusChanged);
    communicationMaster.setOnMessageReceived(this::onMessageReceived);
  }

  protected void onMessageReceived(ReceivedMessage receivedMessage) {
    log.info("new message: {}", receivedMessage);
    if (receivedMessage.message() instanceof InfoLog log) {
      addLog(receivedMessage.receivedAt(), "INFO", log.message());
    } else if (receivedMessage.message() instanceof ErrorLog log) {
      addLog(receivedMessage.receivedAt(), "ERROR", log.message());
    }
  }

  protected void onDeviceStatusChanged(DeviceStatus deviceStatus) {
    log.info("device status changed: {}", deviceStatus);
    this.connectedProperty.setValue(deviceStatus.connected());
  }

  protected void addLog(Instant receivedAt, String type, String message) {
    var date = DATE_TIME_FORMATTER.format(receivedAt);
    logsList.add(0, new LogEntry(date, type, message));
    while (logsList.size() > LOG_LIMIT) {
      logsList.remove(LOG_LIMIT);
    }
  }

  public ListProperty<LogEntry> getLogsProperty() {
    return logsProperty;
  }

  public BooleanProperty getConnectedProperty() {
    return connectedProperty;
  }

  public BooleanProperty loggerEnabledProperty() {
    return loggerEnabledProperty;
  }

  public IntegerProperty ledTimeOnProperty() {
    return ledTimeOnProperty;
  }

  public IntegerProperty ledTimeOffProperty() {
    return ledTimeOffProperty;
  }

  public void start() {
    this.communicationMaster.start();
  }

  public void stop() {
    this.communicationMaster.stop();
  }

  public void updateBlinkParams(int timeOn, int timeOff) {
    communicationMaster.send(new UpdateBlinkParams(timeOn, timeOff));
    ledTimeOnProperty.setValue(timeOn);
    ledTimeOffProperty.setValue(timeOff);
  }

  public void enableLogger(boolean value) {
    communicationMaster.send(new EnableLogger(value));
    loggerEnabledProperty.setValue(value);
  }
}
