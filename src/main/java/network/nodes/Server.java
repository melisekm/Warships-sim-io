package network.nodes;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import constants.GameConstants;
import constants.NetworkConstants;
import game.Game;
import network.connections.PlayerConnection;

public class Server extends Node {
    private final ServerSocket serverSocket;
    private final ArrayList<PlayerConnection> playerConnections;
    private final HashMap<Integer, Game> games;
    private final HashMap<Game, ArrayList<PlayerConnection>> connections;

    public ExecutorService executor = Executors.newCachedThreadPool();

    public Server(int port) throws IOException {
        this.serverSocket = new ServerSocket(port);// inicializacia server soketu na ktorom pocuva
        this.games = this.loadGames();
        this.playerConnections = new ArrayList<>();
        this.connections = new HashMap<>();
        System.out.println("Som server");
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            this.closeAllConections();
            System.out.println("Vypinam server");
        }));
    }
    public void closeAllConections(){
        for(PlayerConnection con : playerConnections){
            try {
                con.closeConnection();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Nepodarilo sa uzatvorit spojenie s " + con.getSocket().toString());
            }
        }
    }

    public HashMap<Integer, Game> loadGames() {
        return new HashMap<>(); // TODO doplnit
    }

    public void handleConnections() throws IOException { // v cykle cakam na klientov
        while (true) {
            try {
                Socket client = this.serverSocket.accept(); // klient sa pripojil
                this.initClient(client); // vytvor spojenie a cakaj na board
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void initClient(Socket sock) throws IOException {
        System.out.println("client connected");
        PlayerConnection newPlayer = new PlayerConnection(this, sock);
        this.playerConnections.add(newPlayer);
        this.executor.execute(newPlayer); // vytvori thread a spusti run metodu
    }

    public String getFormattedGameList() {
        StringBuilder msg = new StringBuilder();
        for (Game game : this.games.values()) {
            msg.append(game.getId()).append("\n");
        }
        return msg.toString();
    }

    public int assignPlayerToGame(PlayerConnection p, int gameId) {
        Game game = this.games.get(gameId);
        if(game == null){
            System.out.println("hra s id " + gameId + " neexistuje.");
            return 1;
        }
        if (game.getPlayersConnected() >= 2){
            System.out.println("hra " + gameId + " je plna.");
            return 2;
        }
        game.setPlayersConnected(game.getPlayersConnected() + 1);
        System.out.println("Hrac bol priradeny do hry; " + gameId);
        if (game.getPlayersConnected() == 1) {
            ArrayList<PlayerConnection> gameConnections = new ArrayList<>();
            gameConnections.add(p);
            this.connections.put(game, gameConnections);
        } else {
            this.connections.get(game).add(p);
        }
        return 0;
    }

    public String createGame(PlayerConnection p) {
        int id = games.size() + 1;
        games.put(id, new Game(id));
        this.assignPlayerToGame(p, id);
        return String.valueOf(id);
    }

    public void gameStateUpdate(PlayerConnection p, int gameId, int type, String gameData) throws IOException {
        Game game = this.games.get(gameId);
        ArrayList<PlayerConnection> players = this.connections.get(game);
        if (game == null) {
            System.out.println("Hra s id " + gameId + " neexistuje.");
            return;
        }

        switch (type) {
            case GameConstants.BOARD:
                System.out.println(gameData);
                game.setBoard(gameData);
                this.informPlayers(players);
                break;
            case GameConstants.ATTACK:
                PlayerConnection origPC;
                PlayerConnection destPC;
                String origin;
                String destination;
                if (players.get(0) == p) {
                    origPC = players.get(0);
                    destPC = players.get(1);
                    origin = "P1";
                    destination = "P2";
                } else {
                    origPC = players.get(1);
                    destPC = players.get(0);
                    origin = "P2";
                    destination = "P1";
                }
                int signal = game.performAttack(origin, destination, gameData);
                if (signal == GameConstants.HIT) {
                    this.sendGameData(origPC, GameConstants.TARGET_HIT, "Zasiahli ste nepriatelsku lod. " + gameData, gameData);
                    this.sendGameData(destPC, GameConstants.SHIP_HIT, "Nepriatel zasiahol Vasu lod. " + gameData, gameData);
                } else if (signal == GameConstants.MISS) {
                    this.sendGameData(origPC, GameConstants.TARGET_MISS, "Nezasiahli ste ziadnu lod.. " + gameData, gameData);
                    this.sendGameData(destPC, GameConstants.SHIP_MISS, "Nepriatel netrafil Vasu lod.. " + gameData, gameData);
                } else {
                    // TODO koniec hry
                }
                this.sendSimpleMsg(destPC, NetworkConstants.REQ_ACTION, "Ste na rade.");
                break;
        }
    }

    public void informPlayers(ArrayList<PlayerConnection> players) throws IOException {
        if (players.size() == 1) {
            this.sendSimpleMsg(players.get(0), NetworkConstants.INFO, "Server obdrzal board. Ste hrac 1, prosim cakajte.");
        } else {
            this.sendSimpleMsg(players.get(1), NetworkConstants.INFO, "Hra spustena.Server obdrzal board.");
            this.sendSimpleMsg(players.get(0), NetworkConstants.REQ_ACTION, "Pripojil sa druhy hrac. Hra spustena.");
        }
    }

}
