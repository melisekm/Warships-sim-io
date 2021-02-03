package network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import app.Constants;
import game.Board;
import game.Game;

public class Server extends Node {
	private ServerSocket serverSocket;
	private PlayerConnection p1Con;
	private PlayerConnection p2Con;
	private int connectedPlayers = 0;
	private Game game;

	public ExecutorService executor = Executors.newCachedThreadPool();

	public Server(int port) throws IOException {
		this.serverSocket = new ServerSocket(port);// inicializacia server soketu na ktorom pocuva
		System.out.println("Som server");
	}

	public void initGame() throws IOException { // v cykle cakam na klientov
		while (connectedPlayers != 2) {
			Socket client;
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
		if (p1Con == null) {
			this.p1Con = p;
			this.game = new Game();
			this.game.setP1Board(board);
			System.out.println("Player 1 Board:");
			this.sendMessage(this.p1Con, Constants.INFO, "Server obdrzal board. Ste hrac 1, prosim cakajte.");
		} else {
			this.p2Con = p;
			this.game.setP2Board(board);
			System.out.println("Player 2 Board:");
			this.sendMessage(this.p2Con, Constants.INFO, "Hra spustena.Server obdrzal board.Prosim cakajte");
			this.sendMessage(this.p1Con, Constants.REQ_ACTION, "Pripojil sa druhy hrac. Hra spustena.");
		}
		board.printBoard();
	}

	public void performAction(PlayerConnection p, String coordinates) throws IOException {
		String origin;
		String destination;
		PlayerConnection origPC;
		PlayerConnection destPC;
		if (p == p1Con) {
			origPC = p1Con;
			destPC = p2Con;
			origin = "P1";
			destination = "P2";
		} else {
			origPC = p2Con;
			destPC = p1Con;
			origin = "P2";
			destination = "P1";
		}
		int signal = this.game.performAttack(origin, destination, coordinates);
		if (signal == Constants.HIT) { // TODO poslat suradnice pre hraca na ktoreho sa striela aby to mohol aktualizovat.
			this.sendMessage(origPC, Constants.TARGET_HIT, "Zasiahli ste nepriatelsku lod. " + coordinates);
			this.sendMessage(destPC, Constants.SHIP_HIT, "Nepriatel zasiahol Vasu lod. " + coordinates);
		} else if (signal == Constants.MISS) {
			this.sendMessage(origPC, Constants.TARGET_MISS, "Nezasiahli ste ziadnu lod.. " + coordinates);
			this.sendMessage(destPC, Constants.SHIP_MISS, "Nepriatel netrafil Vasu lod.. " + coordinates);
		} else {
			// TODO koniec hry
		}
		this.sendMessage(destPC, Constants.REQ_ACTION, "Ste na rade.");
	}

	public ServerSocket getServerSocket() {
		return serverSocket;
	}

	public void setServerSocket(ServerSocket serverSocket) {
		this.serverSocket = serverSocket;
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

}
