package app;

import java.io.IOException;
import java.net.UnknownHostException;

import network.Client;
import network.Server;

public class Main {

    public static void main(String[] args) {
        InputReader in = new InputReader();
        String login = in.login();
        System.out.println("cpavok");
        String ip = "localhost";
        int port = 9999;
        try {
            if (login.equals("c")) {
                Client client = new Client();
                client.initConnection(ip, port);
            } else if (login.equals("s")) {
                Server server = new Server(port);
                server.initGame();
                server.executor.shutdown();
            }
        } catch (UnknownHostException e) {
            System.out.println("Nespravne zadana IP.");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("IO error");
            e.printStackTrace();
        }

    }

}
