package network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;

public class PlayerConnection implements Runnable {

	Server parent;

	Socket socket;
	BufferedReader in;
	PrintWriter out;

	protected PlayerConnection(Server parent, Socket socket) throws IOException {
		try {
			socket.setSoTimeout(0);
			socket.setKeepAlive(true);
		} catch (SocketException e) {
			e.printStackTrace();
		}

		this.parent = parent;
		this.socket = socket;

		this.in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
		this.out = new PrintWriter(this.socket.getOutputStream());

	}
	
	public void closeConnection() throws IOException {
		if(!this.socket.isClosed()) // ak este nebol zavrety tak ho zavrie
			this.socket.close();
	}

	@Override
	public void run() {
		while (!this.socket.isClosed()) {
			try {
				String nextEvent = this.in.readLine();
				if(nextEvent == null) {
					break;
				}
				switch (nextEvent.charAt(0)) {
				case 'B': // board init
					String board = nextEvent.substring(1, nextEvent.length());
					parent.createBoard(this.socket, board);
					break;
				// handle event and inform Server
				}
			} catch (IOException e) {
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
	
}