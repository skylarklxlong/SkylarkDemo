package cn.itcast.net.server;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PushbackInputStream;
import java.io.RandomAccessFile;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.itcast.utils.StreamTool;

public class SocketDownloadServer {

    public static void main(String[] args) {
        try {
            SocketDownloadServer socketUploadServer = new SocketDownloadServer(7879);
            socketUploadServer.start();
            System.out.println("server is running");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ExecutorService executorService;//线程池
    private int port;//监听端口
    private boolean quit = false;//退出
    private ServerSocket server;

    public SocketDownloadServer(int port) {
        this.port = port;
        //创建线程池，池中具有(cpu个数*50)条线程
        executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 50);
    }

    /**
     * 退出
     */
    public void quit() {
        this.quit = true;
        try {
            server.close();
        } catch (IOException e) {
        }
    }

    /**
     * 启动服务
     *
     * @throws Exception
     */
    public void start() throws Exception {
        server = new ServerSocket(port);
        while (!quit) {
            try {
                Socket socket = server.accept();
                //为支持多用户并发访问，采用线程池管理每一个用户的连接请求
                executorService.execute(new SocketTask(socket));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private final class SocketTask implements Runnable {
        private Socket socket = null;

        public SocketTask(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                System.out.println("accepted connection " + socket.getInetAddress() + ":" + socket.getPort());
                OutputStream outStream = socket.getOutputStream();
                String filename = "css.mp3";
                File file = new File(filename);
                String head = "Content-Length=" + file.length() + ";filename=" + filename + ";sourceid=\r\n";
//            String head = "Content-Length=" + file.length() + ";filename=" + filename + ";sourceid=1508806329691\r\n";
                outStream.write(head.getBytes());

                PushbackInputStream inStream = new PushbackInputStream(socket.getInputStream());
                String response = StreamTool.readLine(inStream);
                System.out.println("client say : " + response);
                String[] items = response.split(";");
                String position = items[1].substring(items[1].indexOf("=") + 1);

                RandomAccessFile fileInputStream = new RandomAccessFile(file, "r");
                fileInputStream.seek(Integer.valueOf(position));
                byte[] buffer = new byte[1024*8];
                int len = -1;
                int count = 0;
                while ((len = fileInputStream.read(buffer)) != -1) {
                    outStream.write(buffer, 0, len);
                    count += 1;
                    System.out.println("len : " + len + "--->count : " + count);

                /*if (count == 30) {
                    System.out.println("client pause start");
                    Thread.sleep(10000);
                    System.out.println("client pause end");
                }*/

                }
                System.out.println("server send finish");
                fileInputStream.close();
                outStream.close();
                inStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (socket != null && !socket.isClosed()) socket.close();
                } catch (IOException e) {
                }
            }
        }
    }

}
