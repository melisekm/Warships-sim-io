package network.connections;

import constants.NetworkConstants;
import network.messages.GameListMessage;
import network.nodes.Client;
import network.nodes.Node;
import network.messages.Message;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServerConnection extends Connection {
    Client parent;

    public ServerConnection(Client parent, Socket socket) throws IOException {
        super(socket);
        this.parent = parent;
    }

    public void recvMessage() throws IOException, ClassNotFoundException {
        List<Message> recvdList = super.unpackMsg();
        int type = recvdList.get(0).getType();
        String msg = recvdList.get(0).getMsg();
        switch (type) {
            case NetworkConstants.INFO:
                System.out.println(msg);
            case NetworkConstants.GAMELIST:
                this.parent.chooseGame(msg);
                break;
            case NetworkConstants.REQ_BOARD:
                this.parent.sendBoard();

            // TODO v tychto case-och getnut tu suradnicu
            //pre hraca na ktoreho sa striela aby to mohol aktualizovat
        }
    }
}
