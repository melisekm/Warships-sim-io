package network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.net.Socket;
import java.net.SocketException;
import java.util.List;

import app.Constants;
import game.Board;

public class PlayerConnection implements Runnable {

	Node parent;
	Socket socket;
	ObjectInputStream in;
	ObjectOutputStream out;

	protected PlayerConnection(Node parent, Socket socket) throws IOException {
		try {
			socket.setSoTimeout(0);
			socket.setKeepAlive(true);
		} catch (SocketException e) {
			// e.printStackTrace();
		}

		this.parent = parent;
		this.socket = socket;

		this.out = new ObjectOutputStream(this.socket.getOutputStream());
		this.in = new ObjectInputStream(this.socket.getInputStream());

	}

	public void closeConnection() throws IOException {
		if (!this.socket.isClosed()) // ak este nebol zavrety tak ho zavrie
			this.socket.close();
	}

	@Override
	public void run() {
		while (!this.socket.isClosed()) {
			try {
				this.recvMessage();
			} catch (IOException e) {
				e.printStackTrace();
				break;
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}

		try { // loop bol breaknuty alebo niekto ukoncil spojenie ukoncil spojenie
			System.out.println("Zatvaram spojenie.");
			this.closeConnection();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void recvMessage() throws IOException, ClassNotFoundException {
		List<Message> recvdList = this.unpackMsg();
		int type = recvdList.get(0).getType();
		String msg = recvdList.get(0).getMsg();
		switch (type) {
		case Constants.BOARD: // board init
			Board board = this.recvBoard();
			((Server) parent).createBoard(this, board);
			break;
		case Constants.INFO:
			System.out.println(msg);
			break;
		case Constants.REQ_ACTION: // napr sa pripaja k existujucej hre s boardmi.
			System.out.println(msg);
			((Client) parent).performAction();
			break;
		case Constants.ATTACK:
			System.out.println(msg);
			((Server) parent).performAction(this, msg);
			break;
		case Constants.TARGET_HIT:
			System.out.println(msg);
			((Client) parent).updateBoard(msg);
			break;
		case Constants.SHIP_HIT:
			System.out.println(msg);
			((Client) parent).updateBoard(msg);
			break;
		case Constants.TARGET_MISS:
			System.out.println(msg);
			((Client) parent).updateBoard(msg);
			break;
		case Constants.SHIP_MISS:
			System.out.println(msg);
			((Client) parent).updateBoard(msg);
			break;
			// TODO v tychto case-och getnut tu suradnicu 
			//pre hraca na ktoreho sa striela aby to mohol aktualizovat 
		}
	}

	public List<Message> unpackMsg() throws ClassNotFoundException, IOException {
		return (List<Message>) this.in.readObject();
	}

	public Board recvBoard() throws IOException, ClassNotFoundException {
		List<Board> boardList = (List<Board>) this.in.readObject();
		System.out.println("Received board from socket.");
		return boardList.get(0);
	}

}