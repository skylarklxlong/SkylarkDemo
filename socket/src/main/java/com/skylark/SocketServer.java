package com.skylark;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SocketServer {

    public static void main(String[] args) {
        try {
            SocketServer socketServer = new SocketServer(7878);
            socketServer.start();
            System.out.println("server is running");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ExecutorService executorService;//线程池
    private int port;//监听端口
    private boolean quit = false;//退出
    private ServerSocket server;

    public SocketServer(int port) {
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
                OutputStream out = socket.getOutputStream();
                InputStream in = socket.getInputStream();
                final int orderLen = 1024;
                byte[] orderArr = new byte[orderLen];
                while (true) {
                    /**
                     * 接收并解析指令
                     */
                    System.out.println("接收并解析指令");
                    // 等待client发送指令
                    int realOrderLen = in.read(orderArr, 0, orderLen);
                    if (realOrderLen < 0) {
                        break;
                    }
                    String orderStr = new String(orderArr, 0, realOrderLen);
                    System.out.println("接收的指令为：" + orderStr);
                    // 解析指令
                    String[] order = orderStr.trim().split("\\|"); // 这里是正则表达式，两斜线和一个竖线之间没有空格
                    final String filePath = order[0];
                    final int piece = Integer.parseInt(order[1]);
                    final int pieceSize = Integer.parseInt(order[2]);

                    /**
                     * 通过指令读取指定文件中的指定块
                     */
                    System.out.println("通过指令读取指定文件中的指定块");
                    byte[] buf = new byte[pieceSize]; // 缓冲区
                    FileInputStream f = new FileInputStream(filePath);
                    f.skip(piece * (long) pieceSize); // 跳转
                    int realBufLen = f.read(buf);  // 读取
                    f.close();

                    /**
                     * 数据附上有效数据长度和MD5值
                     */
                    System.out.println("数据附上有效数据长度和MD5值");
                    // 获取md5值并将len转换成byte[]以通过socket传输
                    byte[] md5 = ByteMD5(buf);
                    byte[] lenInB = ByteBuffer.allocate(4).putInt(realBufLen).array();
                    // 合并，有效数据长度、数据的md5值、数据
                    ByteBuffer byteBuffer = ByteBuffer.allocate(lenInB.length + md5.length + buf.length);
                    byte[] result = byteBuffer.put(lenInB).put(md5).put(buf).array();

                    /**
                     * 将数据发送给Client
                     */
                    System.out.println("将数据发送给Client");
                    out.write(result);
                }
                /**
                 * 关闭连接
                 */
                System.out.println("数据发送完毕");
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

    private static byte[] ByteMD5(byte[] data) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(data);
            return md.digest();
        } catch (NoSuchAlgorithmException e) {
            return new byte[0];
        }
    }
}
