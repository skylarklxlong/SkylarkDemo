package com.file.storage;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

//Server side main application
public class ServerSideApplicationMain {
    @SuppressWarnings("resource")
    public static void main(String[] args) throws IOException {
        ServerSocket appServerSocket = new ServerSocket(2004, 10);    //Creating a server socket

        //Continuous loop
        while (true) {
            Socket clientSocket = appServerSocket.accept(); //Wait for a client trying to connect
            ClientRequestThread clientThread = new ClientRequestThread(clientSocket); //Spawn client thread
            clientThread.start(); //Run the thread, individual thread for each client connection.
            //Then loop over and listen again for the next client.
        }
    }//End Main
}//End Class
