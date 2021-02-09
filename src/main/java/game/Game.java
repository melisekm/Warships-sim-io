package game;

import constants.GameConstants;
import constants.NetworkConstants;

import java.io.Serializable;

public class Game implements Serializable {
    private final int id;
    private int playersConnected;
    private Board p1Board;
    private Board p2Board;

    public Game(int id) {
        this.playersConnected = 0;
        this.id = id;
    }

    public void setBoard(String formattedBoard) {
        if (this.playersConnected == 1) {
            this.p1Board = new Board(formattedBoard);
            System.out.println(this.p1Board.getFormattedBoard());
        } else {
            this.p2Board = new Board(formattedBoard);
            System.out.println(this.p1Board.getFormattedBoard());
        }
    }

    public int performAttack(String recvdCoordinates, Board boardToCheck) {
        Coordinates coords = new Coordinates(recvdCoordinates);
        if (boardToCheck.getGameBoard()[coords.getRow()][coords.getColumn()] == '#') {
            return GameConstants.MISS;
        }
        boardToCheck.getGameBoard()[coords.getRow()][coords.getColumn()] = '#';
        return GameConstants.HIT;
    }

    public void printBorads(boolean s) {
        System.out.println("__________________________");
        if(s) System.out.println("| P 2  BOARD||| P 1  BOARD|");
        else System.out.println("|ENEMY BOARD|||YOUR  BOARD|");

        System.out.println("|___________|||__________ |");
        System.out.println("|  12345678 |||  12345678 |");
        StringBuilder b = new StringBuilder();
        char rowId = 'A';
        for(int column = 0; column < GameConstants.GAME_SIZE; column++){
            b.append("|").append(rowId).append(" ");
            for (int row = 0; row < GameConstants.GAME_SIZE; row++){
                b.append(this.p2Board.getGameBoard()[column][row]);
            }
            b.append(" |||");
            b.append(rowId++).append(" ");
            for (int row = 0; row < GameConstants.GAME_SIZE; row++){
                b.append(this.p1Board.getGameBoard()[column][row]);
            }
            b.append(" |\n");
        }
        b.append("|___________|||___________|");
        System.out.println(b.toString());
    }

    public boolean checkEnd() {
        return this.p1Board.checkGameEnd() || this.p2Board.checkGameEnd();
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
