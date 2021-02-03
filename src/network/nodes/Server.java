package network.nodes;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import game.Board;
import game.Game;
import constants.GameConstants;
import constants.NetworkConstants;
import network.connections.Connection;
import network.connections.PlayerConnection;

public class Server extends Node {
    private ServerSocket serverSocket;
    private ArrayList<Game> games; // TODO nacitat gamelist zo suboru.
    private ArrayList<PlayerConnection> playerConnections;

    public ExecutorService executor = Executors.newCachedThreadPool();

    public Server(int port) throws IOException {
        this.serverSocket = new ServerSocket(port);// inicializacia server soketu na ktorom pocuva
        System.out.println("Som server");
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

    public void sendGameList(PlayerConnection dest) throws IOException {
        ArrayList<Integer> ids = new ArrayList<Integer>();
        for (Game game : this.games) {
            ids.add(game.getId());
        }
        this.sendGameList(dest, NetworkConstants.GAMELIST, ids);
    }
    public void assignPlayerToGame(int gameId){
        for(Game game : this.games){
            if(game.getId() == gameId && game.getPlayersConnected() < 2){

            }
        }

    }

    public void createBoard(Connection p, Board board) throws IOException {
        System.out.println("Obrdzal board.");
        if (p1Con == null) {
            this.p1Con = p;
            this.game = new Game(id);
            this.game.setP1Board(board);
            System.out.println("Player 1 Board:");
            this.sendSimpleMsg(this.p1Con, NetworkConstants.INFO, "Server obdrzal board. Ste hrac 1, prosim cakajte.");
        } else {
            this.p2Con = p;
            this.game.setP2Board(board);
            System.out.println("Player 2 Board:");
            this.sendSimpleMsg(this.p2Con, NetworkConstants.INFO, "Hra spustena.Server obdrzal board.Prosim cakajte");
            this.sendSimpleMsg(this.p1Con, NetworkConstants.REQ_ACTION, "Pripojil sa druhy hrac. Hra spustena.");
        }
        board.printBoard();
    }

    public void performAction(Connection p, String coordinates) throws IOException {
        String origin;
        String destination;
        Connection origPC;
        Connection destPC;
        if (p == p1Con) {
            origPC = p1Con;
            destPC = p2Con;
            origin = "P1";
            destination = "P2";
        } else {
            origPC = p2Con;
            destPC = p1Con;
            origin = "P2";
            destination = "P1";
        }
        int signal = this.game.performAttack(origin, destination, coordinates);
        if (signal == GameConstants.HIT) { // TODO poslat suradnice pre hraca na ktoreho sa striela aby to mohol aktualizovat.
            this.sendSimpleMsg(origPC, NetworkConstants.TARGET_HIT, "Zasiahli ste nepriatelsku lod. " + coordinates);
            this.sendSimpleMsg(destPC, NetworkConstants.SHIP_HIT, "Nepriatel zasiahol Vasu lod. " + coordinates);
        } else if (signal == GameConstants.MISS) {
            this.sendSimpleMsg(origPC, NetworkConstants.TARGET_MISS, "Nezasiahli ste ziadnu lod.. " + coordinates);
            this.sendSimpleMsg(destPC, NetworkConstants.SHIP_MISS, "Nepriatel netrafil Vasu lod.. " + coordinates);
        } else {
            // TODO koniec hry
        }
        this.sendSimpleMsg(destPC, NetworkConstants.REQ_ACTION, "Ste na rade.");
    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    public void setServerSocket(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

}
