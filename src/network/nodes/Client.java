package network.nodes;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import app.StdInputReader;
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
        this.initGame();
    }
    public void initGame() throws IOException {
        String prompt = "Vytvorit hru[v] / Pripojit sa k hre[p] / Quit[q]";
        String option = StdInputReader.loopedInput(prompt,"p","v","q");
        if(option.equals("p")){
            this.sendSimpleMsg(this.serverConnection, NetworkConstants.REQ_GAMELIST, "");
        }else if(option.equals("v")){
            this.sendSimpleMsg(this.serverConnection, NetworkConstants.CREATE_GAME, "");
        }else if(option.equals("q")){
            this.serverConnection.closeConnection();
        }

    }

    public String chooseGame(String gameList) {
        int gameId;
        if (gameList.isEmpty()) {
            System.out.println("Ziadne hry niesu k dispozicii.");
            return null;
        } else {
            System.out.println(gameList);
            String[] list = gameList.split("\\n");
            gameId = this.getGameId(list);
        }
        return String.valueOf(gameId);
    }

    public int getGameId(String[] gameIds) {
        while (true) {
            int gameId = Integer.parseInt(StdInputReader.getInput("Zadajte id hry."));
            for (String id : gameIds) {
                if (id.equals(String.valueOf(gameId))) {
                    return gameId;
                }
            }
        }
    }

    public String createGame(int id) {
        // TODO vyplnit suradnice lodi
        System.out.println("Vyplnte hraciu plochu lodami");
        this.game = new Game(id);
        Board playerBoard = this.loadBoard();
        this.game.setP1Board(playerBoard);
        this.game.setP2Board(new Board(5, 5));
        return playerBoard.getFormattedBoard();

    }

    public Board loadBoard() {
        String location = "C:\\Users\\melis\\Desktop\\Eclipse workspace\\Warships-sim-io\\test_board\\board1.txt";
        String loadedBoard = this.io.readBoard(location);
        return new Board(loadedBoard);
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }


//    public void playGame() throws IOException {
//        Board playerBoard = this.getBoard();
//        this.sendSimpleMsg(this.serverConnection, NetworkConstants.BOARD, "nova board"); // pripojil som sa k novej hre.
//        this.sendBoard(this.serverConnection, playerBoard);
//        this.game = new Game(id);
//        this.game.setP1Board(playerBoard);
//        this.game.setP2Board(this.game.createEmptyBoard());
//		/*
//		while(true) {
//			try {
//				Thread.sleep(1000);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//		        Thread.currentThread().interrupt();
//				e.printStackTrace();
//			}
//		}
//		//this.closeConnection();
//		 */
//    }
//
//    public void performAction() throws IOException {
//        this.lastAttack = InputReader.getInput("Zadajte Suradnice Utoku");
//        this.sendSimpleMsg(this.serverConnection, NetworkConstants.ATTACK, this.lastAttack); // pripojil som sa k novej hre.
//    }
//
//    public void updateBoard(String coordinates) {
//        //TODO game - update stuff
//    }
}
