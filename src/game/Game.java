package game;

public class Game {
	private Board p1Board;
	private Board p2Board;

	public void printBoard(Board board) {
		String printedBoard = "";
		for (char[] row : board.gameBoard) {
			for (char c : row) {
				printedBoard += c;
			}
			printedBoard += "\n";
		}
		System.out.println(printedBoard);
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
