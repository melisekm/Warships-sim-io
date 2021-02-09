package network.connections;

import constants.GameConstants;
import constants.NetworkConstants;
import network.nodes.Client;
import network.messages.Message;

import java.io.IOException;
import java.net.Socket;
import java.util.List;

public class ServerConnection extends Connection {
    Client parent;

    public ServerConnection(Client parent, Socket socket) throws IOException {
        super(socket);
        this.parent = parent;
    }

    public void handleConnection() throws IOException, ClassNotFoundException {
        List<Message> recvdList = super.unpackMsg();
        int type = recvdList.get(0).getType();
        String recvdData = recvdList.get(0).getMsg();
        String gameId;
        switch (type) {
            case NetworkConstants.INFO:
                System.out.println(recvdData);
                break;
            case NetworkConstants.GAMELIST:
                String id = this.parent.chooseGame(recvdData);
                if (id != null) {
                    this.parent.sendSimpleMsg(this, NetworkConstants.GAME_ID, id);
                } else {
                    this.parent.initGame();
                }
                break;
            case NetworkConstants.REQ_BOARD:
                String board = this.parent.createGame(Integer.parseInt(recvdData));
                gameId = String.valueOf(this.parent.getGame().getId());
                this.parent.sendGameData(this, GameConstants.BOARD, gameId, board);
                break;
            case NetworkConstants.REQ_ACTION:
                System.out.println(recvdData);
                String coordinates = this.parent.performAction();
                gameId = String.valueOf(this.parent.getGame().getId());
                this.parent.sendGameData(this, GameConstants.ATTACK, gameId, coordinates);
                break;
            case NetworkConstants.GAMEDATA:
                System.out.println(recvdData);
                int recvdGameType = recvdList.get(1).getType();
                String recvdGameData = recvdList.get(1).getMsg();
                this.parent.gameStateUpdate(recvdGameType, recvdGameData);
                break;
            case NetworkConstants.ERROR:
            case NetworkConstants.LOSE:
            case NetworkConstants.WIN:
                System.out.println(recvdData);
                this.parent.closeGame();
                this.parent.initGame();
                break;
        }
    }
}
