package network.nodes;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.Socket;

import app.StdInputReader;
import constants.GameConstants;
import game.Board;
import game.Coordinates;
import game.Game;
import constants.NetworkConstants;
import network.connections.Connection;
import network.connections.ServerConnection;

public class Client extends Node {
    private Connection serverConnection;
    private Game game;
    private String lastAttack;

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
    //TODO Players connected zobrazit.
    //TODO p1 zalozi hru neodosle board, ale vytvori sa hra na ktoru sa neda pripojit a ptm to padne.
    // nezobrazit hru dokym neni poslany board.
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
        String loadedBoard = null;
        while (loadedBoard == null) {
            loadedBoard = this.getBoardFromPlayer();
        }
        this.game = new Game(id);
        Board playerBoard = new Board(loadedBoard);
        System.out.println(playerBoard.getFormattedBoard());
        this.game.setP1Board(playerBoard);
        this.game.setP2Board(new Board(8, 8));
        return loadedBoard;
    }

    // returns false on fail.
    private String getBoardFromPlayer() {
        String loadedBoard = null;
        String option = StdInputReader.loopedInput("Chcete nacitat board zo suboru?", "y", "n");
        if (option.equals("y")) {
            while (loadedBoard == null) {
                try {
                    loadedBoard = this.loadBoard();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    System.out.println("Subor nenajdeny. Vytvorte ho.");
                    option = StdInputReader.loopedInput("Skusit znovu?.", "y", "n");
                    if (option.equals("n")) {
                        break;
                    }
                }
            }
        } else {
            System.out.println("Toto je konzolova appka nacitat sa da len zo suboru :).");
        }
        return loadedBoard;
    }

    public String loadBoard() throws FileNotFoundException {
        String location = "board\\";
        String name = StdInputReader.getInput("Zadajte cestu.");
        location += name;
        System.out.println("nacitavam zo suboru... " + location);
        return this.io.readBoard(location);
    }

    public String performAction() {
        Coordinates coordinates = new Coordinates();
        while (true) {
            String inputCoords = StdInputReader.getInput("Zadajte Suradnice Utoku. [A-H][1-8]");
            coordinates.setFormattedCoordinates(inputCoords);
            if (coordinates.validate()) {
                break;
            } else {
                System.out.println("Nespravne koordinaty.");
            }
        }
        this.lastAttack = coordinates.getFormattedCoordinates();
        return coordinates.getFormattedCoordinates();
    }

    public void gameStateUpdate(int type, String gameData) {
        Coordinates coords;
        switch (type) {
            case GameConstants.TARGET_HIT:
                coords = new Coordinates(lastAttack);
                this.game.getP2Board().getGameBoard()[coords.getRow()][coords.getColumn()] = 'X';
                break;
            case GameConstants.SHIP_HIT:
                coords = new Coordinates(gameData);
                this.game.getP1Board().getGameBoard()[coords.getRow()][coords.getColumn()] = 'X';
                break;
            case GameConstants.TARGET_MISS:
                coords = new Coordinates(gameData);
                this.game.getP2Board().getGameBoard()[coords.getRow()][coords.getColumn()] = 'O';
                break;
            case GameConstants.SHIP_MISS:
                break;
        }
        this.game.printBorads(false);
    }

    public void closeGame(){
        this.game = null;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }
}
