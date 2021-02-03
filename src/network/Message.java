package network;

import java.io.Serializable;

public class Message implements Serializable{
	private final int type;
	private final String msg;
	
	public Message(int type, String msg) {
		this.type = type;
		this.msg = msg;
	}

	public int getType() {
		return type;
	}

	public String getMsg() {
		return msg;
	}
}
