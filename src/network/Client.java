package network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import app.InputReader;
import game.Board;

public class Client extends Node {
	Socket socket; // socket ktorym komunikujeme so serverom
	BufferedReader in;
	PrintWriter out;

	Board playerBoard;

	public Client() {
		System.out.println("Som Client");
	}

	public void initConnection(String ip, int port) throws UnknownHostException, IOException {
		this.socket = new Socket(ip, port); // inicializacia soketu IP servera a port na akom pocuva
		this.in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
		this.out = new PrintWriter(socket.getOutputStream());
		this.playGame();
	}

	public void playGame() throws IOException {
		// String message = InputReader.getInput("zadajte board:");
		this.playerBoard = this.getBoard();
		this.sendMessage(this.socket, this.out, "B"); // pripojil som sa k novej hre.
		this.sendBoard(this.playerBoard, this.socket);

		this.closeConnection();
	}

	public Board getBoard() {
		// TODO get data from input reader// file// recv from server if game is in progress
		int rows = 5;
		int columns = 5;
		return new Board(rows, columns);
	}

	public void closeConnection() throws IOException {
		if (!this.socket.isClosed()) {
			this.socket.close();
			System.out.println("Zatvaram spojenie.");
		}
	}
}
