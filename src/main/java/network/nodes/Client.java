package network.nodes;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import app.StdInputReader;
import constants.GameConstants;
import game.Board;
import game.Game;
import constants.NetworkConstants;
import network.connections.Connection;
import network.connections.ServerConnection;

public class Client extends Node {
    public ExecutorService executor = Executors.newCachedThreadPool();
    private Connection serverConnection;
    private Game game;

    public Client() {
        System.out.println("Som Client");
    }

    public void initConnection(String ip, int port) throws IOException {
        // socket ktorym komunikujeme so serverom
        Socket socket = new Socket(ip, port); // inicializacia soketu IP servera a port na akom pocuva
        this.serverConnection = new ServerConnection(this, socket);
        this.executor.execute(serverConnection); // vytvori thread a spusti run metodu
        this.initGame();
    }

    public void initGame() throws IOException {
        String prompt = "Vytvorit hru[v] / Pripojit sa k hre[p] / Quit[q]";
        String option = StdInputReader.loopedInput(prompt, "p", "v", "q");
        switch (option) {
            case "p":
                this.sendSimpleMsg(this.serverConnection, NetworkConstants.REQ_GAMELIST, "");
                break;
            case "v":
                this.sendSimpleMsg(this.serverConnection, NetworkConstants.CREATE_GAME, "");
                break;
            case "q":
                this.serverConnection.closeSocket();
                break;
        }

    }

    public String chooseGame(String gameList) {
        int gameId;
        if (gameList.isEmpty()) {
            System.out.println("Ziadne hry niesu k dispozicii.");
            return null;
        } else {
            System.out.println("Dostupne hry: ");
            System.out.println(gameList);
            String[] list = gameList.split("\\n");
            gameId = this.getGameId(list);
        }
        return String.valueOf(gameId);
    }

    public int getGameId(String[] gameIds) {
        while (true) {
            try {
                int gameId = Integer.parseInt(StdInputReader.getInput("Zadajte id hry."));
                for (String id : gameIds) {
                    if (id.equals(String.valueOf(gameId))) {
                        return gameId;
                    }
                }
            } catch (NumberFormatException e) {
                System.out.println("Musi to byt cislo.");
            }
        }
    }

    public String createGame(int id) {
        // TODO vyplnit suradnice lodi
        //System.out.println("Vyplnte hraciu plochu lodami");
        System.out.println("nacitavam zo suboru.");
        this.game = new Game(id);
        String loadedBoard = this.loadBoard();
        Board playerBoard = new Board(loadedBoard);
        this.game.setP1Board(playerBoard);
        this.game.setP2Board(new Board(5, 5));
        return loadedBoard;

    }

    public String loadBoard() {
        String location = "board\\board.txt";
        return this.io.readBoard(location);
    }

    public String performAction() {
        return StdInputReader.getInput("Zadajte Suradnice Utoku");
    }

    public void gameStateUpdate(int type, String gameData) {
        switch (type) {
            case GameConstants.TARGET_HIT:
                break;
            case GameConstants.SHIP_HIT:
                break;
            case GameConstants.TARGET_MISS:
                break;
            case GameConstants.SHIP_MISS:
                break;
        }
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }
}
