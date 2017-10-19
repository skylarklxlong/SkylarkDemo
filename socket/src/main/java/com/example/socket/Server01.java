package com.example.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by：LiXueLong 李雪龙 on 2017/10/18 11:05
 * <p>
 * Mail : skylarklxlong@outlook.com
 * <p>
 * Description:
 */
public class Server01 {
    public static final int PORT = 12345;//监听的端口号

    public static void main(String[] args) {
        System.out.println("Server open...\n");
        Server01 server = new Server01();
        server.init();
    }

    private void init() {
        try {
            ServerSocket server = new ServerSocket(PORT);
            HandleThread[] handles = new HandleThread[3]; // 允许三个Client接入
            for (int i = 0; i < handles.length; ++i) {
                handles[i] = new HandleThread(server.accept());
                System.out.println("\n------a new client connect success------");
                handles[i].start();
            }
            for (HandleThread handle : handles) {
                if (handle.isAlive()) {
                    handle.join();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    class HandleThread extends Thread {
        private Socket handle;
        private InputStream in;
        private OutputStream out;

        public HandleThread(Socket accept) throws IOException {
            this.handle = accept;
            this.in = accept.getInputStream();
            this.out = accept.getOutputStream();
        }

        @Override
        public void run() {
            final int bufLen = 65535;
            byte[] buf = new byte[bufLen]; // 缓冲区
            int realLen;
            try {
                while ((realLen = this.in.read(buf, 0, bufLen)) > 0) {
                    System.out.println(new String(buf, 0, realLen)); // 输出
                }
                System.out.println("------connection closed------");
            } catch (IOException e) {
                e.printStackTrace();
            }
            // 关闭
            try {
                this.handle.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
