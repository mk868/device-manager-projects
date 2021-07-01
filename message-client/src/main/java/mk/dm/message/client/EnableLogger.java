package mk.dm.message.client;

import mk.dm.core.message.annotation.Message;
import mk.dm.core.message.annotation.Message.Sender;

@Message(messageId = 0x35, sender = Sender.HOST)
public record EnableLogger(boolean value) {

}
