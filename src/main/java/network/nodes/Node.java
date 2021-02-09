package network.nodes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import app.IOHandler;
import constants.NetworkConstants;
import network.connections.Connection;
import network.messages.Message;

public abstract class Node {
    public ExecutorService executor = Executors.newCachedThreadPool();
    IOHandler io = new IOHandler();
    public void sendSimpleMsg(Connection dest, int type, String message) throws IOException {
        Message data = new Message(type, message);
        List<Message> dataToSend = new ArrayList<>();
        dataToSend.add(data);
        dest.out.writeObject(dataToSend);
    }

    public void sendGameData(Connection dest, int type, String hdrMsg, String message) throws IOException {
        Message header = new Message(NetworkConstants.GAMEDATA, hdrMsg);
        Message data = new Message(type, message);
        List<Message> dataToSend = new ArrayList<>();
        dataToSend.add(header);
        dataToSend.add(data);
        dest.out.writeObject(dataToSend);
    }

}
