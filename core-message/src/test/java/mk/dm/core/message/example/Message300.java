package mk.dm.core.message.example;

import mk.dm.core.message.annotation.VarAsciiString;
import mk.dm.core.message.annotation.Message;

@Message(messageId = 300)
public record Message300(@VarAsciiString String message) {

}
