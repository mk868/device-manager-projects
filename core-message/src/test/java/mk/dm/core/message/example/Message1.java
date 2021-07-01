package mk.dm.core.message.example;

import mk.dm.core.message.annotation.VarAsciiString;
import mk.dm.core.message.annotation.Message;

@Message(messageId = 1)
public record Message1(int i, Integer ii, @VarAsciiString String s, boolean b) {

}
