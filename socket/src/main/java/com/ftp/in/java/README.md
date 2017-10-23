#FTP in java

A FTP client and A command processor on remote machine, in java
using java sockets.

Compile java files and put 

on client side
  1. ClientRun.class 
  2. Client.class
  3. LocalFileHandler.class
  4. FileTransport.class

and on server side
  1. ServerRun.class
  2. Server.class
  3. LocalFileHandler.class
  4. FileTransport.class


Then first start the server using the commmand
  java ServerRun
  
and on the client side run the command
  java ClientRun
  

A virtual prompt will appear on client side using which you can execute
commands on local machine as well as server, using the same prompt.


Commands supported, to be executed on server side :

  1. ls : unix "ls"
  2. cd : unix "cd"
  3. chmod : unix "chmod"

and the ones supported, which will be executed on local machine :

  1. lls : unix "ls"
  2. lcd : unix "cd"
  3. chmod : unix "chmod"
  4. upload [filename1] [filename2]...    : to upload file(s) from local present working directory
                                            into server present working directory.
  5. download [filename1] [filename2]...  : to download file(s) from server's present working directory
                                            into local's present working directory.

I did this as a OS course assignment.

If doubts, email me on anshuman.techie@gmail.com.
Put an appropriate subject line.

#Thanks for reading
