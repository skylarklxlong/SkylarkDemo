package com.ftp.in.java;//code to be run by client
import java.net.*;
import java.io.*;
// import java.util.*;

public class LocalFileHandler
{
	/*
		pwd : present working dierctory
	*/
	private String pwd=null;

	//Initialized to the place where program exists
	public LocalFileHandler()
	{
		pwd = System.getProperty("user.dir");
	}

	/*
		input : void
		output : as in a linux system
	*/
	public String lls(String[] inputArray)
	{
		String output="";
		String outputFiles="";
		String directoryContents = "";
		String notExisting = "";

		if(inputArray.length==1) //ie no arguements
		{
			File directory = new File(this.pwd);
			File[] filesInDirectory = directory.listFiles();

			for(int i=0;i<filesInDirectory.length;i++)
			{
				String fileName = filesInDirectory[i].getName();
				String[] exploded = fileName.split("/");
				outputFiles += exploded[exploded.length-1]+"\t";
			}
		}
		else
		{
			for(int i=1;i<inputArray.length;i++)
			{
				File someFile = new File(inputArray[i]);
				if(someFile.exists())
				{
					if(someFile.isFile()) 
					{
						outputFiles += inputArray[i];
						if(i!=inputArray.length-1) outputFiles+="\n";
					}
					else if(someFile.isDirectory())
					{
						try
						{
							directoryContents += inputArray[i]+" :\n";
							File[] filesInDirectory = someFile.listFiles();

							for(int j=0;j<filesInDirectory.length;j++)
							{
								String fileName = filesInDirectory[j].getName();
								String[] exploded = fileName.split("/");
								directoryContents += exploded[exploded.length-1]+"\t";
							}
						}
						catch(Exception e)
						{
							directoryContents += "Error reading directory\""+inputArray[i]+"\"";
						}
					}
					else
					{
						//This will never happen
					}
				}
				else notExisting += "File/Directory : \""+inputArray[i]+"\" does not exist\n";
			}
		}
		
		output += notExisting;
		output += outputFiles;
		output += directoryContents;

		if(output.endsWith("\n")) output = output.substring(0,output.length()-1);

		return output;
	}

	/*
		input  : directoryName of the directory to which goto
		output : integer showing status code
			status codes
				1 : success
				2 : error

		Suppose a directory is set to 000 (no read permissions) 
		cd will return a error code
	*/
	public int lcd(String[] inputArray)
	{
		File current = new File(this.pwd);

		if(inputArray.length==1)
		{
			String linuxUserName = System.getProperty("user.name");
			this.pwd = "/home/"+linuxUserName;
			return 1;
		}
		else if(inputArray[1].equals("."))
		{
			return 1;
		}
		else if(inputArray[1].equals(".."))
		{
			File parent = current.getParentFile();
			if(parent!=null) 
			{
				this.pwd = parent.getAbsolutePath();
				return 1;
			}
			else return 2;
		}
		else
		{
			File cdTo;
			if(this.pwd=="/") cdTo = new File(this.pwd+inputArray[1]);
			else cdTo = new File(this.pwd+"/"+inputArray[1]);

			if(cdTo.exists())
				if(cdTo.isDirectory())
				{
					this.pwd = cdTo.getAbsolutePath();
					return 1;
				}
		}

		return 2;		
	}


	/*
		input : String handle which will be a file name or a
				directory name
				newPermissions will dictate new permissions of
				file or directory
	*/
	public int lchmod(String[] inputArray) 
	{
		try 
		{
			Runtime runtime = Runtime.getRuntime();
			
			String command = "chmod ";
			
			for(int i=1;i<inputArray.length;i++)
				command += inputArray[i]+" ";

			Process p = runtime.exec(command);
			p.waitFor();

			int commandExecutionStatus = p.exitValue();
			/*
				Exit Value meanings:
					0 : success
					1 : faliure
			*/

			return commandExecutionStatus+1;
		}
		catch(Exception en)
		{
			return 2;
		}
	}



	/*
		input : String which is possibly a fileName
		output: FileHandle
	*/

	public File produceFileHandle(String filename)
	{
		File file = new File(this.pwd+"/"+filename);
		if(file.exists()) return file;
		else return null;
	}		
	
	public String returnPWD()
	{
		return this.pwd;
	}
}
