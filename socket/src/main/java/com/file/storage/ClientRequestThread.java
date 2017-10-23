package com.file.storage;

import java.io.*;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

//Individual thread that each client with get. Each client works in a separate thread to provide scalability
@SuppressWarnings("unused")
public class ClientRequestThread extends Thread {
    //VARIABLES
    private Socket clientSocket;                                    //Socket to connect to the client
    private String message;                                            //Holds messages sent from the client
    private String[] userDetails;                                    //Will hold user name and password in array of size 2
    private String passwords[] = new String[3];                        //Arrays to hold passwords/user names read from text files
    private String usernames[] = new String[3];
    private String username;                                        //The user name and password sent from the client
    private String password;
    private String currentDirectoriesParent;                        //Holds parent directory of current directory
    private File file;                                                //Holds current file/directory that the spawned server thread is working with
    private ArrayList<String> files;                                //Holds list of files in a directory
    private ArrayList<String> directories;                            //Holds list of directories in a directory
    private String serversFilePath;                                    //Holds the path of this source code (server)
    private String userRootDirectoryPath;                            //Path for users root directory
    private String currentDirectory;                                //Current directory, what the user see's
    private String childDirectory;                                    //Holds child directory a user is looking for
    private String visibleUserDirectory;                            //Used to send back the path to current directory user can see
    private String parentDirectory;                                    //Holds parent directory user wants to move to
    private String newDirectoryName;                                //Name of new directory user is creating
    private String newDirectoryPath;                                //Name of new directory path user is creating
    private String result;                                            //Returns success or not to the user for certain tasks, eg new directory
    private String downloadFile;                                    //Holds name of file for download
    private String downloadPath;                                    //Holds path of file for download
    private ObjectOutputStream out;                                    //Input and output streams to communicate with client
    private ObjectInputStream in;
    private boolean authenticated = false;                            //Set to true if authentication is successful
    private volatile boolean running = true;                        //Make sure variable is not cached, loop control variable.
    private int i = 0;                                                //For looping
    private int parentOrChild;                                        //Holds if user wants to move to parent or child directory
    private int choice;                                                //Holds users choice sent from the client
    private int proceedOrNot = 0;                                    //Tells server whether to do file download depending on info sent is fine or not.
    private long uploadSize;
    private String uploadName;
    private String uploadPath;

    //CONSTRUCTOR
    //Every client server thread has a socket connection
    ClientRequestThread(Socket s) {
        clientSocket = s;
    }

    //METHODS USED IN THE RUN METHOD FOR THIS THREAD
    //Method used to send messages to client machines from the server
    private void sendMessage(Object message) {
        try {
            out.writeObject(message);    //sending the message
            out.flush();                //clear the connection
        } catch (IOException e) {
            //Output an error message thats human readable and one thats more specific
            System.out.println();
            System.out.println(e.getMessage());
            System.out.println("There is an IO with sending a message, you may have entered some incorrect information - please try again");
        }
    }

    //Gets and returns the file path of the server
    private String getServersFilePath() {
        String s;
        Path currentRelativePath = Paths.get("");                                                //Create path object
        s = currentRelativePath.toAbsolutePath().toString();                                    //Get the path
        s = s.replace('\\', '/');                                                                //Replace the \ with /
        return s;
    }

    //Creates a directory for the user if one does not exist and a Welcome.txt file
    private String createUserDirectory(String path, String username) {
        String usersDirectoryPath = path + "/" + username;                        //Create a path for the user
        File file = new File(usersDirectoryPath);                                //File/Directory object
        File welcomeFile = new File(usersDirectoryPath + "/Welcome.txt");        //Path for welcome file
        FileWriter writer = null;

        //If the directory does not exist
        if (!file.exists()) {
            file.mkdir();                                                            //Then create it
            welcomeFile.getParentFile().mkdirs();                                    //Ensure that the parent directories exist before writing
            try {
                welcomeFile.createNewFile();                                        //Create welcome file inside the directory
                writer = new FileWriter(welcomeFile);                                //Access the file for writing
                writer.write("Hello " + username + " welcome to the file server");    //write welcome message
                writer.close();
            } catch (IOException e) {
                //Output an error message thats human readable and one thats more specific
                System.out.println();
                System.out.println(e.getMessage());
                System.out.println("There is an IO issue creating a directory or file - please try again");
            }
        }
        return usersDirectoryPath;        //Return the path of the directory that was created.
    }

