package app;

import java.io.IOException;
import java.net.UnknownHostException;

import network.nodes.Client;
import network.nodes.Server;

public class Main {

    public static void main(String[] args) {
        String login = StdInputReader.loopedInput("Server[s]/Client[c]/Quit[q]", "s", "c", "q");
        System.out.println("cpavok");
        String ip = "localhost";
        int port = 9999;
        try {
            if (login.equals("c")) {
                Client client = new Client();
                client.initConnection(ip, port);
            } else if (login.equals("s")) {
                Server server = new Server(port);
                server.handleConnections();
                server.executor.shutdown();
            }
        } catch (UnknownHostException e) {
            System.out.println("Nespravne zadana IP.");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
