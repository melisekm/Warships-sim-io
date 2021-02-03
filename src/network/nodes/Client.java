package network.nodes;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import app.InputReader;
import game.Board;
import game.Game;
import constants.NetworkConstants;
import network.connections.Connection;
import network.connections.ServerConnection;

public class Client extends Node {
    public ExecutorService executor = Executors.newCachedThreadPool();
    private Socket socket; // socket ktorym komunikujeme so serverom
    private Connection serverConnection;
    private String lastAttack;
    private Game game;

    public Client() {
        System.out.println("Som Client");
    }

    public void initConnection(String ip, int port) throws IOException {
        this.socket = new Socket(ip, port); // inicializacia soketu IP servera a port na akom pocuva
        this.serverConnection = new ServerConnection(this, this.socket);
        this.executor.execute(serverConnection); // vytvori thread a spusti run metodu
        //this.playGame();
    }

    public void requestGameList() throws IOException {
        char type = NetworkConstants.REQ_GAMELIST;
        String msg = "";
        this.sendSimpleMsg(this.serverConnection, type, msg);
    }

    public void chooseGame(String gameList) throws IOException {
        Integer gameId = 1;
        if (gameList.isEmpty()) {
            System.out.println("Ziadne hry niesu k dispozicii.");
            System.out.println("Vytvaram novu.");
        } else {
            System.out.println(gameList);
            String[] list = gameList.split("\\n");
            gameId = this.getGameId(list);
        }

        this.game = new Game(gameId);
        this.sendSimpleMsg(this.serverConnection, NetworkConstants.GAME_ID, gameId.toString());
    }

    public Integer getGameId(String[] list) {
        Integer gameId = 1;
        while (true) {
            gameId = Integer.parseInt(InputReader.getInput("Zadajte id hry."));
            for (String game : list) {
                if (game.equals(gameId)) {
                    return gameId;
                }
            }
        }
    }

    public void sendBoard() {
        // TODO vyplnit suradnice lodi
        Board playerBoard = this.getBoard();
        this.game.setP1Board(playerBoard);
        this.game.setP2Board(this.game.createEmptyBoard());
        this.sendSimpleMsg(serverConnection, NetworkConstants.BOARD, this.constructBoard(playerBoard));

    }

    public Board getBoard() {
        // TODO get data from input reader// file// recv from server if game is in progress
        return this.game.createEmptyBoard();
    }


    public void playGame() throws IOException {
        Board playerBoard = this.getBoard();
        this.sendSimpleMsg(this.serverConnection, NetworkConstants.BOARD, "nova board"); // pripojil som sa k novej hre.
        this.sendBoard(this.serverConnection, playerBoard);
        this.game = new Game(id);
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
        this.sendSimpleMsg(this.serverConnection, NetworkConstants.ATTACK, this.lastAttack); // pripojil som sa k novej hre.
    }

    public void updateBoard(String coordinates) {
        //TODO game - update stuff
    }


    public void closeConnection() throws IOException {
        if (!this.socket.isClosed()) {
            this.socket.close();
            System.out.println("Zatvaram spojenie.");
        }
    }
}
