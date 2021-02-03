package network;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import game.Board;

public class Node {
	// nepotrebne?
	public void sendMessage(Socket socket, PrintWriter out, String message) throws IOException {
		out.println(message);
		out.flush();
	}

	public void sendBoard(Board board, Socket socket) throws IOException {
		// get the output stream from the socket.
		OutputStream outputStream = socket.getOutputStream();
		// create an object output stream from the output stream so we can send an
		// object through it
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
		List<Board> boardList = new ArrayList<>();
		boardList.add(board);
		System.out.println("Sending a board.");
		objectOutputStream.writeObject(boardList);
	}
}
