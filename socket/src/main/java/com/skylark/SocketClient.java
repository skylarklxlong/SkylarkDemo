package com.skylark;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 这里的file路径的根目录就是项目的的目录：SkylarkDemo\
 * css.mp3也就是SkylarkDemo\cssserver.mp3
 * 所以在测试的时候，需要将要上传的文件拷贝到SkylarkDemo\目录下，
 * 然后，修改这里的filename
 */
public class SocketClient {

    static String srcFile = "./socket/file/server/cssserver.mp3";//源文件
    static String dstFile = "./socket/file/client/cssclient.mp3";//目标文件

    /**
     * @param args
     */
    public static void main(String[] args) {
        try {

            String[] pathSplit = dstFile.trim().split("\\\\");
            String bpFile = pathSplit[pathSplit.length - 1] + ".index";
            int begin = GetBreakPoint(bpFile, 0);

            Socket socket = new Socket("127.0.0.1", 7878);
            System.out.println("client is running");
            OutputStream out = socket.getOutputStream();
            InputStream in = socket.getInputStream();

            final int bufLen = 8 * 1000 * 1000; // 8M
            byte[] buf = new byte[4 + 16 + bufLen]; // 4位有效数据长度(int)，16位md5值

            for (int piece = begin; ; ++piece) {

                SetBreakPoint(bpFile, piece);

                /**
                 * 向Server发送指令
                 */
                System.out.println("向Server发送指令");
                String order = String.format("%s|%d|%d", srcFile, piece, bufLen);
                out.write(order.getBytes());

                /**
                 * 接收数据
                 */
                System.out.println("接收数据");
                int sum = 0;
                int complete;
                while ((complete = in.read(buf, sum, 20 + bufLen - sum)) > 0 && sum < 20 + bufLen) {
                    sum += complete;
                }

                /**
                 * 分析数据，判断文件结束，校验md5
                 */
                System.out.println("分析数据，判断文件结束，校验md5");
                // 读取有效数据长度
                byte[] realBufLenByte = new byte[4];
                System.arraycopy(buf, 0, realBufLenByte, 0, 4);
                int realBufLen = ByteBuffer.wrap(realBufLenByte).getInt();
                // 文件到头了
                if (realBufLen <= 0) {
                    break;
                }
                // 检查md5值的正确性
                byte[] md5 = new byte[16];
                System.arraycopy(buf, 4, md5, 0, 16);
                if (!CheckMD5(buf, 20, md5)) {
                    // 错误则重试
                    --piece;
                    continue;
                }

                /**
                 * 将数据写入文件
                 */
                System.out.println("将数据写入文件");
                FileOutputStream fw = new FileOutputStream(dstFile, true); // append=true
                fw.write(buf, 20, realBufLen);
                fw.close();
            }
            DelBreakPoint(bpFile);
            /**
             * 关闭连接
             */
            System.out.println("关闭连接");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    // 断点不存在或出现错误则返回backup
    private static int GetBreakPoint(String bpFile, int backup) {
        FileReader fr = null;
        try {
            fr = new FileReader(bpFile);
            BufferedReader br = new BufferedReader(fr);
            String numStr = br.readLine();
            return Integer.parseInt(numStr);
        } catch (NumberFormatException | IOException e) {
            return backup;
        } finally {
            try {
                if (fr != null) {
                    fr.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void SetBreakPoint(String bpFile, int num) throws FileNotFoundException {
        PrintStream f = new PrintStream(bpFile);
        f.println(num);
        f.close();
    }

    private static void DelBreakPoint(String bpFile) {
        File f = new File(bpFile);
        if (f.exists()) {
            f.delete();
        }
    }

    private static Boolean CheckMD5(byte[] data, int start, byte[] md5) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
            if (start == 0) {
                md.update(data);
            } else {
                byte[] part = new byte[data.length - start];
                System.arraycopy(data, start, part, 0, part.length);
                md.update(part);
            }

        } catch (NoSuchAlgorithmException e) {
            return false;
        }
        byte[] result = md.digest();
        for (int i = 0; i < result.length; ++i) {
            if (md5[i] != result[i]) {
                return false;
            }
        }
        return true;
    }
}
