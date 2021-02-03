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
    public ServerConnection(Node parent, Socket socket) throws IOException {
        super(parent, socket);
    }

    public void recvMessage() throws IOException, ClassNotFoundException {
        List<Message> recvdList = super.unpackMsg();
        int type = recvdList.get(0).getType();
//        String msg = recvdList.get(0).getMsg();
        switch (type) {
            case NetworkConstants.INFO:
//                System.out.println(msg);
            case NetworkConstants.GAMELIST:
                ArrayList<Integer> gamelist = ((GameListMessage) recvdList).getGameList();
                ((Client) this.parent).chooseGame(gamelist);
                break;

            // TODO v tychto case-och getnut tu suradnicu
            //pre hraca na ktoreho sa striela aby to mohol aktualizovat
        }
    }
}
