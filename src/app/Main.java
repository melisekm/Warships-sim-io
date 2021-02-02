package app;

import client.Client;
import server.Server;

public class Main {

	public static void main(String[] args) {
		InputReader in = new InputReader();
		String login = in.login();
		if (login.equals("c")) {
			Client client = new Client();

		} else if (login.equals("s")) {
			Server server = new Server();
		}
	}

}
