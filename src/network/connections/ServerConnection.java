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
        switch (type) {
            case NetworkConstants.INFO:
                System.out.println(recvdData);
            case NetworkConstants.GAMELIST:
                String gameId = this.parent.chooseGame(recvdData);
                if (gameId != null) {
                    this.parent.sendSimpleMsg(this, NetworkConstants.GAME_ID, gameId);
                } else {
                    this.parent.initGame();
                }
                break;
            case NetworkConstants.REQ_BOARD:
                String board = this.parent.createGame(Integer.parseInt(recvdData));
                String id = String.valueOf(this.parent.getGame().getId());
                this.parent.sendGameData(this, GameConstants.BOARD, id, board);


                // TODO v tychto case-och getnut tu suradnicu
                //pre hraca na ktoreho sa striela aby to mohol aktualizovat
        }
    }
}
