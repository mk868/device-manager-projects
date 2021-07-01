package mk.dm.core.message.example;

import mk.dm.core.message.annotation.VarAsciiString;

// no @Message annotation
public record SimpleRecord(@VarAsciiString String message) {

}