    //Creates a new directory with specified name
    public String createNewDirectory(String path, String name) {
        File file = new File(path);                            //Create file object set it to specific path
        String result = "";

        //If the directory does not exist
        if (!file.exists()) {
            file.mkdir();    //Then create it
            result = "Directory " + name + " has been created successfully";
        } else //Otherwise send a message stating the directory already exists
        {
            result = "A Directory with the name " + name + " already exists, please try using a different name.";
        }

        return result;            //Return a success or no success message
    }

    //Gets and returns all the files in a directory
    private ArrayList<String> listDirectoriesFiles(File file) {
        ArrayList<String> directoryFiles = new ArrayList<String>();        //Create array list of strings to hold the files

        for (File f : file.listFiles())                                    //for each file in the list of files for the directory
        {
            if (f.isFile()) {
                directoryFiles.add(f.getName());                        //Get its name and add it to the array list
            }
        }

        return directoryFiles;                                            //Return the array list
    }

    //Gets the name of the parent directory of the current directory
    private String getCurrentDirectoriesParent(File file, String username) {
        String parentDirectoryName;
        String parentDirectoryPath;
        if (username.equals(file.getName()))//if the current directory is the same name as the user name we are in root directory
        {
            parentDirectoryName = "You are in your root directory and currently dont have a parent directory";
        } else //otherwise we are not so get the parent directory
        {
            parentDirectoryPath = file.getParent();                        //get the path of the current directories parent
            File parentDirectory = new File(parentDirectoryPath);        //use that path to create a file object
            parentDirectoryName = parentDirectory.getName();            //then get the name of that directory
        }

        return parentDirectoryName;                                        //Return the name of the parent directory
    }

    //Gets and returns all the directories in a directory
    private ArrayList<String> listDirectoriesDirectories(File file) {
        ArrayList<String> directoryDirectories = new ArrayList<String>();            //Array list of strings to hold all the directories

        for (File f : file.listFiles())                                                //For each directory in list of directories
        {
            if (f.isDirectory()) {
                directoryDirectories.add(f.getName());                                //get its name and add it to the array list
            }
        }

        return directoryDirectories;                                                //return the array list
    }

