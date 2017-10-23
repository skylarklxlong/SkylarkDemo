package com.ftp.in.java;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ClientRun 
{
	public static void main (String[] args) throws Exception 
	{
		BufferedReader br =new BufferedReader(new InputStreamReader(System.in));
		Client cl = new Client("127.0.0.1",8090);
		
		int connectionStatus = cl.connect();

		if(connectionStatus==1)	System.out.println("Connected");
		else
		{
			System.out.println("Error in connection");
			// return;
		}

		while (true)
		{
			System.out.print("Enter Command> ");
			String[] inputArray = br.readLine().split("\\s+");
			
			// System.out.println(inputArray[0]);
			// System.out.println("Input array length "+inputArray.length);

			if(inputArray[0].length()==0) continue;
			
			// file transport commands
			if (inputArray[0].equals("upload")) 
			{
				cl.upload(inputArray) ;
				continue;
			}

			if (inputArray[0].equals("download")) 
			{
				cl.download(inputArray) ;	
				continue;
			}

			if(inputArray[0].equals("close")||inputArray[0].equals("exit"))
			{
				System.out.println("Good bye");
				cl.disconnect();
				System.exit(1);
			}

			//default action
			cl.execute(inputArray);

		}
	}
}