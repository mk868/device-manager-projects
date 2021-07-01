package mk.dm.message.client;

import mk.dm.core.message.annotation.VarAsciiString;
import mk.dm.core.message.annotation.Message;
import mk.dm.core.message.annotation.Message.Sender;

@Message(messageId = 0x02, sender = Sender.DEVICE)
public record ErrorLog(@VarAsciiString String message) {

}
