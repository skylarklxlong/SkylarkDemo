/*
 * Author: Adrian Machado
 * Program Name: ServerClient.java
 * Description: Creates a client connection to the server
 */
package com.server.application;

import java.io.*;
import java.net.*;


public class Client {
    public static void main(String[] args) throws IOException {

        /*if (args.length != 2) {//getting the server AND the port number
            System.err.println(
                    "Usage: java EchoClient <host name> <port number>");
            System.exit(1);
        }

        String hostName = args[0];
        int portNumber = Integer.parseInt(args[1]);*/
        String hostName = "127.0.0.1";
        int portNumber = 7878;

        try (
                Socket socket = new Socket(hostName, portNumber);
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
        ) {
            FileInputStream fis = null;
            BufferedInputStream bis = null;
            OutputStream os = null;


            BufferedReader stdIn =
                    new BufferedReader(new InputStreamReader(System.in));
            String fromServer;
            String fromUser;

            while ((fromServer = in.readLine()) != null) {
                System.out.println("Server: " + fromServer);
                if (fromServer.equals("Bye.")) {//end connection
                    break;
                }

                fromUser = stdIn.readLine();
                if (fromUser != null) {
                    System.out.println("Client: " + fromUser);
                    out.println(fromUser);
                    if (fromServer.equalsIgnoreCase("Processing, Enter G to continue")) {
                        // send file
                        File myFile = new File("sample.txt");
                        byte[] mybytearray = new byte[(int) myFile.length()];
                        fis = new FileInputStream(myFile);
                        bis = new BufferedInputStream(fis);
                        long init = System.nanoTime();
                        bis.read(mybytearray, 0, mybytearray.length);
                        os = socket.getOutputStream();
                        System.out.println("Sending " + "sample.txt" + "(" + mybytearray.length + " bytes)");
                        os.write(mybytearray, 0, mybytearray.length);//upload
                        os.flush();
                        long fin = System.nanoTime();
                        System.out.println("Done. Upload Speed: " + 8 * mybytearray.length / (Math.pow(10, -9) * (fin - init)) + "Bits per Second ");


                    }
                }

            }

        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + hostName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " +
                    hostName);
            System.exit(1);
        }
    }
}