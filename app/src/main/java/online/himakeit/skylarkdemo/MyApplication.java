package online.himakeit.skylarkdemo;

import android.app.Application;

/**
 * Created by：LiXueLong 李雪龙 on 2017/8/17 19:21
 * <p>
 * Mail : skylarklxlong@outlook.com
 * <p>
 * Description:
 */
public class MyApplication extends Application {

    private static MyApplication myApplication = null;

    @Override
    public void onCreate() {
        super.onCreate();

        this.myApplication = this;
    }

    public static MyApplication getAppContext(){
        return myApplication;
    }
}
