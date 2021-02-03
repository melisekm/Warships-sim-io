package network;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import game.Board;

public abstract class Node {
	public void sendMessage(PlayerConnection p, int id, String message) throws IOException {
		Message msg = new Message(id, message);
		List<Message> dataToSend = new ArrayList<>();
		dataToSend.add(msg);
        p.out.writeObject(dataToSend);
	}

	public void sendBoard(PlayerConnection p, Board board) throws IOException {
		List<Board> boardList = new ArrayList<>();
		boardList.add(board);
		System.out.println("Sending a board.");
		p.out.writeObject(boardList);
	}
}
