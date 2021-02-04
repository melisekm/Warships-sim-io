package network.nodes;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import constants.GameConstants;
import game.Game;
import network.connections.PlayerConnection;

public class Server extends Node {
    private ServerSocket serverSocket;
    private ArrayList<PlayerConnection> playerConnections;
    private HashMap<Integer, Game> games;

    public ExecutorService executor = Executors.newCachedThreadPool();
    private Boolean shutdown = false;

    public Server(int port) throws IOException {
        this.serverSocket = new ServerSocket(port);// inicializacia server soketu na ktorom pocuva
        this.games = this.loadGames();
        this.playerConnections = new ArrayList<PlayerConnection>();
        System.out.println("Som server");
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            this.shutdown = true; // assuming we have a gameScores object in this scope
            System.out.println("Vypinam server");
        }));
    }

    public HashMap<Integer, Game> loadGames() {
        return new HashMap<Integer, Game>(); // TODO doplnit
    }

    public void handleConnections() throws IOException { // v cykle cakam na klientov
        while (!this.shutdown) {
            try {
                Socket client = this.serverSocket.accept(); // klient sa pripojil
                this.initClient(client); // vytvor spojenie a cakaj na board
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("test");
        //this.closeAllConections();
    }

    public void initClient(Socket sock) throws IOException {
        System.out.println("client connected");
        PlayerConnection newPlayer = new PlayerConnection(this, sock);
        this.playerConnections.add(newPlayer);
        this.executor.execute(newPlayer); // vytvori thread a spusti run metodu
    }

    public String getFormattedGameList() {
        String msg = "";
        for (Game game : this.games.values()) {
            msg += game.getId() + "\n";
        }
        return msg;
    }

    public void assignPlayerToGame(int gameId) {
        Game game = this.games.get(gameId);
        if (game != null && game.getPlayersConnected() < 2) {
            game.setPlayersConnected(game.getPlayersConnected() + 1);
            System.out.println("Hrac bol priradeny do hry; " + gameId);
        } else {
            System.out.println("Hra s id " + gameId + " neexistuje");
        }
    }

    public String createGame() {
        int id = games.size() + 1;
        games.put(id, new Game(id));
        return String.valueOf(id);
    }

    public void gameStateUpdate(int gameId, int type, String gameData) {
        Game game = this.games.get(gameId);
        if (game == null) {
            System.out.println("Hra s id " + gameId + " neexistuje.");
            return;
        }
        switch (type) {
            case GameConstants.BOARD:
                game.setBoard(gameData);
        }
    }

//    public void performAction(Connection p, String coordinates) throws IOException {
//        String origin;
//        String destination;
//        Connection origPC;
//        Connection destPC;
//        if (p == p1Con) {
//            origPC = p1Con;
//            destPC = p2Con;
//            origin = "P1";
//            destination = "P2";
//        } else {
//            origPC = p2Con;
//            destPC = p1Con;
//            origin = "P2";
//            destination = "P1";
//        }
//        int signal = this.game.performAttack(origin, destination, coordinates);
//        if (signal == GameConstants.HIT) { // TODO poslat suradnice pre hraca na ktoreho sa striela aby to mohol aktualizovat.
//            this.sendSimpleMsg(origPC, NetworkConstants.TARGET_HIT, "Zasiahli ste nepriatelsku lod. " + coordinates);
//            this.sendSimpleMsg(destPC, NetworkConstants.SHIP_HIT, "Nepriatel zasiahol Vasu lod. " + coordinates);
//        } else if (signal == GameConstants.MISS) {
//            this.sendSimpleMsg(origPC, NetworkConstants.TARGET_MISS, "Nezasiahli ste ziadnu lod.. " + coordinates);
//            this.sendSimpleMsg(destPC, NetworkConstants.SHIP_MISS, "Nepriatel netrafil Vasu lod.. " + coordinates);
//        } else {
//            // TODO koniec hry
//        }
//        this.sendSimpleMsg(destPC, NetworkConstants.REQ_ACTION, "Ste na rade.");
//    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    public void setServerSocket(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }
}
