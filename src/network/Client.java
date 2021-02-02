package network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import app.InputReader;

public class Client extends Node {
	Socket socket; // socket ktorym komunikujeme so serverom
	BufferedReader in;
	PrintWriter out;
	
	public Client() {
		System.out.println("Som Client");
	}
	public void initConnection(String ip, int port) throws UnknownHostException, IOException  {
		this.socket = new Socket(ip, port); // inicializacia soketu IP servera a port na akom pocuva
		this.in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
		this.out = new PrintWriter(socket.getOutputStream());
		
		String message = InputReader.getInput("zadajte board:");
		this.sendMessage(this.socket, this.out, message);
		this.socket.close();
	}
}
