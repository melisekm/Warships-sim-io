package network.nodes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import game.Board;
import network.connections.Connection;
import network.messages.GameListMessage;
import network.messages.Message;
import network.messages.SimpleMessage;

public abstract class Node {
    public void sendSimpleMsg(Connection dest, char type, String message) throws IOException {
        Message msg = new SimpleMessage(type, message);
        this.send(dest, msg);
    }

    public void sendGameList(Connection dest, char type, ArrayList<Integer> gameList) throws IOException {
        GameListMessage msg = new GameListMessage(type, gameList);
        this.send(dest, msg);
    }

    public void sendBoard(Connection dest, Board board) throws IOException {
        List<Board> boardList = new ArrayList<>();
        boardList.add(board);
        System.out.println("Sending a board.");
        dest.out.writeObject(boardList);
    }

    public void send(Connection dest, Message msg) throws IOException {
        List<Message> dataToSend = new ArrayList<>();
        dataToSend.add(msg);
        dest.out.writeObject(dataToSend);
    }


}
