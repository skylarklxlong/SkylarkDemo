package com.example.socket;

import java.io.File;
import java.io.IOException;

/**
 * Created by：LiXueLong 李雪龙 on 2017/10/18 16:23
 * <p>
 * Mail : skylarklxlong@outlook.com
 * <p>
 * Description:
 */
public class Test {

    public static void main(String[] args){
        try {
            File file = new File(".");
            String path = file.getCanonicalPath();
            System.out.println("--->"+path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
