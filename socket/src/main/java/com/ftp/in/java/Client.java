package com.ftp.in.java;

import java.net.*;
import java.io.*;
import java.util.*;

//code to be run by client
/*
	Client.java will implement this interface.

	A different class containing main function will instantiate
	Client object and run the client there.
*/

public class Client
{
	/*
		presentWorkingDirectory: Will be re-initialized by the constructor function
		to a default value of the directory where the code for client is.
		
		ipAddress : of the host
		port      : as of the server
	*/

	
	private String ipAddress="";
	private int port=8090;
	private LocalFileHandler lFileH = null;
	
	private Socket clientSocket;
	private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;
    private FileTransport fileTransport;

    private BufferedReader br;
	// add more variables if you feel necessary

    
	public Client(String ipAddress,int port)
	{ 
		this.ipAddress = ipAddress;
		this.port = port;
		this.lFileH = new LocalFileHandler();

	}


	/*
	input: String ipAddress, int port
		output: integer showing status code
			status codes
				1 : connected
				2 : error in connection
	*/
	public int connect()
	{
		try 
		{
			clientSocket = new Socket(this.ipAddress , this.port);
			dataInputStream=new DataInputStream(clientSocket.getInputStream());
            dataOutputStream=new DataOutputStream(clientSocket.getOutputStream());
            fileTransport = new FileTransport(clientSocket,dataInputStream,dataOutputStream);

            br=new BufferedReader(new InputStreamReader(System.in));
            return 1;
        }
        catch (Exception en)
        {
          	return 2;
        }
	}


	/*
		input: void
		output: integer showing stauts code
			status codes
				1 : disconnected
				2 : error
	*/
	public int disconnect()
	{
		try
		{
			dataOutputStream.writeUTF("disconnect");
			return 1 ; 
		}
		catch (Exception en) 
		{
			return 2 ;	
		}
	}

	public void execute(String[] inputArray)
	{
		try
		{
			if(inputArray[0].equals("lls")||inputArray[0].startsWith("lc")) //thus covered all local commands
				this.executeOnLocal(inputArray);
			else
				this.executeOnServer(inputArray);
		}
		catch(Exception e)
		{
			System.out.println(":>[!] Error executing command. Please check");
		}
	}

	/*
		executeOnLocal()
		input: command
				domain: {lcd,lls,lchmod}

		output: string as on a linux system

				for ls: list of file in the present working directory.
				for cd: "directory changed to [directory as in command]"
				for chmod: "permission changed for [file name] to rwx------"
	*/
	private void executeOnLocal(String[] inputArray) throws Exception
	{
		//Local commands
		if(inputArray[0].equals("lls"))
		{
			String output = lFileH.lls(inputArray);
			System.out.println(output);
		}
		else if(inputArray[0].equals("lcd"))
		{
			int statusCode = lFileH.lcd(inputArray);

			if(statusCode==1) //success
			{	
				if(inputArray.length>1)	System.out.println("Directory changed to "+inputArray[1]);
				else System.out.println("Directory changed to home");
			}
			else if(statusCode==2) //failure
			{
				if(inputArray.length>1) System.out.println("[!] Error changing directory to : \""+inputArray[1]+"\". Please check.");
				else System.out.println("[!] Error changing directory to home");
			}
			else System.out.println("[!] Some unknown error occoured. Try again");

		}
		else if(inputArray[0].equals("lchmod"))
		{
			int statusCode = lFileH.lchmod(inputArray);
			if(statusCode==2) //failure
				System.out.println("[!] Error changing permission. Please check.");

		}
		else System.out.println(":> [!] Command not supported");
	}

	private void executeOnServer(String[] inputArray) throws Exception
	{
		String commandToServer = inputArray[0]+"<command>";
		String dataToServer = "";

		for(int i=1;i<inputArray.length;i++)
			if(i!=inputArray.length-1) 	dataToServer += inputArray[i]+"<arg>";
			else						dataToServer += inputArray[i];
		
		dataOutputStream.writeUTF(commandToServer+dataToServer);

		String response = dataInputStream.readUTF();
		System.out.println(response);	
	}


	/*
		input: filename to be uploaded.
		output: int showing status code.
			status codes:
				1 : success
				2 : error

		Can add another codes if required
	*/
	public int upload(String[] filenames) throws Exception
	{
		if(filenames.length==1)
		{
			System.out.println("[!] Nothing to upload.");
			return 0;
		}

		for(int i=1;i<filenames.length;i++)
		{
			File file = this.lFileH.produceFileHandle(filenames[i]);

			if(file!=null)
			{
				dataOutputStream.writeUTF("upload<command>"+filenames[i]);

				String alreadyExistsOnServer = dataInputStream.readUTF();
				if(alreadyExistsOnServer.equals("yesFileExists"))
				{
					System.out.println("File \""+filenames[i]+"\" alreaddy exists on server. Do you want to overwrite it ?");
					System.out.print("Please enter (y/n) : ");
					String option = br.readLine();

					while(!(option.equals("y")||option.equals("n")))
					{
						System.out.print("Unrecognized choice. Please enter (y/n) : ");
						option = br.readLine();
					}

					if(option.equals("n")) 
					{
						this.dataOutputStream.writeUTF("abortupload");
						continue;
					}
				}

				System.out.print("Uploading "+filenames[i]+"\t");
				long initialTime = System.currentTimeMillis();

				this.dataOutputStream.writeUTF("uploading");
				this.fileTransport.sendFile(file);
				
				long finalTime = System.currentTimeMillis();

				System.out.println("complete");
				System.out.println("Upload finished in "+(finalTime-initialTime)+"ms");
			}
			else
			{
				System.out.println("File "+filenames[i]+" doesnot exist. Please check for typos.");
			}
		}

		return 1;
	}


	/*
		input: filename to be downloaded.
		output: int showing status code.
			status codes:
				1 : success
				2 : error
	
		Can add another codes if required
	*/
	public void download(String[] filenames) throws Exception
	{
		if(filenames.length==1)
		{
			System.out.println("[!] Nothing to download. Please enter a filename to download");
			return;
		}

		for(int i=1;i<filenames.length;i++)
		{

			dataOutputStream.writeUTF("download<command>"+filenames[i]);

			String fileAlreadyExists = dataInputStream.readUTF();
			if(fileAlreadyExists.equals("nopeFileDoesNotExist"))
			{
				System.out.println("[!] File "+filenames[i]+" does not exist on server. Please check for typos.");
				dataOutputStream.writeUTF("abortdownload");
				continue;
			}

			File fileHandle = this.lFileH.produceFileHandle(filenames[i]);

			if(fileHandle!=null)
			{
				System.out.println("File \""+filenames[i]+"\" alreaddy exists on local. Download anyway and overwrite it?");
				System.out.print("Please enter (y/n) : ");

				String option = br.readLine();
				while(!(option.equals("y")||option.equals("n")))
				{
					System.out.print("Unrecognized choice. Please enter (y/n) : ");
					option = br.readLine();
				}

				if(option.equals("n")) 
				{
					dataOutputStream.writeUTF("abortdownload");
					continue;
				}
			}

			System.out.print("Downloading "+filenames[i]+"\t");
			long initialTime = System.currentTimeMillis();

			this.dataOutputStream.writeUTF("downloading");
			this.fileTransport.receiveFile(this.lFileH.returnPWD());
			
			long finalTime = System.currentTimeMillis();
			System.out.println("complete");

			System.out.println("Download completed in "+(finalTime-initialTime)+"ms");
		}
	}

}