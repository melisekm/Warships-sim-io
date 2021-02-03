package game;

import constants.NetworkConstants;

import java.io.Serializable;

public class Game implements Serializable {
	private final int id;
	private int playersConnected;
	private Board p1Board;
	private Board p2Board;

	public Game(int id) {
		this.playersConnected = his.id = id;
	}

	public Board createEmptyBoard() {
		//TODO iny konstruktor eventualne
		return new Board();
	}

	public int performAttack(String origin, String destination, String coordinates) {
		System.out.println(origin + " vystrelil na " + destination + " koordinaty: " + coordinates);
		//TODO do stuff
		//update stuff
		return NetworkConstants.HIT;
	}
	
	public Board getP1Board() {
		return p1Board;
	}

	public void setP1Board(Board p1Board) {
		this.p1Board = p1Board;
	}

	public Board getP2Board() {
		return p2Board;
	}

	public void setP2Board(Board p2Board) {
		this.p2Board = p2Board;
	}

	public int getId() {
		return id;
	}

	public int getPlayersConnected() {
		return playersConnected;
	}

	public void setPlayersConnected(int playersConnected) {
		this.playersConnected = playersConnected;
	}
}
