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
		this.serverSocket = new ServerSocket(port);//inicializacia server soketu na ktorom pocuva
		System.out.println("Som server");
	}

	public void initGame() throws IOException { // v cykle cakam na klientov
		while (connectedPlayers != 2) {
			Socket client = null;
			try {
				client = this.serverSocket.accept(); // klient sa pripojil
				this.initClient(client); // vytvor spojenie a cakaj na board
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
		this.executor.execute(newPlayer); // vytvori thread a spusti run metodu
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
