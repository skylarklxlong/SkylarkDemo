package com.file.storage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

//THE METHODS IN THIS CLASS ARE USED IN THE ClientSideApplicationMain CLASS
//Client class handling client methods
public class Client {
    //Each client must have a socket, input and output stream.
    public Socket clientSocket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private String serverResponseMsg;    //Also variables for holding information and sending/receiving information from server
    private ArrayList<String> files;
    private String currentDirectory;
    private String parentDirectory;
    private String message;
    private Long length;

    //Constructor creates a new socket connection to server and establishes input and output streams
    public Client(String ipAddress) {
        try {
            clientSocket = new Socket(ipAddress, 2004);    //Create a socket connection for the client
            out = new ObjectOutputStream(clientSocket.getOutputStream());    //open an output stream for client
            out.flush();                                                    //make sure output stream is clear
            in = new ObjectInputStream(clientSocket.getInputStream());        //open an input stream for the client
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Sends authentication details to the server
    public int sendAuthenticationDetails(String userName, String password) {
        String userDetails = userName + " " + password;    //Concatenate user name/password to one string
        int answer = -1;
        try {
            out.writeObject(userDetails);                    //Write to the server
            out.flush();                                    //Clear connection, make sure everything goes through
            serverResponseMsg = (String) in.readObject();    //Wait/Get response from server 0 or 1
            answer = Integer.parseInt(serverResponseMsg);    //Convert to an int
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return answer; //Return the answer. 0 = unsuccessful and 1 = successful
    }

    //Retrieves files and directories sent from the server.
    @SuppressWarnings("unchecked")
    public ArrayList<String> getFilesFromServer() {
        try {
            files = (ArrayList<String>) in.readObject();    //Wait/Get response from server pass into Array list of strings
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return files; //Return the String array list
    }

    //Gets the users current directory from the server
    public String getCurrentDirectory() {
        try {
            currentDirectory = (String) in.readObject();//Wait, then receive the current directory from the server, cast to string
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return currentDirectory;    //return the name of the current directory
    }

    //Gets the parent directory of the current directory on the server
    public String getCurrentDirectoriesParent() {
        try {
            parentDirectory = (String) in.readObject();//Wait, then receive the parent directory from the server, cast to string
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return parentDirectory;    //return the name of the parent directory
    }

    public String getServerMessage() {
        try {
            message = (String) in.readObject();//Wait, then receive the parent directory from the server, cast to string
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return message;    //return the name of the parent directory
    }

    //Retrieves the length of a file thats coming from the server
    public long getFileLengthFromServer() {
        try {
            length = (Long) in.readObject();//Wait, then receive the parent directory from the server, cast to string
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return length;    //return the name of the parent directory
    }

    //Closes the clients input, output streams and socket connection
    public void closeConnections() {
        try {
            in.close();
            out.close();
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Used to send general messages to the server from the client.
    public void sendMessage(Object message) {
        try {
            out.writeObject(message); //Send the command we want to do to the server
            out.flush();              //Clear output after we have sent everything, make sure output is clear.
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Tells the server what command we want to do
    public void sendServerCommand(int command) {
        try {
            out.writeObject(command); //Send the command we want to do to the server
            out.flush();              //Clear output after we have sent everything, make sure output is clear.
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}//End client class
