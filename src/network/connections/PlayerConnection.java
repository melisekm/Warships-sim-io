package network.connections;

import constants.NetworkConstants;
import network.messages.SimpleMessage;
import network.nodes.Node;
import network.nodes.Server;
import network.messages.Message;

import java.io.IOException;
import java.net.Socket;
import java.util.List;

public class PlayerConnection extends Connection {
    public PlayerConnection(Node parent, Socket socket) throws IOException {
        super(parent, socket);
    }

    public void recvMessage() throws IOException, ClassNotFoundException {
        List<Message> recvdList = super.unpackMsg();
        int type = recvdList.get(0).getType();
        switch (type) {
            case NetworkConstants.INFO:
//                System.out.println(msg);
                break;
            case NetworkConstants.REQ_GAMELIST:
                ((Server) parent).sendGameList(this);
            case NetworkConstants.GAME_ID:
                int gameId = Integer.parseInt( ((SimpleMessage) recvdList).getMsg() );
                ((Server) parent).assignPlayerToGame(gameId);

                // TODO v tychto case-och getnut tu suradnicu
                //pre hraca na ktoreho sa striela aby to mohol aktualizovat
        }
    }
}
