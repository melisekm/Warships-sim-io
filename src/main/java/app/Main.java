package app;
import java.io.IOException;
import java.net.UnknownHostException;

import network.nodes.Client;
import network.nodes.Server;

public class Main {

    public static void main(String[] args) {
        int signal = IOHandler.checkArgs(args);
        if (signal > 0){
            System.exit(signal);
        }
        String login = args[1];
        int port;
        try {
            if (login.equals("client")) {
                String ip = StdInputReader.getInput("Zadajte ip: ");
                port = Integer.parseInt(StdInputReader.getInput("Zadajte port: "));
                Client client = new Client();
                client.initConnection(ip, port);
                client.executor.shutdown();
            } else if (login.equals("server")) {
                port = Integer.parseInt(args[2]);
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
