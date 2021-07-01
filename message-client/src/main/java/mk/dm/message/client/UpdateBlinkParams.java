package mk.dm.message.client;

import mk.dm.core.message.annotation.Message;
import mk.dm.core.message.annotation.Message.Sender;

@Message(messageId = 0x33, sender = Sender.HOST)
public record UpdateBlinkParams(int timeOn, int timeOff) {

}
