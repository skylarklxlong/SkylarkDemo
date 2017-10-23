/*
 * Author: Adrian Machado
 * Program Name: ServerMain.java
 * Description: Creates an instance of the server to send/receive requests
 */
package com.server.application;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) throws IOException {

        /*if (args.length != 1) {
            System.err.println("Usage: java Server <port number>");
            System.exit(1);
        }

        int portNumber = Integer.parseInt(args[0]);*/
        int portNumber = 7878;

        try (
                ServerSocket serverSocket = new ServerSocket(portNumber);
                Socket clientSocket = serverSocket.accept();
                PrintWriter out =
                        new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(clientSocket.getInputStream()));
        ) {

            String inputLine, outputLine;

            // Initiate conversation with client
            ServerProtocol p = new ServerProtocol();
            outputLine = p.processInput(null);
            out.println(outputLine);

            int bytesRead;
            int current = 0;
            FileOutputStream fos = null;
            BufferedOutputStream bos = null;

            while ((inputLine = in.readLine()) != null) {
                outputLine = p.processInput(inputLine);
                out.println(outputLine);
                if (outputLine.equals("Bye.")) {//ends listening
                    break;
                } else if (outputLine.equalsIgnoreCase("Processing, Enter G to continue")) {//file exchange begins

                    // receive file

                    byte[] mybytearray = new byte[441540];
                    InputStream is = clientSocket.getInputStream();
                    fos = new FileOutputStream("tester.txt");
                    bos = new BufferedOutputStream(fos);

                    long init = System.nanoTime();//recorded for time difference
                    bytesRead = is.read(mybytearray, 0, mybytearray.length);
                    current = bytesRead;

                    do {//download
                        bytesRead =
                                is.read(mybytearray, current, (mybytearray.length - current));
                        if (bytesRead >= 0) current += bytesRead;
                    } while (current < 441540);

                    bos.write(mybytearray, 0, current);
                    bos.flush();
                    long fin = System.nanoTime();
                    out.println(" Downloading tester.txt Done. Download Speed: " + 8 * current / (Math.pow(10, -9) * (fin - init)) + " Bits per Second");
                }
            }
        } catch (IOException e) {
            System.out.println("Exception caught when trying to listen on port "
                    + portNumber + " or listening for a connection");
            System.out.println(e.getMessage());
        }
    }
}