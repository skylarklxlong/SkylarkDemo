package com.ftp.in.java;

import java.net.Socket;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;

public class Server extends Thread
{   
    private int port;
    private LocalFileHandler lFileH;
    private FileTransport fileTransport;


    private Socket socket;

    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;

    public Server(Socket socket)
    {
        try
        {
            this.socket = socket;
            this.dataInputStream = new DataInputStream(this.socket.getInputStream());
            this.dataOutputStream = new DataOutputStream(this.socket.getOutputStream());
            this.fileTransport = new FileTransport(socket,dataInputStream,dataOutputStream);
            this.lFileH = new LocalFileHandler();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        this.start();
    }


    @Override
    public void run()
    {
        while(true)
        {
            try
            {
                String command = this.dataInputStream.readUTF();

                String[] requestString = command.split("<command>");
                String[] requestArray = new String[1];

                if(requestString.length>1)
                {
                    String[] requestArguments = requestString[1].split("<arg>");
                    requestArray = new String[1+requestArguments.length];

                    for(int i=1;i<requestArray.length;i++)
                        requestArray[i] = requestArguments[i-1];
                }

                requestArray[0] = requestString[0];

                if(requestArray[0].equals("ls"))
                {
                    String reslut = this.lFileH.lls(requestArray);
                    dataOutputStream.writeUTF(reslut);
                }
                else if(requestArray[0].equals("cd"))
                {
                    int statusCode = this.lFileH.lcd(requestArray);
                    
                    String directoryName = "home";
                    if(requestArray.length>1) directoryName = requestArray[1];

                    if(statusCode==1) dataOutputStream.writeUTF("Directory changed to "+directoryName+" on server");
                    else if(statusCode==2) dataOutputStream.writeUTF("Could not change directory to "+ directoryName+" on server");
                    else dataOutputStream.writeUTF("Some unknown error occoured on server. Try again.");
                }
                else if(requestArray[0].equals("chmod"))
                {
                    int statusCode = this.lFileH.lchmod(requestArray);

                    if(statusCode==1) dataOutputStream.writeUTF("Permissions changed for file(s) on server");
                    else if(statusCode==2) dataOutputStream.writeUTF("Could not change permissions on server.");
                    else dataOutputStream.writeUTF("Some unknown error occoured on server. Try again");
                }
                else if(requestArray[0].equals("upload"))
                {
                    String fileName = requestArray[1];

                    File fileHandle = this.lFileH.produceFileHandle(fileName);
                    if(fileHandle!=null) this.dataOutputStream.writeUTF("yesFileExists");
                    else this.dataOutputStream.writeUTF("nopeFileDoesNotExist");

                    if(this.dataInputStream.readUTF().equals("uploading"))
                        this.fileTransport.receiveFile(this.lFileH.returnPWD());

                }
                else if(requestArray[0].equals("download"))
                {
                    String fileName = requestArray[1];

                    File fileHandle = this.lFileH.produceFileHandle(fileName);
                    if(fileHandle!=null) this.dataOutputStream.writeUTF("yesFileExists");
                    else this.dataOutputStream.writeUTF("nopeFileDoesNotExist");            


                    if(this.dataInputStream.readUTF().equals("downloading"))
                        this.fileTransport.sendFile(fileHandle);
                }
                else if(requestArray[0].equals("disconnect"))
                {
                    this.socket.close();
                }
                else
                {
                    dataOutputStream.writeUTF("[!] Command not supported");
                }
            }
            catch(Exception e)
            {
                try
                {
                    this.socket.close();
                }
                catch(java.io.IOException en)
                {

                }
            }
        }
    }


    public void execute()
    {

    }



}