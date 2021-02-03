package network.messages;

public class SimpleMessage extends Message {
    private final String msg;

    public SimpleMessage(char type, String msg) {
        super(type);
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }
}
