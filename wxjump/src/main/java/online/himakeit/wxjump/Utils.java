package online.himakeit.wxjump;

import java.io.File;
/**
 * @author：LiXueLong
 * @date:2018/1/3-8:59
 * @mail1:skylarklxlong@outlook.com
 * @mail2:li_xuelong@126.com
 * @des:
 */
public class Utils {

    public static void jump(int time) {
        try {
            Runtime.getRuntime().exec("adb shell input touchscreen swipe 170 187 170 187 " + time);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void screen() {
        try {
            Process p1 = Runtime.getRuntime().exec("adb shell screencap -p /sdcard/jump.png");
            p1.waitFor();
            File localFile = new File("E:\\jump.png");
            if (!localFile.exists()) {
                localFile.createNewFile();
            }
            Process p2 = Runtime.getRuntime().exec("adb pull /sdcard/jump.png E:\\jump.png");
            p2.waitFor();
        } catch (Exception e) {
        }
    }

    public static int calDistance(Point p1, Point p2) {
        return (int) Math.sqrt(Math.abs((p1.x - p2.x) * (p1.x - p2.x)) + Math.abs((p1.y - p2.y) * (p1.y - p2.y)));
    }

}
