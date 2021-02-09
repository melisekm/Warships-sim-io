package network.connections;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.net.*;
import java.util.List;

import network.messages.Message;
import network.nodes.Server;

public abstract class Connection implements Runnable {
    private Socket socket;
    public ObjectInputStream in;
    public ObjectOutputStream out;

    protected Connection(Socket socket) throws IOException {
        try {
            socket.setSoTimeout(0);
            socket.setKeepAlive(true);
        } catch (SocketException e) {
            // e.printStackTrace();
        }
        this.socket = socket;

        this.out = new ObjectOutputStream(this.socket.getOutputStream());
        this.in = new ObjectInputStream(this.socket.getInputStream());

    }

    @Override
    public void run() {
        while (!this.socket.isClosed()) {
            try {
                this.handleConnection();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Socket bol uzatvoreny./Pripadne ina chyba:D");
                break;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        try { // loop bol breaknuty alebo niekto ukoncil spojenie
            System.out.println("Zatvaram spojenie.");
            this.closeSocket();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public abstract void handleConnection() throws IOException, ClassNotFoundException;

    public List<Message> unpackMsg(Server server) throws ClassNotFoundException, IOException {
        try {
            return (List<Message>) this.in.readObject();
        } catch (IOException e) { //odpojil sa
            server.removePlayer((PlayerConnection) this);
            throw(e);
        }
    }
    public List<Message> unpackMsg() throws ClassNotFoundException, IOException {
        return (List<Message>) this.in.readObject();
    }

    public void closeSocket() throws IOException {
        if (!this.socket.isClosed()) // ak este nebol zavrety tak ho zavrie
            this.socket.close();
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

}