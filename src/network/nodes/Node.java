package network.nodes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import app.IOHandler;
import constants.NetworkConstants;
import network.connections.Connection;
import network.messages.Message;

public abstract class Node {
    IOHandler io = new IOHandler();
    public void sendSimpleMsg(Connection dest, int type, String message) throws IOException {
        Message data = new Message(type, message);
        List<Message> dataToSend = new ArrayList<>();
        dataToSend.add(data);
        dest.out.writeObject(dataToSend);
    }

    public void sendGameData(Connection dest, int type, String gameId, String message) throws IOException {
        Message header = new Message(NetworkConstants.GAMEDATA, gameId);
        Message data = new Message(type, message);
        List<Message> dataToSend = new ArrayList<>();
        dataToSend.add(header);
        dataToSend.add(data);
        dest.out.writeObject(dataToSend);
    }

}
