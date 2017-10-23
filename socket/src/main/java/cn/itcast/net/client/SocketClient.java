package cn.itcast.net.client;

import java.io.File;
import java.io.OutputStream;
import java.io.PushbackInputStream;
import java.io.RandomAccessFile;
import java.net.Socket;

import cn.itcast.utils.StreamTool;

public class SocketClient {
    /**
     * @param args
     */
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("127.0.0.1", 7878);
            System.out.println("client is running");
            OutputStream outStream = socket.getOutputStream();
            /**
             * 这里的file路径的根目录就是项目的的目录：SkylarkDemo\
             * css.mp3也就是SkylarkDemo\cssserver.mp3
             * 所以在测试的时候，需要将要上传的文件拷贝到SkylarkDemo\目录下，
             * 然后，修改这里的filename
             */
            String filename = "./socket/client/cssserver.mp3";
            File file = new File(filename);
            String head = "Content-Length=" + file.length() + ";filename=" + filename + ";sourceid=\r\n";
            outStream.write(head.getBytes());

            PushbackInputStream inStream = new PushbackInputStream(socket.getInputStream());
            String response = StreamTool.readLine(inStream);
            System.out.println("server say : " + response);
            String[] items = response.split(";");
            String position = items[1].substring(items[1].indexOf("=") + 1);

            RandomAccessFile fileInputStream = new RandomAccessFile(file, "r");
            fileInputStream.seek(Integer.valueOf(position));
            byte[] buffer = new byte[1024];
            int len = -1;
            int count = 0;
            while ((len = fileInputStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, len);
                count += 1;
                System.out.println("len : " + len + "--->count : " + count);

                if (count == 30) {
                    System.out.println("client pause start");
                    Thread.sleep(10000);
                    System.out.println("client pause end");
                }

            }
            System.out.println("client send finish");
            fileInputStream.close();
            outStream.close();
            inStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
