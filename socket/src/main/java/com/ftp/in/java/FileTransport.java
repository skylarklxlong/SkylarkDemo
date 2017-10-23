package com.ftp.in.java;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class FileTransport
{
	private Socket socket;
	private DataInputStream dataInputStream;
	private DataOutputStream dataOutputStream;

	public FileTransport(Socket socket,DataInputStream din,DataOutputStream dout)
	{
		this.socket = socket;
		this.dataInputStream = din;
		this.dataOutputStream = dout;
	}

	public void sendFile(File file) throws Exception
	{
		String fileName = file.getName();
		this.dataOutputStream.writeUTF(fileName);

		FileInputStream fIn = new FileInputStream(file);
		int character;
		
		do
		{
			character = fIn.read();
			dataOutputStream.writeUTF(String.valueOf(character));
		} while(character!=-1);

		fIn.close();
	}

	public void receiveFile(String pwd) throws Exception
	{
		String fileName = this.dataInputStream.readUTF();
			
		File file;

		if(pwd=="/") file = new File("/"+fileName);
		else file = new File(pwd+"/"+fileName);

		FileOutputStream fOut = new FileOutputStream(file);
		int character;
		String temp;
		do 
		{
			temp = dataInputStream.readUTF();
			character = Integer.valueOf(temp);
			
			if(character!=-1) fOut.write(character);

		} while(character!=-1);

		fOut.close();
	}
}