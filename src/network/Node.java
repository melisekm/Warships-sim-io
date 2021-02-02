package network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Node {
		
	public void sendMessage(Socket socket, PrintWriter out, String message) throws IOException {
		out.println(message);
		out.flush();
	}
}
