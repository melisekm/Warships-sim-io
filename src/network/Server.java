package network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import game.Board;
import game.Game;

public class Server extends Node {
	private ServerSocket serverSocket;
	private Socket p1Socket;
	private Socket p2Socket;
	private int connectedPlayers = 0;
	private Game game;

	public ExecutorService executor = Executors.newCachedThreadPool();

	public Server(int port) throws IOException {
		this.serverSocket = new ServerSocket(port);// inicializacia server soketu na ktorom pocuva
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

	public void createBoard(PlayerConnection p, Board board) throws IOException {
		System.out.println("Obrdzal board.");
		if (p1Socket == null) {
			this.p1Socket = p.socket;
			this.game = new Game();
			this.game.setP1Board(board);
			System.out.println("Player 1 Board:");
			//this.sendMessage(this.p1Socket, p.out, "Server obdrzal board.");
		} else {
			this.p2Socket = p.socket;
			this.game.setP2Board(board);
			System.out.println("Player 2 Board:");
			//this.sendMessage(this.p2Socket, p.out, "Server obdrzal board.");
			//this.sendMessage(this.p1Socket, p.out, "Pripojil sa druhy hrac.");
		}
		this.game.printBoard(this.game.getP1Board());
	}

	public ServerSocket getServerSocket() {
		return serverSocket;
	}

	public void setServerSocket(ServerSocket serverSocket) {
		this.serverSocket = serverSocket;
	}

	public Socket getClientOneSocket() {
		return p1Socket;
	}

	public void setClientOneSocket(Socket clientOneSocket) {
		this.p1Socket = clientOneSocket;
	}

	public Socket getClientTwoSocket() {
		return p2Socket;
	}

	public void setClientTwoSocket(Socket clientTwoSocket) {
		this.p2Socket = clientTwoSocket;
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

}
