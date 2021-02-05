package game;

import java.io.Serializable;

public class Board implements Serializable {
	private char[][] gameBoard;

	public Board(char[][] gameBoard) {
		this.gameBoard = gameBoard;
	}

	public Board(int rows, int columns) {
		char[][] gameBoard = new char[columns][rows];
		for(int row = 0; row < rows; row++) {
			for(int column = 0; column < columns; column++) {
				gameBoard[column][row] = '#'; // # predstavuje vodu napr
			}
		}
		this.gameBoard = gameBoard;
	}

	public Board(String formattedBoard){
		String[] temp = formattedBoard.split("\\n");
		char[][] res = new char[temp.length][temp.length];
		for(int row = 0; row < temp.length; row++){
			for(int col = 0; col < temp.length; col++){
				res[col][row] = temp[col].charAt(row);
			}
		}
		this.gameBoard = res;
	}
	//TODO dalsi konstruktor dke sa nacita zo suboru napr

	
	public String getFormattedBoard() {
		StringBuilder printedBoard = new StringBuilder();
		for (char[] row : this.gameBoard) {
			for (char c : row) {
				printedBoard.append(c);
			}
			printedBoard.append('\n');
		}
		return printedBoard.toString();
	}
}
