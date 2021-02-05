package app;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;

import network.nodes.Client;
import network.nodes.Server;

public class Main {

    public static void main(String[] args) {
        System.out.println(args[2]);
        URL whatismyip = null;
        try {
            whatismyip = new URL("http://checkip.amazonaws.com");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(whatismyip.openStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        String publicIP = null; //you get the IP as a String
        try {
            publicIP = in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("test");
        System.out.println(publicIP);
        System.out.println("test");
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
