package com.file.storage;//Importing Packages

import java.io.*;
import java.util.*;

//Client Side Main Application
public class ClientSideApplicationMain {
    //GLOBAL VARIABLES
    //Static global variables responsible for client functionality (Client object) and holding important information regards
    //the users whereabouts on the server, the files and directories that are currently accessible
    private static Client client;
    private static ArrayList<String> files;
    private static ArrayList<String> directories;
    private static String currentDirectory;
    private static String parentDirectory;

    //Main program on the client side
    @SuppressWarnings("resource")
    public static void main(String[] args) {
        //VARIABLES
        //Variables to hold user information, read input from the user, set if authenticated and hold users
        //selection of what he/she wants to do.
        BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
        Scanner console = new Scanner(System.in);
        String username = "";                                    //holds user name entered by user
        String password = "";                                    //holds pass word entered by user
        String ipAddress = "";                                    //holds IP address entered by the user
        int authenticated = 0;                                    //Set to 0 or 1 depending on if user is valid
        int choice = 0;                                        //Controls what action to take depending on users choice.
        int parentOrChild = 0;                                    //Holds users choice to navigate to parent or child directory
        String childDirectory = "";                                //holds child directory user wants to move to
        String newDirectory = "";                                //holds name of new directory user wants to create
        String result = "";                                        //Holds result strings returned by the server
        String downloadFile = "";                                //Names and paths for upload and download files.
        String downloadPath = "";
        String uploadPath = "";
        String uploadName = "";
        long uploadSize;                                        //Holds the size of a file for uploading before sending to server
        boolean caseCheck;                                        //Makes sure user enters information correctly (case sensitive)

        System.out.println("ClIENT/SERVER FILE CLOUD STORAGE APPLICATION");
        System.out.println("============================================");
        //AUTHENTICATION
        //Start of do/while to keep asking for user credentials until correct
        do {
            try {    //Ask for user credentials to authenticate themselves
                System.out.println("Enter Your User Name: ");
                username = stdin.readLine();
                System.out.println("Enter Your Password: ");
                password = stdin.readLine();
                System.out.println("Enter IP Address of File Server: ");
                ipAddress = stdin.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }

            //Create a Client object
            client = new Client(ipAddress);
            //Call sendAuthenticationDetails() to send user name/password to server. Gets a response from the
            //server of 0 or 1 to indicate if valid information was entered or not
            authenticated = client.sendAuthenticationDetails(username, password);

            //Depending on the response we are authenticated or not.
            if (authenticated == 0) {
                System.out.println();
                System.out.println("Invalid Credentials Entered - Please Try Again");
            } else if (authenticated == 1) {
                System.out.println();
                System.out.println("Valid User Information - Welcome " + username);
            }
        }
        while (authenticated == 0);//While we are not authenticated keep asking the user for information

        //AFTER AUTHENTICATION WE RECEIVE USER INFORMATION FROM THE SERVER AND OUTPUT IT
        //Outputs information regards, current directory, its parent directory, the directories files, directories.
        //Function is defined below this main method.
        outputCurrentUserInformation();

        //CLIENT SIDE DO-WHILE AND SWITCH FOR MAIN MENU TASKS THAT CORRESPONDS TO THE SERVER SIDE DO-WHILE AND SWITCH
        //Now the user can select what he/she wants to do from here
        do {
            try {
                //User menu displaying the different available options to the user
                System.out.println();
                System.out.println("APPLICATION MAIN MENU");
                System.out.println("=====================");
                System.out.println("Please enter the corresponding number for selection you want");
                System.out.println("1. Download File from current directory");
                System.out.println("2. Upload File to current directory");
                System.out.println("3. List all files in current directory");
                System.out.println("4. Move to one of the currently accessible directories");
                System.out.println("5. Create a new directory inside current directory");
                System.out.println("6. Exit the program");
                System.out.println("What would you like to do?");
                choice = console.nextInt();
            } catch (InputMismatchException e) {
                //If the user does not enter a number output a message and set choice to 6 so program will close.
                choice = 6;
                System.out.println();
                System.out.println("You must restart the program. Please enter a number that corresponds to you choice");
                System.out.println("You have not entered your choice in the correct format - Program will end.");
            }

            switch (choice) {
                case 1:
                    //RESPONSIBLE FOR DOWNLOADING A FILE FROM THE SERVER (CLIENT SIDE)
                    InputStream inStream;                                        //InputStream object
                    caseCheck = false;                                            //Making sure user has typed file for download correctly
                    client.sendServerCommand(choice);                            //Send user choice to server so it moves to right position to wait
                    client.sendMessage(currentDirectory);                        //Send the users current directory to the server
                    outputAccessibleFiles();                                    //Output currently downloadable files

                    try {
                        //Ask the user to enter the name of the file he/she wants to download
                        System.out.println();
                        System.out.println("Listed above are the files downloadable from this directory");
                        System.out.println("Please enter the name of the file you wish to download (Case Sensitive, Include File Extension. eg .txt) ");
                        downloadFile = stdin.readLine();

                        //Make sure there is a file matching what has been typed by the user with the correct casing
                        for (int i = 0; i < files.size(); ++i) {
                            if (files.get(i).equals(downloadFile)) {
                                caseCheck = true;
                            }
                        }

                        //If the file the user referenced exists
                        if (caseCheck == true) {
                            client.sendMessage(1);                    //Send appropriate command to server so it navigates to right position
                            client.sendMessage(currentDirectory);    //Send current directory
                            client.sendMessage(downloadFile);        //Send name of file we want to download

                            //Ask the user to enter the path that he/she would like to download the file to
                            System.out.println("Please specify the path on your computer you want to download your file to: ");
                            downloadPath = stdin.readLine();
                            downloadPath = downloadPath.replace('\\', '/');    //Format the path entered by the user
                            if (!downloadPath.endsWith("/")) {
                                downloadPath += "/";
                            }

                            long fileLength = client.getFileLengthFromServer();            //Receive length of the file from the server
                            String fileName = client.getServerMessage();                //Receive the name of the file from the server
                            byte[] fileByteArray = new byte[(int) fileLength];            //Create array of bytes the same length as the file.

                            inStream = client.clientSocket.getInputStream();                                    //Instantiate the InputStream object
                            FileOutputStream fileOutStream = new FileOutputStream(downloadPath + "" + fileName);    //Create FileOutputStream set to filename received from server

                            BufferedOutputStream buffedOutStream = new BufferedOutputStream(fileOutStream);        //Pass that into BufferedOutputStream (bytes to characters)
                            int bytesRead = inStream.read(fileByteArray, 0, fileByteArray.length);                //Read bytes from server and pass into byte array,
                            buffedOutStream.write(fileByteArray, 0, bytesRead);                                //Write these bytes (buffered) to file.
                            buffedOutStream.close();                                                        //Close the BufferedOutputStream

                            System.out.println();
                            System.out.println("File: " + downloadFile + " download was successful");            //Output success message
                        } else    //If the directory referenced is wrong
                        {
                            //Output an appropriate message and send a command to the server so it can loop over and start again.
                            System.out.println("You have incorrectly typed the file for download - Please Start Again");
                            client.sendMessage(-1);//Server do nothing, just loop over and wait for the next command
                        }
                    } catch (IOException e) {
                        System.out.println();
                        System.out.println(e.getMessage());
                        System.out.println("There appears to be an issue with the download, or possibly the path you have specified does not exist.");
                    }
                    break;
                case 2:
                    //RESPONSIBLE FOR UPLOADING A FILE FROM CLIENT TO SERVER (CLIENT SIDE)
                    client.sendServerCommand(choice);                                        //Send users choice to the server
                    try {
                        System.out.println("Please Specify the entire path of the file you wish to upload - File will be uploaded to current directory");
                        System.out.println("Include the file itself and its extension eg .txt in the path. ");
                        uploadPath = stdin.readLine();
                        uploadPath = uploadPath.replace('\\', '/');            //Format the path entered by the user
                        File uploadFile = new File(uploadPath);                //Create file object from the path
                        if (uploadFile.exists())                                //Check if the file exists and if it does
                        {
                            client.sendMessage(1);                            //Notify server to get ready for an upload
                            uploadName = uploadFile.getName();                //Get the files name
                            uploadSize = uploadFile.length();                //Get the files size
                            client.sendMessage(currentDirectory);            //Send users current directory to the server
                            client.sendMessage(uploadName);                    //Send name of uploading file to server
                            client.sendMessage(uploadSize);                    //Send size of uploading file to server
                            byte[] fileByteArray = new byte[(int) uploadFile.length()];    //Create byte array of file size
                            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(uploadFile));    //Create buffered input stream for the file
                            bis.read(fileByteArray, 0, fileByteArray.length);                        //Read file into an array of bytes
                            OutputStream os = client.clientSocket.getOutputStream();            //Get an output stream for the server
                            os.write(fileByteArray, 0, fileByteArray.length);                        //Write array of bytes to the server
                            os.flush();                                                            //Clear the stream

                            result = client.getServerMessage();
                            System.out.println(result);
                        } else {
                            System.out.println("This file does not exist");        //Output an appropriate message if path is not found
                            client.sendMessage(-1);                                //Send useless command to server allowing it to loop over and wait again
                        }
                    } catch (IOException e) {
                        System.out.println();
                        System.out.println(e.getMessage());
                        System.out.println("There appears to be an issue with the upload, or possibly the path you have specified does not exist.");
                    }
                    break;
                case 3:
                    //RESPONSIBLE FOR RETURNING ALL FILES AND DIRECTORIES IN THE CURRENT DIRECTORY (CLIENT SIDE)
                    client.sendServerCommand(choice);            //Send users choice to the server
                    client.sendMessage(currentDirectory);        //Send the users current directory to the server
                    outputCurrentUserInformation();            //Output the result returned from server to the screen
                    break;
                case 4:
                    //RESPONSIBLE FOR MOVING USER TO DIFFERENT DIRECTORY (CLIENT SIDE)
                    caseCheck = false;                            //makes sure user types directory in a case sensitive way
                    client.sendServerCommand(choice);            //Send users choice to the server
                    client.sendMessage(currentDirectory);        //Send the users current directory to the server
                    outputAccessibleDirectories();    //Output, parent directory & child directories of current directory

                    System.out.println();
                    System.out.println("Are you accessing parent directory or a child directory?");    //Ask where user wants to navigate to
                    System.out.println("1 = Parent");
                    System.out.println("2 = Child");
                    System.out.println("Enter a number: ");
                    parentOrChild = console.nextInt();

                    if (parentOrChild == 1)//If the user wants to navigate to a parent directory
                    {
                        if (currentDirectory.equals(username))//make sure we are not in the root and if we are
                        {
                            //Output appropriate message
                            System.out.println();
                            System.out.println("You have no parent directory, your in the root directory already.");
                            client.sendMessage(-1);//Server do nothing, just loop over & wait for the next command (must be done, server is waiting)
                        } else// if we are not in the root directory
                        {
                            client.sendMessage(parentOrChild);    //send appropriate command to server so it can provide the correct functionality
                            client.sendMessage(currentDirectory);    //send the server our current directory
                            outputCurrentUserInformation();            //when server responds output the results. (defined below)
                        }
                    } else if (parentOrChild == 2)//If the user wants to navigate to a child directory
                    {
                        try {
                            //ask user to enter the name of the child directory
                            System.out.println("Please enter the name of the child directory to move into (Case Sensitive)");
                            childDirectory = stdin.readLine();

                            //Make sure one matching what has been typed to the user exists with the correct casing
                            for (int i = 0; i < directories.size(); ++i) {
                                if (directories.get(i).equals(childDirectory)) {
                                    caseCheck = true;
                                }
                            }

                            //If the child directory the user referenced exists
                            if (caseCheck == true) {
                                client.sendMessage(parentOrChild);        //Send appropriate command to server so it navigates to right position
                                client.sendMessage(currentDirectory);    //Send current directory
                                client.sendMessage(childDirectory);        //Send directory we want to move into
                                outputCurrentUserInformation();    //Output the result returned from server to the screen
                            } else    //If the directory referenced is wrong
                            {
                                //Output an appropriate message and send a command to the server so it can loop over and start again.
                                System.out.println("You have incorrectly typed the child directory - Please Start Again");
                                client.sendMessage(-1);//Server do nothing, just loop over and wait for the next command
                            }
                        } catch (IOException e) {
                            System.out.println();
                            System.out.println(e.getMessage());
                            System.out.println("There is an IO issue, you may have entered some incorrect information - please try again");
                        }
                    } else //if the user inputs a wrong command (not navigating to child or parent directory)
                    {
                        //Output an appropriate message and send a command to the server so it can loop over and start again.
                        System.out.println("Invalid Choice - Please Start Again");
                        client.sendMessage(-1);//Server do nothing, just loop over and wait for the next command
                    }
                    break;
                case 5:
                    //RESPONSIBLE FOR CREATING A NEW DIRECTORY INSIDE USERS CURRENT DIRECTORY (CLIENT SIDE)
                    try {
                        client.sendServerCommand(choice);
                        System.out.println("What would you like to call the new directory? - Enter a name:");
                        newDirectory = stdin.readLine();

                        client.sendMessage(currentDirectory);    //Send current directory
                        client.sendMessage(newDirectory);        //Send name of new directory the user wants
                        result = client.getServerMessage();        //Get result back from the server
                        System.out.println();
                        System.out.println(result);                //Output the result;

                    } catch (IOException e) {
                        System.out.println();
                        System.out.println(e.getMessage());
                        System.out.println("There is an IO issue, you may have entered some incorrect information - please try again");
                    }
                    break;
                case 6:
                    //OUTPUTS AN EXITING MESSAGE (CLIENT SIDE)
                    System.out.println("Have a nice day :D");
                    client.sendServerCommand(choice);
                    break;
                default:
                    //WRONG SELECTION (CLIENT SIDE)
                    System.out.println("Invalid Selection - Please Try Again");
                    client.sendServerCommand(choice);//Send choice to server so it can log it, loop over and wait once more.
                    break;
            }
        }
        while (choice != 6);

