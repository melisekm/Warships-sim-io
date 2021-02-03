package network.connections;

import constants.NetworkConstants;
import network.nodes.Node;
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

    public void recvMessage() throws IOException, ClassNotFoundException {
        List<Message> recvdList = super.unpackMsg();
        int type = recvdList.get(0).getType();
        String msg = recvdList.get(0).getMsg();
        switch (type) {
            case NetworkConstants.INFO:
//                System.out.println(msg);
                break;
            case NetworkConstants.REQ_GAMELIST:
                this.parent.sendGameList(this);
                break;
            case NetworkConstants.GAME_ID:
                this.parent.assignPlayerToGame(this, Integer.parseInt(msg));
                break;
            // TODO v tychto case-och getnut tu suradnicu
            //pre hraca na ktoreho sa striela aby to mohol aktualizovat
        }
    }
}
