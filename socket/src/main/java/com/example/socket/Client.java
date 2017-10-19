package com.example.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by：LiXueLong 李雪龙 on 2017/10/18 10:35
 * <p>
 * Mail : skylarklxlong@outlook.com
 * <p>
 * Description:
 */
public class Client {
    public static final String IP_ADDR = "localhost";//服务器地址
    public static final int PORT = 12345;//服务器端口号

    public static void main(String[] args) {
        System.out.println("client open...");
        System.out.println("when revice server send  \"OK\" , the client will be dead\n");
        while (true) {
            Socket socket = null;
            InputStream is = null;
            PrintWriter out = null;
            BufferedReader reader = null;
            String reqAnswer = null;
            try {
                //创建一个流套接字并将其连接到指定主机上的指定端口号
                socket = new Socket(IP_ADDR, PORT);
                if (socket.isConnected()) {
                    System.out.println("client connect");
                } else {
                    System.out.println("no connection");
                }
                is = socket.getInputStream(); // socket輸入流 >>>接收服务器端返回信息
                System.out.println("is：" + (is == null));
                out = new PrintWriter((new OutputStreamWriter( // socket輸出流 >>> 发出请求
                        socket.getOutputStream())), true);
                out.println("000");

                // 添加读取server回复的信息 为 fileName|fileMD5
                reader = new BufferedReader(new InputStreamReader(is));
                reqAnswer = reader.readLine();
                System.out.println("server send : " + reqAnswer);

                /*//读取服务器端数据
                DataInputStream input = new DataInputStream(socket.getInputStream());
                //向服务器端发送数据
                DataOutputStream dataout = new DataOutputStream(socket.getOutputStream());
                System.out.print("please input: \t");
                String str = new BufferedReader(new InputStreamReader(System.in)).readLine();
                dataout.writeUTF(str);

                String ret = input.readUTF();
                System.out.println("server send : " + ret);
                // 如接收到 "OK" 则断开连接
                if ("OK".equals(ret)) {
                    System.out.println("client will be closed");
                    Thread.sleep(500);
                    break;
                }*/

                out.close();
//                input.close();
                is.close();
                reader.close();
            } catch (Exception e) {
                System.out.println("client error:" + e.getMessage());
            } finally {
                if (socket != null) {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        socket = null;
                        System.out.println("client finally error:" + e.getMessage());
                    }
                }
            }
        }
    }
}