        //Close input and output streams as well as the client socket.
        client.closeConnections();
    }//End Main

    public static void outputCurrentUserInformation() {
        //After Authentication we get details of the clients root server and output
        //Getting the directory the user is currently in on the server (root)
        System.out.println();
        System.out.println("CURRENT DIRECTORY");
        System.out.println("==========================================");
        currentDirectory = client.getCurrentDirectory();
        System.out.println(currentDirectory);

        //Outputs the parent directory and child directories of current directory (defined below this method)
        outputAccessibleDirectories();
        //Outputs files in this directory
        outputAccessibleFiles();
    }//End outputCurrentUserInformation()

    //Outputs the parent directory and child directories of current directory
    public static void outputAccessibleDirectories() {
        //Getting the current directories parent from the server
        System.out.println();
        System.out.println("CURRENTLY ACCESSIBLE PARENT DIRECTORY OF: " + currentDirectory);
        System.out.println("==========================================");
        parentDirectory = client.getCurrentDirectoriesParent();
        System.out.println(parentDirectory);

        //Getting the directories inside users root directory from the server
        System.out.println();
        System.out.println("CURRENTLY ACCESSIBLE CHILD DIRECTORIES INSIDE DIRECTORY: " + currentDirectory);
        System.out.println("==========================================");
        directories = client.getFilesFromServer();    //defined in client class returns all directories in a directory
        if (!directories.isEmpty())//if array list of directories is not empty
        {
            for (int i = 0; i < directories.size(); ++i) {
                System.out.println(directories.get(i)); //loop over each directory and output
            }
        } else {
            System.out.println("No Directories here"); //otherwise the directory does not have any directories
        }
    }//End outputAccessibleDirectories()

    //outputs the files in this directory
    public static void outputAccessibleFiles() {
        //Getting the files inside the users root directory from the server
        System.out.println();
        System.out.println("CURRENTLY ACCESSIBLE FILES INSIDE DIRECTORY: " + currentDirectory);
        System.out.println("==========================================");
        files = client.getFilesFromServer();//same function can also retrieve all files in the current directory on server
        if (!files.isEmpty())    //if files array list is not empty
        {
            for (int i = 0; i < files.size(); ++i) {
                System.out.println(files.get(i)); //loop over and output the files
            }
        } else {
            System.out.println("No Files here"); //otherwise there are no files in the directory on the server
        }
    }
}//End Class
