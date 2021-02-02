package network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server extends Node {
	private ServerSocket serverSocket;
	private Socket clientOneSocket;
	private Socket clientTwoSocket;
	private int connectedPlayers = 0;

	public ExecutorService executor = Executors.newCachedThreadPool();

	public Server(int port) throws IOException {
		this.serverSocket = new ServerSocket(port);
		System.out.println("Som server");
	}

	public void initGame() throws IOException {
		while (connectedPlayers != 2) {
			Socket client = null;
			try {
				client = this.serverSocket.accept();
				this.initClient(client);
				this.connectedPlayers++;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		System.out.println("Pripojili sa dvaja hraci.");

	}

	public void initClient(Socket sock) throws IOException {
		System.out.println("client connected");
		PlayerConnection newPlayer = new PlayerConnection(this, sock);
		this.executor.execute(newPlayer);
	}

	public void createBoard(Socket sock, String board) {
		System.out.println("Obrdzal board.");
		if (clientOneSocket == null) {
			System.out.println("Player 1 Board:");
			this.clientOneSocket = sock;
			// TODO board for p1
		} else {
			this.clientTwoSocket = sock;
			System.out.println("Player 2 Board:");
			// TODO board for p2
		}

		System.out.println("Board: " + board);

	}

	public ServerSocket getServerSocket() {
		return serverSocket;
	}

	public void setServerSocket(ServerSocket serverSocket) {
		this.serverSocket = serverSocket;
	}

	public Socket getClientOneSocket() {
		return clientOneSocket;
	}

	public void setClientOneSocket(Socket clientOneSocket) {
		this.clientOneSocket = clientOneSocket;
	}

	public Socket getClientTwoSocket() {
		return clientTwoSocket;
	}

	public void setClientTwoSocket(Socket clientTwoSocket) {
		this.clientTwoSocket = clientTwoSocket;
	}

}
