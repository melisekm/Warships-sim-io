package app;

import network.nodes.Client;
import network.nodes.Server;

import java.io.IOException;
import java.net.UnknownHostException;

public class App {
    String mode;
    String login;
    int port;

    public void run() {
        try {
            if (login.equals("client")) {
                Client client = new Client();
                String ip = this.getServerDetails();
                client.initConnection(ip, this.port);
                client.executor.shutdown();
            } else if (login.equals("server")) {
                Server server = new Server(this.port);
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

    public String getServerDetails() {
        String ip = "localhost";
        if (this.mode.equals("online")) {
            ip = StdInputReader.getInput("Zadajte ip: ");
            this.port = Integer.parseInt(StdInputReader.getInput("Zadajte port: "));
        }
        return ip;
    }

    public int parseArgs(String[] args) {
        if (args.length == 0)
            return 1;
        this.mode = args[0];
        if (this.mode.equals("online")) {
            if (!(args[1].equals("server") || args[1].equals("client"))) {
                return 2;
            }
            this.login = args[1];
            if (this.login.equals("server")) {
                if (args.length != 3) {
                    return 3;
                }
                int port = Integer.parseInt(args[2]);
                if (port <= 0 || port > 65535) {
                    return 4;
                }
                this.port = port;
            }
        } else {
            this.login = StdInputReader.loopedInput("Server[s]/Client[c]/Quit[q]", "s", "c", "q");
            if (this.login.equals("s"))
                this.login = "server";
            else if (this.login.equals("c"))
                this.login = "client";
            this.port = 9999;
        }
        return 0;
    }

    public void printHelp() {
        System.out.println("Usage: 'online'/'local' 'client'/'server' <port>");
    }


}
