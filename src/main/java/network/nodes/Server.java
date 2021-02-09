package network.nodes;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import constants.GameConstants;
import constants.NetworkConstants;
import game.Board;
import game.Game;
import network.connections.PlayerConnection;

public class Server extends Node {
    private final ServerSocket serverSocket;
    private final ArrayList<PlayerConnection> playerConnections;
    private final HashMap<Integer, Game> games;
    private final HashMap<Game, ArrayList<PlayerConnection>> connections;

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

    private void closeAllConections() {
        for (PlayerConnection con : playerConnections) {
            try {
                System.out.println("Zatvaram spojenie s hracom " + con.getIpAddress());
                con.closeSocket();
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

    private void initClient(Socket sock) throws IOException {
        System.out.println("client connected");
        PlayerConnection newPlayer = new PlayerConnection(this, sock);
        this.playerConnections.add(newPlayer);
        this.executor.execute(newPlayer); // vytvori thread a spusti run metodu
    }

    public void removePlayer(PlayerConnection msgOrigin) throws IOException {
        String ip = msgOrigin.getIpAddress();
        int playerId = 0;
        Game game = null;
        ArrayList<PlayerConnection> players = null;
        // zisti o akeho hraca ide.
        for (Map.Entry<Game, ArrayList<PlayerConnection>> entry : connections.entrySet()) {
            game = entry.getKey();
            players = entry.getValue();
            if (players.get(0) == msgOrigin) {
                playerId = 1;
                break;
            } else if (players.get(1) == msgOrigin) {
                playerId = 2;
                break;
            }
        }

        if (playerId > 0) {
            // posli spravu druhemu hracovi
            System.out.println("Spojenie s hracom " + playerId + " v hre " + game.getId() + " prerusene.");
            game.setPlayersConnected(game.getPlayersConnected() - 1);
            players.remove(msgOrigin);
            if (game.getPlayersConnected() > 0)
                this.sendSimpleMsg(players.get(0), NetworkConstants.ERROR, "Vas protihrac sa odpojil.");
            //vymaz hru zo zoznamov
            this.removeGame(game);
        } else {
            System.out.println("Spojenie s hracom z IP: " + ip + " prerusene." +
                    " Hrac nebol pripojeny k ziadnej hre.");
        }
        // odstran pripojenie hraca.
        this.playerConnections.remove(msgOrigin);
    }

    public void removeGame(Game game) {
        this.connections.remove(game);
        this.games.remove(game.getId());
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
        if (game == null) {
            System.out.println("hra s id " + gameId + " neexistuje.");
            return 1;
        }
        if (game.getPlayersConnected() >= 2) {
            System.out.println("hra " + gameId + " je plna.");
            return 2;
        }
        game.setPlayersConnected(game.getPlayersConnected() + 1);
        if (game.getPlayersConnected() == 1) {
            ArrayList<PlayerConnection> gameConnections = new ArrayList<>();
            gameConnections.add(p);
            this.connections.put(game, gameConnections);
        } else {
            this.connections.get(game).add(p);
        }
        System.out.println("Hrac bol priradeny do hry; " + gameId);
        return 0;
    }

    public String createGame(PlayerConnection p) {
        int id = games.size() + 1;
        games.put(id, new Game(id));
        this.assignPlayerToGame(p, id);
        return String.valueOf(id);
    }

    public void gameStateUpdate(PlayerConnection msgOrigin,
                                int gameId,
                                int type,
                                String gameData
    ) throws IOException {
        Game game = this.games.get(gameId);
        ArrayList<PlayerConnection> connectedPlayers = this.connections.get(game); // pripojeny hraci
        if (connectedPlayers == null || !(connectedPlayers.contains(msgOrigin))) {
            System.out.printf("Chyba. Hrac %s nie je sucastou tejto hry.\n", msgOrigin.getIpAddress());
            return;
        }
        if (game == null) {
            System.out.println("Hra s id " + gameId + " neexistuje.");
            return;
        }

        switch (type) {
            case GameConstants.BOARD:
                this.handleGameBoardAssignment(game, gameData, connectedPlayers);
                break;
            case GameConstants.ATTACK:
                this.handleGameAttack(game, gameData, connectedPlayers, msgOrigin);
                break;
        }
    }

    private void handleGameBoardAssignment(Game game,
                                           String formattedBoard,
                                           ArrayList<PlayerConnection> players
    ) throws IOException {
        System.out.println(formattedBoard);
        game.setBoard(formattedBoard);
        if (players.size() == 1) {
            this.sendSimpleMsg(players.get(0), NetworkConstants.INFO, "Server obdrzal board. Ste hrac 1, prosim cakajte.");
        } else {
            this.sendSimpleMsg(players.get(1), NetworkConstants.INFO, "Hra spustena.Server obdrzal board. Cakajte.");
            this.sendSimpleMsg(players.get(0), NetworkConstants.REQ_ACTION, "Pripojil sa druhy hrac. Hra spustena.");
        }
    }

    private void handleGameAttack(Game game,
                                  String coordinates,
                                  ArrayList<PlayerConnection> connectedPlayers,
                                  PlayerConnection msgOrigin
    ) throws IOException {
        PlayerConnection origPC;
        PlayerConnection destPC;
        String origin;
        String destination;
        Board boardToCheck;
        if (connectedPlayers.get(0) == msgOrigin) {
            origPC = connectedPlayers.get(0);
            destPC = connectedPlayers.get(1);
            origin = "P1";
            destination = "P2";
            boardToCheck = game.getP2Board();
        } else {
            origPC = connectedPlayers.get(1);
            destPC = connectedPlayers.get(0);
            origin = "P2";
            destination = "P1";
            boardToCheck = game.getP1Board();
        }
        System.out.printf("Hra %d\n", game.getId());
        System.out.printf("%s vystrelil na %s koordinaty %s.\n", origin, destination, coordinates);
        int signal = game.performAttack(coordinates, boardToCheck);
        if (signal == GameConstants.HIT) {
            this.sendGameData(origPC, GameConstants.TARGET_HIT, "Zasiahli ste nepriatelsku lod. " + coordinates, coordinates);
            this.sendGameData(destPC, GameConstants.SHIP_HIT, "Nepriatel zasiahol Vasu lod. " + coordinates, coordinates);
        } else if (signal == GameConstants.MISS) {
            this.sendGameData(origPC, GameConstants.TARGET_MISS, "Nezasiahli ste ziadnu lod.. " + coordinates, coordinates);
            this.sendGameData(destPC, GameConstants.SHIP_MISS, "Nepriatel netrafil Vasu lod.. " + coordinates, coordinates);
        }
        boolean checkEnd = game.checkEnd();
        if (checkEnd) {
            this.sendSimpleMsg(origPC, NetworkConstants.WIN, "Znicili ste vsetky nepriatelske lode.");
            this.sendSimpleMsg(destPC, NetworkConstants.LOSE, "Vsetky Vase lode boli znicene.");
            this.removeGame(game);
        } else {
            this.sendSimpleMsg(destPC, NetworkConstants.REQ_ACTION, "Ste na rade.");

        }
    }
}
