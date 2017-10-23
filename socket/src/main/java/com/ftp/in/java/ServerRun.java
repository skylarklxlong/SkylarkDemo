package com.ftp.in.java;

import java.net.Socket;
import java.net.ServerSocket;


public class ServerRun
{
	public static void main(String[] args) throws java.io.IOException
	{
		int defaultPort = 8090;

		if(args.length>0)
			defaultPort = Integer.valueOf(args[0]);

		ServerSocket serverSocket = new ServerSocket(defaultPort);

		while(true)
		{
			Socket socket = serverSocket.accept();
			Server server = new Server(socket);
			System.out.println("New Client connected");
		}

	}
}