    //THE RUN() METHOD, LIKE THE MAIN() METHOD FOR THIS THREAD, ALL METHODS USED IN THIS METHOD ARE DEFINED ABOVE
    //A thread that the server spawns off each time a client connects to the server.
    @SuppressWarnings("resource")
    public void run() {
        try {
            //Creating input and output streams for the server
            out = new ObjectOutputStream(clientSocket.getOutputStream());
            out.flush();
            in = new ObjectInputStream(clientSocket.getInputStream());

            //AUTHENTICATION
            do {
                //Create the string sent from the client with user name/password information
                message = (String) in.readObject();    //wait for authentication details
                userDetails = message.split(" "); //splitting the string to grab the two parts.
                username = userDetails[0];
                password = userDetails[1];

                //Two scanner objects for reading the files for user name and password.
                Scanner passwordFile = new Scanner(new FileReader("UserInformation/Passwords.txt"));
                Scanner usernameFile = new Scanner(new FileReader("UserInformation/Usernames.txt"));

                //Loop over all user names and passwords to see if any match the ones provided by the user
                while (passwordFile.hasNext() && usernameFile.hasNext()) {
                    passwords[i] = passwordFile.next();//Get next password/user name from each file
                    usernames[i] = usernameFile.next();

                    //if they match the ones provided
                    if (passwords[i].equals(password) && usernames[i].equals(username)) {
                        authenticated = true; //authentication is now true
                        break;                  //No need to keep looping if there has been a successful match
                    }
                    ++i;                      //If there wasnt a match increment counter before next iteration(loop)
                }

                passwordFile.close(); //Close the files
                usernameFile.close();

                if (authenticated == true) //If authenticated send 1 back to the client, otherwise sent 0.
                {
                    sendMessage("1");
                    running = false;
                } else if (authenticated == false) {
                    sendMessage("0");
                }
            }
            while (running);    //Break the loop only if you have been authenticated.
        } catch (IOException e) {
            //Output an error message thats human readable and one thats more specific
            System.out.println();
            System.out.println(e.getMessage());
            System.out.println("There is an IO issue with authentication - please try again");
        } catch (ClassNotFoundException classnot) {
            System.err.println("Data received in unknown format");
        }

        //AFTER AUTHENTICATION / MOVING INTO USERS ROOT AND SENDING BACK ITS CONTENTS TO THE CLIENT
        //Gets the path of the current file(this file)
        serversFilePath = getServersFilePath();
        //Creates a new directory for the user if none already exist and also add a Welcome.txt file into it
        //then returns the path of the users root directory which it created.
        userRootDirectoryPath = createUserDirectory(serversFilePath, username);

        //Create a file object set to the root directory for the user
        file = new File(userRootDirectoryPath);

        //Gets the name of the current directories parent or no name if we are in root directory
        currentDirectoriesParent = getCurrentDirectoriesParent(file, username);

        //Pass it into a method that lists and returns all the files in the directory in an array list.
        files = listDirectoriesFiles(file);
        //Pass it into a method that lists and returns all the directories in the directory in an array list.
        directories = listDirectoriesDirectories(file);
        //Send current directory, then send all the directories and files in current directory to the client
        //which happens after authentication
        sendMessage(file.getName());
        sendMessage(currentDirectoriesParent);
        sendMessage(directories);
        sendMessage(files);

        //DO-WHILE LOOP WHICH CONTAINS A SWITCH STATEMENT, REPEATEDLY TAKING COMMANDS FROM THE USER. DEPENDING ON THE COMMAND A CERTAIN SECTION OF
        //THE SWITCH STATEMENT HANDLES IT THEN THE DO-WHILE LOOPS OVER AND THE SERVER WAITS FOR THE USERS NEXT COMMAND.
        //ON THE CLIENT SIDE THERE IS A CORRESPONDING SWITCH STATEMENT.
        do {
            try {
                choice = (Integer) in.readObject(); //wait on a command from the client
                switch (choice) {
                    case 1:
                        //RESPONSIBLE FOR DOWNLOADING A FILE FROM THE SERVER (SERVER SIDE)
                        message = (String) in.readObject();                                        //Receive users current directory name from the client.
                        serversFilePath = getServersFilePath();                                    //Get file path of the server
                        currentDirectory = serversFilePath + "/" + message;                        //Create path for current directory
                        file = new File(currentDirectory);                                        //Create a file object set to current directory
                        files = listDirectoriesFiles(file);                                    //Get directories files
                        sendMessage(files);                                                        //Send the files to the client

                        proceedOrNot = (Integer) in.readObject(); //If we receive 1 we can begin download, otherwise loop over and wait for next command

                        if (proceedOrNot == 1) {
                            message = (String) in.readObject();                            //Receive users current directory name from the client.
                            downloadFile = (String) in.readObject();                        //Receive name of file user wants to download
                            serversFilePath = getServersFilePath();                                    //Get file path of the server
                            downloadPath = serversFilePath + "/" + message + "/" + downloadFile;    //Make the download path
                            File downloadFile = new File(downloadPath);
                            sendMessage(downloadFile.length());                                            //Send its length to the client
                            sendMessage(downloadFile.getName());                                            //Send its name to the client
                            byte[] fileByteArray = new byte[(int) downloadFile.length()];                    //Make byte array thats file length in size
                            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(downloadFile));    //Create buffered input stream for the file
                            bis.read(fileByteArray, 0, fileByteArray.length);                        //Read file into an array of bytes
                            OutputStream os = clientSocket.getOutputStream();                    //Get an output stream for the client
                            os.write(fileByteArray, 0, fileByteArray.length);                        //Write array of bytes to the client
                            os.flush();                                                            //Clear the stream
                        }
                        break;
                    case 2:
                        //RESPONSIBLE FOR UPLOADING A FILE FROM CLIENT TO SERVER (SERVER SIDE)
                        InputStream inStream;
                        proceedOrNot = (Integer) in.readObject();
                        if (proceedOrNot == 1) {
                            message = (String) in.readObject();                                //Receive users current directory name from the client.
                            uploadName = (String) in.readObject();                            //Receive name of the uploaded file from the client.
                            uploadSize = (Long) in.readObject();                                //Receive size of the uploaded file from the client.
                            serversFilePath = getServersFilePath();                            //Get file path of the server
                            uploadPath = serversFilePath + "/" + message + "/" + uploadName;    //Create path for the uploaded file
                            byte[] fileByteArray = new byte[(int) uploadSize];                //Create array of bytes the same length as the file.
                            inStream = clientSocket.getInputStream();                        //Open an input stream from the client
                            FileOutputStream fileOutStream = new FileOutputStream(uploadPath);                    //FileOutputStream to write bytes
                            BufferedOutputStream buffedOutStream = new BufferedOutputStream(fileOutStream);        //Pass that into BufferedOutputStream (bytes to characters)
                            int bytesRead = inStream.read(fileByteArray, 0, fileByteArray.length);                //Read bytes from server and pass into byte array,
                            buffedOutStream.write(fileByteArray, 0, bytesRead);                                    //Write these bytes (buffered) to file.
                            buffedOutStream.close();                                                            //Close the BufferedOutputStream
                            sendMessage("File: " + uploadName + " has been successfully uploaded to Directory: " + message);
                        }
                        break;
                    case 3:
                        //RESPONSIBLE FOR RETURNING ALL FILES AND DIRECTORIES IN THE CURRENT DIRECTORY (SERVER SIDE)
                        message = (String) in.readObject();                                        //Receive users current directory name from the client.
                        serversFilePath = getServersFilePath();                                    //Get file path of the server
                        currentDirectory = serversFilePath + "/" + message;                        //Create path for current directory
                        file = new File(currentDirectory);                                        //Create a file object set to current directory
                        currentDirectoriesParent = getCurrentDirectoriesParent(file, username);    //Get directories parent
                        files = listDirectoriesFiles(file);                                    //Get directories files
                        directories = listDirectoriesDirectories(file);                            //Get directories, directories

                        //Send the current directory, its parent directory, and files and directories it contains
                        sendMessage(message);
                        sendMessage(currentDirectoriesParent);
                        sendMessage(directories);
                        sendMessage(files);
                        break;
                    case 4:
                        //RESPONSIBLE FOR MOVING USER TO DIFFERENT DIRECTORY (SERVER SIDE)
                        message = (String) in.readObject();                        //Receive users current directory name from the client.
                        serversFilePath = getServersFilePath();                    //Get file path of the server
                        currentDirectory = serversFilePath + "/" + message;        //Create path for current directory
                        file = new File(currentDirectory);                                        //Create a file object set to current directory
                        currentDirectoriesParent = getCurrentDirectoriesParent(file, username);    //Get directories parent
                        directories = listDirectoriesDirectories(file);                            //Get directories, directories

                        //Send back parent directory and child directories
                        sendMessage(currentDirectoriesParent);
                        sendMessage(directories);

                        //Wait for the user to make a decision on whether he/she wants to move to a parent or child directory
                        parentOrChild = (Integer) in.readObject();

                        if (parentOrChild == 1)//If the user wants to move up into the parent directory
                        {
                            message = (String) in.readObject();            //Receive users current directory name from the client. (path the user see's)
                            serversFilePath = getServersFilePath();        //Get file path of the server (entire path before what user see's)
                            char[] path = message.toCharArray();        //Create char array of what user see's)
                            char[] reversedPath;                        //Second array to hold path when read backwards (reversed)
                            String currentPath = "";                //Temp variable to hold path reversed then un-reversed (path user see's)
                            int countSlash = 0;                        //Start grabbing everything when this count increments(looping backwards)
                            //means we are passed first slash and everything after if parent path
                            boolean grabTheRest = false;            //if this is true means we add whats remaining to string variable

                            for (int i = (path.length - 1); i >= 0; --i)//Loop backwards through the path the user can see
                            {
                                if (path[i] == '/' && countSlash == 0)//When we reach the first slash we know we are passed the current and in parent
                                {
                                    ++countSlash;    //increment so this only happens once
                                    grabTheRest = true;//boolean meaning we can add whats remaining to our string (parent path, will be new current)
                                    continue; //Move to next iteration, we dont want to include the slash
                                }

                                //Then on next iteration and onwards we take every char and add it to a string (string is now backwards)
                                if (grabTheRest == true) {
                                    currentPath += path[i];
                                }
                            }

                            reversedPath = currentPath.toCharArray();    //Pass reversed string to char array
                            currentPath = "";                            //Reset the current path (resetting the reversed string)

                            for (int i = (reversedPath.length - 1); i >= 0; --i)//loop backwards once more, puts string in correct order
                            {
                                currentPath += reversedPath[i]; //add each char to the string again
                            }

                            //Now we have a new path that has moved up one to the parent directory (user see's this path)

                            currentDirectory = serversFilePath + "/" + currentPath; //Add the path (user see's) to the entire path for the server
                            file = new File(currentDirectory);                        //Use path to create file object then use object to.......
                            currentDirectoriesParent = getCurrentDirectoriesParent(file, username);    //Get directories parent
                            files = listDirectoriesFiles(file); //Get directories files
                            directories = listDirectoriesDirectories(file);    //Get directories, directories

                            //Send the information back to the server along with the new current path(not entire server path, just what user can see)
                            sendMessage(currentPath);
                            sendMessage(currentDirectoriesParent);
                            sendMessage(directories);
                            sendMessage(files);
                        } else if (parentOrChild == 2) {
                            message = (String) in.readObject();                                //Receive users current directory name from the client.
                            childDirectory = (String) in.readObject();                        //Receive users chosen child directory name from client.
                            serversFilePath = getServersFilePath();                            //Get file path of the server
                            currentDirectory = serversFilePath + "/" + message + "/" + childDirectory;        //Path for current directory
                            file = new File(currentDirectory);
                            visibleUserDirectory = message + "/" + childDirectory; //Moving to child directory so append child directory to current
                            //directory. User will see this path in current directory
                            currentDirectoriesParent = getCurrentDirectoriesParent(file, username);    //Get directories parent
                            files = listDirectoriesFiles(file);                                    //Get directories files
                            directories = listDirectoriesDirectories(file);                            //Get directories, directories
                            //Send the current directory, its parent directory, and files and directories it contains
                            sendMessage(visibleUserDirectory);
                            sendMessage(currentDirectoriesParent);
                            sendMessage(directories);
                            sendMessage(files);
                        }
                        break;
                    case 5:
                        //RESPONSIBLE FOR CREATING A NEW DIRECTORY INSIDE USERS CURRENT DIRECTORY (SERVER SIDE)
                        message = (String) in.readObject();                    //Getting current directory from client
                        newDirectoryName = (String) in.readObject();            //Getting name for new directory from the client
                        serversFilePath = getServersFilePath();                //Get file path of the server
                        newDirectoryPath = serversFilePath + "/" + message + "/" + newDirectoryName;    //Path for the directory
                        result = createNewDirectory(newDirectoryPath, newDirectoryName);    //Create directory and return a
                        //message for success or no success
                        sendMessage(result);                //Send the result back to the client
                        break;
                    case 6:
                        //OUTPUTS AN EXITING MESSAGE (SERVER SIDE)
                        System.out.println("Client " + username + " is finished");
                        break;
                    default:
                        //WRONG SELECTION (SERVER SIDE)
                        System.out.println();    //No need to do anything, just print a blank line and then loop over.
                        break;
                }
            } catch (ClassNotFoundException e) {
                //Output an error message thats human readable and one thats more specific
                System.out.println();
                System.out.println(e.getMessage());
                System.out.println("There is a class not found issue - please try again");
            } catch (IOException e) {
                //Output an error message thats human readable and one thats more specific
                System.out.println();
                System.out.println(e.getMessage());
                System.out.println("There is an IO issue, - please try again");
            }
        }
        while (choice != 6);        //If the user chooses 6 it will be sent to the server and the loop will end, hence ending the clientRequestThread

        try {
            clientSocket.close();    //Making sure we close all the connections before the program ends, shuts the program gracefully.
            in.close();
            out.close();
        } catch (IOException e) {
            //Output an error message thats human readable and one thats more specific
            System.out.println(e.getMessage());
            System.out.println("There appears to have been an issue closing the connection to the server");
            System.out.println("The connection may possibly have been lost already ");
        }

    }//End run
}//End class
