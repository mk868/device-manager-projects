package mk.dm.core.communication.serial;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.Instant;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import mk.dm.core.communication.serial.model.DeviceStatus;
import mk.dm.core.communication.serial.model.ReceivedMessage;
import mk.dm.core.message.MessageSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommunicationMaster {

  private static final Logger log = LoggerFactory.getLogger(CommunicationMaster.class);

  private final ExecutorService callbackExecutor = Executors.newSingleThreadExecutor();
  private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
  private final MessageSerializer messageSerializer = new MessageSerializer();

  private Consumer<DeviceStatus> onDeviceStatusChanged;
  private Consumer<ReceivedMessage> onMessageReceived;

  private SerialPort comPort = null;

  public MessageSerializer getMessageSerializer() {
    return messageSerializer;
  }

  public void setOnDeviceStatusChanged(Consumer<DeviceStatus> onDeviceStatusChanged) {
    this.onDeviceStatusChanged = onDeviceStatusChanged;
  }

  public void setOnMessageReceived(Consumer<ReceivedMessage> onMessageReceived) {
    this.onMessageReceived = onMessageReceived;
  }

  private <T> void invokeCallback(Consumer<T> cb, T data) {
    if (cb != null) {
      callbackExecutor.submit(() -> {
        try {
          cb.accept(data);
        } catch (Exception ex) {
          log.error("Could not invoke callback", ex);
        }
      });
    }
  }

  private void publishDeviceStatusChanged(DeviceStatus deviceStatus) {
    invokeCallback(this.onDeviceStatusChanged, deviceStatus);
  }

  private void publishMessageReceived(ReceivedMessage receivedMessage) {
    invokeCallback(this.onMessageReceived, receivedMessage);
  }

  private void runTimer() {
    executor.schedule(() -> {
      runTimer();
      tick();
    }, 1000, TimeUnit.MILLISECONDS);
  }

  private void tick() {
    var serialPorts = SerialPort.getCommPorts();

    if (comPort == null) {
      findAndOpenDevice(serialPorts);
    } else {
      var online = Arrays.stream(serialPorts)
          .map(SerialPort::getSystemPortName)
          .anyMatch(a -> a.equals(comPort.getSystemPortName()));

      if (!online) {
        connectionLost();
      }
    }
  }

  private void findAndOpenDevice(SerialPort[] serialPorts) {
    SerialPort selected = null;
    for (SerialPort s : serialPorts) {
      log.debug("found device: {}", s.getDescriptivePortName());
      if (s.getDescriptivePortName().contains("ch341")) {
        selected = s;
        break;
      }
    }

    log.info("Selected: {}", selected);

    if (selected != null) {
      openDevice(selected);
    }
  }

  private void openDevice(SerialPort comPort) {
    comPort.setBaudRate(115200);
    comPort.openPort();
    comPort.addDataListener(new SerialPortDataListener() {
      @Override
      public int getListeningEvents() {
        return SerialPort.LISTENING_EVENT_DATA_AVAILABLE +
            SerialPort.LISTENING_EVENT_DATA_RECEIVED;
      }

      @Override
      public void serialEvent(SerialPortEvent event) {
        if (event.getEventType() == SerialPort.LISTENING_EVENT_DATA_RECEIVED) {
          log.debug("Got {} bytes", event.getReceivedData().length);
          var is = new ByteArrayInputStream(event.getReceivedData());
          try {
            var message = messageSerializer.read(is);
            var receivedMessage = new ReceivedMessage(Instant.now(), message);
            publishMessageReceived(receivedMessage);
          } catch (Exception e) {
            log.error("Parse message error", e);
          }
        }
      }
    });
    this.comPort = comPort;

    var status = new DeviceStatus(true);
    publishDeviceStatusChanged(status);
  }

  private void connectionLost() {
    log.info("Device connection lost");
    closeDevice();
  }

  private void closeDevice() {
    if (comPort == null) {
      return;
    }
    comPort.closePort();
    this.comPort = null;
    var status = new DeviceStatus(false);
    publishDeviceStatusChanged(status);
  }

  public void start() {
    log.info("HELLO, start");
    runTimer();
  }

  public void stop() {
    closeDevice();
    executor.shutdown();
    callbackExecutor.shutdown();
    log.info("BYE, stop");
  }

  public void send(Record value) {
    executor.submit(() -> {
      var os = comPort.getOutputStream();
      try {
        messageSerializer.write(os, value);
        Thread.sleep(50);
      } catch (IOException e) {
        log.error("Could not send value", e);
      } catch (InterruptedException ex) {
        Thread.currentThread().interrupted();
      }
    });
  }
}
