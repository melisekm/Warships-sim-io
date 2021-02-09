package network.connections;

import constants.NetworkConstants;
import network.nodes.Server;
import network.messages.Message;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.List;

public class PlayerConnection extends Connection {
    private final Server parent;
    private String IpAddress;

    public PlayerConnection(Server parent, Socket socket) throws IOException {
        super(socket);
        this.parent = parent;
        this.IpAddress = this.getPlayerIP(socket);
    }

    private String getPlayerIP(Socket socket) {
        InetSocketAddress socketAddress = (InetSocketAddress) socket.getRemoteSocketAddress();
        InetAddress inetAddress = ((InetSocketAddress) socketAddress).getAddress();
        return inetAddress.toString();
    }

    public void handleConnection() throws IOException, ClassNotFoundException {
        List<Message> recvdList = super.unpackMsg(this.parent);
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
                int code = this.parent.assignPlayerToGame(this, Integer.parseInt(recvdData));
                if (code == 0)
                    this.parent.sendSimpleMsg(this, NetworkConstants.REQ_BOARD, recvdData);
                else if (code == 1)
                    this.parent.sendSimpleMsg(this, NetworkConstants.ERROR, "Hra neexistuje.");
                else if (code == 2)
                    this.parent.sendSimpleMsg(this, NetworkConstants.ERROR, "Hra je plna.");
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
                break;
            default:
                this.parent.sendSimpleMsg(this, NetworkConstants.ERROR, "Nasta neocakavana chyba, restartujte program.");
        }
    }

    public String getIpAddress() {
        return IpAddress;
    }

    public void setIpAddress(String ipAddress) {
        IpAddress = ipAddress;
    }
}
