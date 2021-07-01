package mk.dm.core.communication.serial.model;

import java.time.Instant;

public record ReceivedMessage(Instant receivedAt, Record message) {

}
