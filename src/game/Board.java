package game;

import java.io.Serializable;

public class Board implements Serializable {
	char[][] gameBoard;
	public Board(int rows, int columns) {
		char[][] gameBoard = new char[columns][rows];
		for(int row = 0; row < rows; row++) {
			for(int column = 0; column < columns; column++) {
				gameBoard[column][row] = '#'; // # predstavuje vodu napr
			}
		}
		this.gameBoard = gameBoard;
	}
	//TODO dalsi konstruktor dke sa nacita zo suboru napr
}
