package network.messages;

import java.io.Serializable;

public abstract class Message implements Serializable {
    private final char type;

    public Message(char type) {
        this.type = type;
    }

    public char getType() {
        return type;
    }
}
