module root.core.communication.serial {
  requires org.slf4j;
  requires root.core.message;
  requires com.fazecast.jSerialComm;
  requires java.desktop;

  exports mk.dm.core.communication.serial;
  exports mk.dm.core.communication.serial.model;
}
