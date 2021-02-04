package network.connections;

import constants.NetworkConstants;
import network.nodes.Server;
import network.messages.Message;

import java.io.IOException;
import java.net.Socket;
import java.util.List;

public class PlayerConnection extends Connection {
    Server parent;

    public PlayerConnection(Server parent, Socket socket) throws IOException {
        super(socket);
        this.parent = parent;
    }

    public void handleConnection() throws IOException, ClassNotFoundException {
        List<Message> recvdList = super.unpackMsg();
        int type = recvdList.get(0).getType();
        String recvdData = recvdList.get(0).getMsg();
        switch (type) {
            case NetworkConstants.INFO:
                System.out.println(recvdData);
                break;
            case NetworkConstants.REQ_GAMELIST:
                String gameList = this.parent.getFormattedGameList();
                this.parent.sendSimpleMsg(this, NetworkConstants.GAMELIST, gameList);
                break;
            case NetworkConstants.GAME_ID:
                this.parent.assignPlayerToGame(this, Integer.parseInt(recvdData));
                this.parent.sendSimpleMsg(this, NetworkConstants.REQ_BOARD, recvdData);
                break;
            case NetworkConstants.CREATE_GAME:
                String id = this.parent.createGame(this);
                this.parent.sendSimpleMsg(this, NetworkConstants.REQ_BOARD, id);
                break;
            case NetworkConstants.GAMEDATA:
                int recvdId = Integer.parseInt(recvdData);
                int recvdGameType = recvdList.get(1).getType();
                String recvdGameData = recvdList.get(1).getMsg();
                this.parent.gameStateUpdate(this, recvdId, recvdGameType, recvdGameData);
        }
    }
}
