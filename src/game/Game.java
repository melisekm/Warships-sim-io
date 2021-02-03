package game;

import app.Constants;

public class Game {
	private Board p1Board;
	private Board p2Board;
	
	public Board createEmptyBoard() {
		return new Board();
	}

	public int performAttack(String origin, String destination, String coordinates) {
		System.out.println(origin + " vystrelil na " + destination + " koordinaty: " + coordinates);
		//TODO do stuff
		//update stuff
		return Constants.HIT;
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
}
