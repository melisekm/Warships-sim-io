package network;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import app.Constants;
import app.InputReader;
import game.Board;
import game.Game;

public class Client extends Node {
	public ExecutorService executor = Executors.newCachedThreadPool();
	Socket socket; // socket ktorym komunikujeme so serverom
	PlayerConnection serverConnection;
	String lastAttack;
	Game game;

	public Client() {
		System.out.println("Som Client");
	}

	public void initConnection(String ip, int port) throws UnknownHostException, IOException {
		this.socket = new Socket(ip, port); // inicializacia soketu IP servera a port na akom pocuva
		this.serverConnection = new PlayerConnection(this, this.socket);
		this.executor.execute(serverConnection); // vytvori thread a spusti run metodu
		this.playGame();
	}

	public void playGame() throws IOException {
		Board playerBoard = this.getBoard();
		this.sendMessage(this.serverConnection, Constants.BOARD, "nova board"); // pripojil som sa k novej hre.
		this.sendBoard(this.serverConnection, playerBoard);
		this.game = new Game();
		this.game.setP1Board(playerBoard);
		this.game.setP2Board(this.game.createEmptyBoard());
		/*
		while(true) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
		        Thread.currentThread().interrupt();
				e.printStackTrace();
			}
		}
		//this.closeConnection();
		 */
	}
	public void performAction() throws IOException {
		this.lastAttack = InputReader.getInput("Zadajte Suradnice Utoku");
		this.sendMessage(this.serverConnection, Constants.ATTACK, this.lastAttack); // pripojil som sa k novej hre.
	}
	
	public void updateBoard(String coordinates) {
		//TODO game - update stuff
	}

	public Board getBoard() {
		// TODO get data from input reader// file// recv from server if game is in progress
		return new Board();
	}

	public void closeConnection() throws IOException {
		if (!this.socket.isClosed()) {
			this.socket.close();
			System.out.println("Zatvaram spojenie.");
		}
	}
}
