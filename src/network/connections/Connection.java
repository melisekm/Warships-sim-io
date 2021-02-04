package network.connections;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.net.Socket;
import java.net.SocketException;
import java.util.List;

import game.Board;
import network.messages.Message;

public abstract class Connection implements Runnable {
    Socket socket;
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
                break;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        try { // loop bol breaknuty alebo niekto ukoncil spojenie ukoncil spojenie
            System.out.println("Zatvaram spojenie.");
            this.closeConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public abstract void handleConnection() throws IOException, ClassNotFoundException;

    public List<Message> unpackMsg() throws IOException, ClassNotFoundException {
        return (List<Message>) this.in.readObject();
    }

    public void closeConnection() throws IOException {
        if (!this.socket.isClosed()) // ak este nebol zavrety tak ho zavrie
            this.socket.close();
    }

    public Board recvBoard() throws IOException, ClassNotFoundException {
        List<Board> boardList = (List<Board>) this.in.readObject();
        System.out.println("Received board from socket.");
        return boardList.get(0);
    }

}