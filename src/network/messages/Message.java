package network.messages;

import java.io.Serializable;

public class Message implements Serializable {
    private final char type;
    private final String msg;

    public Message(char type, String msg) {
        this.type = type;
        this.msg = msg;
    }

    public char getType() {
        return type;
    }

    public String getMsg() {
        return msg;
    }
}
